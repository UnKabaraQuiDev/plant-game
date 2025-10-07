package lu.kbra.plant_game;

import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.ScheduledTask;

public class StandaloneDispatcher extends Dispatcher implements Runnable {

	private Thread thread;

	public StandaloneDispatcher(String name) {
		super(name);

		thread = new Thread(this, "Dispatcher#" + name);
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				final ScheduledTask task = queue.take();
				task.run();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Thread getThread() {
		return thread;
	}

	public void shutdownNow() {
		thread.interrupt();
	}

}
