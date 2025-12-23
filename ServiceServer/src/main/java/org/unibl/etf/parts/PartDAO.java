package org.unibl.etf.parts;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import org.unibl.etf.util.Config;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PartDAO {
	private static final Logger LOGGER = Logger.getLogger(PartDAO.class.getName());
	
	private JedisPool jedisPool;
    private Gson gson;
    
    public PartDAO() {
    	this(Config.get("redis.host"), Config.getInt("redis.port"));
    }

//    RedisPartManager manager = new RedisPartManager("localhost", 6379);
    public PartDAO(String host, int port) {
        this.jedisPool = new JedisPool(host, port);
        this.gson = new Gson();
    }
    
    public ArrayList<PartEntity> getAllParts() {
    	ArrayList<PartEntity> parts = new ArrayList<>();
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys("*");
            for (String key : keys) {
                String json = jedis.get(key);
                PartEntity part = gson.fromJson(json, PartEntity.class);
                parts.add(part);
            }
        }
        LOGGER.info("Fetched all parts ");
        return parts;
    }

    public PartEntity getPart(String code) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(code);
            if (json != null) {
                return gson.fromJson(json, PartEntity.class);
            } else {
                return null;
            }
        }
    }
    
    public void savePart(PartEntity part) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = gson.toJson(part);
            jedis.set(part.getCode(), json);
            LOGGER.info("Saved part " + part.getCode());
        }
    }

    public void deletePart(String code) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(code);
            LOGGER.info("Delete part " + code);
        }
    }

    public void close() {
        jedisPool.close();
    }
}
