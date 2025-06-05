package br.com.valorcerto.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.valorcerto.app.R;
import br.com.valorcerto.app.data.Product;
import br.com.valorcerto.app.data.ProductDao;
import br.com.valorcerto.app.data.PurchaseItem;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {
    private final List<PurchaseItem> items;
    private final ProductDao productDao;

    public PurchaseAdapter(List<PurchaseItem> items, ProductDao productDao) {
        this.items = items;
        this.productDao = productDao;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItemImage;
        TextView tvItemName, tvItemPrice, tvItemQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemImage    = itemView.findViewById(R.id.ivItemImage);
            tvItemName     = itemView.findViewById(R.id.tvItemName);
            tvItemPrice    = itemView.findViewById(R.id.tvItemPrice);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_purchase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchaseItem item = items.get(position);
        Product product = productDao.findByCode(item.getProductCode());

        holder.tvItemName.setText(product.getName());
        holder.tvItemPrice.setText(String.format("R$ %.2f", item.getUnitPrice()));
        holder.tvItemQuantity.setText("x" + item.getQuantity());

        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
        } else {
            holder.ivItemImage.setImageResource(R.drawable.ic_placeholder);
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
