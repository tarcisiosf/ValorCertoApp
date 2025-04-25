package br.com.valorcerto.app.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import br.com.valorcerto.app.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Aqui depois vamos inicializar RecyclerView, adapters etc.
    }
}
