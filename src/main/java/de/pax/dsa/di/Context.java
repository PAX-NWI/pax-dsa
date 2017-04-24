package de.pax.dsa.di;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class Context {

	private HashMap<Class<?>, Object> hashMap;

	public Context() {
		hashMap = new HashMap<>();
		hashMap.put(Context.class, this);
	}

	public void set(Class<?> clazz, Object object) {
		hashMap.put(clazz, object);
	}

	public <T> T create(Class<T> clazz) {
		try {
			T creating = clazz.newInstance();
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(Inject.class)) {
					Object value = hashMap.get(field.getType());
					if (value == null) {
						throw new IllegalStateException("No value registered for " + field.getName());
					}
					field.setAccessible(true);
					field.set(creating, value);
				}
			}

			for (Method method : clazz.getDeclaredMethods()) {
				if (method.isAnnotationPresent(PostConstruct.class)) {
					if (method.getParameters().length != 0) {
						throw new IllegalStateException(
								clazz + "." + method + " invoke fail. can only init empty @PostConstruct Methods");
					}
					method.setAccessible(true);
					method.invoke(creating);
				}
			}

			return creating;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public <T> T createAndSet(Class<T> clazz) {
		T create = create(clazz);
		hashMap.put(clazz, create);
		return create;
	}

}
