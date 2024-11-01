package ar.unrn.tp.servicios;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.excepciones.ConcurrenciaEx;
import ar.unrn.tp.excepciones.ProductoEx;
import ar.unrn.tp.modelo.Producto;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service

public class JPAProductoServices implements ProductoService {
    @PersistenceContext
    private final EntityManager em;


    public JPAProductoServices(EntityManager em) {
        this.em = em;
    }

    @Override
    public void crearProducto(String codigo, String descripcion, float precio, String IdCategoria, String marca) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Producto producto = new Producto(codigo, descripcion, IdCategoria, precio, marca);
            em.persist(producto);
            tx.commit();

        } catch (Exception e) {
            throw new ProductoEx("Error al crear el producto: " + e.getMessage());
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

    }
    @Transactional
    @Override
        public void modificarProducto(Long idProducto, String codigo, String descripcion, String categoria, double precio,  String marca) {{
            try {
                System.out.println("Pase al service");
                Producto producto = em.find(Producto.class, idProducto);
                System.out.println(producto);
                if (producto == null) {
                    throw new ProductoEx("No se encontró ningún producto.");
                }

                int versionPrevia = 6;
                if(!producto.sameVersion(versionPrevia))
                {
                    throw new OptimisticLockException();
                }

                // Actualizar el producto
                producto.setCodigo(codigo);
                producto.setDescripcion(descripcion);
                producto.setMiCategoria(categoria);
                producto.setMarca(marca);
                producto.setPrecio(precio);
          //     em.merge(producto);
            } catch (OptimisticLockException e) {
                throw new ConcurrenciaEx("El producto ha sido modificado por otro usuario. Por favor, recarga la página.");
            }

        }
    }

    @Override

    public List listarProductos() {
        return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();

    }

    @Override
    public List<Producto> buscarProductos(List<Long> idsProductos) {
        return em.createQuery("SELECT p FROM Producto p WHERE p.id IN :id", Producto.class)
                .setParameter("id", idsProductos)
                .getResultList();
    }
}
