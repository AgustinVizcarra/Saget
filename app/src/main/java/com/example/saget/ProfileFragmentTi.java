package com.example.saget;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragmentTi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragmentTi extends Fragment {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseStorage storage;
    StorageReference StorRef;
    String imgurl;
    ArrayList<SlideModel> imageList = new ArrayList<>();
    ImageSlider imageSlider;
    ImageView imageView;
    //static int cont = 1;






    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragmentTi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragmentTi.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragmentTi newInstance(String param1, String param2) {
        ProfileFragmentTi fragment = new ProfileFragmentTi();
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
        View view = inflater.inflate(R.layout.fragment_profile_ti, container, false);

        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorRef = storageReference.child("PerfilTiFotos");
        imageView=view.findViewById(R.id.img_test);

        int numero = (int)(Math.random()*11351+1);
        ActivityResultLauncher<Intent> launcherPhotos = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() ==RESULT_OK) {
                        Uri uri = result.getData().getData();
                        Intent intent=result.getData();
                        if(intent!=null){
                            try {
                                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),intent.getData());
                                //set bitmap on image view
                                imageView.setImageBitmap(bitmap);
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
                                                                imgurl=fileLink;

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


                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    } else {
                        Toast.makeText(getContext(), "Debe seleccionar un archivo", Toast.LENGTH_SHORT).show();
                    }
                }
        );







        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //String correoUsuarioPrestamo = currentUser.getEmail();
        String correoUsuarioPrestamo = "a20181454@pucp.edu.pe";
        ImageButton botonCamara = view.findViewById(R.id.imageButtonPerfilTI);
        Button botonActualizar = view.findViewById(R.id.buttonactualizarPerfTI);
        EditText nombresTxT = view.findViewById(R.id.editTextTextNamePerfilTi);
        EditText apellidosTxt = view.findViewById(R.id.editTextTextApellPerfti);
        EditText correoTxt = view.findViewById(R.id.editTextCorrePerfti);
        imageSlider = view.findViewById(R.id.sliderPerfilTi);


        databaseReference.child("ti").orderByChild("correo").equalTo(correoUsuarioPrestamo).addListenerForSingleValueEvent(new ValueEventListener() {
            String keyUsuario;
            Usuario user;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren()){
                    Usuario usuario = children.getValue(Usuario.class);
                    boolean igualCorreo = usuario.getCorreo().equals(correoUsuarioPrestamo);
                    if(igualCorreo){
                        keyUsuario = children.getKey();
                        user = usuario;
                        break;
                    }

                }
                imageList.clear();
                botonCamara.setVisibility(View.INVISIBLE);

                nombresTxT.setText(user.getNombres());
                nombresTxT.setFocusable(false);

                apellidosTxt.setText(user.getApellidos());
                apellidosTxt.setFocusable(false);

                correoTxt.setText(user.getCorreo());
                correoTxt.setFocusable(false);



                imageList.add(new SlideModel(user.getImgurl(),null));
                imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);


                botonActualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        botonActualizar.setText("Guardar");
                        botonCamara.setVisibility(View.VISIBLE);

                        //nombresTxT.setFocusableInTouchMode(true);

                        //apellidosTxt.setFocusableInTouchMode(true);

                        //btn foto
                        botonCamara.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.setType("image/jpeg");
                                launcherPhotos.launch(intent);

                            }
                        });



                        botonActualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String nombresActualizados = nombresTxT.getText().toString();
                                String apellidosActualizados = apellidosTxt.getText().toString();
                                String imgUrlActualizado;
                                if(imgurl!=null){
                                     imgUrlActualizado=imgurl;
                                }else{
                                    imgUrlActualizado=user.getImgurl();
                                }

                                databaseReference.child("ti").child(keyUsuario).child("nombres").setValue(nombresActualizados);
                                databaseReference.child("ti").child(keyUsuario).child("apellidos").setValue(apellidosActualizados);
                                databaseReference.child("ti").child(keyUsuario).child("imgurl").setValue(imgUrlActualizado);
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new ProfileFragmentTi()).addToBackStack(null).commit();

                            }
                        });


                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error message
            }
        });


        return view;
    }



}