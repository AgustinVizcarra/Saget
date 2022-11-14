package com.example.saget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class RequestFragmentUsuario extends Fragment {
    RecyclerView recycleview;
    SolicitudesAdapter adapter;

    public RequestFragmentUsuario(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View botonRetrocederCatalogo = view.findViewById(R.id.buttonBackSolicitudes);
        botonRetrocederCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonRetrocederCatalogo(view);
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request_usuario,container,false);

        recycleview = (RecyclerView) view.findViewById(R.id.recyclersolicitudusuario);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<SolicitudDePrestamo> options = new FirebaseRecyclerOptions.Builder<SolicitudDePrestamo>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("prestamos"),SolicitudDePrestamo.class)
                .build();

        adapter = new SolicitudesAdapter(options);
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

    public void botonRetrocederCatalogo(View view){
        //Falta implementar vista o fragmento
        //AppCompatActivity activity = (AppCompatActivity) getContext();
        //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario()).addToBackStack(null).commit();
    }
}