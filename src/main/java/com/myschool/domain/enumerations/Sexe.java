package com.myschool.domain.enumerations;

public enum Sexe {

    Masculin("Masculin"),
    Feminin("Feminin");

    private final String value;

    Sexe(String value) {
        this.value = value;
    }

    public static Sexe fromValue(String value) {
        if (value != null) {
            for (Sexe sexe : values()) {
                if (sexe.value.equals(value)) {
                    return sexe;
                }
            }
        }

        // you may return a default value
        return getDefault();
        // or throw an exception
        // throw new IllegalArgumentException("Invalid sexe: " + value);
    }

    public String toValue() {
        return value;
    }

    public static Sexe getDefault() {
    return Masculin;
}

}