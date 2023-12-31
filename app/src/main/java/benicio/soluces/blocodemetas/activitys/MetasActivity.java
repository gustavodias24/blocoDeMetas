package benicio.soluces.blocodemetas.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
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
import benicio.soluces.blocodemetas.models.SubMetaModel;
import benicio.soluces.blocodemetas.utils.MetasUtils;
import benicio.soluces.blocodemetas.utils.RecyclerItemClickListener;

public class MetasActivity extends AppCompatActivity {

    private ActivityMetasBinding vb;
    private Dialog dialog_save, dialog_subMeta, dialog_remove_concluir_meta;
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

//        getSupportActionBar().setTitle("Metas do " + dadosObjetivo.getStringExtra("objetivoNome"));
        getSupportActionBar().setTitle(dadosObjetivo.getStringExtra("objetivoNome"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recyclerView = vb.recycleometas;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new AdapterMetas(lista, MetasActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //adicionar subMeta
                Toast.makeText(MetasActivity.this, "Adicionar submeta", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder b = new AlertDialog.Builder(MetasActivity.this);
                b.setTitle("Adicionar subMeta");
                SaveLayoutBinding saveLayoutBinding = SaveLayoutBinding.inflate(getLayoutInflater());
                saveLayoutBinding.salvarBtn.setOnClickListener( saveView -> {

                    SubMetaModel subMetaModel = new SubMetaModel();
                    subMetaModel.setConcluido(false);
                    subMetaModel.setTitulo(
                            saveLayoutBinding.tituloEdt.getText().toString()

                    );
                    MetaModel metaClicada = lista.get(position);
                    metaClicada.getLista().add(subMetaModel);
                    dialog_subMeta.dismiss();
                    saveLayoutBinding.tituloEdt.setText("");
                    int atual_pos = 0;
                    for (MetaModel meta : todaAlista){
                        if ( meta.getTitulo().equals(metaClicada.getTitulo())){
                            if( todaAlista.get(atual_pos).getTitulo().equals(metaClicada.getTitulo())){
                                break;
                            }
                        }
                        atual_pos++;
                    }

                    todaAlista.remove(atual_pos);
                    todaAlista.add(metaClicada);


                    salvarMetas();
                    carregar_metas();

                });
                b.setView(saveLayoutBinding.getRoot());
                dialog_subMeta = b.create();
                dialog_subMeta.show();
//                MetaModel metaClicada = lista.get(position);
//                Intent i = new Intent(getApplicationContext(), SubMetasActivity.class);
//                i.putExtra("idMeta", metaClicada.getId());
//                i.putExtra("metaNome", metaClicada.getTitulo());
//                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                MetaModel metaModel = lista.get(position);

                AlertDialog.Builder b = new AlertDialog.Builder(MetasActivity.this);
                b.setMessage("Escolha uma opção para a meta.");
                b.setPositiveButton("Concluir", (dialogInterface, i) -> {
                    if (!metaModel.getConcluido()){
//                    Toast.makeText(MetasActivity.this, metaModel.getTitulo() + " concluída!", Toast.LENGTH_SHORT).show();
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
                    dialog_remove_concluir_meta.dismiss();
                });

                b.setNegativeButton("Excluir", (dialogInterface, i) -> {
                    Toast.makeText(MetasActivity.this, "Removido!", Toast.LENGTH_SHORT).show();
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
                    salvarMetas();
                    carregar_metas();
                    dialog_remove_concluir_meta.dismiss();
                });

                dialog_remove_concluir_meta = b.create();
                dialog_remove_concluir_meta.show();


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