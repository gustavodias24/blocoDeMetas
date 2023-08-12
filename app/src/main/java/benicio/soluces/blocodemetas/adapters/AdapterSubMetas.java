package benicio.soluces.blocodemetas.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.soluces.blocodemetas.R;
import benicio.soluces.blocodemetas.models.MetaModel;
import benicio.soluces.blocodemetas.models.SubMetaModel;

public class AdapterSubMetas extends RecyclerView.Adapter<AdapterSubMetas.MyViewHolder> {
    List<SubMetaModel> lista;
    Context c;

    public AdapterSubMetas(List<SubMetaModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }


    @NonNull
    @Override
    public AdapterSubMetas.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSubMetas.MyViewHolder holder, int position) {
        SubMetaModel submetaModel = lista.get(position);

        holder.nome.setText(submetaModel.getTitulo());

        if ( submetaModel.getConcluido() ){
            holder.nome.setPaintFlags(holder.nome.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            int flagsToRemove = Paint.STRIKE_THRU_TEXT_FLAG | Paint.UNDERLINE_TEXT_FLAG;
            holder.nome.setPaintFlags(holder.nome.getPaintFlags() & ~flagsToRemove);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textNome);
        }
    }
}

