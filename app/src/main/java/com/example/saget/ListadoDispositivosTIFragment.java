package com.example.saget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListadoDispositivosTIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListadoDispositivosTIFragment extends Fragment {
    RecyclerView recycleview;
    DispositivosTIAdapter adapter;
    TextView txtViewTablet;
    int tipoint;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListadoDispositivosTIFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListadoDispositivosTIFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListadoDispositivosTIFragment newInstance(String param1, String param2) {
        ListadoDispositivosTIFragment fragment = new ListadoDispositivosTIFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listado_dispositivos_t_i,container,false);
        tipoint=1;
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados != null) {
            tipoint = datosRecuperados.getInt("tipo");

        }

        //cambiamos el titulo segun el tipo de equipo
        txtViewTablet=view.findViewById(R.id.txtViewTablet);
        switch (tipoint){
            case 1:
                txtViewTablet.setText("TABLET");
                break;
            case 2:
                txtViewTablet.setText("LAPTOP");
                break;
            case 3:
                txtViewTablet.setText("CELULAR");
                break;
            case 4:
                txtViewTablet.setText("MONITOR");
                break;
            default:
                txtViewTablet.setText("OTROS EQUIPOS");
                break;
        }

        //btn retroceder menu principal
        View btnRetroceder=view.findViewById(R.id.imageBtnTI);
        btnRetroceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new InicioFragmentTi()).addToBackStack(null).commit();
            }
        });
        //btn añadir equipo
        View btnadd=view.findViewById(R.id.imageBtnTiAdd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.frame_container_TI,new FormDispositivosFragment(tipoint))
                        .addToBackStack(null).commit();
            }
        });

        recycleview = (RecyclerView) view.findViewById(R.id.recycleListadoDispoTI);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Equipo> options = new FirebaseRecyclerOptions.Builder<Equipo>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("equipo")
                        .orderByChild("tipo").equalTo(tipoint),Equipo.class)
                .build();

        adapter = new DispositivosTIAdapter(options);
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