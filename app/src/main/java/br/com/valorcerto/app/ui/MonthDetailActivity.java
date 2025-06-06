package br.com.valorcerto.app.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.valorcerto.app.R;
import br.com.valorcerto.app.data.Purchase;
import br.com.valorcerto.app.data.PurchaseDao;
import br.com.valorcerto.app.data.PurchaseItem;
import br.com.valorcerto.app.data.PurchaseItemDao;
import br.com.valorcerto.app.ValorCertoApp;
import br.com.valorcerto.app.domain.PurchaseSummary;

public class MonthDetailActivity extends AppCompatActivity {
    private RecyclerView rvPurchases;
    private PurchaseSummaryAdapter adapter;
    private final List<PurchaseSummary> summaries = new ArrayList<>();

    // DAOs
    private PurchaseDao purchaseDao;
    private PurchaseItemDao purchaseItemDao;

    // Formatadores
    private final SimpleDateFormat periodFmt =
            new SimpleDateFormat("MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat dateFmt =
            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_detail);

        // 1️⃣ Referencia RecyclerView
        rvPurchases = findViewById(R.id.rvPurchases);
        rvPurchases.setLayoutManager(new LinearLayoutManager(this));

        // 2️⃣ DAOs
        purchaseDao      = ValorCertoApp.getDatabase().purchaseDao();
        purchaseItemDao  = ValorCertoApp.getDatabase().purchaseItemDao();

        // 3️⃣ Adapter
        adapter = new PurchaseSummaryAdapter(summaries);
        rvPurchases.setAdapter(adapter);

        // 4️⃣ Recupera o período passado pela Intent
        String periodLabel = getIntent().getStringExtra("period_label");

        // 5️⃣ Carrega detalhes das compras daquele período
        loadMonthDetails(periodLabel);
    }

    private void loadMonthDetails(String periodLabel) {
        new Thread(() -> {
            try {
                Date periodDate = periodFmt.parse(periodLabel);
                // Busca todas as compras do usuário
                List<Purchase> purchases =
                        purchaseDao.findByUser(1); // Substituir com ID real

                for (Purchase p : purchases) {
                    // Filtra pelo mesmo mês/ano
                    if (periodFmt.format(p.getPurchaseDate()).equals(periodLabel)) {
                        // Calcula total da compra
                        double total = calculatePurchaseTotal(p.getPurchaseId());
                        summaries.add(new PurchaseSummary(p.getPurchaseDate(), total));
                    }
                }
                runOnUiThread(() -> adapter.notifyDataSetChanged());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private double calculatePurchaseTotal(int purchaseId) {
        List<PurchaseItem> items = purchaseItemDao.findByPurchase(purchaseId);
        double sum = 0;
        for (PurchaseItem it : items) {
            sum += it.getUnitPrice() * it.getQuantity();
        }
        return sum;
    }
}
