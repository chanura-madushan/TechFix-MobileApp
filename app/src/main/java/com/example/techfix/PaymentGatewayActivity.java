package com.example.techfix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.data.FirestoreSingleCallback;
import com.example.techfix.data.PaymentRepository;
import com.example.techfix.model.Payment;
import com.example.techfix.util.CardValidationUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentGatewayActivity extends AppCompatActivity {

    private String appointmentId;
    private double amount;
    private PaymentRepository paymentRepository;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        appointmentId = getIntent().getStringExtra("APPOINTMENT_ID");
        amount = getIntent().getDoubleExtra("AMOUNT", 0.0);
        paymentRepository = new PaymentRepository();

        ((TextView) findViewById(R.id.tvAmount)).setText(String.format(Locale.getDefault(), "Rs. %.2f", amount));

        setupCardNumberFormatting();
        setupExpiryFormatting();

        findViewById(R.id.btnPayGateway).setOnClickListener(v -> attemptPayment());
        findViewById(R.id.tvCancelPayment).setOnClickListener(v -> finish());
    }

    /** Auto-inserts a space every 4 digits as the user types, like a real card input field. */
    private void setupCardNumberFormatting() {
        EditText etCardNumber = findViewById(R.id.etCardNumber);
        etCardNumber.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;
                isFormatting = true;

                String digitsOnly = s.toString().replaceAll("\\s", "");
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < digitsOnly.length(); i++) {
                    if (i > 0 && i % 4 == 0) formatted.append(" ");
                    formatted.append(digitsOnly.charAt(i));
                }

                etCardNumber.setText(formatted.toString());
                etCardNumber.setSelection(formatted.length());
                isFormatting = false;
            }
        });
    }

    /** Auto-inserts the slash in MM/YY as the user types. */
    private void setupExpiryFormatting() {
        EditText etExpiry = findViewById(R.id.etExpiry);
        etExpiry.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;
                isFormatting = true;

                String digitsOnly = s.toString().replaceAll("[^\\d]", "");
                if (digitsOnly.length() > 4) digitsOnly = digitsOnly.substring(0, 4);

                String formatted = digitsOnly;
                if (digitsOnly.length() >= 3) {
                    formatted = digitsOnly.substring(0, 2) + "/" + digitsOnly.substring(2);
                }

                etExpiry.setText(formatted);
                etExpiry.setSelection(formatted.length());
                isFormatting = false;
            }
        });
    }

    private void attemptPayment() {
        if (isProcessing) return;

        String cardholderName = ((EditText) findViewById(R.id.etCardholderName)).getText().toString().trim();
        String cardNumber = ((EditText) findViewById(R.id.etCardNumber)).getText().toString().replaceAll("\\s", "");
        String expiry = ((EditText) findViewById(R.id.etExpiry)).getText().toString().trim();
        String cvv = ((EditText) findViewById(R.id.etCvv)).getText().toString().trim();

        TextView errorView = findViewById(R.id.tvGatewayError);

        if (cardholderName.isEmpty()) {
            showError(errorView, "Enter the name on the card.");
            return;
        }
        if (!CardValidationUtils.isValidCardNumber(cardNumber)) {
            showError(errorView, "That card number doesn't look valid. Double-check it.");
            return;
        }
        if (!CardValidationUtils.isValidExpiry(expiry)) {
            showError(errorView, "Enter a valid, non-expired date (MM/YY).");
            return;
        }
        if (!CardValidationUtils.isValidCvv(cvv)) {
            showError(errorView, "CVV should be 3 or 4 digits.");
            return;
        }

        errorView.setVisibility(android.view.View.GONE);
        processPayment();
    }

    private void showError(TextView errorView, String message) {
        errorView.setText(message);
        errorView.setVisibility(android.view.View.VISIBLE);
    }

    /** Simulates the brief delay of a real payment processor confirming a transaction. */
    private void processPayment() {
        isProcessing = true;
        Button payButton = findViewById(R.id.btnPayGateway);
        payButton.setEnabled(false);
        payButton.setText("Processing...");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            paymentRepository.getPaymentByAppointment(appointmentId, new FirestoreSingleCallback<Payment>() {
                @Override
                public void onSuccess(Payment existing) {
                    paymentRepository.markAsPaid(existing.paymentId, "Paid", today);
                    onPaymentSuccess();
                }

                @Override
                public void onFailure(Exception e) {
                    Payment payment = new Payment(appointmentId, amount, "Paid", today, "Card");
                    paymentRepository.insertPayment(payment);
                    onPaymentSuccess();
                }
            });
        }, 1500);
    }

    private void onPaymentSuccess() {
        Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}