package de.pax.dsa.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class IcarusMainController {
	
	@FXML
	private Pane pane;
	
	@Inject
	private Logger logger;
	
	@PostConstruct
	private void postConstruct(IIcarusSession session) {
		System.out.println("happy");
		logger.info("logger happy");
		session.connect("dsa", "dsa");

	}

}
