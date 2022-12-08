package com.example.saget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogoUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogoUsuarioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CatalogoUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CatalogoUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CatalogoUsuarioFragment newInstance(String param1, String param2) {
        CatalogoUsuarioFragment fragment = new CatalogoUsuarioFragment();
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
        View view=inflater.inflate(R.layout.fragment_catalogo_usuario, container, false);
        ImageView imageTabletUser = view.findViewById(R.id.imageTabletUser);
        imageTabletUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI, new InicioFragmentUsuario("1")).addToBackStack(null).commit();
            }
        });

        ImageView imageLaptopUser = view.findViewById(R.id.imageLaptopUser);
        imageLaptopUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new InicioFragmentUsuario("2")).addToBackStack(null).commit();
            }
        });

        ImageView imageCelularUSer = view.findViewById(R.id.imageCelularUser);
        imageCelularUSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new InicioFragmentUsuario("3")).addToBackStack(null).commit();
            }
        });
        ImageView iamgeMonitorUser = view.findViewById(R.id.imageMonitorUser);
        iamgeMonitorUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new InicioFragmentUsuario("4")).addToBackStack(null).commit();
            }
        });

        ImageView imageOtrosUser = view.findViewById(R.id.imageOtrosUser);
        imageOtrosUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new InicioFragmentUsuario("5")).addToBackStack(null).commit();
            }
        });
        return view;
    }
}