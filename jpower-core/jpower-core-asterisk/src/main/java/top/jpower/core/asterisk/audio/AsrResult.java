package top.jpower.core.asterisk.audio;

import top.jpower.core.util.utils.Fc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public record AsrResult(CompletableFuture<String> future, AtomicBoolean started, AtomicReference<Function<String, Boolean>> dataReference, AtomicReference<Consumer<Long>> noDataReference) {
    /**
     * 获取最终结果（阻塞）
     *
     * @return 最终结果
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String getResult() throws ExecutionException, InterruptedException {
        return future.get();
    }

    /**
     * 非阻塞检查是否完成
     * @return
     */
    public boolean isDone() {
        return future.isDone();
    }

    /**
     * 检查是否开始识别到用户话语
     * @return
     */
    public boolean isStarted() {
        return started.get();
    }

    /**
     * 取消识别
     * @return
     */
    public boolean cancel() {
        if (Fc.notNull(future)){
            return future.cancel(true);
        }
        return false;
    }
}