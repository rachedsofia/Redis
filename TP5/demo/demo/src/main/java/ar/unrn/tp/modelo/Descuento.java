    package ar.unrn.tp.modelo;

    import jakarta.persistence.DiscriminatorColumn;


    import jakarta.persistence.*;
    import lombok.Data;

    import java.time.LocalDate;
    @Data
    @Entity
    @DiscriminatorColumn(name = "tipo_descuento")
    @Table(name = "descuento")

    public abstract class Descuento {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        protected LocalDate fechaInicio;
        protected LocalDate fechaFin;
        private int activo;
        private double porcentaje;



        public Descuento() {
            // Constructor por defecto
        }

        public Descuento(LocalDate fechaInicio, LocalDate fechaFin) {
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
        }

        public boolean estaVigente() {
                LocalDate fechaActual = LocalDate.now();
                return !fechaActual.isBefore(fechaInicio) && !fechaActual.isAfter(fechaFin);

        }


        public abstract double aplicar(Producto producto, double precioProducto);

        public LocalDate getFechaInicio() {
            return fechaInicio;
        }

        public LocalDate getFechaFin() {
            return fechaFin;
        }
        // Getters y Setters...
        public double getPorcentaje() {
            return porcentaje;
        }

        public void setPorcentaje(double porcentaje) {
            this.porcentaje = porcentaje;
        }

    }