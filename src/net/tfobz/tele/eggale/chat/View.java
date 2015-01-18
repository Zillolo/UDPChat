package net.tfobz.tele.eggale.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Queue;

import javax.net.ssl.HostnameVerifier;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class View extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1585255825023458415L;

	private Queue<Message> outgoingQueue;

	private JTextField inputField;
	private JButton sendButton;
	private JButton joinButton;
	private JScrollPane scrollPane;
	private JScrollPane scrollUser;
	private JScrollPane scrollHost;
	private JScrollPane scrollPort;
	private JTextField usernameField;
	private JTextField groupField;
	private JTextField portField;
	private JTextArea chatArea;

	private boolean joinRequested = true;

	public View(Queue<Message> out) {
		this.outgoingQueue = out;

		this.setLayout(null);
		this.setSize(800, 600);

		chatArea = new JTextArea();
		chatArea.setEditable(false);
		// chatArea.setLineWrap(true);
		this.add(chatArea);

		scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createTitledBorder("Messages"));
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(chatArea);

		scrollPane.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						e.getAdjustable().setValue(
								e.getAdjustable().getMaximum());
					}
				});

		scrollPane.setSize(600, 500);
		scrollPane.setLocation(10, 10);
		this.add(scrollPane);

		inputField = new JTextField(40);
		inputField.setSize(600, 30);
		inputField.setLocation(10, 530);
		this.add(inputField);

		usernameField = new JTextField(40);
		usernameField.setSize(150, 30);
		this.add(usernameField);

		scrollUser = new JScrollPane();
		scrollUser.setBorder(BorderFactory.createTitledBorder("Username"));
		scrollUser
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollUser
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollUser.setViewportView(usernameField);
		scrollUser.setSize(165, 50);
		scrollUser.setLocation(615, 150);
		this.add(scrollUser);

		groupField = new JTextField(40);
		groupField.setText("224.0.8.8");
		this.add(groupField);

		portField = new JTextField(40);
		portField.setText("12347");
		this.add(portField);

		scrollPort = new JScrollPane();
		scrollPort = new JScrollPane();
		scrollPort.setBorder(BorderFactory.createTitledBorder("Port"));
		scrollPort
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPort
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPort.setViewportView(portField);
		scrollPort.setSize(165, 50);
		scrollPort.setLocation(615, 70);
		this.add(scrollPort);

		scrollHost = new JScrollPane();
		scrollHost.setBorder(BorderFactory.createTitledBorder("Group"));
		scrollHost
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollHost
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollHost.setViewportView(groupField);
		scrollHost.setSize(165, 50);
		scrollHost.setLocation(615, 20);
		this.add(scrollHost);

		sendButton = new JButton("Send");
		sendButton.setSize(150, 30);
		sendButton.setLocation(625, 530);
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String data = inputField.getText();

				// TODO: Sanitize

				String username = usernameField.getText();
				if (username.isEmpty() == true) {
					username = "Anonym";
				}

				Message message = new Message(username, data);
				outgoingQueue.add(message);

				inputField.setText("");
			}

		});
		this.add(sendButton);

		joinButton = new JButton("Join");
		joinButton.setSize(150, 30);
		joinButton.setLocation(625, 490);
		joinButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				joinRequested = true;
			}

		});
		this.add(joinButton);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// this.setResizable(false);
		this.setTitle("UDPChat");
		this.setVisible(true);
	}

	public void addText(String text) {
		chatArea.append(text);
		chatArea.append("\n");
	}

	public boolean joinRequested() {
		boolean ret = joinRequested;
		joinRequested = false;
		return ret;
	}

	public String getHost() {
		return groupField.getText();
	}

	public int getPort() throws NumberFormatException {
		try {
			return Integer.parseInt(portField.getText());
		} catch (NumberFormatException e) {
			throw e;
		}
	}

	public String getUsername() {
		String username = this.usernameField.getText();
		if (username != null && username.isEmpty() == false) {
			return username;
		} else {
			return "Anonym";
		}
	}

	public void showAlert(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
}
