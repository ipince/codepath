package com.ipince.android.tipcalculator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TipActivity extends Activity {

    private static final String TAG = "TipActivity";

    private final int TOAST_TIME_SECS = 2;

    /**
     * Threshold used to decide whether to round tips up or down so that the total is a whole dollar
     * amount. Setting this to 50 would correspond to plain old rounding. Usually set below 50 so
     * that tips aren't impacted much.
     */
    private final int ROUNDING_THRESHOLD_CENTS = 25;

    private EditText etFullAmount;
    private Button bTipPctLeft;
    private Button bTipPctMiddle;
    private Button bTipPctRight;
    private TextView tvTipAmount;

    private final NumberFormat formatter = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        etFullAmount = (EditText) findViewById(R.id.etFullAmount);

        bTipPctLeft = (Button) findViewById(R.id.bTipPctLeft);
        bTipPctMiddle = (Button) findViewById(R.id.bTipPctMiddle);
        bTipPctRight = (Button) findViewById(R.id.bTipPctRight);

        tvTipAmount = (TextView) findViewById(R.id.tvTipAmount);

        bTipPctLeft.setOnClickListener(getListener(0.1));
        bTipPctMiddle.setOnClickListener(getListener(0.15));
        bTipPctRight.setOnClickListener(getListener(0.2));

        // TODO: add on change value listeners
    }

    private OnClickListener getListener(final double pct) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = etFullAmount.getText().toString();
                try {
                    Number num = formatter.parse(input);
                    int amountCents = (int) Math.round(num.doubleValue() * 100);
                    setTip(amountCents, pct, true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (ParseException e) {
                    Log.i(TAG, "Invalid currency: " + input, e);
                    Toast.makeText(TipActivity.this, getString(R.string.amount_error_msg), TOAST_TIME_SECS).show();
                }
            }
        };
    }

    private void setTip(int amountCents, double targetPct, boolean round) {
        // Calculate tip.
        int tipCents = (int) Math.round(amountCents * targetPct);
        if (round) {
            // Round total up if above threshold, otherwise down.
            int totalCents = amountCents + tipCents;
            int rem = totalCents % 100;
            if (rem > ROUNDING_THRESHOLD_CENTS) {
                tipCents = tipCents + 100 - rem;
            } else {
                tipCents = tipCents - rem;
            }
        }

        // Set UI.
        double actualPct = (tipCents * 1.0 / amountCents) * 100.0;
        double tip = tipCents / 100.0;
        double total = (amountCents + tipCents) / 100.0;
        tvTipAmount.setText(getString(R.string.tip_message,
                formatter.format(tip), formatter.format(actualPct),
                formatter.format(total)));
    }
}
