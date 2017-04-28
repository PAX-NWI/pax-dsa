package de.pax.dsa.di;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			wire(creating);
			return creating;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public void wire(Object objectToWire) throws IllegalAccessException, InvocationTargetException {
		Class<? extends Object> clazz = objectToWire.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Inject.class)) {
				field.setAccessible(true);

				// provide new slf4j logger on inject
				if (field.getType() == org.slf4j.Logger.class) {
					Logger logger = LoggerFactory.getLogger(objectToWire.getClass());
					field.set(objectToWire, logger);
					continue;
				}

				Object value = hashMap.get(field.getType());
				if (value == null) {
					throw new IllegalStateException("No value registered for field " + field.getName());
				}
				field.set(objectToWire, value);
			}
		}

		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(PostConstruct.class)) {
				List<Object> requiredParameters = new ArrayList<>();
				for (Parameter parameter : method.getParameters()) {
					Object value = hashMap.get(parameter.getType());
					if (value == null) {
						throw new IllegalStateException(
								"No value registered for PostConstruct parameter " + parameter.getName());
					}
					requiredParameters.add(value);
				}
				method.setAccessible(true);
				if (requiredParameters.size() == 0) {
					method.invoke(objectToWire);
				} else {
					method.invoke(objectToWire, requiredParameters.toArray());
				}
			}
		}
	}

	public <T> T createAndSet(Class<T> clazz) {
		T create = create(clazz);
		hashMap.put(clazz, create);
		return create;
	}

}
