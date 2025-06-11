package br.com.valorcerto.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.valorcerto.app.R;
import br.com.valorcerto.app.data.Product;
import br.com.valorcerto.app.data.Purchase;
import br.com.valorcerto.app.data.PurchaseDao;
import br.com.valorcerto.app.data.PurchaseItem;
import br.com.valorcerto.app.data.PurchaseItemDao;
import br.com.valorcerto.app.data.ProductDao;
import br.com.valorcerto.app.ValorCertoApp;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SCAN = 1001;
    private FloatingActionButton fabScanner;
    private Button btnFinalize;
    private TextView tvTotal;
    private RecyclerView rvShoppingList;
    private PurchaseAdapter adapter;
    private final List<PurchaseItem> items = new ArrayList<>();
    private PurchaseItemDao purchaseItemDao;
    private ProductDao productDao;
    private PurchaseDao purchaseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1️⃣ Referencia as views
        fabScanner = findViewById(R.id.fabScanner);
        btnFinalize = findViewById(R.id.btnFinalize);
        rvShoppingList = findViewById(R.id.rvShoppingList);
        tvTotal = findViewById(R.id.tvTotal);

        // 2️⃣ Instancia os DAOs
        purchaseItemDao = ValorCertoApp.getDatabase().purchaseItemDao();
        productDao     = ValorCertoApp.getDatabase().productDao();
        purchaseDao    = ValorCertoApp.getDatabase().purchaseDao();

        // 3️⃣ Configura RecyclerView + Adapter
        adapter = new PurchaseAdapter(items, productDao);
        rvShoppingList.setLayoutManager(new LinearLayoutManager(this));
        rvShoppingList.setAdapter(adapter);

        // 4️⃣ Inicializa total
        updateTotal();

        // 5️⃣ FAB: abre scanner
        fabScanner.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        });

        // 6️⃣ Botão Finalizar Compra
        btnFinalize.setOnClickListener(v -> {
            if (items.isEmpty()) {
                Toast.makeText(this,
                        "Nenhum item na lista para finalizar.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // Persiste a compra completa em background
            new Thread(() -> {
                // 6.1️⃣ Cria o registro Purchase
                Purchase purchase = new Purchase();
                purchase.setUserId(1);  // TODO: substituir pelo ID do usuário logado
                purchase.setPurchaseDate(new Date());
                long purchaseId = purchaseDao.insert(purchase);

                // 6.2️⃣ Persiste cada PurchaseItem vinculado
                for (PurchaseItem item : items) {
                    item.setPurchaseId((int) purchaseId);
                    purchaseItemDao.insert(item);
                }

                // 6.3️⃣ Limpa lista e atualiza UI
                runOnUiThread(() -> {
                    items.clear();
                    adapter.notifyDataSetChanged();
                    updateTotal();
                    Toast.makeText(this,
                            "Compra finalizada com sucesso!",
                            Toast.LENGTH_LONG
                    ).show();
                });
            }).start();
        });
    }

    // 7️⃣ Recebe resultado do scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK && data != null) {
            String productCode = data.getStringExtra("scanned_product_code");

            // Insere o item escaneado
            new Thread(() -> {
                // Cria PurchaseItem
                PurchaseItem item = new PurchaseItem();
                item.setProductCode(productCode);
                item.setQuantity(1);

                Product p = productDao.findByCode(productCode);
                item.setUnitPrice(p.getPrice());

                purchaseItemDao.insert(item);

                runOnUiThread(() -> {
                    items.add(item);
                    adapter.notifyItemInserted(items.size() - 1);
                    updateTotal();
                });
            }).start();
        }
    }

    // 8️⃣ Atualiza o TextView com o total da compra
    private void updateTotal() {
        double sum = 0;
        for (PurchaseItem i : items) {
            sum += i.getUnitPrice() * i.getQuantity();
        }
        tvTotal.setText("Total: R$ " + String.format("%.2f", sum));
    }
}
