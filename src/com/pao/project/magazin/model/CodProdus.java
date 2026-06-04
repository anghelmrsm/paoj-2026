package com.pao.project.magazin.model;

import java.util.Objects;

public final class CodProdus {
    private final String valoare;

    public CodProdus(String valoare) {
        if (valoare == null || valoare.isBlank()) {
            throw new IllegalArgumentException("Codul produsului este obligatoriu.");
        }
        this.valoare = valoare.toUpperCase();
    }

    public String getValoare() {
        return valoare;
    }

    @Override
    public String toString() {
        return valoare;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CodProdus && Objects.equals(valoare, ((CodProdus) o).valoare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valoare);
    }
}
