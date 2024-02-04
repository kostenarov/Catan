package com.game.catan.Functionality;

public class VillagePair <K, U> {
    private K first;
    private U second;

    public VillagePair(K first, U second) {
        this.first = first;
        this.second = second;
    }

    public VillagePair() {
    }

    public VillagePair(K first) {
        this.first = first;
    }

    public K getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    public void addSecond(U second) {
        this.second = second;
    }

    public boolean hasBoth() {
        return hasFirst() && hasSecond();
    }

    public boolean hasFirst() {
        return first != null;
    }

    public boolean hasSecond() {
        return second != null;
    }

    public boolean hasOne() {
        return !hasBoth() && !hasNone();
    }

    public boolean hasNone() {
        return first == null && second == null;
    }
}
