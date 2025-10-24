package com.ulbra.bancodesenhas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class SenhasAdapter extends RecyclerView.Adapter<SenhasAdapter.SenhaViewHolder> {
    private List<Banco> listaSenhas;

    public SenhasAdapter(List<Banco> listaSenhas) {
        this.listaSenhas = listaSenhas;
    }

    @NonNull
    @Override
    public SenhaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_senha, parent, false);
        return new SenhaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SenhaViewHolder holder, int position) {
        Banco appSenha = listaSenhas.get(position);
        holder.textAppName.setText(appSenha.getAppName());
        holder.textLogin.setText("Login: " + appSenha.getLogin());
        holder.textSenha.setText("Senha: " + appSenha.getSenha());
    }

    @Override
    public int getItemCount() {
        return listaSenhas.size();
    }

    static class SenhaViewHolder extends RecyclerView.ViewHolder {
        TextView textAppName, textLogin, textSenha;

        public SenhaViewHolder(@NonNull View itemView) {
            super(itemView);
            textAppName = itemView.findViewById(R.id.txtNomeApp);
            textLogin = itemView.findViewById(R.id.txtLogin);
            textSenha = itemView.findViewById(R.id.txtSenha);
        }
    }
}