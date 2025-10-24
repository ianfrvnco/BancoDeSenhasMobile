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

public class MainActivity extends AppCompatActivity {

    TextView txtTitulo, txtCadastrar;
    EditText edtEmail, edtSenha;
    Button btnLogar;

    private void carregarAtributos(){
        Log.d("Tag", "Inicializando componentes da interface");
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);

    }

    public void abrirMenu() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void abrirCadastro() {
        Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_login);

        FirebaseAuth fb = FirebaseAuth.getInstance();
        btnLogar = findViewById(R.id.btnLogar);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtCadastrar = findViewById(R.id.txtCadastrar);

        txtCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastro();
            }
        });


        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarAtributos();

                String email= edtEmail.getText().toString();
                String senha = edtSenha.getText().toString().trim();

                if(email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(MainActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    fb.signInWithEmailAndPassword(email, senha)
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                   // Toast.makeText(MainActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                                    // Obter UID do usuário autenticado
                                    String uid = fb.getCurrentUser().getUid();
                                    Log.d("UIDFirebase", "UID do usuário: " + uid);

                                    // Redirecionar para outra Activity
                                    abrirMenu();
                                } else {
                                    Toast.makeText(MainActivity.this, "Erro ao logar!" , Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
}