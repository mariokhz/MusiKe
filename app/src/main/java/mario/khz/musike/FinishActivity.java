package mario.khz.musike;

import android.content.Intent;
import android.os.Bundle;
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

        MaterialButton btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(FinishActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
