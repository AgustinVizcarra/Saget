package com.example.saget;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class InicioAdminFragment extends Fragment {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    UsuarioTIAdapter adapter;
    RecyclerView recyclerView;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference usuariosProfileStorage = storageReference.child("Usuarios");
    List<String> filenames = new ArrayList<>();
    SearchView txtBuscar;
    ValueEventListener queryListener;
    String option;
    EditarUsuarioTIFragment editar;
    View filtros;
    FloatingActionButton floatingFiltros;

    public InicioAdminFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //obtengo todos los archivos
        usuariosProfileStorage.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    filenames.add(item.getName());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewPersonalTI);
        txtBuscar = (SearchView) view.findViewById(R.id.textBuscar);
        option = "";
        //Llamo al floting action button
        floatingFiltros = (FloatingActionButton) view.findViewById(R.id.floatingFiltros);
        floatingFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerForContextMenu(view);
            }
        });
        txtBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textBuscar(s, option);
                return true;
            }
        });
        FirebaseRecyclerOptions<Usuario> options = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(databaseReference.child("ti").orderByChild("rol").equalTo("2"), Usuario.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UsuarioTIAdapter(options);
        recyclerView.setAdapter(adapter);
        //Floating button action -> Agregar
        FloatingActionButton fabAgregar = (FloatingActionButton) view.findViewById(R.id.floatingAgregar);
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hago uso del constructor vacio para no enviar data :v
                editar = new EditarUsuarioTIFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container_admin, editar).commit();
            }
        });
        return view;
    }

    public void textBuscar(String s, String option) {
        Query defaultQuery = databaseReference.child("ti").startAt(s).endAt(s + "~");
        Query query = databaseReference.child("ti");
        switch (option) {
            case "":
                FirebaseRecyclerOptions<Usuario> options = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(defaultQuery, Usuario.class).build();
                adapter = new UsuarioTIAdapter(options);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
                break;
            case "nombres":
                FirebaseRecyclerOptions<Usuario> options1 = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(query.orderByChild("nombres").startAt(s).endAt(s + "~"), Usuario.class).build();
                adapter = new UsuarioTIAdapter(options1);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
                break;
            case "apellidos":
                FirebaseRecyclerOptions<Usuario> options2 = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(query.orderByChild("apellidos").startAt(s).endAt(s + "~"), Usuario.class).build();
                adapter = new UsuarioTIAdapter(options2);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
                break;
            case "correo":
                FirebaseRecyclerOptions<Usuario> options3 = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(query.orderByChild("correo").startAt(s).endAt(s + "~"), Usuario.class).build();
                adapter = new UsuarioTIAdapter(options3);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
                break;
            default:
                Toast.makeText(this.getContext(), "Error en la busqueda con filtros", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int id = v.getId();
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.filtros_admin_ti,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.opcion1:
                txtBuscar.setQueryHint("Ingresar el nombre");
                option = "nombres";
                return true;
            case R.id.opcion2:
                txtBuscar.setQueryHint("Ingresar el apellido");
                option = "apellidos";
                return true;
            case R.id.opcion3:
                txtBuscar.setQueryHint("Ingresar el correo");
                option = "correo";
                return true;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}
