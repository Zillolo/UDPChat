package net.tfobz.tele.eggale.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Iterator;
import java.util.Queue;

public class Sender extends Thread {

	private Queue<Message> queue;

	private MulticastSocket socket;

	private InetAddress group;

	private int port;

	private boolean isRunning = true;

	public Sender(MulticastSocket socket, Queue<Message> queue,
			InetAddress group, int port) throws IOException {
		this.queue = queue;

		this.group = group;
		this.port = port;

		this.socket = socket;
	}

	public void run() {
		while (Thread.currentThread().isInterrupted() == false
				&& isRunning == true) {
			DatagramPacket packet = null;

			Iterator<Message> iterator = queue.iterator();
			while (iterator.hasNext() == true) {
				Message message = iterator.next();

				String data = message.toString();
				packet = new DatagramPacket(data.getBytes(),
						data.getBytes().length, group, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					// Lel.
				}

				iterator.remove();
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setRunning(boolean running) {
		this.isRunning = running;
	}
}
