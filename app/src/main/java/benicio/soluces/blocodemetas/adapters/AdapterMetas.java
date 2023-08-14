package benicio.soluces.blocodemetas.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.soluces.blocodemetas.R;
import benicio.soluces.blocodemetas.activitys.SubMetasActivity;
import benicio.soluces.blocodemetas.models.MetaModel;
import benicio.soluces.blocodemetas.models.SubMetaModel;
import benicio.soluces.blocodemetas.utils.MetasUtils;
import benicio.soluces.blocodemetas.utils.RecyclerItemClickListener;

public class AdapterMetas extends RecyclerView.Adapter<AdapterMetas.MyViewHolder>{
    private Dialog d;
    private AdapterSubMetas adapter;
    private int position1;
    private MetaModel metaModel;
    List<MetaModel> lista;
    Context c;

    public AdapterMetas(List<MetaModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }


    @NonNull
    @Override
    public AdapterMetas.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meta_layout, parent, false);
        return new AdapterMetas.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMetas.MyViewHolder holder, int position) {
        metaModel = lista.get(position);

        holder.nome.setText(metaModel.getTitulo());

        if ( metaModel.getConcluido() ){

            ColorStateList colorStateList = new ColorStateList(
                    new int[][] {
                            new int[] { android.R.attr.state_enabled }, // Estado padrão
                            new int[] { android.R.attr.state_pressed }  // Estado pressionado
                    },
                    new int[] {
                            Color.GREEN,     // Cor para estado padrão
                            Color.RED     // Cor para estado pressionado
                    }
            );
            holder.status_btn.setBackgroundTintList(colorStateList);
            holder.status_btn.setTextColor(Color.WHITE);
            holder.status_btn.setText("Concluído");
            holder.nome.setPaintFlags(holder.nome.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            ColorStateList colorStateList = new ColorStateList(
                    new int[][] {
                            new int[] { android.R.attr.state_enabled }, // Estado padrão
                            new int[] { android.R.attr.state_pressed }  // Estado pressionado
                    },
                    new int[] {
                            Color.YELLOW,     // Cor para estado padrão
                            Color.RED     // Cor para estado pressionado
                    }
            );
            holder.status_btn.setBackgroundTintList(colorStateList);
            holder.status_btn.setTextColor(Color.BLACK);
            holder.status_btn.setText("Pendente");
            int flagsToRemove = Paint.STRIKE_THRU_TEXT_FLAG | Paint.UNDERLINE_TEXT_FLAG;
            holder.nome.setPaintFlags(holder.nome.getPaintFlags() & ~flagsToRemove);
        }

        criarDialog();

        if ( metaModel.getLista() != null){
            if ( metaModel.getLista().size() > 0){
                holder.recyclerSubMetas.setVisibility(View.VISIBLE);
                holder.recyclerSubMetas.setHasFixedSize(true);
                holder.recyclerSubMetas.setLayoutManager(new LinearLayoutManager(c));
                holder.recyclerSubMetas.addItemDecoration(new DividerItemDecoration(c, DividerItemDecoration.VERTICAL));
                adapter = new AdapterSubMetas(metaModel.getLista(), c);
                holder.recyclerSubMetas.setAdapter(adapter);
                holder.recyclerSubMetas.addOnItemTouchListener( new RecyclerItemClickListener(c, holder.recyclerSubMetas, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int p) {
                        position1 = p;
                        d.show();

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }));
            }
        }
    }
    public void criarDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setMessage("Escolha uma opção para a SubMeta");
        b.setPositiveButton("Concluir", (dialogInterface, i) -> {
            concluirSubMeta();
        });


        b.setNegativeButton("Excluir", (dialogInterface, i) -> {
            excluirSubMeta();
        });

        d = b.create();

    }

    public void concluirSubMeta(){
        SubMetaModel subMetaModel = metaModel.getLista().get(position1);

        if (!subMetaModel.getConcluido()){
//                                Toast.makeText(c, subMetaModel.getTitulo() + " concluída!", Toast.LENGTH_SHORT).show();
            subMetaModel.setConcluido(true);
        }else{
            subMetaModel.setConcluido(false);
        }
        adapter.notifyDataSetChanged();
        MetasUtils.saveMetas(c, lista);
        d.dismiss();
    }

    public void excluirSubMeta(){
        metaModel.getLista().remove(position1);

        adapter.notifyDataSetChanged();
        MetasUtils.saveMetas(c, lista);
        d.dismiss();
    }
    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        Button status_btn;
        RecyclerView recyclerSubMetas;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textNomeMeta);
            recyclerSubMetas = itemView.findViewById(R.id.recyclerSubMetas);
            status_btn = itemView.findViewById(R.id.status_btn);
        }
    }
}
