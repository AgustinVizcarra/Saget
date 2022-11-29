package com.example.saget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioFragmentTi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragmentTi extends Fragment {
    Fragment listadoDispositivosTIFragment = new ListadoDispositivosTIFragment();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InicioFragmentTi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragmentTi.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioFragmentTi newInstance(String param1, String param2) {
        InicioFragmentTi fragment = new InicioFragmentTi();
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

        View view= inflater.inflate(R.layout.fragment_inicio_ti, container, false);
        ImageView imageTablet = view.findViewById(R.id.imageTablet);
        imageTablet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putInt("tipo",1);
                listadoDispositivosTIFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,listadoDispositivosTIFragment).addToBackStack(null).commit();
            }
        });

        ImageView imageLaptop = view.findViewById(R.id.imageLaptop);
        imageLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("tipo",2);
                listadoDispositivosTIFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,listadoDispositivosTIFragment).addToBackStack(null).commit();
            }
        });

        ImageView imageCelular = view.findViewById(R.id.imageCelular);
        imageCelular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("tipo",3);
                listadoDispositivosTIFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,listadoDispositivosTIFragment).addToBackStack(null).commit();
            }
        });
        ImageView iamgeMonitor = view.findViewById(R.id.imageMonitor);
        iamgeMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("tipo",4);
                listadoDispositivosTIFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,listadoDispositivosTIFragment).addToBackStack(null).commit();
            }
        });

        ImageView imageOtros = view.findViewById(R.id.imageOtros);
        imageOtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("tipo",5);
                listadoDispositivosTIFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,listadoDispositivosTIFragment).addToBackStack(null).commit();
            }
        });

        return view;
    }


}