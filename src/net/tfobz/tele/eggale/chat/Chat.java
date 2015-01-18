package net.tfobz.tele.eggale.chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Chat extends Thread {

	private InetAddress host;

	private int port;

	private Queue<Message> incomingQueue;

	private Queue<Message> outgoingQueue;

	private ViewUpdater viewUpdater;

	private Listener listener;

	private Sender sender;

	private MulticastSocket socket;

	private View view;

	public Chat(String host, int port) {
		this.port = port;
		try {
			this.host = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		incomingQueue = new ConcurrentLinkedQueue<Message>();
		outgoingQueue = new ConcurrentLinkedQueue<Message>();

		view = new View(outgoingQueue);

		viewUpdater = new ViewUpdater(view, incomingQueue);
		viewUpdater.start();

		boolean ok = false;
		while (Thread.currentThread().isInterrupted() == false) {
			if (view.joinRequested() == true) {
				ok = true;
				try {
					this.host = InetAddress.getByName(view.getHost());
				} catch (UnknownHostException e1) {
					view.showAlert("Invalid host name.");
					ok = false;
				}

				try {
					this.port = view.getPort();

					if (port < 1 || port > 65535) {
						view.showAlert("Invalid port.");
						ok = false;
					}
				} catch (NumberFormatException e) {
					view.showAlert("Invalid Port.");
					ok = false;
				}

				if (ok) {

					if (socket != null) {
						try {
							socket.leaveGroup(host);
						} catch (IOException e) {
							e.printStackTrace();
						}
						socket.close();
					}

					if (listener != null && listener.isAlive()) {
						listener.setRunning(false);
					}

					if (sender != null && sender.isAlive()) {
						sender.setRunning(false);
					}

					System.out.println("Joining " + host + " on " + port);
					try {
						socket = new MulticastSocket(port);
						socket.joinGroup(host);

						listener = new Listener(socket, incomingQueue);
						sender = new Sender(socket, outgoingQueue, host, port);

						listener.start();
						sender.start();

						outgoingQueue
								.add(new Message(InetAddress.getLocalHost()
										.toString(), "Joined the group "
										+ host.toString() + " as "
										+ view.getUsername()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Chat c = new Chat("224.0.8.8", 12347);
		c.start();
	}

}
