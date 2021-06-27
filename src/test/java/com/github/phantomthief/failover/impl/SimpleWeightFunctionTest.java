package com.github.phantomthief.failover.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author huangli
 * Created on 2020-05-06
 */
public class SimpleWeightFunctionTest {

    @Test
    public void testFail() {
        /**
         * 每次失败后扣减最大权重的0.5，成功后增加最大权重的0.01
         */
        SimpleWeightFunction<String> f = new SimpleWeightFunction<>(0.5, 0.01);
        assertEquals(0.5, f.fail(1, 0, 0, 1, "R1"));
        assertEquals(0, f.fail(1, 0, 0, 0.5, "R1"));
        assertEquals(0, f.fail(1, 0, 0, 0, "R1"));
    }

    @Test
    public void testSuccess() {
        SimpleWeightFunction<String> f = new SimpleWeightFunction<>(0.5, 0.5);
        assertEquals(0.5, f.success(1, 0, 0, 0, "R1"));
        assertEquals(1, f.success(1, 0, 0, 0.5, "R1"));
        assertEquals(1, f.success(1, 0, 0, 1, "R1"));
    }

}
