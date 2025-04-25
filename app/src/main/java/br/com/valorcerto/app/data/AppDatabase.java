package br.com.valorcerto.app.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

// Import do conversor que você criou
import br.com.valorcerto.app.data.Converters;

@Database(
        entities = {
                User.class,
                Product.class,
                Purchase.class,
                PurchaseItem.class
        },
        version = 1,
        exportSchema = false
)
// Indica ao Room que use os métodos de conversão de tipos em Converters
@TypeConverters({ Converters.class })
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    public abstract PurchaseDao purchaseDao();
    public abstract PurchaseItemDao purchaseItemDao();
}
