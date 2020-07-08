package com.myschool.domain.enumerations;

public enum TypeEtablissement {

    College("College"),
    Primaire("Primaire");

    private final String value;

    TypeEtablissement(String value) {
        this.value = value;
    }

    public static TypeEtablissement fromValue(String value) {
        if (value != null) {
            for (TypeEtablissement typeEtablissement : values()) {
                if (typeEtablissement.value.equals(value)) {
                    return typeEtablissement;
                }
            }
        }

        // you may return a default value
        return getDefault();
        // or throw an exception
        // throw new IllegalArgumentException("Invalid typeEtablissement: " + value);
    }

    public String toValue() {
        return value;
    }

    public static TypeEtablissement getDefault() {
    return College;
}

}