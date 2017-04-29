package de.pax.dsa.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.di.Context;
import de.pax.dsa.ui.internal.MapContent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class IcarusMainController {

	@FXML
	private Pane pane;

	@FXML
	private ListView<String> mapObjectList;

	@FXML
	private Button doMovesButton;

	@Inject
	private Logger logger;

	@Inject
	private Context context;

	@PostConstruct
	private void postConstruct(IIcarusSession session) {

		MapContent mapContent = context.create(MapContent.class);
		Group content = mapContent.build();
		pane.getChildren().add(content);

		doMovesButton.setOnAction(e -> {
			mapContent.doMoves();
		});

		System.out.println("happy");
		logger.info("logger happy");
		session.connect("dsa", "dsa");

	}

}
