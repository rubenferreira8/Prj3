package estg.ipvc.prj3;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hisham on 9/6/2015.
 */
public class MovieModel {
    private String DESIGNACAO;
    private int TELEFONE;
    private String CATEGORIA;
    private String DESCRICAO;

    private String FOTO;


    public String getDESIGNACAO() {
        return DESIGNACAO;
    }

    public void setDESIGNACAO(String DESIGNACAO) {
        this.DESIGNACAO = DESIGNACAO;
    }

    public String getDESCRICAO() {
        return DESCRICAO;
    }

    public void setDESCRICAO(String DESIGNACAO) {
        this.DESCRICAO = DESCRICAO;
    }

    public int getTELEFONE() {
        return TELEFONE;
    }

    public void setTELEFONE(int TELEFONE) {
        this.TELEFONE = TELEFONE;
    }

    public String getCATEGORIA() {
        return CATEGORIA;
    }

    public void setCATEGORIA(String CATEGORIA) {
        this.CATEGORIA = CATEGORIA;
    }

    public String getImage() {
        return FOTO;
    }

    public void setFOTO(String FOTO) {
        this.FOTO = FOTO;
    }

}
