package de.pax.dsa.di;

import javafx.application.Platform;

public class JavaFxUiSynchronize implements IUiSynchronize{

	@Override
	public void run(Runnable runnable) {
		Platform.runLater(runnable);
	}
	
}
