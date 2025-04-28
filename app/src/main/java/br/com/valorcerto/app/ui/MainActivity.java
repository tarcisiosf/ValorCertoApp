package br.com.valorcerto.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.valorcerto.app.R;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SCAN = 1001;
    private FloatingActionButton fabScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Referencia o FloatingActionButton
        fabScanner = findViewById(R.id.fabScanner);

        //  OnClick: abre ScannerActivity esperando um resultado
        fabScanner.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        });

        //  (Futuro) Aqui vamos inicializar RecyclerView e adapters
    }

    //  Captura o resultado do scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK && data != null) {
            //  Recupera o código do produto escaneado
            String productCode = data.getStringExtra("scanned_product_code");
            //  Feedback temporário (substituir depois por inserir na lista)
            Toast.makeText(this,
                    "Produto escaneado: " + productCode,
                    Toast.LENGTH_LONG
            ).show();

            // TODO: buscar o Product no DB, criar PurchaseItem e atualizar RecyclerView
        }
    }
}
