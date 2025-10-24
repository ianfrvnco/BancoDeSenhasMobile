package com.ulbra.bancodesenhas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    TextView txtTitulo;
    Button btnCadastrar;
    private RecyclerView recyclerView;
    private List<Banco> listaSenhas;
    private SenhasAdapter adapter;
    private DatabaseReference database;

    public void abrirRegistroSenhas() {
        Intent intent = new Intent(MenuActivity.this, RegistroSenhaActivity.class);
        startActivity(intent);
    }

    private void carregarDadosFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("senhas").child(uid).child("senhas");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listaSenhas.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String appName = item.getKey();
                    String login = item.child("login").getValue(String.class);
                    String senha = item.child("senha").getValue(String.class);

                    listaSenhas.add(new Banco(appName, login, senha));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Erro ao carregar dados", error.toException());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_menu);

        btnCadastrar = findViewById(R.id.btnCadastrar);
        txtTitulo = findViewById(R.id.txtTitulo);
        recyclerView = findViewById(R.id.recyclerView); // Certifique-se de que esse ID existe no XML
        database = FirebaseDatabase.getInstance().getReference();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaSenhas = new ArrayList<>();
        adapter = new SenhasAdapter(listaSenhas);
        recyclerView.setAdapter(adapter);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirRegistroSenhas();
            }
        });

        carregarDadosFirebase();

    }


}
