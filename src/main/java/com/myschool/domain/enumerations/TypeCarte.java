package com.myschool.domain.enumerations;

public enum TypeCarte {

    A6("A6"),
    BusinessCard("BusinessCard");

    private final String value;

    TypeCarte(String value) {
        this.value = value;
    }

    public static TypeCarte fromValue(String value) {
        if (value != null) {
            for (TypeCarte typeCarte : values()) {
                if (typeCarte.value.equals(value)) {
                    return typeCarte;
                }
            }
        }

        // you may return a default value
        return getDefault();
        // or throw an exception
        // throw new IllegalArgumentException("Invalid typeCarte: " + value);
    }

    public String toValue() {
        return value;
    }

    public static TypeCarte getDefault() {
    return A6;
}

}