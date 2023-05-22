package com.example.noticia.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Periodista extends Usuario {

    @OneToMany
    private List<Noticia> misNoticias;
    private Integer sueldoMensual;

    public Periodista() {
        
    }

    public List<Noticia> getMisNoticias() {
        return misNoticias;
    }

    public void setMisNoticias(List<Noticia> misNoticias) {
        this.misNoticias = misNoticias;
    }

    public Integer getSueldoMensual() {
        return sueldoMensual;
    }

    public void setSueldoMensual(Integer sueldoMensual) {
        this.sueldoMensual = sueldoMensual;
    }

}
