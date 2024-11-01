package ar.unrn.tp.modelo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue
    private Long id;

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni=" + dni +
                ", email='" + email + '\'' +
                ", tarjetas=" + (tarjetaCred != null ? "tamaño=" + tarjetaCred.size() : "null") +
                '}';
    }

    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    @JsonManagedReference
    private List<Tarjeta> tarjetaCred;

    protected Cliente() {
    }

    public Cliente(String nombre, String apellido, String dni, String email) {

        Objects.requireNonNull(dni);
        Objects.requireNonNull(nombre);
        Objects.requireNonNull(apellido);
        Objects.requireNonNull(email);
        if (nombre.isEmpty())
            throw new RuntimeException("El nombre no puede estar vacio.");

        if (apellido.isEmpty())
            throw new RuntimeException("El apellido no puede esta vacio.");

        if (email.isEmpty())
            throw new RuntimeException("Debe ingresar un mail");

        if (!emailValido(email))
            throw new RuntimeException("Email debe ser valido");


        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.tarjetaCred = new ArrayList<>();

    }


    private boolean emailValido(String email) {
        return email.contains("@");
    }

    public void agregarTarjeta(Tarjeta unaTarjeta) {
        tarjetaCred.add(unaTarjeta);
    }

    private List<Tarjeta> getTarjeta() {
        return tarjetaCred;
    }

    private String getDNI() {
        return dni;
    }

    private String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    private String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetaCred;
    }

    public void setTarjetaCred(List<Tarjeta> tarjetaCred) {
        this.tarjetaCred = tarjetaCred;
    }

}
