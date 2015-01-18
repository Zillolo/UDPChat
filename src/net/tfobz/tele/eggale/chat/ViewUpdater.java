package net.tfobz.tele.eggale.chat;

import java.util.Iterator;
import java.util.Queue;

public class ViewUpdater extends Thread {

	private Queue<Message> queue;

	private View view;

	public ViewUpdater(View view, Queue<Message> queue) {
		this.queue = queue;
		this.view = view;
	}

	public void run() {
		while (Thread.currentThread().isInterrupted() == false) {
			Iterator<Message> iterator = queue.iterator();
			while (iterator.hasNext()) {
				view.addText((iterator.next()).toString());
				iterator.remove();
			}
		}
	}
}
