package com.n1njac.yiqipao.android.run.trackutils;

public class NumericWheelAdapter implements WheelAdapter {

    public static final int DEFAULT_MAX_VALUE = 9;

    private int minValue;
    private int maxValue;

    private String format;

    private String values = null;

    public NumericWheelAdapter(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public NumericWheelAdapter(int minValue, int maxValue, String format) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }

    public String getItem(int index) {
        if (index >= 0 && index < getItemsCount()) {
            int value = minValue + index;
            values = (format != null ? String.format(format, value) : Integer
                    .toString(value));
            setValue(values);
            return values;
        }
        return null;
    }

    public String getValues() {
        return values;
    }

    public void setValue(String value) {
        this.values = value;
    }

    public int getItemsCount() {
        return maxValue - minValue + 1;
    }

    public int getMaximumLength() {
        int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
        @SuppressWarnings("unused")
        int maxLen = Integer.toString(max).length();
        if (minValue < 0) {
            maxLen++;
        }
        return maxValue - minValue + 1;
    }

}
