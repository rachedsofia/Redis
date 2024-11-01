package ar.unrn.tp.modelo;

import jakarta.persistence.*;

@Entity
public class NextNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int anio;
    private int numeroActual;

    @Version
    private Long version;

    public int getNumeroActual() {
        return numeroActual;
    }

    public void setNumeroActual(int numeroActual) {
        this.numeroActual = numeroActual;
    }

    public int recuperarSiguiente() {
        this.numeroActual += 1;
        return this.numeroActual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
}

