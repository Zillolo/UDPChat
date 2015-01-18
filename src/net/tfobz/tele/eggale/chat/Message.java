package net.tfobz.tele.eggale.chat;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Message {

	private static long lastId = 0;

	private long id;

	private InetAddress host;

	private int port;

	private String owner;

	private String message;

	public Message(DatagramPacket packet) throws UnsupportedEncodingException {
		if (packet != null) {
			id = lastId++;

			host = packet.getAddress();
			port = packet.getPort();

			parseData(packet.getData());
		} else {
			throw new IllegalArgumentException("Packet may not be null.");
		}
	}

	public Message(String owner, String message) {
		if (owner != null && message != null) {
			this.owner = owner;
			this.message = message;
		} else {
			throw new IllegalArgumentException(
					"Neither of the arguments may be null.");
		}
	}

	public long getId() {
		return id;
	}

	public void setHost(InetAddress host) {
		if (host != null) {
			this.host = host;
		} else {
			throw new IllegalArgumentException("Host may not be null.");
		}
	}

	public InetAddress getHost() {
		return host;
	}

	public void setPort(int port) {
		if (port >= 0 && port <= 65535) {
			this.port = port;
		} else {
			throw new IllegalArgumentException("Port must be in range.");
		}
	}

	public int getPort() {
		return port;
	}

	public void setOwner(String owner) {
		if (owner != null && owner.isEmpty() == false) {
			this.owner = owner;
		} else {
			throw new IllegalArgumentException(
					"Owner may not be null nor empty.");
		}
	}

	public String getOwner() {
		return owner;
	}

	public void setMessage(String message) {
		if (message != null) {
			this.message = message;
		} else {
			throw new IllegalArgumentException("Message may not be null.");
		}
	}

	public String getMessage() {
		return message;
	}

	private void parseData(byte[] data) throws UnsupportedEncodingException {
		if (data != null && data.length != 0) {
			String s = new String(data, "UTF-8");
			String[] s1 = s.split(":");
			if (s1.length > 1) {
				owner = s1[0];
				message = "";
				for (int i = 1; i < s1.length; i++) {
					message += s1[i];
				}
			}
		} else {
			throw new IllegalArgumentException(
					"Data may not be null nor empty.");
		}
	}

	public String toString() {
		return this.owner + ": " + this.message;
	}
}
