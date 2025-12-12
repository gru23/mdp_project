package org.unibl.etf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.unibl.etf.parts.PartEntity;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//vjerovatno nece trebat', moze se obrisat' jer sam je prebacio kao PartDAO (samo za dijelove (Part) se koristi)
public class RedisPartManager {
	private JedisPool jedisPool;
    private Gson gson;

    // Konstruktor
    //RedisPartManager manager = new RedisPartManager("localhost", 6379);
    public RedisPartManager(String host, int port) {
        this.jedisPool = new JedisPool(host, port);
        this.gson = new Gson();
    }

    // CREATE / UPDATE
    public void savePart(PartEntity part) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = gson.toJson(part);
            jedis.set(part.getCode(), json);
        }
    }

    // READ pojedinačni dio po šifri
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

    // DELETE dio po šifri
    public void deletePart(String code) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(code);
        }
    }

    // READ ALL dijelove
    public List<PartEntity> getAllParts() {
        List<PartEntity> parts = new ArrayList<>();
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys("*"); // za test, u produkciji bolje koristiti set/list
            for (String key : keys) {
                String json = jedis.get(key);
                PartEntity part = gson.fromJson(json, PartEntity.class);
                parts.add(part);
            }
        }
        return parts;
    }

    // Zatvaranje pool-a kada završimo sa aplikacijom
    public void close() {
        jedisPool.close();
    }
}
