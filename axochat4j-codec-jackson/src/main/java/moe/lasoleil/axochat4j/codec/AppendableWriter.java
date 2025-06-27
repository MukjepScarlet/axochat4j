package moe.lasoleil.axochat4j.codec;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

class AppendableWriter extends Writer {

    private final Appendable appendable;

    public AppendableWriter(Appendable appendable) {
        this.appendable = appendable;
    }

    @Override
    public void write(int c) throws IOException {
        appendable.append((char) c);
    }

    @Override
    public void write(char @NotNull [] cbuf) throws IOException {
        appendable.append(new String(cbuf));
    }

    @Override
    public void write(@NotNull String str) throws IOException {
        appendable.append(str);
    }

    @Override
    public void write(@NotNull String str, int off, int len) throws IOException {
        appendable.append(str, off, len);
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        appendable.append(csq);
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        appendable.append(csq, start, end);
        return this;
    }

    @Override
    public Writer append(char c) throws IOException {
        appendable.append(c);
        return this;
    }

    @Override
    public void write(char @NotNull [] cbuf, int off, int len) throws IOException {
        appendable.append(new String(cbuf, off, len));
    }

    @Override
    public void flush() throws IOException {
        if (appendable instanceof Flushable) {
            ((Flushable) appendable).flush();
        }
    }

    @Override
    public void close() throws IOException {
        if (appendable instanceof Closeable) {
            ((Closeable) appendable).close();
        }
    }
}
