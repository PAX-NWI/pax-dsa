package de.pax.dsa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by swinter on 04.04.2017.
 */
public class HelloWorld {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(HelloWorld.class);
        logger.info("Hello World!");
        logger.info("Hallo Sascha");
    }
}
