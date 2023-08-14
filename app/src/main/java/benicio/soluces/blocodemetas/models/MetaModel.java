package benicio.soluces.blocodemetas.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MetaModel implements Serializable {
    String id, titulo, objetivo;
    List<SubMetaModel> lista = new ArrayList<>();
    Boolean concluido;

    public List<SubMetaModel> getLista() {
        return lista;
    }

    public void setLista(List<SubMetaModel> lista) {
        this.lista = lista;
    }

    public Boolean getConcluido() {
        return concluido;
    }

    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }
}
