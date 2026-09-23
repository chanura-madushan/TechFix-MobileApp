package com.example.techfix.util;

public class CardValidationUtils {

    /** Luhn algorithm — the same checksum real payment processors use to catch mistyped card numbers. */
    public static boolean isValidCardNumber(String digitsOnly) {
        if (digitsOnly.length() < 13 || digitsOnly.length() > 19) return false;

        int sum = 0;
        boolean alternate = false;
        for (int i = digitsOnly.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(digitsOnly.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    public static boolean isValidExpiry(String mmYY) {
        if (!mmYY.matches("\\d{2}/\\d{2}")) return false;
        int month = Integer.parseInt(mmYY.substring(0, 2));
        int year = Integer.parseInt(mmYY.substring(3, 5)) + 2000;
        if (month < 1 || month > 12) return false;

        java.util.Calendar now = java.util.Calendar.getInstance();
        int currentYear = now.get(java.util.Calendar.YEAR);
        int currentMonth = now.get(java.util.Calendar.MONTH) + 1;

        return year > currentYear || (year == currentYear && month >= currentMonth);
    }

    public static boolean isValidCvv(String cvv) {
        return cvv.matches("\\d{3,4}");
    }
}