package ar.unrn.tp.web;

import ar.unrn.tp.modelo.Tarjeta;
import ar.unrn.tp.servicios.JPAClienteServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {

    private final JPAClienteServices clienteService;

    public TarjetaController(JPAClienteServices clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/{idCliente}")
    public List<Tarjeta> obtenerTarjetas(@PathVariable Long idCliente) {
        return clienteService.listarTarjetas(idCliente);
    }
}
