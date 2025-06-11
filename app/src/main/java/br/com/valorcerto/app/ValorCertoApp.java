package br.com.valorcerto.app;

import android.app.Application;

import br.com.valorcerto.app.data.AppDatabase;

public class ValorCertoApp extends Application {
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        // inicializa o Room ali, com o contexto da aplicação
        database = AppDatabase.getInstance(this);
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
