package com.rjgconfeccoes.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.ui.adapters.AdapterPedidosFinalizados;
import com.rjgconfeccoes.ui.fragments.dummy.DummyContent;


public class PedidosFinalizadosFragment extends Fragment {

    public PedidosFinalizadosFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PedidosFinalizadosFragment newInstance(int columnCount) {
        PedidosFinalizadosFragment fragment = new PedidosFinalizadosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lst_pedidos_finalizados, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new AdapterPedidosFinalizados(DummyContent.ITEMS));
        }
        return view;
    }
}