package com.example.saget;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PuntosRecojoFragment extends Fragment {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    PuntosRecojoAdapter puntosRecojoAdapter;
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_puntos_recojo, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerPuntoRecojo);
        Query query = databaseReference.child("punto_recojo").orderByChild("estado").equalTo(1);
        FirebaseRecyclerOptions<PuntoRecojo> options = new FirebaseRecyclerOptions.Builder<PuntoRecojo>().setQuery(query,PuntoRecojo.class).build();
        puntosRecojoAdapter = new PuntosRecojoAdapter(options);
        recyclerView.setAdapter(puntosRecojoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //Fab redirecccion a creacion de un nuevo punto de recojo
        FloatingActionButton fabAgregar = (FloatingActionButton) view.findViewById(R.id.floatingAregarPuntoRecojo);
        fabAgregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container_admin,new FormPuntoRecojoFragment()).addToBackStack(null).commit();
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        puntosRecojoAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        puntosRecojoAdapter.stopListening();
    }
}