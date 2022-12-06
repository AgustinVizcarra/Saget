package com.example.saget;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
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
    Fragment listadoDispositivosTIFragment = new ListadoDispositivosTIFragment();
    EditText nombreEquipo,marcaEquipo,stockEquipo,caracteristicasEquipo,equiposAdicion;
    Spinner spinnerFormEquipo;
    int tipoint;
    ArrayList<String> urls = new ArrayList<>();
    ArrayList<SlideModel> imageList = new ArrayList<>();



    //static int cont = 1;

    int numero = (int)(Math.random()*11351+1);
    ActivityResultLauncher<Intent> launcherPhotos = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() ==RESULT_OK) {
                    Uri uri = result.getData().getData();
                    StorageReference child = StorRef.child("photo" + numero + ".jpg");

                    child.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                            new OnCompleteListener<Uri>() {

                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    String fileLink = task.getResult().toString();
                                                    Log.d("url", fileLink);
                                                    urls.add(fileLink);
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(e -> Log.d("msg-test", "error"))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("msg-test", "ruta archivo: " + task.getResult());
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Debe seleccionar un archivo", Toast.LENGTH_SHORT).show();
                }
            }
    );

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FormDispositivosFragment(int tipoint) {
        this.tipoint=tipoint;
    }

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
        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorRef = storageReference.child("EquiposFotos");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form_dispositivos, container, false);
        DatabaseReference ref = firebaseDatabase.getReference();
        //data
        spinnerFormEquipo=view.findViewById(R.id.spinnerFormEquipo);
        nombreEquipo=view.findViewById(R.id.editTextNameEquipo);
        marcaEquipo=view.findViewById(R.id.editTextMarcaEquipo);
        stockEquipo=view.findViewById(R.id.editTextStockEquipo);
        caracteristicasEquipo=view.findViewById(R.id.editTextCaracEquipo);
        equiposAdicion=view.findViewById(R.id.editTextAdiciEquipo);
        CheckBox checkBox=view.findViewById(R.id.checkBoxFormDispo);
        ImageSlider imageSlider = view.findViewById(R.id.sliderAddEquipo);

        if(urls != null || urls.size() != 0){
            for(int i=1;i<urls.size();i++){
                imageList.add(new SlideModel(urls.get(i),null));
            }
            imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);
        }
        DatabaseReference refequipos = ref.child("equipo");
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
                //validaciones
                if(nameEquipoStr.isEmpty()){
                    nombreEquipo.requestFocus();
                    nombreEquipo.setError("No dejar en blanco");
                    fine=false;
                }
                String marcaEquipoStr=marcaEquipo.getText().toString().trim();
                if(marcaEquipoStr.isEmpty()){
                    marcaEquipo.requestFocus();
                    marcaEquipo.setError("No dejar en blanco");
                    fine=false;
                }

                String stockEquipoStr=stockEquipo.getText().toString();
                if(stockEquipoStr.isEmpty()){
                    stockEquipo.requestFocus();
                    stockEquipo.setError("No dejar en blanco");
                    fine=false;
                }
                int stockint=Integer.parseInt(stockEquipoStr);
                String caracteristicasEquipoStr=caracteristicasEquipo.getText().toString().trim();
                if(caracteristicasEquipoStr.isEmpty()){
                    caracteristicasEquipo.requestFocus();
                    caracteristicasEquipo.setError("No dejar en blanco");
                    fine=false;
                }

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
                    Bundle bundle=new Bundle();
                    bundle.putInt("tipo",tipoint);
                    listadoDispositivosTIFragment.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.frame_container_TI,listadoDispositivosTIFragment)
                            .addToBackStack(null).commit();


                }else{
                    Toast.makeText(getContext(), "Debe llenar correctamente los datos pedidos", Toast.LENGTH_SHORT).show();
                }


            }

        });











        //btn foto
        View btnimgadd=view.findViewById(R.id.imgbtnsubir);
        btnimgadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/jpeg");
                launcherPhotos.launch(intent);
                if(urls.size() != 0){
                    Log.d("msg-foto",urls.get(0));
                }

            }
        });

        //btn retroceder listado equipos
        View btnRetroceder=view.findViewById(R.id.imgBtnBackListEquipo);
        btnRetroceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("tipo",tipoint);
                listadoDispositivosTIFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,listadoDispositivosTIFragment).addToBackStack(null).commit();
            }
        });

        return view;
    }




}