package at.ac.tgm.hit.sew7.mmirceski.rechner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.NumberFormat;


import com.example.rechner.R;

public class MainActivity extends AppCompatActivity {
    private EditText editVal1, editVal2;
    private RadioGroup rg;
    private RadioButton rbAdd, rbSub, rbMul, rbDiv;
    private TextView tvResult;
    private Button btnCalc, btnMS, btnMR;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI-Widgets initialisieren
        editVal1 = findViewById(R.id.edit_value1);
        editVal2 = findViewById(R.id.edit_value2);

        rg = findViewById(R.id.radioGroup);
        rbAdd = findViewById(R.id.radioButton_add);
        rbSub = findViewById(R.id.radioButton_sub);
        rbMul = findViewById(R.id.radioButton_mul);
        rbDiv = findViewById(R.id.radioButton_div);

        tvResult = findViewById(R.id.textView_result);

        btnCalc = findViewById(R.id.button_calc);
        btnMS = findViewById(R.id.button_ms);
        btnMR = findViewById(R.id.button_mr);


        btnCalc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NumberFormat nf = NumberFormat.getInstance();


                String editText1 = editVal1.getText().toString(),
                        editText2 = editVal2.getText().toString();

                if (editText1.length() == 0 || editText2.length() == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.enter_both_values), Toast.LENGTH_SHORT).show();
                    return;
                }

                double val1 = 0.0, val2 = 0.0;
                try {
                    val1 = nf.parse(editText1).doubleValue();
                    val2 = nf.parse(editText2).doubleValue();
                } catch (ParseException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_number_format), Toast.LENGTH_SHORT).show();
                    return;
                }


                double result = 0.0;
                if (rbAdd.isChecked()) result = val1 + val2;
                else if (rbSub.isChecked()) result = val1 - val2;
                else if (rbMul.isChecked()) result = val1 * val2;
                else if (rbDiv.isChecked()) result = val1 / val2;
                else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.select_operation), Toast.LENGTH_SHORT).show();
                    return;
                }

                tvResult.setText(nf.format(result));
                if (result < 0.0) tvResult.setTextColor(Color.RED);
                else tvResult.setTextColor(Color.WHITE);
            }
        });


        btnMS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                editor.putString("result", tvResult.getText().toString());
                editor.apply();


                Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });


        btnMR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                String result = prefs.getString("result", "");
                tvResult.setText(result);
                // Negative Zahlen sollen rot sein
                if (result.startsWith("-")) tvResult.setTextColor(Color.RED);
                else tvResult.setTextColor(Color.WHITE);
            }
        });


        tvResult.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvResult.setText("");
                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


        rbAdd.setEnabled(true);
        rbSub.setEnabled(true);
        rbMul.setEnabled(true);
        rbDiv.setEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}