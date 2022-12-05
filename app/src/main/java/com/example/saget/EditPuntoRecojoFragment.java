package com.example.saget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EditPuntoRecojoFragment extends Fragment {

    PuntoRecojo puntoRecojo;

    public EditPuntoRecojoFragment() {
        // Required empty public constructor
    }
    public EditPuntoRecojoFragment(PuntoRecojo puntoRecojo){
        this.puntoRecojo =puntoRecojo;
    }
    public static EditPuntoRecojoFragment newInstance(String param1, String param2) {
        EditPuntoRecojoFragment fragment = new EditPuntoRecojoFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_punto_recojo, container, false);
    }
}