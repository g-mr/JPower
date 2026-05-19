package top.jpower.core.asterisk.audio;


import top.jpower.core.util.utils.Fc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public record TtsResult(CompletableFuture<Void> future, CountDownLatch startLatch, boolean isWriterPlay) {
    /**
     * 非阻塞检查是否完成
     * @return
     */
    public boolean isDone() {
        return future.isDone();
    }

    /**
     * 检查是否开始写入字节流
     * @return 是否开始
     */
    public boolean isStarted() {
        return startLatch.getCount() == 0;
    }

    /**
     * 阻塞等待开始
     */
    public void waitStart() throws InterruptedException {
        startLatch.await();
    }

    /**
     * 取消转写
     * @return
     */
    public boolean cancel() {
        if (Fc.notNull(future)){
            if (isDone()){
                return true;
            }
            return future.cancel(true);
        }
        return false;
    }
}