package com.rjgconfeccoes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rjgconfeccoes.R;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.ui.adapters.AdapterProdutos;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ProdutosFragment extends Fragment {

    private AdapterProdutos adapter;
    private List<Produto> listProdutos = new ArrayList<>();
    private RecyclerView recyclerViewProdutos;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerProdutos;
    private AlertDialog alertDialog;
    private TextView tv_nao_existe_produto_fragment;

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerProdutos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerProdutos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);
        tv_nao_existe_produto_fragment = view.findViewById(R.id.tv_nao_existe_produto_fragment);

        //Abre o progresso de busca dos produtos
        alertDialog = Util.criaProgressBar(getContext(), "Carregando Produtos");
        alertDialog.show();

        //recupero os produtos salvos no banco
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PRODUTOS);
        valueEventListenerProdutos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //limpo minha lista de produtos
                listProdutos.clear();

                if (snapshot.getValue() != null) {
                    //listar produtos
                    for (DataSnapshot produtosBanco : snapshot.getChildren()) {
                        Produto produto = produtosBanco.getValue(Produto.class);
                        listProdutos.add(produto);
                    }

                    tv_nao_existe_produto_fragment.setVisibility(View.GONE);
                    recyclerViewProdutos.setVisibility(View.VISIBLE);
                } else {
                    tv_nao_existe_produto_fragment.setVisibility(View.VISIBLE);
                    recyclerViewProdutos.setVisibility(View.GONE);
                }

                //Recupero os dados do sistema e atualizo a lista de produtos e salvo nos dados
                Dados dados = Util.recuperaDados();
                dados.obtemListaProdutos().clear();
                dados.obtemListaProdutos().addAll(listProdutos);
                Util.defineDados(dados);

                //Atualiza lista e finaliza o progresso de busca dos produtos
                adapter.notifyDataSetChanged();
                Util.escondeProgressBar(alertDialog);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        configuraAdapter(view);
        return view;
    }

    private void configuraAdapter(View view) {
        adapter = new AdapterProdutos(getActivity(), listProdutos);
        recyclerViewProdutos = view.findViewById(R.id.recyclerview_produtos);
        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewProdutos.setAdapter(adapter);
    }
}