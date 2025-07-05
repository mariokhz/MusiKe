package mario.khz.musike;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private String correctInstrument;
    private String[] instrumentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        // Ajustar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cargar y reproducir un instrumento aleatorio y preparar opciones
        instrumentos = cargarInstrumentosDesdeAssets();
        if (instrumentos != null && instrumentos.length > 0) {
            correctInstrument = seleccionarInstrumentoAleatorio(instrumentos);
            int recurso = getResources().getIdentifier(correctInstrument, "raw", getPackageName());
            mediaPlayer = MediaPlayer.create(this, recurso);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
                // Mostrar opciones de respuesta
                setupOptions();
            }
        }
    }

    private void setupOptions() {
        // Construir lista de opciones: una correcta y tres incorrectas
        List<String> wrong = new ArrayList<>();
        for (String ins : instrumentos) {
            if (!ins.equals(correctInstrument)) wrong.add(ins);
        }
        Collections.shuffle(wrong);
        List<String> options = new ArrayList<>();
        options.add(correctInstrument);
        for (int i = 0; i < 3 && i < wrong.size(); i++) {
            options.add(wrong.get(i));
        }
        Collections.shuffle(options);
        // Asignar a botones
        Button b1 = findViewById(R.id.option1);
        Button b2 = findViewById(R.id.option2);
        Button b3 = findViewById(R.id.option3);
        Button b4 = findViewById(R.id.option4);
        b1.setText(options.get(0));
        b2.setText(options.get(1));
        b3.setText(options.get(2));
        b4.setText(options.get(3));
    }

    public void onOptionSelected(View view) {
        String selected = ((Button) view).getText().toString();
        if (selected.equals(correctInstrument)) {
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
        }
        // Opcional: detener audio o pasar a siguiente ronda
        if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.stop();
    }

    private String[] cargarInstrumentosDesdeAssets() {
        try {
            InputStream is = getAssets().open("instruments.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            JSONObject root = new JSONObject(sb.toString());
            JSONArray arr = root.getJSONArray("instruments");
            String[] instruments = new String[arr.length()];
            for (int i = 0; i < arr.length(); i++) {
                instruments[i] = arr.getJSONObject(i).getString("file");
            }
            return instruments;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String seleccionarInstrumentoAleatorio(String[] instruments) {
        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(instruments.length);
        return instruments[index];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}