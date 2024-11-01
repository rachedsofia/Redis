package ar.unrn.tp.servicios;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.excepciones.AplicacionEx;
import ar.unrn.tp.excepciones.ClienteEx;
import ar.unrn.tp.excepciones.TarjetaEx;
import ar.unrn.tp.excepciones.VentaEx;
import ar.unrn.tp.modelo.*;

import jakarta.persistence.*;
import org.hibernate.annotations.processing.Suppress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class JPAVentasService implements VentaService {
    private final EntityManager em;
    private final ProcesoDePago procesoDePago;
    private final ClienteService clienteService;
    private final JPAProductoServices productoService;

    public JPAVentasService(EntityManager em, ProcesoDePago procesoDePago, ClienteService clienteService,
                            JPAProductoServices productoService) {
        this.em = em;
        this.procesoDePago = procesoDePago;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    @Override
    public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) {
        if (idCliente == null)
            throw new ClienteEx("El id del cliente es nulo: " + idCliente);

        if(idTarjeta == null)
            throw new TarjetaEx("El id de la tarjeta es nulo: " + idTarjeta);

        //EntityTransaction tx = em.getTransaction();
        try {
            //tx.begin();

            Cliente cliente = clienteService.buscarCliente(idCliente);

            List<Producto> listaProductos = productos.stream()
                    .map(idProducto -> em.find(Producto.class, idProducto))
                    .collect(Collectors.toList());
            System.out.println("REALIZAR VENTA - LISTA PRODUCTOS" + listaProductos);
            Tarjeta tarjeta = em.find(Tarjeta.class, idTarjeta);
            System.out.println("TARJETA" +tarjeta);

            Carrito carrito = new Carrito(cliente);
            listaProductos.forEach(carrito::agregarProducto);

            List<Descuento> descuentos = em.createQuery("SELECT d FROM Descuento d", Descuento.class).getResultList();
            System.out.println("descuentos" +descuentos.toString());
           Venta venta = procesoDePago.procesarPago(carrito, tarjeta, descuentos);
            String numeroUnico = generarNumeroUnico();
            venta.setNumeroUnico(numeroUnico);
            em.persist(venta);

          //  ultimasComprasServices.agregarVenta(idCliente,venta);
          //  tx.commit();
        } catch (NoResultException e){
            throw new AplicacionEx("No se econtraron resultados para el id de cliente o la tarjeta: " + e.getMessage());

        } catch (NonUniqueResultException e) {
            throw new AplicacionEx("Se encontraron múltiples resultados para este ID de clientes o tarjeta: " + e.getMessage());

        }catch (Exception e) {
            throw new VentaEx("Error al querer realizazr la venta: " + e.getMessage());
        }
    }



    @Override
    public float calcularMonto(List<Long> productos, Long idTarjeta) {
        Tarjeta tarjeta = em.find(Tarjeta.class, idTarjeta);
        List<Producto> listaProductos = productoService.buscarProductos(productos);

        // Obtener descuentos activos
        List<Descuento> descuentos = em.createQuery("SELECT d FROM Descuento d WHERE d.activo = 1", Descuento.class).getResultList();

        double montoTotal = 0.0;

         for (Producto producto : listaProductos) {
            double precioProducto = producto.getPrecio();

            double descuentoFinalMarca = 0.0;
            // Aplicar descuentos por marca
            for (Descuento descuento : descuentos) {
                if (descuento instanceof PromocionMarca promocionMarca) {
                    descuentoFinalMarca += promocionMarca.aplicar(producto, precioProducto);
                }
            }
            precioProducto = Math.max(precioProducto - descuentoFinalMarca, 0);

            montoTotal += precioProducto; // Acumular el precio ajustado
        }
        // Obtener el porcentaje de descuento para el medio de pago
        double descuentoFinal = 0.0;
        for (Descuento descuento : descuentos) {
            if (descuento instanceof PromocionMedioDePago promocionMedioDePago) {
                double porcentaje = descuento.getPorcentaje();
                System.out.println("Porcentaje COMPRA: " + porcentaje);
                descuentoFinal = promocionMedioDePago.aplicarDescuento(montoTotal, tarjeta.getMarca(), porcentaje);
                    System.out.println("Descuento aplicado por medio de pago: " + descuentoFinal);
            }
        }
        System.out.println("MONT TOTALLLL: "+ montoTotal +"-=" +descuentoFinal +"RESULTADO");
        montoTotal = descuentoFinal;

        System.out.println("Monto total después de aplicar descuentos: " + montoTotal);

        return (float) montoTotal;
    }


   @SuppressWarnings("rawtypes")
    @Override
    public List ventas() {
        return em.createQuery("SELECT v FROM ventas v", Venta.class).getResultList();
    }

    public List<Producto> listarProductos() {
        return em.createQuery("SELECT p FROM productos p", Producto.class).getResultList();
    }

    public List<Descuento> listarDescuentos() {
        return em.createQuery("SELECT d FROM descuento d", Descuento.class).getResultList();
    }

    public List<Tarjeta> obtenerTarjetasCliente(Long idCliente) {
        return em.createQuery("SELECT t FROM tarjetas t WHERE t.cliente.id = :idCliente", Tarjeta.class)
                .setParameter("idCliente", idCliente)
                .getResultList();
    }

    private String generarNumeroUnico() {
        int anioActual = LocalDate.now().getYear();
        int siguienteNumero = obtenerSiguienteNumero(anioActual);
        return siguienteNumero + "-" + anioActual;
    }

    private int obtenerSiguienteNumero(int anio) {
        TypedQuery<NextNumber> query = em.createQuery("from NextNumber where anio = :anioActual", NextNumber.class);
        query.setParameter("anioActual", anio);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);  //  bloqueo pesimista
        NextNumber nextNumber;

        // Si no existe un registro para el año actual, ccreo uno
        try {
            nextNumber = query.getSingleResult();
        } catch (NoResultException e) {
            nextNumber = new NextNumber();
            nextNumber.setAnio(anio);
            nextNumber.setNumeroActual(0);
            em.persist(nextNumber);
        }

        return nextNumber.recuperarSiguiente();
    }


    /*TP 5 - REDIS */
    @PersistenceContext
    private EntityManager entityManager;

    public List<Venta> obtenerUltimasVentasCliente(Long idCliente, int limit) {
        return entityManager.createQuery("SELECT v FROM Venta v WHERE v.cliente.id = :idCliente ORDER BY v.fechaYHora DESC", Venta.class)
                .setParameter("idCliente", idCliente)
                .setMaxResults(limit)
                .getResultList();
    }

    @Transactional
    public void registrarVenta(Venta nuevaVenta) {
        entityManager.persist(nuevaVenta);
    }
}

