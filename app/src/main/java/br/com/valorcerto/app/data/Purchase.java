package br.com.valorcerto.app.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.util.Date;

@Entity(tableName = "purchases")
public class Purchase {
    @PrimaryKey(autoGenerate = true)
    private int purchaseId;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "purchase_date")
    private Date purchaseDate;

    public Purchase() { }

    // Getters e Setters
    public int getPurchaseId() {
        return purchaseId;
    }
    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
