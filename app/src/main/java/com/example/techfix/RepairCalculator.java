package com.example.techfix;
public final class RepairCalculator {
    private RepairCalculator() {}
    public static double total(double servicePrice, double partsCost, double discount) {
        return Math.max(0.0, servicePrice + partsCost - discount);
    }
}