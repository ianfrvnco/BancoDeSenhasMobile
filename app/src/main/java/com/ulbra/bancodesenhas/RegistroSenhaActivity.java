package com.ulbra.bancodesenhas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroSenhaActivity extends AppCompatActivity {

    TextView txtTitulo;
    EditText edtNome, edtLogin, edtSenha, edtSenhaConfirma;
    Button btnAdicionar;

    private void carregarAtributos(){
        txtTitulo = findViewById(R.id.txtTitulo);
        edtNome = findViewById(R.id.edtNome);
        edtLogin = findViewById(R.id.edtLogin);
        edtSenha = findViewById(R.id.edtSenha);
        edtSenhaConfirma = findViewById(R.id.edtSenhaConfirma);
        btnAdicionar = findViewById(R.id.btnAdicionar);
    }

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
            String senhaHash = senha; // Substitua por hash real (ex: bcrypt)

            // Obter UID do usuário logado
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Criar objeto para salvar no Firestore
            Map<String, Object> dadosSenha = new HashMap<>();
            dadosSenha.put("nome", nome);
            dadosSenha.put("login", login);
            dadosSenha.put("senha_hash", senhaHash);
            dadosSenha.put("uid", uid); // FK para o usuário

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("senhas")
                    .add(dadosSenha)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(RegistroSenhaActivity.this, "Senha registrada com sucesso!", Toast.LENGTH_SHORT).show();
                        // Você pode limpar os campos ou redirecionar aqui
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(RegistroSenhaActivity.this, "Erro ao registrar senha", Toast.LENGTH_SHORT).show();
                    });
        }
    });
}


/*
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /senhas/{senhaId} {
      allow read, write: if request.auth.uid == resource.data.uid;
    }
  }
}
 */

// implementation 'com.google.firebase:firebase-firestore:24.9.1'