package br.com.valorcerto.app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface PurchaseItemDao {
    @Insert
    void insert(PurchaseItem item);

    @Query("SELECT * FROM purchase_items WHERE purchase_id = :purchaseId")
    List<PurchaseItem> findByPurchase(int purchaseId);

    @Query("DELETE FROM purchase_items WHERE purchase_id = :purchaseId")
    void deleteByPurchase(int purchaseId);
}
