package moe.lasoleil.axochat4j.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import moe.lasoleil.axochat4j.exception.PacketIOException;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public final class NettyAxochatClient implements AxochatClientConnection.Factory<Bootstrap> {

    public static final NettyAxochatClient INSTANCE = new NettyAxochatClient();

    private NettyAxochatClient() {}

    private static @Nullable SslContext insecureSslContextOrNull(@NotNull URI uri) {
        boolean ssl = "wss".equalsIgnoreCase(uri.getScheme());
        SslContext sslContext = null;
        if (ssl) {
            try {
                sslContext = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to create SSL context", e);
            }
        }
        return sslContext;
    }

    @Override
    public @NotNull AxochatClientConnection create(
            @NotNull Bootstrap bootstrap,
            AxochatClientConnection.@NotNull Config config
    ) {
        if (config.getPacketAdaptor().type() != AxochatPacket.Adaptor.Type.CLIENT) {
            throw new IllegalArgumentException("Packet adaptor type must be CLIENT");
        }

        URI uri;
        try {
            uri = config.getUrl().toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL", e);
        }

        SslContext sslContext = insecureSslContextOrNull(uri);

        Channel[] channelHolder = new Channel[1];

        AxochatClientConnection connection = new AxochatClientConnection() {
            @Override
            public void send(AxochatPacket.@NotNull C2S packet) throws PacketIOException {
                Channel channel = channelHolder[0];
                if (channel == null || !channel.isWritable()) {
                    throw new IllegalStateException("Channel is not writable");
                }
                ByteBuf buf = channel.alloc().buffer(256);
                try (Writer writer = new OutputStreamWriter(new ByteBufOutputStream(buf), StandardCharsets.UTF_8)) {
                    config.getPacketAdaptor().write(writer, packet);
                } catch (IOException e) {
                    throw new PacketIOException(e);
                }
                channel.writeAndFlush(new TextWebSocketFrame(buf));
            }

            @Override
            public void close() throws IOException {
                Channel channel = channelHolder[0];
                if (channel == null || !channel.isWritable()) {
                    throw new IllegalStateException("Channel is not writable");
                }
                channel.writeAndFlush(new CloseWebSocketFrame(1000, null));
            }
        };

        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null,
                true, new DefaultHttpHeaders());

        ChannelPromise[] handshakePromiseHolder = new ChannelPromise[1];

        SimpleChannelInboundHandler<WebSocketFrame> handler = new SimpleChannelInboundHandler<WebSocketFrame>() {

            @Override
            public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                handshakePromiseHolder[0] = ctx.newPromise();
            }

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                handshaker.handshake(ctx.channel());
            }

            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                config.getWebSocketHandler().accept(
                        connection,
                        new WebSocketConnectionEvent.Disconnected(config.getUrl(), -1, null, System.currentTimeMillis())
                );
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                config.getWebSocketHandler().accept(
                        connection,
                        new WebSocketConnectionEvent.ErrorOccurred(cause, System.currentTimeMillis())
                );
                if (!handshakePromiseHolder[0].isDone()) {
                    handshakePromiseHolder[0].setFailure(cause);
                }
                ctx.close();
            }

            @Override
            protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
                if (!handshaker.isHandshakeComplete()) {
                    try {
                        handshaker.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
                        handshakePromiseHolder[0].setSuccess();

                        config.getWebSocketHandler().accept(
                                connection,
                                new WebSocketConnectionEvent.Connected(config.getUrl(), System.currentTimeMillis())
                        );
                    } catch (WebSocketHandshakeException e) {
                        handshakePromiseHolder[0].setFailure(e);
                    }
                    return;
                }

                if (msg instanceof TextWebSocketFrame) {
                    TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
                    String text = textFrame.text();
                    config.getWebSocketHandler().accept(
                            connection,
                            new WebSocketConnectionEvent.TextFrameReceived(text, System.currentTimeMillis())
                    );

                    AxochatPacket.S2C packet = null;
                    try {
                        packet = (AxochatPacket.S2C) config.getPacketAdaptor().read(text);
                    } catch (Exception e) {
                        config.getWebSocketHandler().accept(
                                connection,
                                new WebSocketConnectionEvent.UnknownMessageFormat(text, e)
                        );
                    }
                    if (packet != null) {
                        config.getPacketHandler().accept(connection, packet);
                    }
                } else if (msg instanceof BinaryWebSocketFrame) {
                    BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) msg;
                    ByteBuf byteBuf = binaryFrame.content();
                    config.getWebSocketHandler().accept(
                            connection,
                            new WebSocketConnectionEvent.BinaryFrameReceived(byteBuf.nioBuffer(), System.currentTimeMillis())
                    );
                } else if (msg instanceof PongWebSocketFrame) {
                    PongWebSocketFrame pongFrame = (PongWebSocketFrame) msg;
                    // NOOP
                } else if (msg instanceof PingWebSocketFrame) {
                    PingWebSocketFrame pingFrame = (PingWebSocketFrame) msg;
                    // NOOP
                } else if (msg instanceof CloseWebSocketFrame) {
                    CloseWebSocketFrame closeFrame = (CloseWebSocketFrame) msg;
                    config.getWebSocketHandler().accept(
                            connection,
                            new WebSocketConnectionEvent.Disconnected(config.getUrl(), closeFrame.statusCode(), closeFrame.reasonText(), System.currentTimeMillis())
                    );
                }
            }
        };

        try {
            channelHolder[0] = bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    if (sslContext != null) ch.pipeline().addLast(sslContext.newHandler(ch.alloc()));

                    ch.pipeline().addLast(new HttpClientCodec(), new HttpObjectAggregator(8192), handler);
                }
            }).connect(uri.getHost(), uri.getPort()).sync().channel();
        } catch (InterruptedException e) {
            config.getWebSocketHandler().accept(
                    connection,
                    new WebSocketConnectionEvent.ErrorOccurred(e, System.currentTimeMillis())
            );
        }

        return connection;
    }
}