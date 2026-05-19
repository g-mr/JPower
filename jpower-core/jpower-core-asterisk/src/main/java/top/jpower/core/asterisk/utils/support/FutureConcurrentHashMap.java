package top.jpower.core.asterisk.utils.support;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 自己实现的可以自动获取Future结果的Map，没有实现全部方法，用到哪个方法自己实现去
 * @param <K>
 * @param <V>
 */
@Slf4j
public class FutureConcurrentHashMap<K,V> extends ConcurrentHashMap<K,V> {

    public FutureConcurrentHashMap() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        V value = super.get(key);
        if (value instanceof Future<?> future) {
            try {
                value = (V) future.get(1, TimeUnit.MINUTES);
                put((K) key, value);
            } catch (InterruptedException | ExecutionException e) {
                log.warn("获取值[{}]失败===>>{}", key, ExceptionUtil.stacktraceToString(e));
                remove(key);
            } catch (TimeoutException e) {
                log.warn("获取值[{}]超时===>>{}", key, e.getMessage());
            }
        }
        return value;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new FutureEntrySet(super.entrySet());
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        super.forEach((k, v) -> {
            // 处理 Future 类型的值
            if (v instanceof Future<?> future) {
                try {
                    //noinspection unchecked
                    v = (V) future.get(1, TimeUnit.MINUTES);
                    // 将结果存回 Map
                    put(k, v);
                } catch (InterruptedException | ExecutionException e) {
                    log.warn("获取值[{}]执行失败===>>{}", k, ExceptionUtil.stacktraceToString(e));
                } catch (TimeoutException e) {
                    log.warn("获取值[{}]超时===>>{}", k, e.getMessage());
                }
            }
            // 调用传入的 action
            action.accept(k, v);
        });
    }

    /**
     * 使用 Stream API 重写 values()
     */
    @Override
    public Collection<V> values() {
        return super.values().stream()
                .map(v->{
                    if (v instanceof Future<?> future) {
                        try {
                            //noinspection unchecked
                            v = (V) future.get(1, TimeUnit.MINUTES);
                        } catch (InterruptedException | ExecutionException e) {
                            log.warn("获取值失败===>>{}", ExceptionUtil.stacktraceToString(e));
                        } catch (TimeoutException e) {
                            log.warn("获取值超时===>>{}", e.getMessage());
                        }
                    }
                    return v;
                })
                .collect(Collectors.toList());
    }

    /**
     * 自定义的EntrySet实现，用于包装原始的Entry
     */
    class FutureEntrySet extends AbstractSet<Entry<K, V>> {
        private final Set<Entry<K, V>> originalSet;

        public FutureEntrySet(Set<Entry<K, V>> originalSet) {
            this.originalSet = originalSet;
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new FutureEntryIterator(originalSet.iterator());
        }

        @Override
        public Spliterator<Entry<K, V>> spliterator() {
            return Spliterators.spliterator(iterator(), size(),
                    Spliterator.CONCURRENT | Spliterator.NONNULL);
        }

        @Override
        public void forEach(Consumer<? super Entry<K, V>> action) {
            originalSet.forEach(entry ->
                    action.accept(new FutureEntry(entry))
            );
        }

        @Override
        public int size() {
            return originalSet.size();
        }
    }

    class FutureEntryIterator implements Iterator<Entry<K, V>> {
        private final Iterator<Entry<K, V>> originalIterator;

        public FutureEntryIterator(Iterator<Entry<K, V>> originalIterator) {
            this.originalIterator = originalIterator;
        }

        @Override
        public boolean hasNext() {
            return originalIterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
            return new FutureEntry(originalIterator.next());
        }

        @Override
        public void remove() {
            originalIterator.remove();
        }

        @Override
        public void forEachRemaining(Consumer<? super Entry<K, V>> action) {
            originalIterator.forEachRemaining(entry ->
                    action.accept(new FutureEntry(entry))
            );
        }
    }

    class FutureEntry implements Entry<K, V> {
        private final Entry<K, V> originalEntry;

        public FutureEntry(Entry<K, V> originalEntry) {
            this.originalEntry = originalEntry;
        }

        @Override
        public K getKey() {
            return originalEntry.getKey();
        }

        @Override
        public V getValue() {
            V originalValue = originalEntry.getValue();
            if (originalValue instanceof Future<?> future) {
                try {
                    //noinspection unchecked
                    originalValue = (V) future.get(1, TimeUnit.MINUTES);
                    originalEntry.setValue(originalValue);
                } catch (InterruptedException | ExecutionException e) {
                    log.warn("获取值[{}]失败===>>{}", getKey(), ExceptionUtil.stacktraceToString(e));
                } catch (TimeoutException e) {
                    log.warn("获取值[{}]超时===>>{}", getKey(), e.getMessage());
                }
            }
            return originalValue;
        }

        @Override
        public V setValue(V value) {
            return originalEntry.setValue(value);
        }

        @Override
        public boolean equals(Object obj) {
            return originalEntry.equals(obj);
        }

        @Override
        public int hashCode() {
            return originalEntry.hashCode();
        }

        @Override
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }
}
