package ar.unrn.tp.web;

import ar.unrn.tp.excepciones.ConcurrenciaEx;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.Venta;
import ar.unrn.tp.servicios.JPAProductoServices;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final JPAProductoServices productoService;

    public ProductoController(JPAProductoServices productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> obtenerProductos() {
        return productoService.listarProductos();
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<String> modificarProducto(@RequestBody ProductoRequest request) {
        try {
            System.out.println("HOLA, PASO ACA");
           System.out.println("ID: " + request.getId() + "/n CODIGO: " + request.getCodigo() + "/n DESCRIPCION : "+ request.getDescripcion() + "CATEGORIA : " + request.getMiCategoria() + "MARCA: "+request.getPrecio() +"precio : "+request.getMarca());
            productoService.modificarProducto(request.getId(),request.getCodigo(), request.getDescripcion(), request.getMiCategoria(), request.getPrecio(), request.getMarca());
            return new ResponseEntity<>("Producto modificado exitosamente", HttpStatus.OK);
        } catch (ConcurrenciaEx e) {
            return new ResponseEntity<>("Error de concurrencia: " + e.getMessage(), HttpStatus.CONFLICT);
        } catch (DataAccessException e) {
            return new ResponseEntity<>("Error de acceso a datos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al modificar el producto: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    public static class ProductoRequest {
        private Long id;
        private String codigo;
        private String descripcion;
        private String miCategoria;
        private double precio;
        private String marca;

        // Constructor sin argumentos
        public ProductoRequest() {}
        // Getters y Setters


        public Long getId() {
            return id;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getMiCategoria() {
            return miCategoria;
        }

        public double getPrecio() {
            return precio;
        }

        public String getMarca() {
            return marca;
        }
    }
}
