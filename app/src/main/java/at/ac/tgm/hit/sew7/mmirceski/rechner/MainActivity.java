package at.ac.tgm.hit.sew7.mmirceski.rechner;

import androidx.appcompat.app.AppCompatActivity;

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

        // Berechnen Button
        btnCalc.setOnClickListener(new View.OnClickListener() {
            /**
             * Diese Methode wird aufgerufen, wenn der Berechnen-Button gedrückt wird.
             * @param view Berechnen-Button.
             */
            @Override
            public void onClick(View view) {
                /* NumberFormat wird verwendet, damit das Locale berücksichtigt wird
                 * siehe https://developer.android.com/reference/java/text/NumberFormat */
                NumberFormat nf = NumberFormat.getInstance();

                // Text aus Textfeldern herausholen
                String editText1 = editVal1.getText().toString(),
                        editText2 = editVal2.getText().toString();
                // Sicherstellen, dass die Textfelder nicht leer sind.
                if (editText1.length() == 0 || editText2.length() == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.enter_both_values), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Text in Zahlen umwandeln (sollte eigentlich immer funktionieren)
                double val1 = 0.0, val2 = 0.0;
                try {
                    val1 = nf.parse(editText1).doubleValue();
                    val2 = nf.parse(editText2).doubleValue();
                } catch (ParseException e ) {
                    // Dieser Fall sollte in der Regel nicht eintreten, da das Eingabefeld eh nur Dezimalzahlen akzeptieren sollte.
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_number_format), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gewählte Rechenoperation herausfinden und durchführen
                double result = 0.0;
                if (rbAdd.isChecked())       result = val1 + val2;
                else if (rbSub.isChecked())  result = val1 - val2;
                else if (rbMul.isChecked())  result = val1 * val2;
                else if (rbDiv.isChecked())  result = val1 / val2;
                else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.select_operation), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Resultat anzeigen
                tvResult.setText(nf.format(result));
                // Negative Zahlen sollen rot sein
                if (result < 0.0)  tvResult.setTextColor(Color.RED);
                else tvResult.setTextColor(Color.WHITE);
            }
        });

        // Memory Store Button
        btnMS.setOnClickListener(new View.OnClickListener() {
            /**
             * Diese Methode wird aufgerufen, wenn der Memory Store Button gedrückt wird.
             * @param view Memory Store Button.
             */
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                // Es gibt ohnehin keine putDouble()-Methode, daher wird einfach direkt der String gespeichert.
                editor.putString("val1", editVal1.getText().toString());
                editor.putString("val2", editVal2.getText().toString());
                editor.putString("result", tvResult.getText().toString());

                String operation = "";
                if (rbAdd.isChecked())       operation = "+";
                else if (rbSub.isChecked())  operation = "-";
                else if (rbMul.isChecked())  operation = "*";
                else if (rbDiv.isChecked())  operation = "/";
                editor.putString("op", operation);

                editor.apply();

                // Der Wert wurde gespeichert, also soll ein kurzer Text "Gespeichert" erscheinen.
                Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });

        // Memory Recall Button
        btnMR.setOnClickListener(new View.OnClickListener() {
            /**
             * Diese Methode wird aufgerufen, wenn der Memory Recall Button gedrückt wird.
             * @param view Memory Recall Button.
             */
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                editVal1.setText(prefs.getString("val1", ""));
                editVal2.setText(prefs.getString("val2", ""));

                String result = prefs.getString("result", "");
                tvResult.setText(result);
                // Negative Zahlen sollen rot sein
                if (result.startsWith("-"))  tvResult.setTextColor(Color.RED);
                else tvResult.setTextColor(Color.WHITE);

                String operation = prefs.getString("op", "");
                rg.clearCheck();
                rbAdd.setChecked(operation.equals("+"));
                rbSub.setChecked(operation.equals("-"));
                rbMul.setChecked(operation.equals("*"));
                rbDiv.setChecked(operation.equals("/"));
            }
        });

        // Berühren des Ausgabefelds soll dieses löschen
        tvResult.setOnTouchListener(new View.OnTouchListener() {
            /**
             * Diese Methode wird aufgerufen, wenn das Ausgabefeld berührt wird.
             * @param v Ausgabefeld
             * @param event Bewegungsereignis (Finger, Stift, Maus, ...)
             * @return true, wenn das Ereignis behandelt wurde, sonst false.
             */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvResult.setText("");
                return true;
            }
        });
    }

    /**
     * Diese Methode wird aufgerufen, wenn die Aktivität gestartet wird (nach onCreate() oder onRestart()).
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Der Berechnen-Button und die Radiobuttons sind initial deaktiviert und werden hier aktiviert.
        btnCalc.setEnabled(true);
        rbAdd.setEnabled(true);
        rbSub.setEnabled(true);
        rbMul.setEnabled(true);
        rbDiv.setEnabled(true);
    }

    /**
     * Diese Methode wird aufgerufen, wenn die Aktivität wieder sichtbar ist und angezeigt wird (nach onStart() oder onPause()).
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Die Hintergrundfarbe des Berechnen-Buttons wird auf eine grüne Farbe gesetzt, um die Einsatzbereitschaft zu signalisieren
        btnCalc.setBackgroundColor(Color.parseColor("#1B7500"));
    }

}