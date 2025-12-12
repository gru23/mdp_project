package servicerclientapp.utils;

import java.util.ArrayList;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONConversion {
	
	public static <T> ArrayList<T> convertArrayList(String jsonResponse, Class<T> clazz) {
		Type listType = TypeToken.getParameterized(ArrayList.class, clazz).getType();
        ArrayList<T> result = new Gson().fromJson(jsonResponse, listType);
        return result;
	}
}
