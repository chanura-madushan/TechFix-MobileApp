package com.example.techfix;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

/** Shared base class — applies a consistent slide/fade transition to every screen change and back navigation. */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}