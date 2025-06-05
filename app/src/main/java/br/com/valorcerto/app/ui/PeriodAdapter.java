package br.com.valorcerto.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.valorcerto.app.R;
import br.com.valorcerto.app.domain.OnPeriodClickListener;
import br.com.valorcerto.app.domain.Period;

public class PeriodAdapter extends RecyclerView.Adapter<PeriodAdapter.ViewHolder> {
    private final List<Period> periods;
    private final OnPeriodClickListener listener;

    public PeriodAdapter(List<Period> periods, OnPeriodClickListener listener) {
        this.periods = periods;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPeriod;
        TextView tvTotalPeriod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPeriod       = itemView.findViewById(R.id.tvPeriod);
            tvTotalPeriod  = itemView.findViewById(R.id.tvTotalPeriod);
        }

        public void bind(Period period, OnPeriodClickListener listener) {
            tvPeriod.setText(period.getLabel());
            tvTotalPeriod.setText(
                    String.format("Total: R$ %.2f", period.getTotal())
            );
            itemView.setOnClickListener(v -> listener.onPeriodClick(period));
        }
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_period, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        holder.bind(periods.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }
}
