package com.example.saget;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class EstadisticasAdminFragment extends Fragment {

    List<EquipoCompleto> equipos;
    List<SolicitudDePrestamo> prestamos;
    HashMap<EquipoCompleto,List<SolicitudDePrestamo>> asociacion;
    HashMap<String,Double> conteoPorEquipoPrestado;
    HashMap<String, Integer> conteoPorMarca;
    EquipoCompleto masPrestado;
    List<String> marcas;
    int cuentaTotal;

    public EstadisticasAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        equipos = new ArrayList<>();
        prestamos = new ArrayList<>();
        asociacion = new HashMap<>();
        conteoPorEquipoPrestado = new HashMap<>();
        conteoPorMarca = new HashMap<>();
        marcas = new ArrayList<>();
        cuentaTotal = 0;
        View view = inflater.inflate(R.layout.fragment_estadisticas_admin, container, false);
        FirebaseDatabase.getInstance().getReference().child("equipo").orderByChild("disponibilidad").equalTo(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Equipo aux = postSnapshot.getValue(Equipo.class);
                    equipos.add(new EquipoCompleto(aux,key));
                }
                FirebaseDatabase.getInstance().getReference().child("prestamos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            SolicitudDePrestamo aux = postSnapshot.getValue(SolicitudDePrestamo.class);
                            prestamos.add(aux);
                        }
                        //Aqui comienza mi camino
                        for(EquipoCompleto e:equipos){
                            List<SolicitudDePrestamo> prestamosAux = new ArrayList<>();
                            for(SolicitudDePrestamo s: prestamos){
                                if(e.getKey().equalsIgnoreCase(s.getEquipo())){
                                    prestamosAux.add(s);
                                    cuentaTotal+=1;
                                }
                            }
                            //Ya añadi todos los prestamos asociados a un equipo
                            asociacion.put(e,prestamosAux);
                            //Añado las marcas asociado a un equipo
                            if(!marcas.contains(e.getEquipo().getMarca())){
                                marcas.add(e.getEquipo().getMarca().toUpperCase());
                            }
                        }
                        //
                        double cuentaTablet = 0;
                        double cuentaLaptop = 0;
                        double cuentaMonitor = 0;
                        double cuentaCelular = 0;
                        double cuentaOtros = 0;
                        for (EquipoCompleto e : asociacion.keySet() ){
                            int cuenta = 0;
                            switch (e.getEquipo().getTipo()){
                                case 1:
                                    cuentaTablet+=asociacion.get(e).size()*100.0/cuentaTotal;
                                    break;
                                case 2:
                                    cuentaLaptop+=asociacion.get(e).size()*100.0/cuentaTotal;
                                    break;
                                case 3:
                                    cuentaMonitor+=asociacion.get(e).size()*100.0/cuentaTotal;
                                    break;
                                case 4:
                                    cuentaCelular+=asociacion.get(e).size()*100.0/cuentaTotal;
                                    break;
                                case 5:
                                    cuentaOtros+=asociacion.get(e).size()*100.0/cuentaTotal;
                                    break;
                                default:
                                    System.out.println("Esto no debería estar pasando!");
                                    break;
                            }
                        }
                        conteoPorEquipoPrestado.put("Tablet",cuentaTablet);
                        conteoPorEquipoPrestado.put("Laptop",cuentaLaptop);
                        conteoPorEquipoPrestado.put("Monitor",cuentaMonitor);
                        conteoPorEquipoPrestado.put("Celular",cuentaCelular);
                        conteoPorEquipoPrestado.put("Otros",cuentaOtros);
                        //Ahora para obtener la cantidad de equipos por marca
                        for(String marca : marcas){
                            int cuenta = 0;
                            for(EquipoCompleto eq : asociacion.keySet()){
                                if(eq.getEquipo().getMarca().toUpperCase().equalsIgnoreCase(marca)){
                                    //Aumenta la cuenta cada vez que haya un equipo asociado a un prestamo
                                    cuenta += 1;
                                }
                            }
                            conteoPorMarca.put(marca,cuenta);
                        }
                        //Para ver el equipo más prestado
                        List<Integer> conteoPorEquipo = new ArrayList<>();
                        for(EquipoCompleto e : asociacion.keySet()){
                            if(!conteoPorEquipo.contains(asociacion.get(e).size())){
                                if(conteoPorEquipo.isEmpty()){
                                    conteoPorEquipo.add(asociacion.get(e).size());
                                }else{
                                    if(Collections.max(conteoPorEquipo)<asociacion.get(e).size()){
                                        //Tengo un nuevo máximo
                                        if(e!=null){
                                            masPrestado = e;
                                            Log.d("msg",e.getEquipo().getNombre());
                                            conteoPorEquipo.add(asociacion.get(e).size());
                                        }
                                    }
                                }
                            }
                        }
                        //Ahora abordamos el tema de las vista
                        HorizontalBarChart horizontalBarChart = view.findViewById(R.id.horizontalBarChartPorMarca);
                        ArrayList<BarEntry> prestamosPorMarca = new ArrayList<>();
                        ArrayList<String> yVals = new ArrayList<>();
                        int counter = 0;

                        for(String marca:conteoPorMarca.keySet()){
                            Log.d("marca",marca);
                            Log.d("conto",String.valueOf(conteoPorMarca.get(marca)));
                            prestamosPorMarca.add(new BarEntry((float) counter,(float) conteoPorMarca.get(marca)));
                            yVals.add(marca);
                            counter += 1;
                        }
                        BarDataSet barDataSet = new BarDataSet(prestamosPorMarca," Prestamos ");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        barDataSet.setValueTextColor(Color.BLACK);
                        BarData data = new BarData(barDataSet);
                        XAxis xAxis = horizontalBarChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(yVals));
                        horizontalBarChart.setFitBars(true);
                        horizontalBarChart.setData(data);
                        horizontalBarChart.getDescription().setEnabled(false);
                        horizontalBarChart.animate();
                        //Ahora con los gráficos circulares
                        for(String equipo: conteoPorEquipoPrestado.keySet()){
                            switch (equipo){
                                case "Laptop":
                                    ArrayList<PieEntry> porcentaje = new ArrayList<>();
                                    PieChart pieChart = view.findViewById(R.id.pieChartLaptop);
                                    if(conteoPorEquipoPrestado.get(equipo)<1.0){
                                        porcentaje.add(new PieEntry(0,"%"));
                                    }else{
                                        porcentaje.add(new PieEntry(conteoPorEquipoPrestado.get(equipo).intValue(),"%"));
                                        porcentaje.add(new PieEntry(100-conteoPorEquipoPrestado.get(equipo).intValue(),"%"));
                                    }
                                    PieDataSet pieDataSet = new PieDataSet(porcentaje,"porcentaje");
                                    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                    PieData pieData = new PieData(pieDataSet);
                                    pieChart.getDescription().setEnabled(false);
                                    pieChart.setData(pieData);
                                    pieChart.animate();
                                    break;
                                case "Tablet":
                                    ArrayList<PieEntry> porcentajeTablet = new ArrayList<>();
                                    PieChart pieChartTable = view.findViewById(R.id.pieChartTablet);
                                    if(conteoPorEquipoPrestado.get(equipo)<1.0){
                                        porcentajeTablet.add(new PieEntry(0,"%"));
                                    }else{
                                        porcentajeTablet.add(new PieEntry(conteoPorEquipoPrestado.get(equipo).intValue(),"%"));
                                        porcentajeTablet.add(new PieEntry(100-conteoPorEquipoPrestado.get(equipo).intValue(),"%"));
                                    }
                                    PieDataSet pieDataSetTablet = new PieDataSet(porcentajeTablet,"porcentaje");
                                    pieDataSetTablet.setColors(ColorTemplate.COLORFUL_COLORS);
                                    PieData pieDataTablet = new PieData(pieDataSetTablet);
                                    pieChartTable.getDescription().setEnabled(false);
                                    pieChartTable.setData(pieDataTablet);
                                    pieChartTable.animate();
                                    break;
                                case "Monitor":
                                    ArrayList<PieEntry> porcentajeMonitor = new ArrayList<>();
                                    PieChart pieChartMonitor = view.findViewById(R.id.pieChartMonitor);
                                    if(conteoPorEquipoPrestado.get(equipo)<1.0){
                                        porcentajeMonitor.add(new PieEntry(0,"%"));
                                    }else{
                                        porcentajeMonitor.add(new PieEntry(conteoPorEquipoPrestado.get(equipo).intValue(),"%"));
                                        porcentajeMonitor.add(new PieEntry(100-conteoPorEquipoPrestado.get(equipo).intValue(),"%"));
                                    }
                                    PieDataSet pieDataSetMonitor = new PieDataSet(porcentajeMonitor,"porcentaje");
                                    pieDataSetMonitor.setColors(ColorTemplate.JOYFUL_COLORS);
                                    PieData pieDataMonitor = new PieData(pieDataSetMonitor);
                                    pieChartMonitor.getDescription().setEnabled(false);
                                    pieChartMonitor.setData(pieDataMonitor);
                                    pieChartMonitor.animate();
                                    break;
                                case "Celular":
                                    ArrayList<PieEntry> porcentajeCelular = new ArrayList<>();
                                    PieChart pieChartCelular = view.findViewById(R.id.pieChartCelular);
                                    if(conteoPorEquipoPrestado.get(equipo)<1.0){
                                        porcentajeCelular.add(new PieEntry(0,"%"));
                                    }else{
                                        porcentajeCelular.add(new PieEntry(conteoPorEquipoPrestado.get(equipo).intValue(),"%"));
                                        porcentajeCelular.add(new PieEntry(100-conteoPorEquipoPrestado.get(equipo).intValue(),"%"));
                                    }
                                    PieDataSet pieDataSetCelular = new PieDataSet(porcentajeCelular,"porcentaje");
                                    pieDataSetCelular.setColors(ColorTemplate.LIBERTY_COLORS);
                                    PieData pieDataCelular = new PieData(pieDataSetCelular);
                                    pieChartCelular.getDescription().setEnabled(false);
                                    pieChartCelular.setData(pieDataCelular);
                                    pieChartCelular.animate();
                                    break;
                                case "Otros":
                                    ArrayList<PieEntry> porcentajeOtros = new ArrayList<>();
                                    PieChart pieChartOtros = view.findViewById(R.id.pieChartOtros);
                                    if(conteoPorEquipoPrestado.get(equipo)<1.0){
                                        porcentajeOtros.add(new PieEntry(0,"%"));
                                    }else{
                                        porcentajeOtros.add(new PieEntry(conteoPorEquipoPrestado.get(equipo).intValue(),"%"));
                                        porcentajeOtros.add(new PieEntry(100-conteoPorEquipoPrestado.get(equipo).intValue(),"%"));
                                    }
                                    PieDataSet pieDataSetOtros = new PieDataSet(porcentajeOtros,"porcentaje");
                                    pieDataSetOtros.setColors(ColorTemplate.PASTEL_COLORS);
                                    PieData pieDataOtros = new PieData(pieDataSetOtros);
                                    pieChartOtros.getDescription().setEnabled(false);
                                    pieChartOtros.setData(pieDataOtros);
                                    pieChartOtros.animate();
                                    break;
                                default:
                                    System.out.println("Esto no deberia pasar!");
                                    break;
                            }
                        }
                        //Finalmente rellenamos al equipo mas prestado
                        if(masPrestado!=null) {
                            TextView nombreEquipo = (TextView) view.findViewById(R.id.nombreMasPrestadoRellenar);
                            nombreEquipo.setText(masPrestado.getEquipo().getNombre());
                            TextView stockEquipo = (TextView) view.findViewById(R.id.stockMasPrestadoRellenar);
                            stockEquipo.setText(String.valueOf(masPrestado.getEquipo().getStock()));
                            TextView marcaEquipo = (TextView) view.findViewById(R.id.marcaMasPrestadoRellenar);
                            marcaEquipo.setText(masPrestado.getEquipo().getMarca());
                            ArrayList<String> imagenes = (ArrayList<String>) masPrestado.getEquipo().getImagenes();
                            int n = (int) (Math.random() * (imagenes.size() - 1)) + 1;
                            ImageView vistaEquipo = view.findViewById(R.id.iconEstadisticaDispositivo);
                            Glide.with(vistaEquipo.getContext()).load(imagenes.get(n)).override(100, 100).into(vistaEquipo);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("msg",error.toException().toString());
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("msg",error.toException().toString());
            }
        });
        return view;
    }
}