package mario.khz.musike;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Color;
import android.content.res.ColorStateList;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;

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
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Arrays;

import com.google.android.material.button.MaterialButton;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_MAX_ROUNDS = "maxRounds";
    private int totalRounds;
    private MediaPlayer mediaPlayer;
    private CircleProgressView circleProgress;
    private Handler progressHandler = new Handler();
    private String correctInstrument;
    private String[] instrumentos;
    private String[] roundInstruments;
    private int roundCount = 0;
    private int correctCount = 0;
    private ColorStateList defaultBtnTint;
    private Map<String, String> displayNameMap;
    private Map<String, String> familyMap;
    private List<String> userAnswers = new ArrayList<>();
    private List<String> correctAnswers = new ArrayList<>();
    private TextView roundNumberText;
    private ImageView instrumentIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        // Obtener número de rondas de selección o usar 10 por defecto
        totalRounds = getIntent().getIntExtra(EXTRA_MAX_ROUNDS, 10);

        // Inicializar CircleProgressView
        circleProgress = findViewById(R.id.circleProgress);
        // Inicializar TextView de número de ronda
        roundNumberText = findViewById(R.id.roundNumberText);
        instrumentIcon = findViewById(R.id.instrumentIcon);
        // Guardar tint original de botones
        defaultBtnTint = ((MaterialButton) findViewById(R.id.option1)).getBackgroundTintList();

        // Ajustar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cargar y reproducir un instrumento aleatorio y preparar opciones
        instrumentos = cargarInstrumentosDesdeAssets();
        // Generar orden de instrumentos para cada ronda sin repeticiones
        generarOrdenRondas();
        startRound();
    }

    /**
     * Mezcla la lista de instrumentos y toma los primeros MAX_ROUNDS para asegurar sin repeticiones.
     */
    private void generarOrdenRondas() {
        List<String> list = new ArrayList<>(Arrays.asList(instrumentos));
        Collections.shuffle(list, new Random(System.currentTimeMillis()));
        roundInstruments = new String[totalRounds];
        for (int i = 0; i < totalRounds && i < list.size(); i++) {
            roundInstruments[i] = list.get(i);
        }
    }

    private void startRound() {
        if (roundCount >= totalRounds) {
            // Terminar juego: pasar listas de respuestas
            Intent intent = new Intent(this, FinishActivity.class);
            intent.putExtra("correctCount", correctCount);
            intent.putExtra("totalRounds", totalRounds);
            intent.putStringArrayListExtra("correctAnswers", new ArrayList<>(correctAnswers));
            intent.putStringArrayListExtra("userAnswers", new ArrayList<>(userAnswers));
            startActivity(intent);
            finish();
            return;
        }
        roundCount++;
        // Preparar instrumento y audio de la lista pre-generada
        correctInstrument = roundInstruments[roundCount - 1];
        // Actualizar número de ronda en UI
        roundNumberText.setText(String.valueOf(roundCount));
        // Actualizar icono de instrumento en círculo
        int iconRes = getResources().getIdentifier(correctInstrument, "drawable", getPackageName());
        instrumentIcon.setImageResource(iconRes);
        // Guardar instrumento correcto para tabla
        correctAnswers.add(displayNameMap.get(correctInstrument));
        int recurso = getResources().getIdentifier(correctInstrument, "raw", getPackageName());
        if (mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(this, recurso);
        mediaPlayer.setLooping(false);
        // Avanzar de ronda cuando termine el audio
        mediaPlayer.setOnCompletionListener(mp -> onAudioComplete());
        mediaPlayer.start();
        startProgressUpdater();
        setupOptions();
    }

    private void onAudioComplete() {
        // Similar a onOptionSelected sin feedback
        progressHandler.removeCallbacksAndMessages(null);
        resetOptionButtons();
        circleProgress.setProgress(0f);
        startRound();
    }

    private void startProgressUpdater() {
        final int duration = mediaPlayer.getDuration();
        progressHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int pos = mediaPlayer.getCurrentPosition();
                    circleProgress.setProgress((float) pos / duration);
                    progressHandler.postDelayed(this, 10);
                }
            }
        });
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
            displayNameMap = new HashMap<>();
            familyMap = new HashMap<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String file = obj.getString("file");
                String name = obj.getString("name");
                String family = obj.getString("family");
                instruments[i] = file;
                displayNameMap.put(file, name);
                familyMap.put(file, family);
            }
            return instruments;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupOptions() {
        // Preparamos los botones para la ronda
        resetOptionButtons();
        disableOptionButtons();
        // Construir lista de opciones: una correcta y tres incorrectas
        List<String> wrong = new ArrayList<>();
        // Solo opciones de la misma familia
        String family = familyMap.get(correctInstrument);
        for (String ins : instrumentos) {
            if (!ins.equals(correctInstrument) && familyMap.get(ins).equals(family)) {
                wrong.add(ins);
            }
        }
        Collections.shuffle(wrong);
        List<String> options = new ArrayList<>();
        options.add(correctInstrument);
        int needed = Math.min(3, wrong.size());
        for (int i = 0; i < needed; i++) {
            options.add(wrong.get(i));
        }
        // Si no hay suficientes de la misma familia, completar con otros
        if (options.size() < 4) {
            for (String ins : instrumentos) {
                if (!options.contains(ins)) {
                    options.add(ins);
                    if (options.size() == 4) break;
                }
            }
        }
        Collections.shuffle(options);
        // Asignar a botones con clave en tag y texto amigable
        Button b1 = findViewById(R.id.option1);
        Button b2 = findViewById(R.id.option2);
        Button b3 = findViewById(R.id.option3);
        Button b4 = findViewById(R.id.option4);
        b1.setTag(options.get(0)); b1.setText(displayNameMap.get(options.get(0))); b1.setClickable(true);
        b2.setTag(options.get(1)); b2.setText(displayNameMap.get(options.get(1))); b2.setClickable(true);
        b3.setTag(options.get(2)); b3.setText(displayNameMap.get(options.get(2))); b3.setClickable(true);
        b4.setTag(options.get(3)); b4.setText(displayNameMap.get(options.get(3))); b4.setClickable(true);
    }

    public void onOptionSelected(View view) {
        // Deshabilitar más clics en esta ronda
        disableOptionButtons();
        // Registrar respuesta del usuario para la tabla
        String key = (String) view.getTag();
        userAnswers.add(displayNameMap.get(key));
        // Luego comprobamos acierto/ fallo
        MaterialButton btn = (MaterialButton) view;
        if (key.equals(correctInstrument)) {
            correctCount++;
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            // Mostrar cuál era la opción correcta
            int[] ids = {R.id.option1, R.id.option2, R.id.option3, R.id.option4};
            for (int id : ids) {
                MaterialButton b = findViewById(id);
                if (correctInstrument.equals(b.getTag())) {
                    b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                    break;
                }
            }
            Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
        }
        // Detener audio y progress
        if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.stop();
        progressHandler.removeCallbacksAndMessages(null);
        // Esperar un segundo y pasar a siguiente ronda
        progressHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Reset botones y preparar siguiente ronda
                resetOptionButtons();
                disableOptionButtons();
                circleProgress.setProgress(0f);
                startRound();
            }
        }, 1000);
    }

    private void disableOptionButtons() {
        int[] ids = {R.id.option1, R.id.option2, R.id.option3, R.id.option4};
        for (int id : ids) {
            MaterialButton b = findViewById(id);
            b.setClickable(false);
        }
    }

    private void resetOptionButtons() {
        int[] ids = {R.id.option1, R.id.option2, R.id.option3, R.id.option4};
        for (int id : ids) {
            MaterialButton b = findViewById(id);
            b.setBackgroundTintList(defaultBtnTint);
            // Mantener texto y tint, solo desactivar clics
            b.setClickable(false);
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
        // Limpiar Handler
        progressHandler.removeCallbacksAndMessages(null);
    }
}