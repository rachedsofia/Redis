package ar.unrn.tp.servicios;
import ar.unrn.tp.modelo.Venta;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UltimasComprasServices {

    @Autowired
    private JedisPool pool;
    @Autowired
    private JPAVentasService ventasService;
    @Autowired
    private ObjectMapper objectMapper;

    public List<Venta> obtenerUltimasCompras(Long idCliente)  {
        String key = "UltimasCompras:" + idCliente;

        try (Jedis jedis = pool.getResource()) {
           // jedis.del(key);
            if(jedis.exists(key)){

                String comprasJson = jedis.get(key);
                System.out.println("Compras JSON: " + comprasJson);
                return objectMapper.readValue(comprasJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Venta.class));
            }else{
                List<Venta> ultimasCompras = ventasService.obtenerUltimasVentasCliente(idCliente, 3);
                System.out.println("Últimas compras: " + ultimasCompras);
                jedis.set(key, objectMapper.writeValueAsString(ultimasCompras));
                return ultimasCompras;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void registrarNuevaCompra(Venta nuevaVenta) throws Exception {
        ventasService.registrarVenta(nuevaVenta);
        String key = "UltimasCompras:" + nuevaVenta.getCliente().getId(); // Eliminar el espacio

        try (Jedis jedis = pool.getResource()) {
            String comprasJson = jedis.get(key);
            List<Venta> ultimasCompras;

            if (comprasJson != null) {
                ultimasCompras = objectMapper.readValue(comprasJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Venta.class));
            } else {
                ultimasCompras = ventasService.obtenerUltimasVentasCliente(nuevaVenta.getCliente().getId(), 3);
            }

            ultimasCompras.add(0, nuevaVenta);

            if (ultimasCompras.size() > 3) {
                ultimasCompras.remove(ultimasCompras.size() - 1);
            }

            jedis.set(key, objectMapper.writeValueAsString(ultimasCompras));
        }
    }
}