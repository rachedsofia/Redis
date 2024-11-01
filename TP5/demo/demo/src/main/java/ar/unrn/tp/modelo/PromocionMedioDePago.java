package ar.unrn.tp.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;
@Entity
@DiscriminatorValue("MEDIO_DE_PAGO")
public class PromocionMedioDePago extends Descuento {
    private String medioDePago;
    private double porcentaje;
    public PromocionMedioDePago(){}
    public PromocionMedioDePago(LocalDate fechaInicio, LocalDate fechaFin, String medioDePago, double porcentaje) {
        super(fechaInicio, fechaFin);
        this.medioDePago = medioDePago;
        this.porcentaje = porcentaje;
    }

    @Override
    public double aplicar(Producto producto, double precioProducto) {
        return 0;
    }

    public double aplicarDescuento(double total, String tarjeta, double porcentaje1) {
        double totalConDescuento = total;
    System.out.println("METODO APLICAR DESCUENTO:TCD:  "+ totalConDescuento);
        if (estaVigente() && tarjeta.equals(this.medioDePago)) {
            // Aplica el descuento
            totalConDescuento = total - (total * (porcentaje1 / 100));
            System.out.println(" APLICAR DESCUENTO:TCD aplicado :  "+ totalConDescuento);
        } else {
            System.out.println("La promoción no está vigente o el medio de pago no coincide.");
        }
        ;
        System.out.println(" APLICAR DESCUENTO:TCD returnnnnn :  "+ totalConDescuento);
        return totalConDescuento;
    }

    public String getMedioDePago() {
        return medioDePago;
    }


}
