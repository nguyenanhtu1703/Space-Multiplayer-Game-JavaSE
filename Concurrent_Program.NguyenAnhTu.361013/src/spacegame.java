import java.awt.EventQueue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class spacegame implements Runnable {

	private JFrame frmSpaceGame;
	
	private String ip = "localhost", myusername, mypassword;
	
	private String username = "username";
	
	private int port = 22222;
	private Scanner scanner = new Scanner(System.in);
	private JFrame frame;
	private final int WIDTH = 506;
	private final int HEIGHT = 527;
	private Thread thread;
	
	private boolean accepted;
	
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	Server server;
	
	Client client;
	
	private boolean isServer = false, unableToCommunicateWithOpponent = false;
	
	int errors = 0, maxConcurrentPlayers = 10;

	private ServerSocket serverSocket;
	
	public JLabel isServerLabel, label00, label, lblXxxxxxxxx, label_2, label_3, label_4, label_5, label_6, label_7, numPlayers, atBound, alowedTxt; 
	private JLabel hasGMPTxt;
	private JLabel scoreTxt;
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					spacegame sp = new spacegame();
					sp.frmSpaceGame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public spacegame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSpaceGame = new JFrame();
		frmSpaceGame.setTitle("Space Game");
		frmSpaceGame.setBounds(100, 100, 541, 588);
		frmSpaceGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSpaceGame.getContentPane().setLayout(null);
		
		label00 = new JLabel("xxxxxxxxx");
		label00.setHorizontalAlignment(SwingConstants.CENTER);
		label00.setFont(new Font("Tahoma", Font.PLAIN, 60));
		label00.setBounds(10, 0, 304, 55);
		frmSpaceGame.getContentPane().add(label00);
		
		isServerLabel = new JLabel("you are server");
		isServerLabel.setForeground(Color.BLUE);
		isServerLabel.setFont(new Font("Tahoma", Font.PLAIN, 43));
		isServerLabel.setBounds(10, 497, 355, 41);
		frmSpaceGame.getContentPane().add(isServerLabel);
		
		numPlayers = new JLabel("there are 6 players");
		numPlayers.setForeground(Color.BLUE);
		numPlayers.setFont(new Font("Tahoma", Font.PLAIN, 25));
		numPlayers.setBounds(10, 470, 355, 41);
		frmSpaceGame.getContentPane().add(numPlayers);
		
		label = new JLabel("xxxxxxxxx");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 60));
		label.setBounds(10, 52, 304, 55);
		frmSpaceGame.getContentPane().add(label);
		
		lblXxxxxxxxx = new JLabel("xxxxxxxxx");
		lblXxxxxxxxx.setHorizontalAlignment(SwingConstants.CENTER);
		lblXxxxxxxxx.setFont(new Font("Tahoma", Font.PLAIN, 60));
		lblXxxxxxxxx.setBounds(10, 104, 304, 55);
		frmSpaceGame.getContentPane().add(lblXxxxxxxxx);
		
		label_2 = new JLabel("xxxxxxxxx");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 60));
		label_2.setBounds(10, 154, 304, 55);
		frmSpaceGame.getContentPane().add(label_2);
		
		label_3 = new JLabel("xxxxxxxxx");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 60));
		label_3.setBounds(10, 204, 304, 55);
		frmSpaceGame.getContentPane().add(label_3);
		
		label_4 = new JLabel("xxxxxxxxx");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 60));
		label_4.setBounds(10, 257, 304, 55);
		frmSpaceGame.getContentPane().add(label_4);
		
		label_5 = new JLabel("xxxxxxxxx");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 60));
		label_5.setBounds(10, 308, 304, 55);
		frmSpaceGame.getContentPane().add(label_5);
		
		label_6 = new JLabel("xxxxxxxxx");
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 60));
		label_6.setBounds(10, 366, 304, 55);
		frmSpaceGame.getContentPane().add(label_6);
		
		label_7 = new JLabel("xxxxxxxxx");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setFont(new Font("Tahoma", Font.PLAIN, 60));
		label_7.setBounds(10, 418, 304, 55);
		frmSpaceGame.getContentPane().add(label_7);
		
		alowedTxt = new JLabel("you are alowed to join");
		alowedTxt.setForeground(Color.BLUE);
		alowedTxt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		alowedTxt.setBounds(324, 257, 163, 55);
		frmSpaceGame.getContentPane().add(alowedTxt);
		
		alowedTxt.setVisible(false);
		
		atBound = new JLabel("you are at bound");
		atBound.setForeground(Color.BLUE);
		atBound.setFont(new Font("Tahoma", Font.PLAIN, 15));
		atBound.setBounds(324, 172, 163, 55);
		frmSpaceGame.getContentPane().add(atBound);
		
		hasGMPTxt = new JLabel("You has GMPrivilage(use SPACE)");
		hasGMPTxt.setForeground(Color.RED);
		hasGMPTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		hasGMPTxt.setBounds(146, 474, 208, 41);
		frmSpaceGame.getContentPane().add(hasGMPTxt);
		
		scoreTxt = new JLabel("");
		scoreTxt.setForeground(Color.BLUE);
		scoreTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scoreTxt.setBounds(319, 27, 138, 28);
		frmSpaceGame.getContentPane().add(scoreTxt);
		
		scoreTxt.setVisible(false);
		
		hasGMPTxt.setVisible(false);
		
		atBound.setVisible(false);
		
		/*

		System.out.println("Please input your username: ");
		myusername = scanner.nextLine();
		System.out.println("Please input the password: ");
		mypassword = scanner.nextLine();
*/
		
		while (port < 1 || port > 65535) {
			System.out.println("The password you entered was invalid, please input another password(digits only): ");
			port = scanner.nextInt();
		}
		
		if (!connect()) initializeServer();
		
		frmSpaceGame.addKeyListener(new InputPC());
		
		thread = new Thread(this, "Space Game");
		
		thread.start();
	}
	

    public void Sleep() {
    	  try        
          {
              Thread.sleep(100);
          } 
          catch(InterruptedException ex) 
          {
              Thread.currentThread().interrupt();
          }
    }

	private boolean connect() {
		// TODO Auto-generated method stub
		try {
			System.out.println("1");
			
			socket = new Socket(ip, port);
			
			isServer = false;

			System.out.println("2");
			
			client = new Client(socket);
	
		} catch (Exception e) {
			System.out.println("Unable to connect to the address: " + ip + ":" + port + " | Starting a server");
			return false;
		}
		System.out.println("Successfully connected to the server.");
		return true;
	}

	private void initializeServer() {
		try {
			serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
			server = new Server(serverSocket);
			System.out.println("Successfully Created Server");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		isServer = true;
		// TODO Auto-generated method stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Update();
			} catch (Exception e) {
				// TODO: handle exception
			}
			//lolocalSystem.out.println("running123 " + isServer);
		}
	}

	public void Update() {
		if (isServer) {
			String tmp00, tmp, tmp_1, tmp_2, tmp_3, tmp_4, tmp_5, tmp_6, tmp_7;
			tmp00 = tmp = tmp_1 = tmp_2 = tmp_3 = tmp_4 = tmp_5 = tmp_6 = tmp_7 = "";
			isServerLabel.setVisible(true);
			atBound.setVisible(false);
			numPlayers.setText("number players: " + Integer.toString(server.concurrentPlayers) + " / " + Integer.toString(server.limitedPlayers));
			int[] boardTmp = new int[9 * 9 + 1];
			for(int i = 0; i < 9 * 9; i++)
				boardTmp[i] = server.board[i];


			//System.out.println(" Update ");
			
			if (server.players != null) {
				for (int i = 0; i < server.players.size(); i++) {
					boolean ok = false;
					if (server.ignorePlayers != null) for (int ii = 0; ii <= server.ignorePlayers.size() - 1; ii++) {
						if (server.ignorePlayers.get(ii) == i + 1)
							ok = true;
						System.out.println(server.ignorePlayers.get(ii) + " AWFAWEFAWEFAWEFAEW");
					}
					if (ok == false) boardTmp[(server.players.get(i).x - 1) * 9 + server.players.get(i).y - 1] = 2;
				}
			}

			
			for(int i = 0; i < 9 * 9; i++)
			{
				if (i / 9 == 0) {
					if (boardTmp[i] == 0)
						tmp00 = tmp00 + "o";
					else if (boardTmp[i] == 2)
						tmp00 = tmp00 + "u";
					else tmp00 = tmp00 + "d";
				}
				if (i / 9 == 1) {
					if (boardTmp[i] == 0)
						tmp = tmp + "o";
					else if (boardTmp[i] == 2)
						tmp = tmp + "u";
					else  tmp = tmp + "d";
				}
				if (i / 9 == 2) {
					if (boardTmp[i] == 0)
						tmp_1 = tmp_1 + "o";
					else if (boardTmp[i] == 2)
						tmp_1 = tmp_1 + "u";
					else tmp_1 = tmp_1 + "d";
				}
				if (i / 9 == 3) {
					if (boardTmp[i] == 0)
						tmp_2 = tmp_2 + "o";
					else if (boardTmp[i] == 2)
						tmp_2 = tmp_2 + "u";
					else  tmp_2 = tmp_2 + "d";
				}
				if (i / 9 == 4) {
					if (boardTmp[i] == 0)
						tmp_3 = tmp_3 + "o";
					else if (boardTmp[i] == 2)
						tmp_3 = tmp_3 + "u";
					else  tmp_3 = tmp_3 + "d";
				}
				if (i / 9 == 5) {
					if (boardTmp[i] == 0)
						tmp_4 = tmp_4 + "o";
					else if (boardTmp[i] == 2)
						tmp_4 = tmp_4 + "u";
					else  tmp_4 = tmp_4 + "d";
				}
				if (i / 9 == 6) {
					if (boardTmp[i] == 0)
						tmp_5 = tmp_5 + "o";
					else if (boardTmp[i] == 2)
						tmp_5 = tmp_5 + "u";
					else  tmp_5 = tmp_5 + "d";
				}
				if (i / 9 == 7) {
					if (boardTmp[i] == 0)
						tmp_6 = tmp_6 + "o";
					else  if (boardTmp[i] == 2)
						tmp_6 = tmp_6 + "u";
					else tmp_6 = tmp_6 + "d";
				}
				if (i / 9 == 8) {
					if (boardTmp[i] == 0)
						tmp_7 = tmp_7 + "o";
					else  if (boardTmp[i] == 2)
						tmp_7 = tmp_7 + "u";
					else tmp_7 = tmp_7 + "d";
				}
			}
			label00.setText(tmp00);
			label.setText(tmp);
			lblXxxxxxxxx.setText(tmp_1);
			label_2.setText(tmp_2);
			label_3.setText(tmp_3);
			label_4.setText(tmp_4);
			label_5.setText(tmp_5);
			label_6.setText(tmp_6);
			label_7.setText(tmp_7);
		} else {
			isServerLabel.setVisible(false);
			isServerLabel.setText("i am not Server");
			numPlayers.setVisible(false);
			scoreTxt.setVisible(true);
			if (client != null && client.hasGMPrivilage) {
				hasGMPTxt.setVisible(true);
			}
			if (client.alowed == false) {
				atBound.setVisible(false);
				alowedTxt.setText("Server is overloaded");
				alowedTxt.setVisible(true);
				return;
			}
			String tmp00, tmp, tmp_1, tmp_2, tmp_3, tmp_4, tmp_5, tmp_6, tmp_7;
			tmp00 = tmp = tmp_1 = tmp_2 = tmp_3 = tmp_4 = tmp_5 = tmp_6 = tmp_7 = "";
			int[] boardTmp = new int[9 * 9 + 1];
			for(int i = 0; i < 9 * 9; i++)
				boardTmp[i] = client.board[i];

			if (client.players.get(client.myPos - 1).x == 1 || client.players.get(client.myPos - 1).x == 9 || client.players.get(client.myPos - 1).y == 1 || client.players.get(client.myPos - 1).y == 9)
				atBound.setVisible(true);
			else atBound.setVisible(false); 

			if (client.players != null) {
				for (int i = 0; i < client.players.size(); i++) {
					boolean ok = false;
					if (client.ignorePlayers != null) for (int ii = 0; ii <= client.ignorePlayers.size() - 1; ii++)
						if (client.ignorePlayers.get(ii) == i + 1)
							ok = true;
					if (ok == false) boardTmp[(client.players.get(i).x - 1) * 9 + client.players.get(i).y - 1] = 2;
				}
				
				for(int i = 0; i < 9 * 9; i++) {
					int xx = i / 9 + 1;
					int yy = i % 9 + 1;
					if (client != null && client.myPos - 1 >= 0 && client.myPos - 1 < client.players.size() && (Math.abs(xx - client.players.get(client.myPos - 1).x) > 2 || Math.abs(yy - client.players.get(client.myPos - 1).y) > 2))
						 boardTmp[i] = 3;
				}
			}

			for(int i = 0; i < 9 * 9; i++)
			{
				if (i / 9 == 0) {
					if (boardTmp[i] == 0)
						tmp00 = tmp00 + "o";
					else if (boardTmp[i] == 2)
						tmp00 = tmp00 + "u";
					else if (boardTmp[i] == 3)
						tmp00 = tmp00 + "x";
					else tmp00 = tmp00 + "d";
				}
				if (i / 9 == 1) {
					if (boardTmp[i] == 0)
						tmp = tmp + "o";
					else if (boardTmp[i] == 2)
						tmp = tmp + "u";
					else if (boardTmp[i] == 3)
						tmp = tmp + "x";
					else tmp = tmp + "d";
				}
				if (i / 9 == 2) {
					if (boardTmp[i] == 0)
						tmp_1 = tmp_1 + "o";
					else if (boardTmp[i] == 2)
						tmp_1 = tmp_1 + "u";
					else if (boardTmp[i] == 3)
						tmp_1 = tmp_1 + "x";
					else tmp_1 = tmp_1 + "d";
				}
				if (i / 9 == 3) {
					if (boardTmp[i] == 0)
						tmp_2 = tmp_2 + "o";
					else if (boardTmp[i] == 2)
						tmp_2 = tmp_2 + "u";
					else if (boardTmp[i] == 3)
						tmp_2 = tmp_2 + "x";
					else tmp_2 = tmp_2 + "d";
				}
				if (i / 9 == 4) {
					if (boardTmp[i] == 0)
						tmp_3 = tmp_3 + "o";
					else if (boardTmp[i] == 2)
						tmp_3 = tmp_3 + "u";
					else if (boardTmp[i] == 3)
						tmp_3 = tmp_3 + "x";
					else  tmp_3 = tmp_3 + "d";
				}
				if (i / 9 == 5) {
					if (boardTmp[i] == 0)
						tmp_4 = tmp_4 + "o";
					else if (boardTmp[i] == 2)
						tmp_4 = tmp_4 + "u";
					else if (boardTmp[i] == 3)
						tmp_4 = tmp_4 + "x";
					else  tmp_4 = tmp_4 + "d";
				}
				if (i / 9 == 6) {
					if (boardTmp[i] == 0)
						tmp_5 = tmp_5 + "o";
					else if (boardTmp[i] == 2)
						tmp_5 = tmp_5 + "u";
					else if (boardTmp[i] == 3)
						tmp_5 = tmp_5 + "x";
					else tmp_5 = tmp_5 + "d";
				}
				if (i / 9 == 7) {
					if (boardTmp[i] == 0)
						tmp_6 = tmp_6 + "o";
					else  if (boardTmp[i] == 2)
						tmp_6 = tmp_6 + "u";
					else if (boardTmp[i] == 3)
						tmp_6 = tmp_6 + "x";
					else tmp_6 = tmp_6 + "d";
				}
				if (i / 9 == 8) {
					if (boardTmp[i] == 0)
						tmp_7 = tmp_7 + "o";
					else  if (boardTmp[i] == 2)
						tmp_7 = tmp_7 + "u";
					else if (boardTmp[i] == 3)
						tmp_7 = tmp_7 + "x";
					else tmp_7 = tmp_7 + "d";
				}
			}
			label00.setText(tmp00);
			label.setText(tmp);
			lblXxxxxxxxx.setText(tmp_1);
			label_2.setText(tmp_2);
			label_3.setText(tmp_3);
			label_4.setText(tmp_4);
			label_5.setText(tmp_5);
			label_6.setText(tmp_6);
			label_7.setText(tmp_7);
			scoreTxt.setText("SCORE:" + Integer.toString(client.score));
		}
	}
	
	public void Modify(int b, int c) {
		System.out.println(b + " " + c);
		if (b == 1) {
			StringBuilder tmp = new StringBuilder(label00.getText());
			tmp.setCharAt(c - 1, 'x');
			label00.setText(tmp.toString());
		}
		if (b == 2) {
			StringBuilder tmp = new StringBuilder(label.getText());
			tmp.setCharAt(c - 1, 'x');
			label.setText(tmp.toString());
		}
		if (b == 3) {
			StringBuilder tmp = new StringBuilder(lblXxxxxxxxx.getText());
			tmp.setCharAt(c - 1, 'x');
			lblXxxxxxxxx.setText(tmp.toString());
		}
		if (b == 4) {
			StringBuilder tmp = new StringBuilder(label_2.getText());
			tmp.setCharAt(c - 1, 'x');
			label_2.setText(tmp.toString());
		}
		if (b == 5) {
			StringBuilder tmp = new StringBuilder(label_3.getText());
			tmp.setCharAt(c - 1, 'x');
			label_3.setText(tmp.toString());
		}
		if (b == 6) {
			StringBuilder tmp = new StringBuilder(label_4.getText());
			tmp.setCharAt(c - 1, 'x');
			label_4.setText(tmp.toString());
		}
		if (b == 7) {
			StringBuilder tmp = new StringBuilder(label_5.getText());
			tmp.setCharAt(c - 1, 'x');
			label_5.setText(tmp.toString());
		}
		if (b == 8) {
			StringBuilder tmp = new StringBuilder(label_6.getText());
			tmp.setCharAt(c - 1, 'x');
			label_6.setText(tmp.toString());
		}
		if (b == 9) {
			StringBuilder tmp = new StringBuilder(label_7.getText());
			tmp.setCharAt(c - 1, 'x');
			label_7.setText(tmp.toString());
		}
	}
	

	public void ModifyServer(int b, int c) {
		System.out.println(b + " " + c);
		if (b == 1) {
			StringBuilder tmp = new StringBuilder(label00.getText());
			if (server.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label00.setText(tmp.toString());
		}
		if (b == 2) {
			StringBuilder tmp = new StringBuilder(label.getText());
			if (server.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label.setText(tmp.toString());
		}
		if (b == 3) {
			StringBuilder tmp = new StringBuilder(lblXxxxxxxxx.getText());
			if (server.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			lblXxxxxxxxx.setText(tmp.toString());
		}
		if (b == 4) {
			StringBuilder tmp = new StringBuilder(label_2.getText());
			if (server.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_2.setText(tmp.toString());
		}
		if (b == 5) {
			StringBuilder tmp = new StringBuilder(label_3.getText());
			if (server.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_3.setText(tmp.toString());
		}
		if (b == 6) {
			StringBuilder tmp = new StringBuilder(label_4.getText());
			if (server.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_4.setText(tmp.toString());
		}
		if (b == 7) {
			StringBuilder tmp = new StringBuilder(label_5.getText());
			if (server.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_5.setText(tmp.toString());
		}
		if (b == 8) {
			StringBuilder tmp = new StringBuilder(label_6.getText());
			if (server.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_6.setText(tmp.toString());
		}
		if (b == 9) {
			StringBuilder tmp = new StringBuilder(label_7.getText());
			if (server.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_7.setText(tmp.toString());
		}
	}



	public void ModifyClient(int b, int c) {
		System.out.println(b + " " + c);
		if (b == 1) {
			StringBuilder tmp = new StringBuilder(label00.getText());
			if (client.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label00.setText(tmp.toString());
		}
		if (b == 2) {
			StringBuilder tmp = new StringBuilder(label.getText());
			if (client.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label.setText(tmp.toString());
		}
		if (b == 3) {
			StringBuilder tmp = new StringBuilder(lblXxxxxxxxx.getText());
			if (client.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			lblXxxxxxxxx.setText(tmp.toString());
		}
		if (b == 4) {
			StringBuilder tmp = new StringBuilder(label_2.getText());
			if (client.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_2.setText(tmp.toString());
		}
		if (b == 5) {
			StringBuilder tmp = new StringBuilder(label_3.getText());
			if (client.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_3.setText(tmp.toString());
		}
		if (b == 6) {
			StringBuilder tmp = new StringBuilder(label_4.getText());
			if (client.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_4.setText(tmp.toString());
		}
		if (b == 7) {
			StringBuilder tmp = new StringBuilder(label_5.getText());
			if (client.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_5.setText(tmp.toString());
		}
		if (b == 8) {
			StringBuilder tmp = new StringBuilder(label_6.getText());
			if (client.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_6.setText(tmp.toString());
		}
		if (b == 9) {
			StringBuilder tmp = new StringBuilder(label_7.getText());
			if (client.board[(b - 1) * 9 + c - 1] == 'o') tmp.setCharAt(c - 1, 'o');
			else tmp.setCharAt(c - 1, 'd');
			label_7.setText(tmp.toString());
		}
	}

	
	private void tick() {
	}
	
	public class PlayerObject {
		public int order;
		public int x, y;
		public PlayerObject(int or, int a, int b) {
			order = or;
			x = a;
			y = b;
			// TODO Auto-generated constructor stub
		}
	}
	
	
	private class InputPC implements KeyListener {
		
		public InputPC() {
			//add
			// TODO Auto-generated constructor stub
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (isServer)
				return;
			if (client == null)
				return;
			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				try {
					client.Up();
					Sleep();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
				try {
					client.Left();
					Sleep();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				try {
					client.Down();
					Sleep();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
				try {
					client.Right();
					Sleep();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE && client.hasGMPrivilage == true) {
				try {
					client.GMP();
					Sleep();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			System.out.println("Key " + e.getKeyCode() + " pressed!");
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}	
	}
}
