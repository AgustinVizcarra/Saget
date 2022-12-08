package com.example.saget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MapsFragment extends Fragment {
    String keyPrestamo;
    private FirebaseDatabase firebaseDatabase;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            firebaseDatabase = FirebaseDatabase.getInstance(); // se obtiene la conexion a la db
            DatabaseReference ref = firebaseDatabase.getReference();
            ref.child("punto_recojo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot children : snapshot.getChildren()){
                        PuntoRecojo puntRecojo=children.getValue(PuntoRecojo.class);
                        String coord=puntRecojo.getCoordenadas();
                        String[] coordSeparados = coord.split(",");
                        double lat=Double.parseDouble(coordSeparados[0]);
                        double lon=Double.parseDouble(coordSeparados[1]);
                        LatLng marker = new LatLng(lat, lon);
                        googleMap.addMarker(new MarkerOptions().position(marker).title(puntRecojo.getDescripcion()));
                    }

                    LatLng cato = new LatLng(-12.070177725264962, -77.08001344814451);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cato,15));
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                            LatLng coordMarker = marker.getPosition();
                            String coordString=String.valueOf(coordMarker.latitude)+","+String.valueOf(coordMarker.longitude);
                            AlertDialog.Builder alertDialog=new AlertDialog.Builder(getContext());
                            alertDialog.setTitle("SAGET");
                            alertDialog.setMessage("Está seguro de escoger el punto de recojo:"+marker.getTitle()+" con coordenadas:"+ coordString);
                            alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ref.child("prestamos/"+keyPrestamo).child("punto_recojo").setValue(coordString);
                                    Map<String, Object> childupdate1= new HashMap<>();
                                    childupdate1.put("estado","Aprobado");
                                    ref.child("prestamos/"+keyPrestamo).updateChildren(childupdate1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Se aceptó la solicitud", Toast.LENGTH_SHORT).show();
                                            AppCompatActivity activity = (AppCompatActivity) getContext();
                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new SolicitudesFragmentTi()).addToBackStack(null).commit();
                                        }
                                    });
                                }
                            });
                            alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getContext(), "Accion cancelada", Toast.LENGTH_SHORT).show();
                                }
                            });
                            alertDialog.show();
                            return false;
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_maps, container, false);

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados != null) {
            keyPrestamo = datosRecuperados.getString("keyPrestamo");

        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}