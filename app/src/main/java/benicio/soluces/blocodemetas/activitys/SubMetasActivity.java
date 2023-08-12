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

import benicio.soluces.blocodemetas.R;
import benicio.soluces.blocodemetas.adapters.AdapterMetas;
import benicio.soluces.blocodemetas.adapters.AdapterSubMetas;
import benicio.soluces.blocodemetas.databinding.ActivityMetasBinding;
import benicio.soluces.blocodemetas.databinding.ActivitySubMetasBinding;
import benicio.soluces.blocodemetas.databinding.SaveLayoutBinding;
import benicio.soluces.blocodemetas.models.MetaModel;
import benicio.soluces.blocodemetas.models.SubMetaModel;
import benicio.soluces.blocodemetas.utils.MetasUtils;
import benicio.soluces.blocodemetas.utils.RecyclerItemClickListener;
import benicio.soluces.blocodemetas.utils.SubMetasUtils;

public class SubMetasActivity extends AppCompatActivity {
    private ActivitySubMetasBinding vb;
    private Dialog dialog_save;
    private RecyclerView recyclerView;
    private AdapterSubMetas adapter;
    private List<SubMetaModel> lista = new ArrayList<>();
    private List<SubMetaModel> todaAlista = new ArrayList<>();
    private String idMeta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivitySubMetasBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        Intent dadosObjetivo = getIntent();

        idMeta = dadosObjetivo.getStringExtra("idMeta");

        getSupportActionBar().setTitle("Submetas do " + dadosObjetivo.getStringExtra("metaNome"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recyclerView = vb.recycleosubmetas;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new AdapterSubMetas(lista, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                SubMetaModel subMetaModel = lista.get(position);

                if (!subMetaModel.getConcluido()){
                    Toast.makeText(SubMetasActivity.this, subMetaModel.getTitulo() + " concluída!", Toast.LENGTH_SHORT).show();
                    subMetaModel.setConcluido(true);
                }else{
                    subMetaModel.setConcluido(false);
                }

                int atual_pos = 0;
                for (SubMetaModel submeta : todaAlista){
                    if ( submeta.getTitulo().equals(subMetaModel.getTitulo())){
                        if( todaAlista.get(atual_pos).getTitulo().equals(subMetaModel.getTitulo())){
                            break;
                        }
                    }
                    atual_pos++;
                }

                todaAlista.remove(atual_pos);
                todaAlista.add(subMetaModel);


                salvarSubMetas();
                carregarSubMetas();

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

        carregarSubMetas();

        vb.submetasFab.setOnClickListener( view -> {
            AlertDialog.Builder b = new AlertDialog.Builder(SubMetasActivity.this);
            SaveLayoutBinding saveLayoutBinding = SaveLayoutBinding.inflate(getLayoutInflater());
            saveLayoutBinding.salvarBtn.setOnClickListener( saveView -> {
                SubMetaModel subMetaModel = new SubMetaModel();
                subMetaModel.setTitulo(
                        saveLayoutBinding.tituloEdt.getText().toString()
                );
                subMetaModel.setId(
                        UUID.randomUUID().toString()
                );
                subMetaModel.setConcluido(false);
                subMetaModel.setMeta(idMeta);
                todaAlista.add(subMetaModel);
                lista.add(subMetaModel);
                adapter.notifyDataSetChanged();

                saveLayoutBinding.tituloEdt.setText("");
                Toast.makeText(this, "Submeta adicionada!", Toast.LENGTH_SHORT).show();
                salvarSubMetas();
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
    public void salvarSubMetas(){
        SubMetasUtils.saveSubMetas(getApplicationContext(), todaAlista);
    }

    public void carregarSubMetas(){
        lista.clear();
        todaAlista.clear();
        if ( SubMetasUtils.loadSubMetas(getApplicationContext()) != null){

            todaAlista.addAll(SubMetasUtils.loadSubMetas(getApplicationContext()));

            for ( SubMetaModel subMetaModel : SubMetasUtils.loadSubMetas(getApplicationContext())){
                if ( subMetaModel.getMeta().equals(idMeta)){
                    lista.add(subMetaModel);
                }
            }

            adapter.notifyDataSetChanged();

            if ( lista.size() > 0){
                vb.textAviso.setVisibility(View.GONE);
            }
        }
    }
}