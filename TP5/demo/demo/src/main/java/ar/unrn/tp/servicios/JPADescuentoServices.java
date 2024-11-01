package ar.unrn.tp.servicios;

import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.excepciones.DescuentoEx;
import ar.unrn.tp.modelo.Descuento;
import ar.unrn.tp.modelo.PromocionMarca;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
@Service
public class JPADescuentoServices implements DescuentoService {
    private final EntityManager em;

    public JPADescuentoServices(EntityManager em) {
        this.em = em;
    }
    @Transactional
    @Override
    public void crearDescuentoSobreTotal(String marcaTarjeta, LocalDate fechaDesde, LocalDate fechaHasta,
                                         float porcentaje) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Descuento descuento = new PromocionMarca(fechaDesde, fechaHasta, marcaTarjeta, porcentaje);
            em.persist(descuento);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new DescuentoEx("No se pudo crear el descuento"+ e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void crearDescuento(String marcaProducto, LocalDate fechaDesde, LocalDate fechaHasta, float porcentaje) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Descuento descuento = new PromocionMarca(fechaDesde, fechaHasta, marcaProducto, porcentaje);
            em.persist(descuento);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Descuento> listarDescuentos() {
        return em.createQuery("SELECT d FROM Descuento d WHERE d.activo = 1", Descuento.class)
               .getResultList();
    }

}
