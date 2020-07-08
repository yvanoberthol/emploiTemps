package com.myschool.domain.enumerations;

public enum Statut {

    Redoublant("Redoublant"),
    Nouveau("Nouveau");

    private final String value;

    Statut(String value) {
        this.value = value;
    }

    public static Statut fromValue(String value) {
        if (value != null) {
            for (Statut statut : values()) {
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

    public static Statut getDefault() {
    return Nouveau;
}

}