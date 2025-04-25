package br.com.valorcerto.app;

import android.app.Application;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.annotation.NonNull;

import br.com.valorcerto.app.data.AppDatabase;
import br.com.valorcerto.app.data.Product;
import br.com.valorcerto.app.data.ProductDao;
public class ValorCertoApp extends Application {
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        // Instanciação do Room
        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "valorcerto-db"
        ).build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
