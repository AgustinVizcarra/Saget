package com.example.saget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class MessageBuscadorFragmentUsuario extends DialogFragment {

    public MessageBuscadorFragmentUsuario() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.message_buscador_request_usuario, null);

        Button botonEnTramite = view.findViewById(R.id.botonentramite);
        Button botonAceptado = view.findViewById(R.id.botonaprobado);
        Button botonRechazado = view.findViewById(R.id.botonrechazado);

        botonEnTramite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario("En trámite")).addToBackStack(null).commit();
                dismiss();
            }
        });

        botonAceptado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario("Aprobado")).addToBackStack(null).commit();
                dismiss();
            }
        });

        botonRechazado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario("Rechazado")).addToBackStack(null).commit();
                dismiss();
            }
        });

        builder.setView(view);


        return builder.create();
    }





}
