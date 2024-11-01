package ar.unrn.tp.modelo;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    Cliente cliente;

    private List<Producto> misProductos;

    public Carrito(Cliente unCliente) {
        this.cliente = unCliente;
        this.misProductos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("Producto no puede ser nulo");
        }
        misProductos.add(producto);
    }

    public void eliminarProducto(Producto producto) {
        if (!misProductos.contains(producto)) {
            throw new IllegalArgumentException("El producto no está en el carrito");
        }
        misProductos.remove(producto);
    }

    public double calcularTotal() {
        return misProductos.stream()
                .mapToDouble(Producto::getPrecio)
                .sum();
    }

    public Cliente obtenerCliente() {
        return cliente;
    }

    public List<Producto> obtenerProductos() {
        return new ArrayList<>(misProductos);
    }

    public boolean contieneProducto(Producto producto) {
        return misProductos.contains(producto);
    }

    public boolean estaVacio() {
        return misProductos.isEmpty();
    }

    public int cantidadProductos() {
        return misProductos.size();
    }

    public void vaciarCarrito() {
        misProductos.clear();
    }

}
