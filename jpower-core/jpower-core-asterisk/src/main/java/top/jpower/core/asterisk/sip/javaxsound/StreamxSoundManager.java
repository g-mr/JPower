/*
    This file is part of Peers, a java SIP softphone.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2010, 2011, 2012 Yohann Martineau
*/

package top.jpower.core.asterisk.sip.javaxsound;

import cn.hutool.core.thread.ThreadUtil;
import top.jpower.core.asterisk.sip.function.BufferFunction;
import top.jpower.core.asterisk.sip.media.AbstractSoundManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.WritableByteChannel;

@Slf4j
public class StreamxSoundManager extends AbstractSoundManager {

    @Getter
    private WritableByteChannel sink;
    private Pipe.SourceChannel source;
    private final BufferFunction function;

    public StreamxSoundManager(BufferFunction function) {
        this.function = function;
    }

    @Override
    public void init() {
        try {
            Pipe pipe = Pipe.open();
            source = pipe.source();
            sink = pipe.sink();
        } catch (IOException e) {
            log.error("input/output error", e);
        }
    }

    @Override
    public synchronized void close() {
        if (source != null){
            try {
                source.close();
            } catch (Exception e){
                log.error("input close error", e);
            }
        }

        if (sink != null){
            try {
                sink.close();
            } catch (Exception e){
                log.error("output close error", e);
            }
        }

    }

    /**
     * 计算G.711打包周期
     * @param bytes 音频数据字节数
     * @param sampleRate 采样率(Hz)，默认8000
     * @param channels 声道数，默认1
     * @return 打包周期(ms)
     */
    public static double calculatePacketDuration(int bytes,
                                                 int sampleRate,
                                                 int channels) {
        // G.711固定参数：8位/样本
        final int bitsPerSample = 8;

        // 每秒字节数 = 采样率 × 字节/样本 × 声道数
        double bytesPerSecond = sampleRate * (bitsPerSample / 8.0) * channels;

        // 打包周期 = 总字节数 / 每秒字节数 × 1000(转换为ms)
        return (bytes / bytesPerSecond) * 1000;
    }

    @Override
    public synchronized byte[] readData() {
        try {
            source.configureBlocking(false); // 非阻塞模式

            ByteBuffer buffer = ByteBuffer.allocateDirect(320);
            // 清空缓冲区准备读取
            buffer.clear();
            // 尝试读取数据
            int bytesRead = source.read(buffer);
            byte[] chunk = new byte[bytesRead];
            if (bytesRead > 0) {
                // 切换到读模式
                buffer.flip();
                // 获取数据副本
                buffer.get(chunk);
            }

            ThreadUtil.sleep(calculatePacketDuration(bytesRead, 8000, 2));
            return chunk;

        } catch (Exception e){
            log.error("input/output error", e);
        }
        return new byte[0];
    }

    @Override
    public int writeData(byte[] buffer, int offset, int length) {
        return function.apply(buffer, offset, length);
    }

}
