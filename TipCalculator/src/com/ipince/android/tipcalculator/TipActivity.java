package com.ipince.android.tipcalculator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TipActivity extends Activity {

    private static final String TAG = "TipActivity";

    private final int TOAST_TIME_SECS = 2;

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
        // TODO: add rounded tip calculation
    }

    private void setTipAmount(int amountCents, int tipCents) {
        double total = (amountCents + tipCents)*1.0/100;
        tvTipAmount.setText(getString(R.string.tip_message,
                formatter.format(tipCents*1.0/100),
                formatter.format(total)));
    }

    private OnClickListener getListener(final double pct) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = etFullAmount.getText().toString();
                try {
                    Number num = formatter.parse(input);
                    int amountCents = (int) Math.round(num.doubleValue() * 100);
                    int tipCents = (int) Math.round(amountCents * pct);
                    setTipAmount(amountCents, tipCents);
                } catch (ParseException e) {
                    Log.i(TAG, "Invalid currency: " + input, e);
                    Toast.makeText(TipActivity.this, getString(R.string.amount_error_msg), TOAST_TIME_SECS).show();
                }
            }
        };
    }
}
