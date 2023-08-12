package benicio.soluces.blocodemetas.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import benicio.soluces.blocodemetas.adapters.AdapterMetas;
import benicio.soluces.blocodemetas.adapters.AdapterObjetivos;
import benicio.soluces.blocodemetas.databinding.ActivityMetasBinding;
import benicio.soluces.blocodemetas.databinding.SaveLayoutBinding;
import benicio.soluces.blocodemetas.models.MetaModel;
import benicio.soluces.blocodemetas.models.ObjetivoModel;
import benicio.soluces.blocodemetas.utils.MetasUtils;
import benicio.soluces.blocodemetas.utils.RecyclerItemClickListener;

public class MetasActivity extends AppCompatActivity {

    private ActivityMetasBinding vb;
    private Dialog dialog_save;
    private RecyclerView recyclerView;
    private AdapterMetas adapter;
    private List<MetaModel> lista = new ArrayList<>();
    private List<MetaModel> todaAlista = new ArrayList<>();
    private String idObjetivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityMetasBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        Intent dadosObjetivo = getIntent();

        idObjetivo = dadosObjetivo.getStringExtra("idObjetivo");

        getSupportActionBar().setTitle("Metas do " + dadosObjetivo.getStringExtra("objetivoNome"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recyclerView = vb.recycleometas;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new AdapterMetas(lista, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MetaModel metaClicada = lista.get(position);
                Intent i = new Intent(getApplicationContext(), SubMetasActivity.class);
                i.putExtra("idMeta", metaClicada.getId());
                i.putExtra("metaNome", metaClicada.getTitulo());
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                MetaModel metaModel = lista.get(position);

                if (!metaModel.getConcluido()){
                    Toast.makeText(MetasActivity.this, metaModel.getTitulo() + " concluída!", Toast.LENGTH_SHORT).show();
                    metaModel.setConcluido(true);
                }else{
                    metaModel.setConcluido(false);
                }

                int atual_pos = 0;
                for (MetaModel meta : todaAlista){
                    if ( meta.getTitulo().equals(metaModel.getTitulo())){
                        if( todaAlista.get(atual_pos).getTitulo().equals(metaModel.getTitulo())){
                            break;
                        }
                    }
                    atual_pos++;
                }

                todaAlista.remove(atual_pos);
                todaAlista.add(metaModel);


                salvarMetas();
                carregar_metas();

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    carregar_metas();

    vb.metasFab.setOnClickListener( view -> {
        AlertDialog.Builder b = new AlertDialog.Builder(MetasActivity.this);
        SaveLayoutBinding saveLayoutBinding = SaveLayoutBinding.inflate(getLayoutInflater());
        saveLayoutBinding.salvarBtn.setOnClickListener( saveView -> {
            MetaModel metaModel = new MetaModel();
            metaModel.setTitulo(
                    saveLayoutBinding.tituloEdt.getText().toString()
            );
            metaModel.setId(
                    UUID.randomUUID().toString()
            );
            metaModel.setConcluido(false);
            metaModel.setObjetivo(idObjetivo);
            todaAlista.add(metaModel);
            lista.add(metaModel);
            adapter.notifyDataSetChanged();

            saveLayoutBinding.tituloEdt.setText("");
            Toast.makeText(this, "Meta adicionada!", Toast.LENGTH_SHORT).show();
            salvarMetas();
            dialog_save.dismiss();
            vb.textAviso.setVisibility(View.GONE);
        });
        b.setView(saveLayoutBinding.getRoot());
        dialog_save = b.create();
        dialog_save.show();
    });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void salvarMetas(){
        MetasUtils.saveMetas(getApplicationContext(), todaAlista);
    }

    public void carregar_metas(){
        lista.clear();
        todaAlista.clear();
        if ( MetasUtils.loadMetas(getApplicationContext()) != null){

            todaAlista.addAll(MetasUtils.loadMetas(getApplicationContext()));

            for ( MetaModel metaModel : MetasUtils.loadMetas(getApplicationContext())){
                if ( metaModel.getObjetivo().equals(idObjetivo)){
                    lista.add(metaModel);
                }
            }

            adapter.notifyDataSetChanged();

            if ( lista.size() > 0){
                vb.textAviso.setVisibility(View.GONE);
            }
        }
    }
}