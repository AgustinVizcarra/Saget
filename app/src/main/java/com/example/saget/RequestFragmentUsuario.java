package com.example.saget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class RequestFragmentUsuario extends Fragment {
    RecyclerView recycleview;
    SolicitudesAdapter adapter;
    String filtrador;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public RequestFragmentUsuario(){

    }

    public RequestFragmentUsuario(String filtrador){
        this.filtrador = filtrador;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request_usuario, container, false);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String keyusuario = currentUser.getUid();

        ImageButton botonFiltrar = view.findViewById(R.id.imageButton6);

        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                MessageBuscadorFragmentUsuario messageBuscadorFragmentUsuario = new MessageBuscadorFragmentUsuario();
                messageBuscadorFragmentUsuario.show(activity.getSupportFragmentManager(),"My buscador fragment");
            }
        });


        if(filtrador == null){
            recycleview = (RecyclerView) view.findViewById(R.id.recyclersolicitudusuario);
            recycleview.setLayoutManager(new LinearLayoutManager(getContext()));

            FirebaseRecyclerOptions<SolicitudDePrestamo> options = new FirebaseRecyclerOptions.Builder<SolicitudDePrestamo>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("prestamos").orderByChild("usuario").equalTo(keyusuario),SolicitudDePrestamo.class)
                    .build();

            adapter = new SolicitudesAdapter(options);
            recycleview.setAdapter(adapter);

        }else{
            recycleview = (RecyclerView) view.findViewById(R.id.recyclersolicitudusuario);
            recycleview.setLayoutManager(new LinearLayoutManager(getContext()));

            FirebaseRecyclerOptions<SolicitudDePrestamo> options = new FirebaseRecyclerOptions.Builder<SolicitudDePrestamo>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("prestamos").orderByChild("estado").equalTo(filtrador),SolicitudDePrestamo.class)
                    .build();
            adapter = new SolicitudesAdapter(options);
            recycleview.setAdapter(adapter);

        }

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