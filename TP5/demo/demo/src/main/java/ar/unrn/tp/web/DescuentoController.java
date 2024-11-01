package ar.unrn.tp.web;

import ar.unrn.tp.modelo.Descuento;
import ar.unrn.tp.servicios.JPADescuentoServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/descuentos")
public class DescuentoController {

    private final JPADescuentoServices descuentoService;

    public DescuentoController(JPADescuentoServices descuentoService) {
        this.descuentoService = descuentoService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public List<Descuento> obtenerDescuentos() {
        return descuentoService.listarDescuentos();
    }


}
