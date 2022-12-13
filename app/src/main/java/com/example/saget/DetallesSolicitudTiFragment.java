package com.example.saget;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetallesSolicitudTiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetallesSolicitudTiFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase;
    TextView textViewSoliNumber,textViewFechaSoli,nombreEquipoSolicitudTI,tiempoTIPrestamo,cursosolicitudTi,programassolicitudTi,motivosolicitudTi,detallessolicitudTi;
    ImageView imageViewDniSoliUser;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference imageRef = firebaseStorage.getReference();
    Fragment solifFragmentTi =new SolicitudesFragmentTi();
    Fragment mapsFragment =new MapsFragment();
    int stock;
    String keyequipo;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String key;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetallesSolicitudTiFragment(String keySoliTi) {
        this.key=keySoliTi;
    }

    public DetallesSolicitudTiFragment() {
        // Required empty public constructor
    }




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetallesSolicitudTiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetallesSolicitudTiFragment newInstance(String param1, String param2) {
        DetallesSolicitudTiFragment fragment = new DetallesSolicitudTiFragment();
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
        View view=inflater.inflate(R.layout.fragment_detalles_solicitud_ti, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance(); // se obtiene la conexion a la db
        DatabaseReference ref = firebaseDatabase.getReference();
        //data
        textViewSoliNumber=view.findViewById(R.id.textViewSoliNumber);
        textViewFechaSoli=view.findViewById(R.id.textViewFechaSoli);
        nombreEquipoSolicitudTI=view.findViewById(R.id.nombreEquipoSolicitudTI);
        tiempoTIPrestamo=view.findViewById(R.id.tiempoTIPrestamo);
        cursosolicitudTi=view.findViewById(R.id.cursosolicitudTi);
        programassolicitudTi=view.findViewById(R.id.programassolicitudTi);
        motivosolicitudTi=view.findViewById(R.id.motivosolicitudTi);
        detallessolicitudTi=view.findViewById(R.id.detallessolicitudTi);
        imageViewDniSoliUser=view.findViewById(R.id.imageViewDniSoliUser);




        ref.child("prestamos/"+key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    SolicitudDePrestamo solicprestm = snapshot.getValue(SolicitudDePrestamo.class);

                    Log.d("msj-test",key);

                    textViewSoliNumber.setText(String.valueOf(key));
                    textViewFechaSoli.setText(solicprestm.getFechasoli());
                    ref.child("equipo/"+solicprestm.getEquipo()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue() != null){
                                Equipo equip = snapshot.getValue(Equipo.class);
                                String txto=solicprestm.getEquipo()+"-"+equip.getNombre()+"-"+equip.getMarca()+"-"+equip.getStock();
                                stock=equip.getStock();
                                keyequipo=solicprestm.getEquipo();
                                nombreEquipoSolicitudTI.setText(txto);
                            }else{
                                //error message
                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new InicioFragmentTi()).addToBackStack(null).commit();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            nombreEquipoSolicitudTI.setText(solicprestm.getEquipo());
                        }
                    });

                    tiempoTIPrestamo.setText(String.valueOf(solicprestm.getTiempoPrestamo()));
                    cursosolicitudTi.setText(String.valueOf(solicprestm.getCurso()));
                    programassolicitudTi.setText(String.valueOf(solicprestm.getProgramas()));
                    motivosolicitudTi.setText(String.valueOf(solicprestm.getMotivo()));
                    detallessolicitudTi.setText(String.valueOf(solicprestm.getDetalles()));

                    imageRef.child(solicprestm.getFoto()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Conseguimos el enlace de descarga
                            String downloadUrl = uri.toString();
                            Glide.with(view.getContext()).load(downloadUrl).into(imageViewDniSoliUser);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Manejamos algun error
                        }
                    });




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

        //btn aceptar solicitud
        View btnaceptar=view.findViewById(R.id.btnAceptarSoliTi);
        btnaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(view.getContext());
                alertDialog.setTitle("Observación");
                final EditText obsInput=new EditText(view.getContext());
                obsInput.setInputType(InputType.TYPE_CLASS_TEXT);
                alertDialog.setView(obsInput);
                alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ref.child("prestamos/"+key).child("observacion").setValue(obsInput.getText().toString());
                        //enviar a la vista del mapa gps con los puntos añadidos
                        Bundle bundle=new Bundle();
                        bundle.putString("keyPrestamo",key);
                        mapsFragment.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,mapsFragment).addToBackStack(null).commit();

                    }
                });
                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Accion cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.show();




            }
        });

        //btn denegar solicitud
        View btndenegar=view.findViewById(R.id.btnDenegarSoliTi);
        btndenegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(view.getContext());
                alertDialog.setTitle("Observación");
                final EditText obsInput=new EditText(view.getContext());
                obsInput.setInputType(InputType.TYPE_CLASS_TEXT);
                alertDialog.setView(obsInput);
                alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ref.child("prestamos/"+key).child("observacion").setValue(obsInput.getText().toString());
                        Map<String, Object> childupdate= new HashMap<>();
                        childupdate.put("estado","Denegado");
                        ref.child("prestamos/"+key).updateChildren(childupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Map<String, Object> childupdatenuev= new HashMap<>();
                                int nuevostocl=stock+1;
                                childupdatenuev.put("stock",nuevostocl);
                                ref.child("equipo/"+keyequipo).updateChildren(childupdatenuev).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "Se ha denegado la solicitud", Toast.LENGTH_SHORT).show();
                                        AppCompatActivity activity = (AppCompatActivity) getContext();
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,solifFragmentTi).addToBackStack(null).commit();
                                    }
                                });



                            }
                        });

                    }
                });
                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Accion cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.show();

            }
        });

        //btn retroceder listado solicitudes
        View btnbacksolilista=view.findViewById(R.id.imgBtnSolicBackListSoli);
        btnbacksolilista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new SolicitudesFragmentTi()).addToBackStack(null).commit();
            }
        });


        return view;
    }
}