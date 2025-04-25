package br.com.valorcerto.app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PurchaseDao {
    @Insert
    long insert(Purchase purchase);

    @Query("SELECT * FROM purchases WHERE user_id = :userId ORDER BY purchase_date DESC")
    List<Purchase> findByUser(int userId);
}
