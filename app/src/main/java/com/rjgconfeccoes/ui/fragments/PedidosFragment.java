package com.rjgconfeccoes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.rjgconfeccoes.R;

public class PedidosFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        return view;
    }


//        toolbar = findViewById(R.id.pedidos_toolbar);

//        Button btnDeslogar = findViewById(R.id.btnDeslogar);
//        btnDeslogar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                autenticacao.signOut();
//                vaiparatelalogin();
//            }
//        });

    private void vaiparatelalogin() {
//        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
//        startActivity(intent);
    }
}