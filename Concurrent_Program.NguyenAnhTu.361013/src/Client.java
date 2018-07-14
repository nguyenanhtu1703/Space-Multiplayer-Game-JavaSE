import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
	
	public class PlayerPossition {
		public int pos, x, y;
		public PlayerPossition(int a, int b, int c) {
			pos = a;
			x = b;
			y = c;
		}
	}
	
    private ConnectionToServer server;
    public ArrayList<PlayerPossition> players;
    public ArrayList<Integer> ignorePlayers; 
    private LinkedBlockingQueue<Object> messages;
    
    private Socket socket;
    
    public int myPos, myX, myY;
    
    public boolean alowed = false;
    
    public boolean hasGMPrivilage = false;
    
    public int[] board;
    
    public int score = 0;
    
    public void Up() throws IOException {
    	if (players == null)
    		return;
    	if (players.get(myPos - 1).x <= 1)
    		return;
    	for (int i = 0; i < players.size(); i++) {
    		boolean ok = false;
    		if (ignorePlayers != null) for (int ii = 0; ii <= ignorePlayers.size() - 1; ii++)
				if (ignorePlayers.get(ii) == i + 1)
					ok = true;
    		
    		if (ok == false && i != myPos - 1 && players.get(myPos - 1).x - 1 == players.get(i).x && players.get(myPos - 1).y == players.get(i).y)
    			return;
    	}
    	server.Send(myPos, players.get(myPos - 1).x - 1, players.get(myPos - 1).y);	
    }
    

	public void Left() throws IOException {
		// TODO Auto-generated method stub
		if (players == null)
    		return;
    	if (players.get(myPos - 1).y <= 1)
    		return;
    	for (int i = 0; i < players.size(); i++) {
    		boolean ok = false;
    		if (ignorePlayers != null) for (int ii = 0; ii <= ignorePlayers.size() - 1; ii++)
				if (ignorePlayers.get(ii) == i + 1)
					ok = true;
    		if (ok == false && i != myPos - 1 && players.get(myPos - 1).x == players.get(i).x && players.get(myPos - 1).y - 1 == players.get(i).y)
    			return;
		}
    	server.Send(myPos, players.get(myPos - 1).x, players.get(myPos - 1).y - 1);	
	}
	
	public void GMP() throws IOException {
		// TODO Auto-generated method stub
		if (players == null)
    		return;
    	server.SendGMP(players.get(myPos - 1).x, players.get(myPos - 1).y);	
	}
	
	public void Right() throws IOException {
		// TODO Auto-generated method stub
		if (players == null)
    		return;
    	if (players.get(myPos - 1).y >= 9)
    		return;
    	for (int i = 0; i < players.size(); i++) {
    		boolean ok = false;
    		if (ignorePlayers != null) for (int ii = 0; ii <= ignorePlayers.size() - 1; ii++)
				if (ignorePlayers.get(ii) == i + 1)
					ok = true;
    		if (ok == false && i != myPos - 1 && players.get(myPos - 1).x == players.get(i).x && players.get(myPos - 1).y + 1 == players.get(i).y)
    			return;
    	}
    	server.Send(myPos, players.get(myPos - 1).x, players.get(myPos - 1).y + 1);	
	}
	public void Down() throws IOException {
		// TODO Auto-generated method stub
		if (players == null)
    		return;
    	if (players.get(myPos - 1).x >= 9)
    		return;
    	for (int i = 0; i < players.size(); i++) {
    		boolean ok = false;
    		if (ignorePlayers != null) for (int ii = 0; ii <= ignorePlayers.size() - 1; ii++)
				if (ignorePlayers.get(ii) == i + 1)
					ok = true;
    		if (ok == false && i != myPos - 1 && players.get(myPos - 1).x + 1 == players.get(i).x && players.get(myPos - 1).y == players.get(i).y)
    			return;
    	}
    	server.Send(myPos, players.get(myPos - 1).x + 1, players.get(myPos - 1).y);	
	}
    
    public void Decode(String message) {
    	int f = 0, current = 0;
    	message = message + " ";
    	while (current < message.length()) {
    		if (message.charAt(current) == ' ')
    			break;
    		f = f * 10 + (message.charAt(current) - '0');
    		current++;
    	}
    	current++;
    	System.out.println("Client, First number: " + f + ".");
    	
    	if (f == 0) {
    		int s = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		s = s * 10 + (message.charAt(current) - '0');
        		current++;
        	}
        	current++;
        	System.out.println("Client, Second number: " + s + ".");
        	if (s == 0) alowed = false;
        	else alowed = true;
    	}
    	
    	if (f == 1) {
    		int s = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		s = s * 10 + (message.charAt(current) - '0');
        		current++;
        	}
        	current++;
        	System.out.println("Client, Second number: " + s + ".");
        	myPos = s;
        	int th = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		th = th * 10 + (message.charAt(current) - '0');
        		current++;
        	}
        	current++;
        	System.out.println("Client, Third number: " + th + ".");
        	myX = th;
        	int four = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		four = four * 10 + (message.charAt(current) - '0');
        		current++;
        	}
        	current++;
        	myY = four;
        	System.out.println("Client, fourth number: " + four + ".");
    	}    	
    	
    	if (f == 2) {
    		for (int i = 0; i < 9 * 9; i ++) {
    			int s = 0;
        		while (current < message.length()) {
            		if (message.charAt(current) == ' ')
            			break;
            		s = s * 10 + (message.charAt(current) - '0');
            		current++;
            	}
            	current++;
            	board[i] = s;
    		}       	
    		System.out.println("Read Map");
    	}
    	
    	if (f == 3) {
    		players = new ArrayList<PlayerPossition>();
    		int num = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		num = num * 10 + (message.charAt(current) - '0');
        		current++;
        	}
        	current++;
        	
    		for (int i = 0; i < num; i ++) {
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
            	current++;
            	
            	int c = 0;
        		while (current < message.length()) {
            		if (message.charAt(current) == ' ')
            			break;
            		c = c * 10 + (message.charAt(current) - '0');
            		current++;
            	}
            	current++;
            	
            	players.add(new PlayerPossition(a, b, c));
    		}       	
    		System.out.println("Read Map");
    	}
    	
    	if (f == 4) {
    		score ++;
    	}
    	
    	if (f == 5 || f == 6) {
    		hasGMPrivilage = true;
    	}
    	
    	if (f == 9) {
    		ignorePlayers = new ArrayList<Integer>();
    		int num = 0;
    		while (current < message.length()) {
        		if (message.charAt(current) == ' ')
        			break;
        		num = num * 10 + (message.charAt(current) - '0');
        		current++;
        	}
        	current++;
        	
    		for (int i = 0; i < num; i ++) {
    			int a = 0;
        		while (current < message.length()) {
            		if (message.charAt(current) == ' ')
            			break;
            		a = a * 10 + (message.charAt(current) - '0');
            		current++;
            	}
            	current++;
            	
            	ignorePlayers.add(a);
    		}       	
    		System.out.println("Read Map");
    	}
    	
    }

    public Client(Socket sk) throws IOException {
        socket = sk;
        
        board = new int[9 * 9 + 10];
        
        messages = new LinkedBlockingQueue<Object>();
        
        server = new ConnectionToServer(socket);

        System.out.println("Client Created");
        
        Thread messageHandling = new Thread() {
            public void run() {
                while (true) {
                    try{
                        Object message = messages.take();
                        Decode((String) message);
                        // Do some handling here...
                        System.out.println("Message Received in Client: " + message);
                    }
                    catch(InterruptedException e){ }
                }
            }
        };

        messageHandling.setDaemon(true);
        messageHandling.start();
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

    private class ConnectionToServer {
        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket;
        
        public void Send(int a, int b, int c) throws IOException {
        	String message = "1 " + Integer.toString(a) + " " + Integer.toString(b) + " "  + Integer.toString(c) + " ";	
        	out.writeObject(message);
        	out.flush();
        	Sleep();
        }
        
        public void SendGMP(int b, int c) throws IOException {
        	String message = "2 " + Integer.toString(b) + " "  + Integer.toString(c) + " ";	
        	out.writeObject(message);
        	out.flush();
        	Sleep();
        }
        
        ConnectionToServer(Socket socket) throws IOException {
        	
            this.socket = socket;
            
            out = new ObjectOutputStream(socket.getOutputStream());
            
         //   out.writeObject(6);

           // out.flush();
            
            in = new ObjectInputStream(socket.getInputStream());
            
            Thread read = new Thread(){
                public void run(){
                    while(true){
                        try{
                            Object obj = in.readObject();
                            messages.put(obj);
                        }
                        catch(IOException e){ e.printStackTrace(); }
                        catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
            };

            read.setDaemon(true);
            read.start();
        }

        private void write(Object obj) {
            try{
                out.writeObject(obj);
            }
            catch(IOException e){ e.printStackTrace(); }
        }
    }

    public void send(Object obj) {
        server.write(obj);
    }

	
}