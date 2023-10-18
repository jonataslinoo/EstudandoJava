package com.rjgconfeccoes.api.client;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDataHelper {

    public static <T> void buscaListaDados(String path, final Class<T> dataType, final OnDataFetchedListener<List<T>> listener) {

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(path);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<T> dataList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    T data = snapshot.getValue(dataType);
                    dataList.add(data);
                }

                if (listener != null) {
                    listener.onDataFetched(dataList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (listener != null) {
                    listener.onCancelled(databaseError.toException());
                }
            }
        });
    }

    public static <T> void buscaObjetoUnico(String path, final Class<T> dataType, final OnDataFetchedListener<T> listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                T objet = dataSnapshot.getValue(dataType);
                if (listener != null) {
                    listener.onDataFetched(objet);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (listener != null) {
                    listener.onCancelled(databaseError.toException());
                }
            }
        });
    }
}