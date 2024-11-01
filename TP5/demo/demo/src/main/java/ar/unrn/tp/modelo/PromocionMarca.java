package ar.unrn.tp.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("MARCA")
public class PromocionMarca extends Descuento {
    private String marca;
    private double porcentaje;

    // Constructor por defecto
    public PromocionMarca() {
    }

    public PromocionMarca(LocalDate fechaInicio, LocalDate fechaFin, String marca, double porcentaje) {
        super(fechaInicio, fechaFin);
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100.");
        }
        this.marca = marca;
        this.porcentaje = porcentaje;
    }

    @Override
    public double aplicar(Producto producto, double precioProducto) {
        double precio = 0.0;
        if (estaVigente() && (producto.getCategoria().equals(this.marca))){
            // Devuelve el precio con el descuento aplicad
            precio=  precioProducto - (precioProducto * (porcentaje / 100));
        }

        return precio;
    }

    public String getMarca() {
        return marca;
    }
}
