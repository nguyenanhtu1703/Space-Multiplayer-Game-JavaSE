import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;


public class Server {	
	public class PlayerPossition {
		public int pos, x, y;
		public PlayerPossition(int a, int b, int c) {
			pos = a;
			x = b;
			y = c;
		}
	}
    private ArrayList<ConnectionToClient> clientList;
    public ArrayList<PlayerPossition> players;
    public ArrayList<Integer> ignorePlayers;
    private LinkedBlockingQueue<Object> messages;
    private ServerSocket serverSocket;
    public int concurrentPlayers = 0;
    public int limitedPlayers = 2;
    public int[] board, boardTmp;
    
    public void Decode(String message) throws IOException {
    	int f = 0, current = 0;
    	message = message + " ";
    	while (current < message.length()) {
    		if (message.charAt(current) == ' ')
    			break;
    		f = f * 10 + (message.charAt(current) - '0');
    		current++;
    	}
    	current++;
    	System.out.println("Server, First number: " + f + ".");
    	
    	if (f == 1) {
    		int s = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		s = s * 10 + (message.charAt(current) - '0');
        		current++;
        	}
        	current++;
        	System.out.println("Server, Second number: " + s + ".");
        	int th = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		th = th * 10 + (message.charAt(current) - '0');
        		current++;
        	}
        	current++;
        	System.out.println("Server, Third number: " + th + ".");
        	int four = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		four = four * 10 + (message.charAt(current) - '0');
        		current++;
        	}
        	current++;
        	System.out.println("Server, fourth number: " + four + ".");
        	
        	
        	players.get(s - 1).x = th;
        	players.get(s - 1).y = four;
        	
        	if (board[(th - 1) * 9 + four - 1] == 1) {
        		board[(th - 1) * 9 + four - 1] = 0;
        		SendScore(s - 1);
        	}
        	
        	UpdateAllPositions();
        	
        	Sleep();
        	
        	sendToAllMap();
    	}
    	

    	if (f == 2) {
			int a = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		a = a * 10 + (message.charAt(current) - '0');
        		current++;
        	}
        	current++;
        	
        	int b = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		b = b * 10 + (message.charAt(current) - '0');
        		current++;
        	}
    		GMPPRESS(a, b + 1);
    		GMPPRESS(a, b - 1);
    		GMPPRESS(a - 1, b);
    		GMPPRESS(a + 1, b);
    		GMPPRESS(a - 1, b - 1);
    		GMPPRESS(a - 1, b + 1);
    		GMPPRESS(a + 1, b + 1);
    		GMPPRESS(a + 1, b - 1);
    		sendToAllMap();
        	current++;
    		System.out.println("GMP PRESSED RECEIVED");
    	}
    }
    
    public void GMPPRESS(int a, int b) {
    	if (a < 1 || a > 9 || b < 1 || b > 9)
    		return;
    	board[(a - 1) * 9 + b - 1] = 1;
    }
    
    public void SendScore(int a) throws IOException {
    	clientList.get(a).out.writeObject("4 ");
    	clientList.get(a).out.flush();
    	Sleep();
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
    
    public Server(ServerSocket tmS) {
        clientList = new ArrayList<ConnectionToClient>();
        players = new ArrayList<PlayerPossition>(); 
        ignorePlayers = new ArrayList<Integer>();
        messages = new LinkedBlockingQueue<Object>();
        serverSocket = tmS;
        
        board = new int[9 * 9 + 1];
        
        for (int i = 0; i < 9 * 9; i++) {
			Random rand = new Random();
			int n = rand.nextInt(5) + 1;
			if (n == 1) board[i] = 1;
			else board[i] = 0;
		}
        
        boardTmp = new int[9 * 9 + 1];
        
        Thread accept = new Thread() {
            public void run(){
                while(true){
                    try{
                        Socket s = serverSocket.accept();
                        clientList.add(new ConnectionToClient(s));
                    }
                    catch(IOException e){ e.printStackTrace(); }
                }
            }
        };

       // accept.setDaemon(true);
        accept.start();

        Thread messageHandling = new Thread() {
            public void run(){
                while(true){
                    try{
                        System.out.println("Message Waiting!");
                        Object message = messages.take();
                        Decode((String) message);
                        // Do some handling here...
                        System.out.println("Message Received in Server: " + message);
                    }
                    catch(InterruptedException e){ } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        };

     //   messageHandling.setDaemon(true);
        messageHandling.start();
    }
    
    public void SendAccepted (ObjectOutputStream out) throws IOException {
    	if (concurrentPlayers < limitedPlayers) {
    		out.writeObject("0 1");
    	} else out.writeObject("0 0");
    }
    
    public void SendNewPosition (ObjectOutputStream out, int pos, int x, int y) throws IOException {
    	String z = "1 " + Integer.toString(pos) + " " + Integer.toString(x) + " " + Integer.toString(y) + " ";
    	out.writeObject(z);
    }

    private class ConnectionToClient {
        public ObjectInputStream in;
        public ObjectOutputStream out;
        public Socket socket;
        
        public void SendGMP(int a) throws IOException {
        	String message = Integer.toString(a) + " ";
        	out.writeObject(message);
        	out.flush();
        }
        
        public void WriteMap() throws IOException {
        	String message = "2 ";
        	for (int i = 0; i < 9 * 9; i ++) {
        		message = message + Integer.toString(board[i]) + " ";
        	}
        	out.writeObject(message);
        	out.flush();
        }
        
        public void SendAllIgnorePs() throws IOException {
        	
        	String message = "9 ";
        	
        	int num = ignorePlayers.size();
        	
        	message = message + Integer.toString(num) + " ";
        	
        	for (int i = 0; i < num; i ++) {
        		message = message + Integer.toString(ignorePlayers.get(i)) + " ";
        	}  
        	
        	System.out.println("SERVER Ignore" + message);
        	
        	out.writeObject(message);
        	out.flush();
        }
        
        public void SendAllPositionPlayers() throws IOException {
        	
        	String message = "3 ";
        	
        	int num = players.size();
        	
        	message = message + Integer.toString(num) + " ";
        	
        	for (int i = 0; i < num; i ++) {
        		message = message + Integer.toString(players.get(i).pos) + " " + Integer.toString(players.get(i).x) + " " + Integer.toString(players.get(i).y) + " ";
        	}  
        	
        	System.out.println("SERVER " + message);
        	
        	out.writeObject(message);
        	out.flush();
        }

        ConnectionToClient(Socket socket) throws IOException {
            this.socket = socket;
            
            out = new ObjectOutputStream(socket.getOutputStream());

            in = new ObjectInputStream(socket.getInputStream());
            
            SendAccepted(out);
            
            Sleep();
            
            if (concurrentPlayers < limitedPlayers) {
            	
            	concurrentPlayers ++;
            	
            	Random rand = new Random();
            	
            	int pos = clientList.size() + 1;
            	
            	int x = rand.nextInt(9) + 1;
            	int y = rand.nextInt(9) + 1;
            	
            	SendNewPosition(out, pos, x, y);
            	
            	Sleep();
            	
            	int k = rand.nextInt(3) + 1;
            	
            	SendGMP(4 + k);
            	
            	Sleep();
            	
            	players.add(new PlayerPossition(pos, x, y));
            	
            	WriteMap();
            	
            	Sleep();
            	
            	UpdateAllPositions();
            	
            	Sleep();
            	
            	SendAllPositionPlayers();
            	
            	Sleep();
            	
            	SendAllIgnorePs();
            	
            	Sleep();
            }
            
          // out.writeObject("1234 nguyen anh tu 1234565oi65oi6uo5i6uo5i6o");
            
          //  out.flush();
            
            
            Thread read = new Thread(){
                public void run(){
                	int bug = 0;
                	int poss = clientList.size();
                	System.out.println(poss + " KKKKKKKKK");
                    while(true){
                        try {
                            Object obj = in.readObject();
                            messages.put(obj);
                        }
                        catch(IOException e){ e.printStackTrace(); bug++; } 
                        
                        catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							bug++;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							bug++;
						}
                        if (bug >= 30) {
                        	concurrentPlayers--;
                        	ignorePlayers.add(poss);
                        	System.out.println("SERVER Ignore" + poss);
                        	try {
								SendAllIgnorePlayers ();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        	break;
                        }
                   }
                }
            };
            
            

          //  read.setDaemon(true); // terminate when main ends
            read.start();
        }

        public void write(Object obj) {
            try{
                out.writeObject(obj);
            }
            catch(IOException e){ e.printStackTrace(); }
        }
    }

    public void sendToOne(int index, Object message)throws IndexOutOfBoundsException {
        clientList.get(index).write(message);
    }

    public void sendToAll(Object message) {
    	int z = 0;
        for(ConnectionToClient client : clientList) {
        	z++;
			boolean ok = true;
			for (int i = 0; i < ignorePlayers.size(); i++)
    			 if (ignorePlayers.get(i) == z)
    				 ok = false;
			 if (ok) client.write(message);
        }
    }
    
    public void UpdateAllPositions() throws IOException {
    	 int z = 0;
    	 for(ConnectionToClient client : clientList) {
    		 z++;
    		 boolean ok = true;
    		 for (int i = 0; i < ignorePlayers.size(); i++)
    			 if (ignorePlayers.get(i) == z)
    				 ok = false;
             if (ok) client.SendAllPositionPlayers();
         }
    }
    
    public void SendAllIgnorePlayers () throws IOException {
    	int z = 0;
   	 	for(ConnectionToClient client : clientList) {
	   	 	z++;
			boolean ok = true;
			for (int i = 0; i < ignorePlayers.size(); i++)
    			 if (ignorePlayers.get(i) == z)
    				 ok = false;
			 if (ok) client.SendAllIgnorePs ();
        }
    }
    
    public void sendToAllMap() throws IOException {
    	int z = 0;
        for(ConnectionToClient client : clientList) {
        	z++;
			boolean ok = true;
			for (int i = 0; i < ignorePlayers.size(); i++)
    			 if (ignorePlayers.get(i) == z)
    				 ok = false;
			 if (ok) client.WriteMap();
        }
    }

}