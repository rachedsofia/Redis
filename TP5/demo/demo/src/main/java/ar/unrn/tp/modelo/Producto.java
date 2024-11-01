package ar.unrn.tp.modelo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.extern.jackson.Jacksonized;

@Entity
@Table(name = "productos")
@Jacksonized
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String descripcion;
    private String miCategoria;
    private double precio;
    private String marca;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "id_venta")
    private Venta venta;
    @Version
    private Long version;

    protected Producto() {
    }

    public Producto(String codigo, String descripcion, String categoria, double precio, String marca) {
        if (codigo == null || descripcion == null || categoria == null || precio <= 0) {
            throw new IllegalArgumentException("Datos de producto inválidos");
        }
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.miCategoria = categoria;
        this.precio = precio;
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

   /* public String getDescripcion() {
        return descripcion;
    }*/

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return miCategoria;
    }

    public boolean containsMarca(String marca) {
        return this.descripcion.contains(marca);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMiCategoria() {
        return miCategoria;
    }

    public void setMiCategoria(String miCategoria) {
        this.miCategoria = miCategoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    public boolean sameVersion(int versionPrevia) {
        return this.version == versionPrevia;
    }
}
