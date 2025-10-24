package com.ulbra.bancodesenhas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroSenhaActivity extends AppCompatActivity {

    TextView txtTitulo;
    EditText edtNome, edtLogin, edtSenha, edtSenhaConfirma;
    Button btnAdicionar;

    private void carregarAtributos() {
        txtTitulo = findViewById(R.id.txtTitulo);
        edtNome = findViewById(R.id.edtNome);
        edtLogin = findViewById(R.id.edtLogin);
        edtSenha = findViewById(R.id.edtSenha);
        edtSenhaConfirma = findViewById(R.id.edtSenhaConfirma);
        btnAdicionar = findViewById(R.id.btnAdicionar);
    }

    public void abrirMenu() {
        Intent intent = new Intent(RegistroSenhaActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_adicionar_senha);
        carregarAtributos();

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edtNome.getText().toString().trim();
                String login = edtLogin.getText().toString().trim();
                String senha = edtSenha.getText().toString().trim();
                String senhaConfirma = edtSenhaConfirma.getText().toString().trim();

                if (nome.isEmpty() || login.isEmpty() || senha.isEmpty() || senhaConfirma.isEmpty()) {
                    Toast.makeText(RegistroSenhaActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!senha.equals(senhaConfirma)) {
                    Toast.makeText(RegistroSenhaActivity.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Aqui você pode aplicar hash na senha antes de salvar
                String senhaHash = senha; // Substitua por hash real se quiser

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();

                    // Criar objeto Banco
                    Banco banco = new Banco(nome, login, senhaHash);

                    // Caminho: senhas/UID/senhas/nome_do_app
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("senhas")
                            .child(uid)
                            .child("senhas")
                            .child(nome);

                    ref.setValue(banco)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(RegistroSenhaActivity.this, "Senha registrada com sucesso!", Toast.LENGTH_SHORT).show();
                                abrirMenu();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(RegistroSenhaActivity.this, "Erro ao registrar senha", Toast.LENGTH_SHORT).show();
                                Log.e("Firebase", "Erro ao salvar senha", e);
                            });
                } else {
                    Toast.makeText(RegistroSenhaActivity.this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
