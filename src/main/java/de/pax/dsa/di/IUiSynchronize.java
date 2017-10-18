package de.pax.dsa.di;

/**
 * Interface to allow execution of operations that need to be synchronized with
 * the UI without being dependent on an specific UIFramework such as FX. Much
 * easier to handle in Unit test also.
 * 
 * @author alexander.bunkowski
 *
 */
public interface IUiSynchronize {

	void run(Runnable runnable);

}
