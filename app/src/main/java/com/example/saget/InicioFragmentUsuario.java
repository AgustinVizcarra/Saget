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

import java.util.ArrayList;

public class InicioFragmentUsuario extends Fragment {

    ArrayList<Equipo> listaEquipos = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View vista = inflater.inflate(R.layout.fragment_inicio_usuario, container, false);

        Equipo equipo1 = new Equipo(20,"Toshiba");
        Equipo equipo2 = new Equipo(30,"HP");
        Equipo equipo3 = new Equipo(40,"Asus");
        Equipo equipo4 = new Equipo(50,"Acer");
        listaEquipos.add(equipo1);
        listaEquipos.add(equipo2);
        listaEquipos.add(equipo3);
        listaEquipos.add(equipo4);



        /*DispositivosAdapter adapter = new DispositivosAdapter();
        adapter.setEquipoArrayList(listaEquipos);
        adapter.setContext(getContext());

        RecyclerView recyclerEquipos = vista.findViewById(R.id.recycleriniciousuario);
        recyclerEquipos.setAdapter(adapter);
        recyclerEquipos.setLayoutManager(new LinearLayoutManager(getContext()));*/

        return vista;
    }


}