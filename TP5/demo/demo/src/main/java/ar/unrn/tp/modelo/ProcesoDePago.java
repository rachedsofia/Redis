package ar.unrn.tp.modelo;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ProcesoDePago {

    public Venta procesarPago(Carrito carrito, Tarjeta tarjeta, List<Descuento> descuentos) {
             double total = 0;

            // Calcular el total aplicando descuentos por producto
            for (Producto producto : carrito.obtenerProductos()) {
                double precioProducto = producto.getPrecio();
                System.out.println("Precio prod: " + precioProducto);

                // Aplicar descuentos por marca
                for (Descuento descuento : descuentos) {
                    if (descuento instanceof PromocionMarca promocionMarca) {
                        double descuentoAplicado = promocionMarca.aplicar(producto, precioProducto);
                        precioProducto -= descuentoAplicado;
                        System.out.println("Descuento por marca aplicado: " + descuentoAplicado + ", Precio después del descuento: " + precioProducto);
                    }
                }

                total += precioProducto;
            }

            System.out.println("Total antes de descuentos de marca: " + total);

            double descuentoMedioDePago = 0;

            // Aplicar descuento por medio de pago
            for (Descuento descuento : descuentos) {
                if (descuento instanceof PromocionMedioDePago) {
                    if (descuento.estaVigente()) {
                        double porcentaje = descuento.getPorcentaje();
                        System.out.println("DESCUENTO VIGENTE : "+ descuento.getPorcentaje());
                                descuentoMedioDePago = ((PromocionMedioDePago) descuento).aplicarDescuento(total, tarjeta.getMarca(), porcentaje);
                        System.out.println("Descuento por medio de pago aplicado: " + descuentoMedioDePago);
                    }
                }
            }

            total -= descuentoMedioDePago;
            System.out.println("Total después de descuentos de medio de pago: " + total);

            // Validar fondos suficientes en la tarjeta
            if (!tarjeta.tieneFondosSuficientes(total)) {
                throw new RuntimeException("Fondos insuficientes");
            }
            // Crear y devolver la venta
            Venta venta = new Venta(carrito.obtenerCliente(), carrito.obtenerProductos(), total);
            return venta;
        }



    }
