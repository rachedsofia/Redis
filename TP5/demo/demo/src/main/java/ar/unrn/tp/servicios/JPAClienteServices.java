package ar.unrn.tp.servicios;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.excepciones.AplicacionEx;
import ar.unrn.tp.excepciones.ClienteEx;
import ar.unrn.tp.excepciones.TarjetaEx;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.Tarjeta;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JPAClienteServices implements ClienteService {
    @Autowired
    private final EntityManager em;

    public JPAClienteServices(EntityManager em) {
        this.em = em;
    }

    @Override
    public void crearCliente(String nombre, String apellido, String dni, String email) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente nuevoCliente = new Cliente(nombre, apellido, dni, email);
            em.persist(nuevoCliente);
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
    public void modificarCliente(Long idCliente, String nombre, String apellido, String dni, String email) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente cliente = em.find(Cliente.class, idCliente);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDni(dni);
            cliente.setEmail(email);
            //updateData(atributos);
            tx.commit();
        } catch (NoResultException e){
            throw new ClienteEx("No se encontro el cliente: " + e.getMessage());

        } catch (Exception e){
            throw new ClienteEx("No se pudo actualizar el cliente con el ID: " + idCliente);

        }
    }


    @Override
    public void agregarTarjeta(Long idCliente, String nro, String marca) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente cliente = em.find(Cliente.class, idCliente);
            Tarjeta nuevaTarjeta = new Tarjeta(nro, marca);
            cliente.agregarTarjeta(nuevaTarjeta);
            em.merge(cliente);
            tx.commit();
        } catch (NoResultException e) {
            throw new AplicacionEx("No se encontro el cliente o la tarjeta: " + e.getMessage());

        } catch (NonUniqueResultException e) {
            throw new AplicacionEx("Se encontro varios resultados para el ID de clientes o la tarjeta: " + e.getMessage());

        } catch (Exception e) {
            throw new TarjetaEx("No se pudo agregar la tarjeta del cliente ID " + idCliente);
        }
    }

    @Override
    public List listarTarjetas(Long idCliente) {
        try {  Cliente cliente = em.getReference(Cliente.class, idCliente);
            // Cliente cliente = em.find(Cliente.class, idCliente);
            return cliente.getTarjetas();
        } catch (NoResultException e){
            throw new ClienteEx("Cliente con el ID " + idCliente + " no se encontró: " + e.getMessage());
        }
    }

    @Override
    public Cliente buscarCliente(Long idCliente) {
        return em.find(Cliente.class, idCliente);
    }

    @Override
    public Tarjeta buscarTarjeta(Long idTarjeta) {
        return em.getReference(Tarjeta.class, idTarjeta);
    }

    private void existeDNI(EntityManager em, Integer dni) {
        try {
            String consultaCrear = "SELECT c FROM Client c WHERE c.dni = :dni";
            TypedQuery<Cliente> query = em.createQuery(consultaCrear, Cliente.class);
            query.setParameter("dni", dni);

            query.getSingleResult();

        } catch (NoResultException e) {
            throw new ClienteEx("No se encontro un cliente con el DNI proporcionado: " + e.getMessage());

        } catch (Exception e) {
            throw new ClienteEx("Error al querer recuperar el cliente por dni: " + e.getMessage());
        }
    }

}

