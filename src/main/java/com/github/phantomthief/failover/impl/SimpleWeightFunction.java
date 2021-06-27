package com.github.phantomthief.failover.impl;

/**
 * @author huangli
 * Created on 2020-01-20
 *
 * SimpleWeightFunction 用不到父类的recoverCountMap
 */
public class SimpleWeightFunction<T> extends AbstractWeightFunction<T> {

    private static final double DEFAULT_FAIL_DECREASE_RATE = 0.05;
    private static final double DEFAULT_SUCCESS_INCREASE_RATE = 0.01;

    private final double failDecreaseRate;
    private final double successIncreaseRate;

    public SimpleWeightFunction() {
        this(DEFAULT_FAIL_DECREASE_RATE, DEFAULT_SUCCESS_INCREASE_RATE);
    }

    public SimpleWeightFunction(double failDecreaseRate, double successIncreaseRate) {
        this(failDecreaseRate, successIncreaseRate, DEFAULT_RECOVER_THRESHOLD);
    }

    public SimpleWeightFunction(double failDecreaseRate, double successIncreaseRate, int recoverThreshold) {
        super(recoverThreshold);
        if (failDecreaseRate < 0 || failDecreaseRate > 1) {
            throw new IllegalArgumentException("bad failDecreaseRate:" + failDecreaseRate);
        }
        if (successIncreaseRate < 0 || successIncreaseRate > 1) {
            throw new IllegalArgumentException("bad successIncreaseRate:" + successIncreaseRate);
        }
        this.failDecreaseRate = failDecreaseRate;
        this.successIncreaseRate = successIncreaseRate;
    }

    @Override
    public double computeSuccess(double maxWeight, double minWeight, int priority, double currentOldWeight, T resource) {
        return currentOldWeight + maxWeight * successIncreaseRate;
    }

    @Override
    public double computeFail(double maxWeight, double minWeight, int priority, double currentOldWeight, T resource) {
        return currentOldWeight - maxWeight * failDecreaseRate;
    }

}
