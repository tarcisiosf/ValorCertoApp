package br.com.valorcerto.app.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "purchase_items")
public class PurchaseItem {
    @PrimaryKey(autoGenerate = true)
    private int itemId;

    @ColumnInfo(name = "purchase_id")
    private int purchaseId;

    @ColumnInfo(name = "product_code")
    private String productCode;

    private int quantity;
    private double unitPrice;

    public PurchaseItem() { }

    // Getters e Setters
    public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getPurchaseId() {
        return purchaseId;
    }
    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
