package mario.khz.musike;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.content.res.ColorStateList;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

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

        MaterialButton btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(FinishActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
