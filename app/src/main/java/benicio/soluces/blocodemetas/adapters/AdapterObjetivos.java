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
import benicio.soluces.blocodemetas.models.ObjetivoModel;

public class AdapterObjetivos extends RecyclerView.Adapter<AdapterObjetivos.MyViewHolder>{

    List<ObjetivoModel> lista;
    Context c;

    public AdapterObjetivos(List<ObjetivoModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ObjetivoModel objetivoModel = lista.get(position);

        holder.nome.setText(objetivoModel.getTitulo());

        if ( objetivoModel.getConcluido() ){
            holder.nome.setPaintFlags(holder.nome.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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
