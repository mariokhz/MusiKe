package mario.khz.musike;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class RoundSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_selection);

        MaterialButton b5 = findViewById(R.id.button5);
        MaterialButton b10 = findViewById(R.id.button10);
        MaterialButton b15 = findViewById(R.id.button15);
        MaterialButton b20 = findViewById(R.id.button20);

        b5.setOnClickListener(v -> launchGame(5));
        b10.setOnClickListener(v -> launchGame(10));
        b15.setOnClickListener(v -> launchGame(15));
        b20.setOnClickListener(v -> launchGame(20));
    }

    private void launchGame(int rounds) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_MAX_ROUNDS, rounds);
        startActivity(intent);
        finish();
    }
}
