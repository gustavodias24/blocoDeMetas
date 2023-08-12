package benicio.soluces.blocodemetas.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import benicio.soluces.blocodemetas.adapters.AdapterObjetivos;
import benicio.soluces.blocodemetas.databinding.ActivityMainBinding;
import benicio.soluces.blocodemetas.databinding.SaveLayoutBinding;
import benicio.soluces.blocodemetas.models.ObjetivoModel;
import benicio.soluces.blocodemetas.utils.ObjetivosUtils;
import benicio.soluces.blocodemetas.utils.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity {
    private Dialog dialog_save;
    private ActivityMainBinding vb;
    private RecyclerView recyclerView;
    private AdapterObjetivos adapter;
    private List<ObjetivoModel> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        getSupportActionBar().setTitle("Objetivos");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



        if ( ObjetivosUtils.loadObjetivos(getApplicationContext()) != null){
            lista.addAll(ObjetivosUtils.loadObjetivos(getApplicationContext()));
            vb.textAviso.setVisibility(View.GONE);
        }

        recyclerView = vb.recycleobjetivos;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new AdapterObjetivos(lista, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ObjetivoModel objetivoModelClicado = lista.get(position);
                Intent i = new Intent(getApplicationContext(), MetasActivity.class);

                i.putExtra("objetivoNome", objetivoModelClicado.getTitulo());
                i.putExtra("idObjetivo", objetivoModelClicado.getId());

                startActivity(i);

            }

            @Override
            public void onLongItemClick(View view, int position) {
                ObjetivoModel objetivoAtualizado = lista.get(position);
                lista.remove(position);
                objetivoAtualizado.setConcluido(true);
                lista.add(objetivoAtualizado);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, objetivoAtualizado.getTitulo() + " concluído!", Toast.LENGTH_SHORT).show();
                salvarObjetivos();
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

        vb.objetivoFab.setOnClickListener( view -> {
            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
            SaveLayoutBinding saveLayoutBinding = SaveLayoutBinding.inflate(getLayoutInflater());
            saveLayoutBinding.salvarBtn.setOnClickListener( saveView -> {
                ObjetivoModel objetivoModel = new ObjetivoModel();
                objetivoModel.setTitulo(
                        saveLayoutBinding.tituloEdt.getText().toString()
                );
                objetivoModel.setId(
                        UUID.randomUUID().toString()
                );
                objetivoModel.setConcluido(false);
                lista.add(objetivoModel);
                adapter.notifyDataSetChanged();
                saveLayoutBinding.tituloEdt.setText("");
                Toast.makeText(this, "Objetivo adicionado!", Toast.LENGTH_SHORT).show();
                salvarObjetivos();
                dialog_save.dismiss();
                vb.textAviso.setVisibility(View.GONE);
            });
            b.setView(saveLayoutBinding.getRoot());
            dialog_save = b.create();
            dialog_save.show();
        });
    }

    public void salvarObjetivos(){
        ObjetivosUtils.saveObjetivos(getApplicationContext(), lista);
    }
}