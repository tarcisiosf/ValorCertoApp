package br.com.valorcerto.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import br.com.valorcerto.app.R;
import br.com.valorcerto.app.data.Product;
import br.com.valorcerto.app.data.ProductDao;
import br.com.valorcerto.app.ValorCertoApp;

public class ScannerActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private ProductDao productDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // Referencia o view do scanner
        barcodeView = findViewById(R.id.barcode_scanner);
        // Inicializa o ProductDao
        productDao = ValorCertoApp.getDatabase().productDao();
        // Inicia uma leitura única
        barcodeView.decodeSingle(callback);
    }

    /**
     * Exibe diálogo de confirmação com nome, preço e imagem do produto.
     * Ao confirmar, retorna o código do produto para a lista de compras.
     */
    private void showProductDialog(Product product) {
        // Infla layout customizado para diálogo
        View view = getLayoutInflater().inflate(R.layout.dialog_product_confirmation, null);

        ImageView ivImage = view.findViewById(R.id.ivProductImage);
        TextView tvName   = view.findViewById(R.id.tvProductName);
        TextView tvPrice  = view.findViewById(R.id.tvProductPrice);

        // Preenche os campos do diálogo
        tvName.setText(product.getName());
        tvPrice.setText("R$ " + String.format("%.2f", product.getPrice()));
        // TODO: carregar imagem real se disponível, ex:
        // Glide.with(this).load(product.getImageUrl()).into(ivImage);

        // Constroi e exibe o diálogo
        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Adicionar", (dialog, which) -> {
                    // Retorna código do produto à Activity chamadora
                    Intent result = new Intent();
                    result.putExtra("scanned_product_code", product.getProductCode());
                    setResult(RESULT_OK, result);
                    finish();  // Fecha o Scanner
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    barcodeView.resume();  // Retoma leitura
                })
                .setOnCancelListener(d -> barcodeView.resume())
                .show();
    }

    // Callback chamado quando um código é lido
    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            String code = result.getText();
            if (code == null) return;

            barcodeView.pause();  // Pausa a leitura para processar

            // Busca o produto em background
            new Thread(() -> {
                Product product = productDao.findByCode(code);
                runOnUiThread(() -> {
                    if (product != null) {
                        // Produto encontrado: exibe diálogo completo
                        showProductDialog(product);
                    } else {
                        // Produto não encontrado na base local
                        Toast.makeText(
                                ScannerActivity.this,
                                "Produto não encontrado: " + code,
                                Toast.LENGTH_LONG
                        ).show();
                        barcodeView.resume();  // Retoma leitura
                    }
                });
            }).start();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            // Não utilizado, mas necessário para a interface
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // Retoma a câmera ao voltar para essa Activity
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausa a câmera para economizar recursos
        barcodeView.pause();
    }
}