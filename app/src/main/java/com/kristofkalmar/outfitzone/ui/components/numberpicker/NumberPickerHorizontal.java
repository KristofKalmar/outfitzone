package com.kristofkalmar.outfitzone.ui.components.numberpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.kristofkalmar.outfitzone.R;

import java.util.ArrayList;
import java.util.List;

public class NumberPickerHorizontal extends LinearLayout {
    private EditText et_number;
    public Button btn_less;
    public Button btn_more;
    private int min, max;

    List<OnClickListener> clickListeners = new ArrayList<>();

    public NumberPickerHorizontal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.number_picker_horizontal, this);

        et_number = findViewById(R.id.et_number);

        this.btn_less = findViewById(R.id.btn_less);
        btn_less.setOnClickListener(new AddHandler(-1));

        this.btn_more = findViewById(R.id.btn_more);
        btn_more.setOnClickListener(new AddHandler(1));
    }

    public void addListener(View.OnClickListener listener) {
        clickListeners.add(listener);
    }

    private class AddHandler implements OnClickListener {
        final int diff;

        public AddHandler(int diff) {
            this.diff = diff;
        }

        @Override
        public void onClick(View v) {
            int newValue = getValue() + diff;
            if (newValue < min) {
                newValue = min;
            } else if (newValue > max) {
                newValue = max;
            }
            et_number.setText(String.valueOf(newValue));

            for (View.OnClickListener listener : clickListeners) {
                listener.onClick(v);
            }
        }
    }

    public int getValue() {
        if (et_number != null) {
            try {
                final String value = et_number.getText().toString();
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
            }
        }
        return 0;
    }

    public void setValue(final int value) {
        if (et_number != null) {
            et_number.setText(String.valueOf(value));
        }
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}