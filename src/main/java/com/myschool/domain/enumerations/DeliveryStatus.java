package com.myschool.domain.enumerations;

public enum DeliveryStatus {

    PENDING("PENDING"),
    DELIVRD("DELIVRD"),
    UNDELIV("UNDELIV"),
    EXPIRED("EXPIRED");

    private final String value;

    DeliveryStatus(String value) {
        this.value = value;
    }

    public static DeliveryStatus fromValue(String value) {
        if (value != null) {
            for (DeliveryStatus statut : values()) {
                if (statut.value.equals(value)) {
                    return statut;
                }
            }
        }

        // you may return a default value
        return getDefault();
        // or throw an exception
        // throw new IllegalArgumentException("Invalid statut: " + value);
    }

    public String toValue() {
        return value;
    }

    public static DeliveryStatus getDefault() {
    return PENDING;
}

}