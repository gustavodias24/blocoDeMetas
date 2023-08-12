package benicio.soluces.blocodemetas.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjetivoModel  implements Serializable {
    String titulo, id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Boolean concluido;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean getConcluido() {
        return concluido;
    }

    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }
}
