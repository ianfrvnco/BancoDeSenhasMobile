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

public class CadastroActivity extends AppCompatActivity {

    TextView txtTitulo;
    EditText edtEmail, edtSenha, edtSenhaConfirma;
    Button btnCadastrar;

    private void carregarAtributos(){
        Log.d("Tag", "Inicializando componentes da interface");
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtSenhaConfirma = findViewById(R.id.edtSenhaConfirma);
    }

    public void abrirLogin() {
        Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_cadastro);

        txtTitulo = findViewById(R.id.txtTitulo);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        FirebaseAuth fb = FirebaseAuth.getInstance();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarAtributos();

                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString().trim();
                String senhaConfirma = edtSenhaConfirma.getText().toString().trim();

                if(email.isEmpty() || senha.isEmpty() || senhaConfirma.isEmpty()){

                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();

                } else {
                    if(senha.equals(senhaConfirma)){

                        fb.createUserWithEmailAndPassword(email, senha)
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                        abrirLogin();
                                    } else {
                                        Toast.makeText(CadastroActivity.this, "Erro ao cadastrar!", Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else {
                        Toast.makeText(CadastroActivity.this, "Senhas não coincidem.", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }
    /*

*/
}
