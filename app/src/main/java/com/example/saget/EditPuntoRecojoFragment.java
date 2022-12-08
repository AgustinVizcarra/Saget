package com.example.saget;

import static android.app.Activity.RESULT_OK;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.SQLOutput;

public class EditPuntoRecojoFragment extends Fragment implements OnMapReadyCallback {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    StorageReference StorRef;
    DatabaseReference databaseReference;
    ConnectivityManager manager;
    NetworkInfo networkInfo;
    GoogleMap mMap;
    EditText coordenadasEdit;
    EditText descripcionEdit;
    Button btnEditar;
    Uri imageUri;
    ImageButton btnSubirFoto;
    String fileLink = "";
    ImageButton backToPuntosRecojo;
    PuntoRecojo puntoRecojo;

    public EditPuntoRecojoFragment() {
        // Required empty public constructor
    }

    public EditPuntoRecojoFragment(PuntoRecojo puntoRecojo) {
        this.puntoRecojo = puntoRecojo;
    }

    public static EditPuntoRecojoFragment newInstance(String param1, String param2) {
        EditPuntoRecojoFragment fragment = new EditPuntoRecojoFragment();
        return fragment;
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
        databaseReference = firebaseDatabase.getReference().child("punto_recojo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_punto_recojo, container, false);
        coordenadasEdit = (EditText) view.findViewById(R.id.ediTextCambiarCoordenadas);
        coordenadasEdit.setText(this.puntoRecojo.getCoordenadas());
        descripcionEdit = (EditText) view.findViewById(R.id.editTextCambiarDescripcion);
        descripcionEdit.setText(this.puntoRecojo.getDescripcion());
        btnSubirFoto = (ImageButton) view.findViewById(R.id.imgBtnEditarFoto);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapEditarPunto);
        mapFragment.getMapAsync(this);
        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                openImageLauncher.launch(intent);
            }
        });
        btnEditar = (Button) view.findViewById(R.id.btnEditarPuntoRecojo);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean sinImagen = imageUri == null;
                boolean sinCoordenadas = coordenadasEdit.getText().toString().equalsIgnoreCase("");
                boolean sinDescripcion = descripcionEdit.getText().toString().equalsIgnoreCase("");
                if (sinCoordenadas || sinDescripcion) {
                    if (sinCoordenadas) {
                        coordenadasEdit.setError("Debe ingresar coordenadas");
                        coordenadasEdit.setTextColor(Color.RED);
                    }
                    if (sinDescripcion) {
                        descripcionEdit.setError("Debe ingresar una descripcion");
                        descripcionEdit.setTextColor(Color.RED);
                    }
                } else {
                    if (sinImagen) {
                        //Quiere decir que se queda con la foto anterior
                        databaseReference.child(puntoRecojo.getKey()).child("descripcion").setValue(descripcionEdit.getText().toString());
                        databaseReference.child(puntoRecojo.getKey()).child("coordenadas").setValue(coordenadasEdit.getText().toString());
                    } else {
                        //Quiere decir que tendrá nueva foto
                        int numero = (int) (Math.random() * 11351 + 1);
                        String[] path = imageUri.toString().split("/");
                        String filename = path[path.length-1];
                        StorageReference storageReference = StorRef.child("puntoRecojo" + numero + filename);
                        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        fileLink = task.getResult().toString();
                                        //Actualizo
                                        databaseReference.child(puntoRecojo.getKey()).child("descripcion").setValue(descripcionEdit.getText().toString());
                                        databaseReference.child(puntoRecojo.getKey()).child("coordenadas").setValue(coordenadasEdit.getText().toString());
                                        databaseReference.child(puntoRecojo.getKey()).child("imagenes").setValue(fileLink);
                                        Toast.makeText(getContext(), "Punto de recojo actualizado exitosamente!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            ;
                        }).addOnFailureListener(e -> Log.d("msg-test", "error")).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("msg-test", "ruta Foto: " + task.getResult());
                            }
                        });
                    }
                }
            }
        });
        //Acabamos con el boton hacia atras
        backToPuntosRecojo = (ImageButton) view.findViewById(R.id.imgBtnBackListPuntosRecojoEdit);
        backToPuntosRecojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_admin, new PuntosRecojoFragment()).addToBackStack(null).commit();
            }
        });
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //Del valor obtenido lo que hago es tomar la latitud y longitud
        String[] coordenadas = this.puntoRecojo.getCoordenadas().split(",");
        LatLng editable = new LatLng(Double.parseDouble(coordenadas[0]), Double.parseDouble(coordenadas[1]));
        mMap.addMarker(new MarkerOptions().position(editable).title(this.puntoRecojo.getDescripcion()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(editable));
        this.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                coordenadasEdit.setText(latLng.latitude + "," + latLng.longitude);
                mMap.clear();
                LatLng marker = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(marker).title(""));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
            }
        });
        this.mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                coordenadasEdit.setText(latLng.latitude + "," + latLng.longitude);
                mMap.clear();
                LatLng marker = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(marker).title(""));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
            }
        });
    }
}