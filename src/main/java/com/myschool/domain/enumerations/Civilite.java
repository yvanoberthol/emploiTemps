package com.myschool.domain.enumerations;

public enum Civilite {

    Mr("Mr"),
    Mme("Mme");

    private final String value;

    Civilite(String value) {
        this.value = value;
    }

    public static Civilite fromValue(String value) {
        if (value != null) {
            for (Civilite civilite : values()) {
                if (civilite.value.equals(value)) {
                    return civilite;
                }
            }
        }

        // you may return a default value
        return getDefault();
        // or throw an exception
        // throw new IllegalArgumentException("Invalid civilite: " + value);
    }

    public String toValue() {
        return value;
    }

    public static Civilite getDefault() {
    return Mr;
}

}