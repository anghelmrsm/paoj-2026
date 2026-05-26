package com.pao.project.elearning.model;

import java.util.Objects;

public final class CourseCode {
    private final String prefix;
    private final int number;

    public CourseCode(String prefix, int number) {
        this.prefix = prefix;
        this.number = number;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return prefix + number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseCode)) return false;
        CourseCode that = (CourseCode) o;
        return number == that.number && Objects.equals(prefix, that.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, number);
    }
}
