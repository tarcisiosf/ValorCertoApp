package br.com.valorcerto.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.com.valorcerto.app.R;
import br.com.valorcerto.app.data.User;
import br.com.valorcerto.app.data.UserDao;
import br.com.valorcerto.app.ValorCertoApp;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        // Obtém o DAO
        userDao = ValorCertoApp.getDatabase().userDao();

        //  Vai para RegisterActivity
        tvRegisterLink.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        // Lógica de login
        btnLogin.setOnClickListener(v -> {
            String email    = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            boolean valid = true;
            if (email.isEmpty()) {
                etEmail.setError("E-mail é obrigatório");
                valid = false;
            }
            if (password.isEmpty()) {
                etPassword.setError("Senha é obrigatória");
                valid = false;
            }
            if (!valid) return;

            // Consulta em background
            new Thread(() -> {
                User user = userDao.findByEmail(email);
                runOnUiThread(() -> {
                    if (user != null && user.getPasswordHash().equals(password)) {
                        // Login com sucesso — abre MainActivity
                        Toast.makeText(this, "Bem-vindo, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        etPassword.setError("E-mail ou senha inválidos");
                    }
                });
            }).start();
        });
    }
}