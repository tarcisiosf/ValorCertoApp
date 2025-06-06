package br.com.valorcerto.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import br.com.valorcerto.app.R;
import br.com.valorcerto.app.domain.PurchaseSummary;

public class PurchaseSummaryAdapter
        extends RecyclerView.Adapter<PurchaseSummaryAdapter.ViewHolder> {

    private final List<PurchaseSummary> summaries;
    // Formatador de data
    private final SimpleDateFormat dateFmt =
            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public PurchaseSummaryAdapter(List<PurchaseSummary> summaries) {
        this.summaries = summaries;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate  = itemView.findViewById(R.id.tvPurchaseDate);
            tvTotal = itemView.findViewById(R.id.tvPurchaseTotal);
        }
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_purchase_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {
        PurchaseSummary ps = summaries.get(position);
        holder.tvDate.setText(dateFmt.format(ps.getDate()));
        holder.tvTotal.setText(
                String.format("R$ %.2f", ps.getTotal()));
    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }
}
