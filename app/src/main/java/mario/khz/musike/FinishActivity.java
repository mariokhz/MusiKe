package mario.khz.musike;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.content.res.ColorStateList;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        int correctCount = getIntent().getIntExtra("correctCount", 0);
        int totalRounds = getIntent().getIntExtra("totalRounds", 0);

        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setText(getString(R.string.result_text, correctCount, totalRounds));

        // Mostrar aciertos en button1 y errores en button2
        MaterialButton btnCorrect = findViewById(R.id.button1);
        btnCorrect.setText(String.valueOf(correctCount));
        btnCorrect.setTextColor(Color.parseColor("#4CAF50"));
        MaterialButton btnWrong = findViewById(R.id.button2);
        int wrongCount = totalRounds - correctCount;
        btnWrong.setText(String.valueOf(wrongCount));
        btnWrong.setTextColor(Color.parseColor("#F44336"));

        // Llenar tabla de respuestas
        TableLayout table = findViewById(R.id.answersTable);
        ArrayList<String> correctAnswers = getIntent().getStringArrayListExtra("correctAnswers");
        ArrayList<String> userAnswers = getIntent().getStringArrayListExtra("userAnswers");
        if (correctAnswers != null && userAnswers != null) {
            for (int i = 0; i < correctAnswers.size(); i++) {
                String correct = correctAnswers.get(i);
                String user = userAnswers.get(i);
                TableRow row = new TableRow(this);
                row.setGravity(Gravity.CENTER_HORIZONTAL);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(8, 8, 8, 8);
                row.setLayoutParams(lp);

                TextView tvAnswer = new TextView(this);
                tvAnswer.setGravity(Gravity.CENTER);
                tvAnswer.setText(correct);
                tvAnswer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvAnswer.setTextColor(correct.equals(user)
                        ? Color.parseColor("#4CAF50")
                        : Color.parseColor("#F44336"));
                row.addView(tvAnswer);

                table.addView(row);
            }
        }

        MaterialButton btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(FinishActivity.this, RoundSelectionActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
