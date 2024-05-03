package com.game.catan.Functionality;

import com.game.catan.Map.Cell.VillageCell;

public class VillagePair {
    private VillageCell first;
    private VillageCell second;

    public VillagePair(VillageCell first, VillageCell second) {
        this.first = first;
        this.second = second;
    }

    public VillagePair() {
    }

    public VillagePair(VillageCell first) {
        this.first = first;
    }

    public VillageCell getFirst() {
        return (VillageCell) first;
    }

    public VillageCell getSecond() {
        return second;
    }

    public void setFirst(VillageCell first) {
        this.first = first;
    }

    public void setSecond(VillageCell second) {
        this.second = second;
    }

    public void addSecond(VillageCell second) {
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
