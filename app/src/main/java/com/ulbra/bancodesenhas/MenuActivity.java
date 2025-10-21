package com.ulbra.bancodesenhas;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_menu)

        RecyclerView recycler = findViewById(R.id.recyclerSenhas)
        Button btnCadastrar = findViewById(R.id.btnCadastrar)

        List<Senha> senhas = new ArrayList<>()
        senhas.add(new Senha(titulo: "Email Pessoal", email: "meuemail@gmail.com", senha: "123456"))
        senhas.add(new Senha(titulo: "Netflix", email: "usuario@netflix.com", senha: "senha123"))
        // Adicione mais senhas aqui

        recycler.setLayoutManager(new LinearLayoutManager(this))
        recycler.setAdapter(new SenhaAdapter(senhas, this))

        btnCadastrar.setOnClickListener {
            // Navegar para tela de adicionar senha
            startActivity(new Intent(this, AdicionarSenhaActivity.class))
        }
    }
}
