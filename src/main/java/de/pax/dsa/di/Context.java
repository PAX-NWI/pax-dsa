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

/**
 * Dependency Injection Container to enable usage of @Inject and @PostConstruct
 * 
 * @author alexander.bunkowski
 *
 */
public class Context {

	Logger logger = LoggerFactory.getLogger(Context.class);

	private HashMap<Class<?>, Object> hashMap;

	public Context() {
		hashMap = new HashMap<>();
		hashMap.put(Context.class, this);
	}

	public <T> void set(Class<T> clazz, T object) {
		if (hashMap.containsKey(clazz)) {
			logger.warn("Class {} already exists, it's overwritten now.");
		}
		hashMap.put(clazz, object);
	}

	/**
	 * Creates a new Object of Type T and injects requested objects into it
	 */
	public <T> T create(Class<T> clazz) {
		try {
			T creating = clazz.newInstance();
			wire(creating);
			return creating;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Creates a new Object of Type T, injects requested objects into it and
	 * puts it into context
	 */
	public <T> T createAndSet(Class<T> clazz) {
		T create = create(clazz);
		hashMap.put(clazz, create);
		return create;
	}

	public void wire(Object objectToWire) throws IllegalAccessException, InvocationTargetException {
		Class<? extends Object> clazz = objectToWire.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Inject.class)) {
				field.setAccessible(true);

				// provide new slf4j logger on inject if no logger present
				// logger can be set manually in unit tests to check log
				// responses
				if (field.getType() == Logger.class && !hashMap.containsKey(Logger.class)) {
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
				if (requiredParameters.isEmpty()) {
					method.invoke(objectToWire);
				} else {
					method.invoke(objectToWire, requiredParameters.toArray());
				}
			}
		}
	}

}
