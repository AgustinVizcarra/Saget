package com.example.saget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InicioFragmentUsuario extends Fragment {

    RecyclerView recycleview;
    DispositivosAdapter adapter;

    public InicioFragmentUsuario(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio_usuario,container,false);

        recycleview = (RecyclerView) view.findViewById(R.id.recycleriniciousuario);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Equipo> options = new FirebaseRecyclerOptions.Builder<Equipo>()
                .setQuery(FirebaseDatabase.getInstance("https://saget-d5557-default-rtdb.firebaseio.com/").getReference().child("equipo"),Equipo.class)
                .build();

        adapter = new DispositivosAdapter(options);
        recycleview.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}