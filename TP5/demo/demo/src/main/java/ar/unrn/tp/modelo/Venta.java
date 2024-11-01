package ar.unrn.tp.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


import jakarta.persistence.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ventas")
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fechayhora")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date fechaYHora;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @JsonBackReference
    private Cliente cliente;

    /* TRABAJO PRÁCTICO 5 */
    private String numeroUnico;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "venta")
    @JsonManagedReference
    private List<Producto> misProductos;
    private double montoTotal;
    public Venta() {
    }


    public Venta(Cliente cliente, List<Producto> productoSelecc, double montoTotal) {
        this.fechaYHora = new Date();
        this.cliente = cliente;
        this.misProductos = productoSelecc;
        this.montoTotal = montoTotal;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Producto> getMisProductos() {
        return misProductos;
    }

    public void setMisProductos(List<Producto> misProductos) {
        this.misProductos = misProductos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

       public Date getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(Date fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    /* TRABAJO PRÁCTICO 5 */
    public String getNumeroUnico() {
        return numeroUnico;
    }

    public void setNumeroUnico(String numeroUnico) {
        this.numeroUnico = numeroUnico;
    }

}
