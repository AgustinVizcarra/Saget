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
 * Use the {@link FormEditDispositivosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormEditDispositivosFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    StorageReference StorRef;
    Fragment listadoDispositivosTIFragment = new ListadoDispositivosTIFragment();
    int tipoint;
    EditText nombreEditEquipo,marcaEditEquipo,stockEditEquipo,caracteristicasEditEquipo,equiposEditAdicion;
    Spinner spinnerFormEditEquipo;
    CheckBox checkBox;
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
    private String key;
    public FormEditDispositivosFragment(String keyEquipo,int tipoint){
        this.key = keyEquipo;
        this.tipoint=tipoint;
    }

    public FormEditDispositivosFragment(){

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormEditDispositivosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormEditDispositivosFragment newInstance(String param1, String param2) {
        FormEditDispositivosFragment fragment = new FormEditDispositivosFragment();
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
        View view=inflater.inflate(R.layout.fragment_form_edit_dispositivos, container, false);
        DatabaseReference ref = firebaseDatabase.getReference();
        //data
        spinnerFormEditEquipo=view.findViewById(R.id.spinnerFormEditEquipo);
        nombreEditEquipo=view.findViewById(R.id.editTextEditNameEquipo);
        marcaEditEquipo=view.findViewById(R.id.editTextEditMarcaEquipo);
        stockEditEquipo=view.findViewById(R.id.editTextEditStockEquipo);
        caracteristicasEditEquipo=view.findViewById(R.id.editTextEditCaracEquipo);
        equiposEditAdicion=view.findViewById(R.id.editTextEditAdiciEquipo);
        checkBox=view.findViewById(R.id.checkBoxEditFormDispo);
        ImageSlider imageSlider = view.findViewById(R.id.sliderdetalleEditequipo);


        ref.child("equipo/"+key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    Equipo equipoForm = snapshot.getValue(Equipo.class);

                    Log.d("msj-test",key);
                    Log.d("msj-test",equipoForm.getNombre());

                    nombreEditEquipo.setText(String.valueOf(equipoForm.getNombre()));
                    stockEditEquipo.setText(String.valueOf(equipoForm.getStock()));
                    marcaEditEquipo.setText(String.valueOf(equipoForm.getMarca()));
                    caracteristicasEditEquipo.setText(String.valueOf(equipoForm.getCaracteristicas()));
                    equiposEditAdicion.setText(String.valueOf(equipoForm.getEquiposAdicionales()));
                    spinnerFormEditEquipo.setSelection(equipoForm.getTipo());
                    checkBox.setSelected(equipoForm.getDisponibilidad()==1);

                    urls = (ArrayList<String>) equipoForm.getImagenes();

                    for(int i=1;i<urls.size();i++){
                        imageList.add(new SlideModel(urls.get(i),null));
                    }
                    imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);
                }else{
                    //error message
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new InicioFragmentTi()).addToBackStack(null).commit();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error message
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new InicioFragmentTi()).addToBackStack(null).commit();
            }
        });






        DatabaseReference refequipos = ref.child("equipo");


        //btn guardar data form
        View btnadd=view.findViewById(R.id.btnActualizarEquipoForm);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fine = true;
                int tipoEquipo=0;

                String nameEquipoStr=nombreEditEquipo.getText().toString().trim();
                //validaciones
                if(nameEquipoStr.isEmpty()){
                    nombreEditEquipo.requestFocus();
                    nombreEditEquipo.setError("No dejar en blanco");
                    fine=false;
                }
                String marcaEquipoStr=marcaEditEquipo.getText().toString().trim();
                if(marcaEquipoStr.isEmpty()){
                    marcaEditEquipo.requestFocus();
                    marcaEditEquipo.setError("No dejar en blanco");
                    fine=false;
                }

                String stockEquipoStr=stockEditEquipo.getText().toString();
                if(stockEquipoStr.isEmpty()){
                    stockEditEquipo.requestFocus();
                    stockEditEquipo.setError("No dejar en blanco");
                    fine=false;
                }
                int stockint=Integer.parseInt(stockEquipoStr);
                String caracteristicasEquipoStr=caracteristicasEditEquipo.getText().toString().trim();
                if(caracteristicasEquipoStr.isEmpty()){
                    caracteristicasEditEquipo.requestFocus();
                    caracteristicasEditEquipo.setError("No dejar en blanco");
                    fine=false;
                }

                String equiposAdicionStr=equiposEditAdicion.getText().toString().trim();
                String txtSpinnnerTipoEquipo=spinnerFormEditEquipo.getSelectedItem().toString();
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
                    refequipos.child(key).setValue(equipo).addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Equipo guardado correctamente", Toast.LENGTH_SHORT).show();
                    });
                    //4. se resetean los valores
                    nombreEditEquipo.setText("");
                    marcaEditEquipo.setText("");
                    stockEditEquipo.setText("");
                    caracteristicasEditEquipo.setText("");
                    equiposEditAdicion.setText("");
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
        View btnimgadd=view.findViewById(R.id.imgbtnEditsubir);
        btnimgadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/jpeg");
                launcherPhotos.launch(intent);

            }
        });

        //btn retroceder listado equipos
        View btnRetroceder=view.findViewById(R.id.imgBtnEditBackListEquipo);
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