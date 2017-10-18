package mocks;

import de.pax.dsa.di.IUiSynchronize;

public class MockUiSync implements IUiSynchronize {

	@Override
	public void run(Runnable runnable) {
		runnable.run();
	}

}
