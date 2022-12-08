package com.example.saget;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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


public class FormPuntoRecojoFragment extends Fragment implements OnMapReadyCallback {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    StorageReference StorRef;
    DatabaseReference databaseReference;
    Fragment listadoPuntosRecojo = new PuntosRecojoFragment();
    ConnectivityManager manager;
    NetworkInfo networkInfo;
    GoogleMap mMap;
    EditText coordenadas;
    EditText descripcion;
    Button btnAgregar;
    Uri imageUri;
    ImageButton btnSubirFoto;
    String fileLink = "";
    ImageButton backToPuntosRecojo;

    public FormPuntoRecojoFragment() {
        // Required empty public constructor
    }

    ActivityResultLauncher<Intent> openImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Toast.makeText(getContext(), "Se añadio la imagen exitosamente!", Toast.LENGTH_LONG).show();
            imageUri = result.getData().getData();
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        StorRef = storage.getReference().child("PuntosRecojo");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_punto_recojo, container, false);
        coordenadas = (EditText) view.findViewById(R.id.ediTextCoordenadas);
        descripcion = (EditText) view.findViewById(R.id.editTextDescripcion);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapAgregarPunto);
        mapFragment.getMapAsync(this);
        //Para Añadir la foto
        btnSubirFoto = (ImageButton) view.findViewById(R.id.imgBtnSubirFoto);
        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                openImageLauncher.launch(intent);
            }
        });
        //Registrar = ()
        btnAgregar = (Button) view.findViewById(R.id.btnAgregarPuntoRecojo);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verifico que todos los campos no sean nulos
                boolean sinImagen = imageUri == null;
                boolean sinCoordenadas = coordenadas.getText().toString().equalsIgnoreCase("");
                boolean sinDescripcion = descripcion.getText().toString().equalsIgnoreCase("");
                if (sinImagen || sinCoordenadas || sinDescripcion) {
                    if (sinImagen) {
                        Toast.makeText(getContext(), "Debe añadir una imagen!", Toast.LENGTH_LONG).show();
                    }
                    if (sinCoordenadas) {
                        coordenadas.setError("Debe ingresar coordenadas");
                        coordenadas.setTextColor(Color.RED);
                    }
                    if (sinDescripcion) {
                        descripcion.setError("Debe ingresar una descripcion");
                        descripcion.setTextColor(Color.RED);
                    }
                } else {
                    //Todo en orden
                    //Primero Guardo la imagen para posteriormente obtener su URL
                    int numero = (int) (Math.random() * 11351 + 1);
                    String[] path = imageUri.toString().split("/");
                    String filename = path[path.length-1];
                    StorageReference storageReference = StorRef.child("puntoRecojo" + numero + filename);
                    Log.d("msg",filename);
                    storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            fileLink = task.getResult().toString();
                                            //Una vez obtenido el enlace
                                            String keyPuntoRecojo = databaseReference.child("punto_recojo").push().getKey();
                                            PuntoRecojo puntoRecojo = new PuntoRecojo(descripcion.getText().toString(), coordenadas.getText().toString(), fileLink, keyPuntoRecojo, 1);
                                            databaseReference.child("punto_recojo").child(keyPuntoRecojo).setValue(puntoRecojo);
                                            //Salvado exitoso
                                            descripcion.setText("");
                                            coordenadas.setText("");
                                            imageUri = null;
                                            mMap.clear();
                                            LatLng catolica = new LatLng(-12.06876909848102, -77.07850266247988);
                                            mMap.addMarker(new MarkerOptions().position(catolica).title("PUCP"));
                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(catolica));
                                            Toast.makeText(getContext(), "Se añadio el Punto de Recojo exitosamente!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                };
                            }).addOnFailureListener(e -> Log.d("msg-test", "error"))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("msg-test", "ruta Foto: " + task.getResult());
                                }
                            });
                }
            }
        });
        //Finalmente regresamos a la vista de puntos de recojo
        backToPuntosRecojo = (ImageButton) view.findViewById(R.id.imgBtnBackListPuntosRecojo);
        backToPuntosRecojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng catolica = new LatLng(-12.06876909848102, -77.07850266247988);
        mMap.addMarker(new MarkerOptions().position(catolica).title("PUCP"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(catolica));
        this.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                coordenadas.setText(latLng.latitude + "," + latLng.longitude);
                mMap.clear();
                LatLng marker = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(marker).title(""));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
            }
        });
        this.mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                coordenadas.setText(latLng.latitude + "," + latLng.longitude);
                mMap.clear();
                LatLng marker = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(marker).title(""));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
            }
        });
    }
    public void onBackPressed(){
        AppCompatActivity activity = (AppCompatActivity)getContext();
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container_admin,new PuntosRecojoFragment()).addToBackStack(null).commit();
    }
}