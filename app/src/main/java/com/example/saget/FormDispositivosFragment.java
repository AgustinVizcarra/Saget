package com.example.saget;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormDispositivosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormDispositivosFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    StorageReference StorRef;
    EditText nombreEquipo,marcaEquipo,stockEquipo,caracteristicasEquipo,equiposAdicion;
    Spinner spinnerFormEquipo;





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FormDispositivosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormDispositivosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormDispositivosFragment newInstance(String param1, String param2) {
        FormDispositivosFragment fragment = new FormDispositivosFragment();
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
        Random random = new Random();
        firebaseDatabase = FirebaseDatabase.getInstance(); // se obtiene la conexion a la db
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form_dispositivos, container, false);
        DatabaseReference ref = firebaseDatabase.getReference();
        DatabaseReference refequipos = ref.child("equipo");
        //data
        spinnerFormEquipo=view.findViewById(R.id.spinnerFormEquipo);
        nombreEquipo=view.findViewById(R.id.editTextNameEquipo);
        marcaEquipo=view.findViewById(R.id.editTextMarcaEquipo);
        stockEquipo=view.findViewById(R.id.editTextStockEquipo);
        caracteristicasEquipo=view.findViewById(R.id.editTextCaracEquipo);
        equiposAdicion=view.findViewById(R.id.editTextAdiciEquipo);
        CheckBox checkBox=view.findViewById(R.id.checkBoxFormDispo);

        //btn guardar data form
        View btnadd=view.findViewById(R.id.btnAddEquipoForm);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fine = true;
                int tipoEquipo=0;
                char randomizedCharacter1 = (char) (random.nextInt(26) + 'A');
                char randomizedCharacter2 = (char) (random.nextInt(26) + 'A');
                char randomizedCharacter3 = (char) (random.nextInt(26) + 'A');
                String identificador=String.valueOf(randomizedCharacter1)
                        +String.valueOf(randomizedCharacter2)+String.valueOf(randomizedCharacter3);
                String nameEquipoStr=nombreEquipo.getText().toString().trim();
                String marcaEquipoStr=marcaEquipo.getText().toString().trim();
                String stockEquipoStr=stockEquipo.getText().toString();
                int stockint=Integer.parseInt(stockEquipoStr);
                String caracteristicasEquipoStr=caracteristicasEquipo.getText().toString().trim();
                String equiposAdicionStr=equiposAdicion.getText().toString().trim();
                String txtSpinnnerTipoEquipo=spinnerFormEquipo.getSelectedItem().toString();
                switch (txtSpinnnerTipoEquipo){
                    case "Tablet":
                        tipoEquipo=1;
                        break;
                    case "Laptop":
                        tipoEquipo=2;
                        break;
                    case "Celular":
                        tipoEquipo=3;
                        break;
                    case "Otro":
                        tipoEquipo=4;
                        break;
                    default:
                        fine=false;
                }
                //disponibilidad
                int disponi;
                if(checkBox.isChecked()){
                    disponi=1;
                }else{
                    disponi=0;
                }
                if(fine){

                    Equipo equipo= new Equipo();
                    equipo.setNombre(nameEquipoStr);
                    equipo.setMarca(marcaEquipoStr);
                    equipo.setStock(stockint);
                    equipo.setCaracteristicas(caracteristicasEquipoStr);
                    equipo.setEquiposAdicionales(equiposAdicionStr);
                    equipo.setTipo(tipoEquipo);
                    equipo.setDisponibilidad(disponi);

                    // 3. se guardan los datos
                    refequipos.child(identificador).setValue(equipo).addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Equipo guardado correctamente", Toast.LENGTH_SHORT).show();
                    });
                    //4. se resetean los valores
                    nombreEquipo.setText("");
                    marcaEquipo.setText("");
                    stockEquipo.setText("");
                    caracteristicasEquipo.setText("");
                    equiposAdicion.setText("");
                    //5. cambiamos de vista
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.frame_container_TI,ListadoDispositivosTIFragment.newInstance("",""))
                            .addToBackStack(null).commit();


                }else{
                    Toast.makeText(getContext(), "Debe seleccionar un tipo de equipo", Toast.LENGTH_SHORT).show();
                }


            }

        });

        return view;
    }




}