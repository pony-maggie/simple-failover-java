package com.github.phantomthief.failover.util;

import static java.lang.Math.min;
import static java.util.Collections.emptyList;
import static java.util.Collections.shuffle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

/**
 * @author w.vela
 */
public class RandomListUtils {

    private static final int LCG_THRESHOLD = 3;

    // 这种用法学习下
    private RandomListUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Random，ThreadLocalRandom，SecureRandom是Java中的随机数生成器，其中ThreadLocalRandom是jdk7才出现的，是Random的增强版。
     * 在并发访问的环境下，使用ThreadLocalRandom来代替Random可以减少多线程竞争，最终保证系统具有更好的线程安全。
     *
     * Random是线程安全的，但是多线程下可能性能比较低。
     *
     * 特别是在生成验证码的情况下，不要使用Random，因为它是线性可预测的。所以在安全性要求比较高的场合，应当使用SecureRandom。
     */
    @Nullable
    public static <T> T getRandom(List<T> source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return source.get(ThreadLocalRandom.current().nextInt(source.size()));
    }

    /**
     * 线性同余方法（LCG）产生随机数.
     *
     * shuffle()是一个Java Collections类方法，其工作原理是随机置换指定列表元素。
     * 有两种不同类型的Java shuffle（）方法，可以根据其参数进行区分。这些都是：
     *
     * Java Collections shuffle（list）方法
     * Java Collections shuffle (list, random)方法
     *
     * RandomAccess 接口是一个标记性接口(它没有方法)，表明实现类支持快速随机访问。
     *
     * RandomAccess 接口的目的就是使算法选择一个性能更好的行为，当这个算法应用到随机访问列表( 例如 ArrayList )
     * 或者顺序访问列表( 例如 LinkedList )时。参考：
     * https://blog.csdn.net/zwlove5280/article/details/109228250
     */
    public static <T> List<T> getRandom(Collection<T> source, int size) {
        if (source == null || source.isEmpty()) {
            return emptyList();
        }
        if (source instanceof List && source instanceof RandomAccess
                && size < source.size() / LCG_THRESHOLD) {
            return getRandomUsingLcg((List<T>) source, size);
        } else {
            return getRandomUsingShuffle(source, size);
        }
    }

    static <T> List<T> getRandomUsingShuffle(Collection<T> source, int size) {
        List<T> newList = new ArrayList<>(source);
        shuffle(newList, ThreadLocalRandom.current());
        return newList.subList(0, min(newList.size(), size));
    }

    static <T> List<T> getRandomUsingLcg(List<T> source, int size) {
        int targetSize = min(source.size(), size);
        List<T> newList = new ArrayList<>(targetSize);
        LcgRandomIterator<T> iterator = new LcgRandomIterator<>(source);
        for (int i = 0; i < targetSize; i++) {
            newList.add(iterator.next());
        }
        return newList;
    }
}
