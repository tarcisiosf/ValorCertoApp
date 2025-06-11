package br.com.valorcerto.app.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

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
    private static volatile AppDatabase instance;

    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    public abstract PurchaseDao purchaseDao();
    public abstract PurchaseItemDao purchaseItemDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "valor_certo_db"
                            )
                            .addCallback(prepopulateCallback)
                            .allowMainThreadQueries()    // aqui
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

    private static final RoomDatabase.Callback prepopulateCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        ProductDao dao = instance.productDao();

                        // Produto 1: Nescau
                        Product nescau = new Product();
                        nescau.setProductCode("7891000379585");
                        nescau.setName("Nescau");
                        nescau.setPrice(11.29);
                        dao.insert(nescau);

                        // Produto 2: Toddy
                        Product toddy = new Product();
                        toddy.setProductCode("7894321711171");
                        toddy.setName("Toddy");
                        toddy.setPrice(7.49);
                        dao.insert(toddy);
                    });
                }
            };
}
