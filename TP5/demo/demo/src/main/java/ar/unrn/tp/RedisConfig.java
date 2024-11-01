package ar.unrn.tp;

import ar.unrn.tp.modelo.Venta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
   @Bean
        public JedisPool jedisPool() {
            JedisPoolConfig config = new JedisPoolConfig();

            return new JedisPool(config, "localhost", 6379);
        }


    }


