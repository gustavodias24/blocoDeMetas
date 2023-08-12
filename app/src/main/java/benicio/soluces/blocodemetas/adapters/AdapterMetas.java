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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_layout, parent, false);
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
