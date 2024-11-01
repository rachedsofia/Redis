package ar.unrn.tp.web;

import ar.unrn.tp.modelo.Venta;
import ar.unrn.tp.servicios.UltimasComprasServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/compras")
public class ComprasController {
    @Autowired
    private UltimasComprasServices ultimasComprasService;

    @GetMapping("/ultimas/{idCliente}")
    public ResponseEntity<List<Venta>> obtenerUltimasCompras(@PathVariable Long idCliente) {
        List<Venta> ultimasVentas = ultimasComprasService.obtenerUltimasCompras(idCliente);
        return ResponseEntity.ok(ultimasVentas);
    }
   /* @PostMapping("/registrar")
    public void registrarCompra(@RequestBody Venta nuevaVenta) throws Exception {
        ultimasComprasService.registrarNuevaCompra(nuevaVenta);
    }*/


}
