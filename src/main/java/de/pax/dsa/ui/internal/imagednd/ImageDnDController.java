package de.pax.dsa.ui.internal.imagednd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.function.Consumer;

import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class ImageDnDController {

	private Consumer<ImageDnDResponse> dragConsumer;

	public ImageDnDController(Consumer<ImageDnDResponse> imageConsumer) {
		this.dragConsumer = imageConsumer;
	}

	public void mouseDragDropped(final DragEvent drag) {
		final Dragboard dragboard = drag.getDragboard();
		boolean success = false;
		if (dragboard.hasFiles()) {
			success = true;
			// Only get the first file from the list
			File file = drag.getDragboard().getFiles().get(0);
			try {
				Image image = new Image(new FileInputStream(file.getAbsolutePath()));
				ImageDnDResponse response = new ImageDnDResponse(image, file.getName(), drag.getX(), drag.getY());
				dragConsumer.accept(response);
			} catch (FileNotFoundException e) {
				throw new IllegalStateException("Error reading image to drop", e);
			}

		}
		drag.setDropCompleted(success);
		drag.consume();
	}

	public void mouseDragOver(final DragEvent e) {
		final Dragboard db = e.getDragboard();
		if (db.hasFiles() && db.getFiles().size() == 1) {
			String filename = db.getFiles().get(0).getName().toLowerCase();
			if (filename.endsWith(".png") || filename.endsWith(".jpeg") || filename.endsWith(".jpg")) {
				//only accept drops with exactly one image file
				e.acceptTransferModes(TransferMode.COPY);
			}
		} else {
			e.consume();
		}
	}

}
