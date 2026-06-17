package top.jpower.core.asterisk.audio;

import java.io.PipedInputStream;

public interface AsrClient extends AutoCloseable {


    AsrResult process(PipedInputStream pipedInputStream) throws InterruptedException;

    void close();

}
