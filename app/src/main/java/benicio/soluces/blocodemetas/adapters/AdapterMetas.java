package benicio.soluces.blocodemetas.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        MetaModel metaModel = lista.get(position);

        holder.nome.setText(metaModel.getTitulo());

        if ( metaModel.getConcluido() ){
            holder.nome.setPaintFlags(holder.nome.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            int flagsToRemove = Paint.STRIKE_THRU_TEXT_FLAG | Paint.UNDERLINE_TEXT_FLAG;
            holder.nome.setPaintFlags(holder.nome.getPaintFlags() & ~flagsToRemove);
        }

        if ( metaModel.getLista() != null){
            if ( metaModel.getLista().size() > 0){
                holder.recyclerSubMetas.setVisibility(View.VISIBLE);
                holder.recyclerSubMetas.setHasFixedSize(true);
                holder.recyclerSubMetas.setLayoutManager(new LinearLayoutManager(c));
                holder.recyclerSubMetas.addItemDecoration(new DividerItemDecoration(c, DividerItemDecoration.VERTICAL));
                AdapterSubMetas adapter = new AdapterSubMetas(metaModel.getLista(), c);
                holder.recyclerSubMetas.setAdapter(adapter);
                holder.recyclerSubMetas.addOnItemTouchListener( new RecyclerItemClickListener(c, holder.recyclerSubMetas, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        SubMetaModel subMetaModel = metaModel.getLista().get(position);

                        if (!subMetaModel.getConcluido()){
                            Toast.makeText(c, subMetaModel.getTitulo() + " concluída!", Toast.LENGTH_SHORT).show();
                            subMetaModel.setConcluido(true);
                        }else{
                            subMetaModel.setConcluido(false);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }));
            }
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        RecyclerView recyclerSubMetas;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textNomeMeta);
            recyclerSubMetas = itemView.findViewById(R.id.recyclerSubMetas);
        }
    }
}
