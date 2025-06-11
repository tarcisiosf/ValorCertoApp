package br.com.valorcerto.app.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.valorcerto.app.R;
import br.com.valorcerto.app.data.Purchase;
import br.com.valorcerto.app.data.PurchaseDao;
import br.com.valorcerto.app.data.PurchaseItem;
import br.com.valorcerto.app.data.PurchaseItemDao;
import br.com.valorcerto.app.ValorCertoApp;
import br.com.valorcerto.app.domain.OnPeriodClickListener;
import br.com.valorcerto.app.domain.Period;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView rvPeriods;
    private PeriodAdapter adapter;
    private final List<Period> periods = new ArrayList<>();

    private PurchaseDao purchaseDao;
    private PurchaseItemDao purchaseItemDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1️⃣ Referencia o RecyclerView
        rvPeriods = findViewById(R.id.rvPeriods);

        // 2️⃣ Obtém instâncias dos DAOs
        purchaseDao     = ValorCertoApp.getDatabase().purchaseDao();
        purchaseItemDao = ValorCertoApp.getDatabase().purchaseItemDao();

        // 3️⃣ Configura o Adapter com o listener de clique e o LayoutManager
        adapter = new PeriodAdapter(
                periods,
                new OnPeriodClickListener() {
                    @Override
                    public void onPeriodClick(Period period) {
                        // Ao clicar num período, abre a MonthDetailActivity
                        Intent intent = new Intent(HistoryActivity.this, MonthDetailActivity.class);
                        intent.putExtra("period_label", period.getLabel());
                        startActivity(intent);
                    }
                }
        );
        rvPeriods.setLayoutManager(new LinearLayoutManager(this));
        rvPeriods.setAdapter(adapter);

        // 4️⃣ Carrega e agrupa os períodos
        loadPeriods();
    }

    /**
     * Carrega todas as compras do usuário, agrupa por mês/ano e preenche a lista de Period.
     */
    private void loadPeriods() {
        new Thread(() -> {
            // A. Busca todas as compras do usuário (ID = 1 como exemplo)
            List<Purchase> purchases = purchaseDao.findByUser(1);

            // B. Mapa temporário para somar o total por mês/ano
            Map<String, Double> map = new LinkedHashMap<>();
            SimpleDateFormat fmt = new SimpleDateFormat("MM/yyyy", Locale.getDefault());

            // C. Itera nas compras acumulando o total de cada período
            for (Purchase p : purchases) {
                String key = fmt.format(p.getPurchaseDate());  // ex: "04/2025"
                double existingSum = map.getOrDefault(key, 0.0);
                double purchaseSum = calculatePurchaseTotal(p.getPurchaseId());
                map.put(key, existingSum + purchaseSum);
            }

            // D. Converte o mapa em objetos Period e adiciona à lista
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                periods.add(new Period(entry.getKey(), entry.getValue()));
            }

            // E. Notifica o adapter na thread de UI
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
    }

    /**
     * Calcula o total de uma compra somando unitPrice * quantity em todos os itens.
     */
    private double calculatePurchaseTotal(int purchaseId) {
        List<PurchaseItem> items = purchaseItemDao.findByPurchase(purchaseId);
        double sum = 0;
        for (PurchaseItem item : items) {
            sum += item.getUnitPrice() * item.getQuantity();
        }
        return sum;
    }
}
