package br.com.valorcerto.app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("SELECT * FROM products WHERE productCode = :code")
    Product findByCode(String code);

    @Query("SELECT * FROM products")
    List<Product> getAll();
}
