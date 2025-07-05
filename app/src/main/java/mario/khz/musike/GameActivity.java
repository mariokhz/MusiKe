package mario.khz.musike;

import android.media.MediaPlayer;
import android.os.Bundle;

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
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

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

        // Cargar y reproducir un instrumento aleatorio
        String[] instrumentos = cargarInstrumentosDesdeAssets();
        if (instrumentos != null && instrumentos.length > 0) {
            String instrumentoAleatorio = seleccionarInstrumentoAleatorio(instrumentos);
            int recurso = getResources().getIdentifier(instrumentoAleatorio, "raw", getPackageName());
            mediaPlayer = MediaPlayer.create(this, recurso);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
        }
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