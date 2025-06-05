package br.com.valorcerto.app.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


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

@TypeConverters({ Converters.class })
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    public abstract PurchaseDao purchaseDao();
    public abstract PurchaseItemDao purchaseItemDao();
}
