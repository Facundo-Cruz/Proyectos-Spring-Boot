package com.egg.estancias.entidades;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Cliente extends Usuario {

   
    private String calle;
    private int numero;
    private String codPostal;
    private String ciudad;
    private String pais;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
   private List<Estancia> estancias;

    public Cliente() {
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public List<Estancia> getEstancias() {
        return estancias;
    }

    public void setEstancias(List<Estancia> estancias) {
        this.estancias = estancias;
    }

    

    

}
