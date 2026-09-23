package com.example.techfix;

public final class RepairCalculator {
    private RepairCalculator() {}
    public static double total(double servicePrice, double partsCost, double discount) {
        return Math.max(0.0, servicePrice + partsCost - discount);
    }
    public static String rupees(double amount) {
        return String.format("Rs. %.2f", amount);
    }
}