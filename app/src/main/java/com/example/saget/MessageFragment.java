package com.example.saget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class MessageFragment extends DialogFragment{
   String titulo;
   String observacion;
   String solicitudID;

    public MessageFragment(String titulo, String observacion, String solicitudID) {
        this.titulo = titulo;
        this.observacion = observacion;
        this.solicitudID = solicitudID;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.message_request_usuario, null);

        TextView title = view.findViewById(R.id.namesolicitudmessage);
        title.setText(String.valueOf(titulo));

        TextView obs = view.findViewById(R.id.obs);
        if(observacion == null){
            obs.setText("Su solicitud aún está en trámite!");

        }else{
            obs.setText(observacion);
        }


        Button boton = view.findViewById(R.id.buttonObservacion);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new HistorialPrestamoFragmentUsuario(solicitudID)).addToBackStack(null).commit();
                dismiss();
            }
        });


        builder.setView(view);


        return builder.create();
    }



}