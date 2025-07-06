package mario.khz.musike;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class StudyActivity extends AppCompatActivity {

    private String[] instrumentos;
    private Map<String, String> displayNameMap;
    private StudyGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        instrumentos = cargarInstrumentosDesdeAssets();

        GridLayout grid = findViewById(R.id.studyGrid);
        adapter = new StudyGridAdapter(this, instrumentos, displayNameMap);
        adapter.populate(grid);
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
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String file = obj.getString("file");
                String name = obj.getString("name");
                instruments[i] = file;
                displayNameMap.put(file, name);
            }
            return instruments;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}
