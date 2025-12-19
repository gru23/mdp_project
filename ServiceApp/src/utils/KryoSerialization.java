package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import models.ClientMessage;

public class KryoSerialization {
	
	public static byte[] serialize(Object obj) {
        Kryo kryo = new Kryo();
        kryo.register(ClientMessage.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output out = new Output(baos);

        kryo.writeClassAndObject(out, obj);
        out.close();

        return baos.toByteArray();
    }
	
	public static Object deserialize(byte[] data) {
        Kryo kryo = new Kryo();
        kryo.register(ClientMessage.class);

        Input in = new Input(new ByteArrayInputStream(data));
        Object obj = kryo.readClassAndObject(in);
        in.close();

        return obj;
    }	
}
