package top.jpower.core.asterisk.sip.function;

@FunctionalInterface
public interface BufferFunction {

    int apply(byte[] buffer, int offset, int length);

}
