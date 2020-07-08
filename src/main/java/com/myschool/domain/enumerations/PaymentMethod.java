package com.myschool.domain.enumerations;

public enum PaymentMethod {
    BANK("BANK"), CASH("CASH");

private final String value;

PaymentMethod(String value) {
    this.value = value;
}

    public static PaymentMethod fromValue(String value) {
        if (value != null) {
            for (PaymentMethod paymentMethod : values()) {
                if (paymentMethod.value.equals(value)) {
                    return paymentMethod;
                }
            }
        }

        // you may return a default value
        return getDefault();
        // or throw an exception
        // throw new IllegalArgumentException("Invalid paymentMethod: " + value);
    }

    public String toValue() {
        return value;
    }

    public static PaymentMethod getDefault() {
    return BANK;
}

}