package com.example.saget;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class UsuariosFragment extends Fragment {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    UsuarioAdapter adapter;
    RecyclerView recyclerView;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference usuariosProfileStorage = storageReference.child("Usuarios");
    List<String> filenames = new ArrayList<>();
    SearchView txtBuscar;
    ValueEventListener queryListener;
    String option;
    EditarUsuarioTIFragment editar;
    FloatingActionButton filtrosUsuarios;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Query query = databaseReference.child("usuarios").orderByChild("rol").equalTo(1);
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);
        txtBuscar = (SearchView) view.findViewById(R.id.textBuscarU);
        txtBuscar.clearFocus();
        option = "";
        //Filtrado
        filtrosUsuarios = (FloatingActionButton) view.findViewById(R.id.floatingFiltrosU);
        filtrosUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), filtrosUsuarios);
                popupMenu.getMenuInflater().inflate(R.menu.filtros_admin_usuario, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.opcionU1:
                                txtBuscar.setQueryHint("Ingresar el cargo");
                                option="cargo";
                                return true;
                            case R.id.opcionU2:
                                txtBuscar.setQueryHint("Ingresar los nombres");
                                option="nombres";
                                return true;
                            case R.id.opcionU3:
                                txtBuscar.setQueryHint("Ingresar el apellidos");
                                option="apellidos";
                                return true;
                        }
                        return true;
                    }
                });
                //
                popupMenu.show();
            }
        });
        // Inflate the layout for this fragment
        txtBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textBuscar(s,option);
                return true;
            }
        });
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewUsuario);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Usuario> options = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(query,Usuario.class).build();
        adapter = new UsuarioAdapter(options);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void textBuscar(String s,String option){
        Log.d("msg received",s);
        Log.d("msg options",option);
        Query deffaultQuery = databaseReference.child("usuario").orderByChild("nombres").startAt(s).endAt(s+"~");
        Query query = databaseReference.child("usuario");
        switch (option){
            case "":
                FirebaseRecyclerOptions<Usuario> options = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(deffaultQuery,Usuario.class).build();
                adapter = new UsuarioAdapter(options);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
                break;
            case "cargo":
                FirebaseRecyclerOptions<Usuario> options1 = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(query.orderByChild("cargo").startAt(s).endAt(s+"~"),Usuario.class).build();
                adapter = new UsuarioAdapter(options1);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
                break;
            case "nombres":
                FirebaseRecyclerOptions<Usuario> options2 = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(query.orderByChild("nombres").startAt(s).endAt(s+"~"),Usuario.class).build();
                adapter = new UsuarioAdapter(options2);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
                break;
            case "apellidos":
                FirebaseRecyclerOptions<Usuario> options3 = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(query.orderByChild("apellidos").startAt(s).endAt(s+"~"),Usuario.class).build();
                adapter = new UsuarioAdapter(options3);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
                break;
            default:
                Toast.makeText(this.getContext(),"Error en la busqueda con filtros",Toast.LENGTH_LONG).show();
        }
    }

}