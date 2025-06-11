package br.com.valorcerto.app.ui;

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
import br.com.valorcerto.app.data.AppDatabase;
import br.com.valorcerto.app.ValorCertoApp;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etRegisterEmail, etRegisterPassword;
    private Button btnRegister;
    private TextView tvLoginLink;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        etName = findViewById(R.id.etName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        //  Obtém o DAO do nosso AppDatabase
        userDao = ValorCertoApp.getDatabase().userDao();

        // Clique no link "Já tem conta? Faça login"
        tvLoginLink.setOnClickListener(v -> finish());

        // Clique no botão Cadastrar
        btnRegister.setOnClickListener(v -> {
            String name     = etName.getText().toString().trim();
            String email    = etRegisterEmail.getText().toString().trim();
            String password = etRegisterPassword.getText().toString();

            boolean valid = true;
            if (name.isEmpty()) {
                etName.setError("Nome é obrigatório");
                valid = false;
            }
            if (email.isEmpty()) {
                etRegisterEmail.setError("E-mail é obrigatório");
                valid = false;
            }
            if (password.isEmpty()) {
                etRegisterPassword.setError("Senha é obrigatória");
                valid = false;
            }
            if (!valid) return;

            //Cria objeto User e salva em background
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPasswordHash(password);

            new Thread(() -> {
                // ☆ Nova verificação: existe usuário com este e-mail?
                User existing = userDao.findByEmail(email);
                if (existing != null) {
                    // se sim, mostra erro no campo sem tentar inserir
                    runOnUiThread(() ->
                            etRegisterEmail.setError("E-mail já cadastrado")
                    );
                    return;
                }

                // se não existe, faz a inserção
                long id = userDao.insert(newUser);
                runOnUiThread(() -> {
                    if (id > 0) {
                        Toast.makeText(this,
                                "Cadastro realizado com sucesso!",
                                Toast.LENGTH_SHORT
                        ).show();
                        finish();  // volta ao LoginActivity
                    } else {
                        Toast.makeText(this,
                                "Erro ao cadastrar.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            }).start();
        });
    }
}