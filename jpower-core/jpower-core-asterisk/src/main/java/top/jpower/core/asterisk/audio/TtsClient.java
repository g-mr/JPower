package top.jpower.core.asterisk.audio;

import java.io.File;

public interface TtsClient extends AutoCloseable {

    TtsResult process(String say, File file);

    void close();

}
