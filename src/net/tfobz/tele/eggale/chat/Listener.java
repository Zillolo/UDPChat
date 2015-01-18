package net.tfobz.tele.eggale.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.Queue;

public class Listener extends Thread {

	private Queue<Message> queue;

	private MulticastSocket socket;

	private boolean isRunning = true;

	public Listener(MulticastSocket socket, Queue<Message> queue)
			throws IOException {
		this.queue = queue;

		this.socket = socket;
	}

	public void run() {
		while (Thread.currentThread().isInterrupted() == false
				&& isRunning == true) {
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				socket.receive(packet);
				queue.add(new Message(packet));
			} catch (IOException e) {
				e.printStackTrace();
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
