import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;

import java.io.*;


public class server {
	
	public static void main(String[] args) throws IOException{
		ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
		int height = Integer.parseInt(args[1]);
		int width = Integer.parseInt(args[2]);
		String colors[] = new String[args.length-3];
		int n = 0;
		for(int i = 2; i < args.length; i++) {
			if(i > 2) {
				colors[n] = args[i];
				n++;
			}
		}
		
		System.out.println("server local port = " + args[0]);
		//System.out.println("server ip address = " + ss.getLocalSocketAddress());
		System.out.println(Arrays.toString(colors));
		//BBoard board = new BBoard(height, width, colors);
		List<note> notes = new ArrayList<note>();
		//GUI g = new GUI();
		
		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
				
				System.out.println("client connected");
				
				DataInputStream in = new DataInputStream(s.getInputStream());
				DataOutputStream bf = new DataOutputStream(s.getOutputStream());
				
				Thread t = new ClientHandler(s,in,bf, notes, colors, width, height);
				
				t.start();
			}
			catch(Exception e) {
				s.close();
				e.printStackTrace();
			}
		}
		//ss.close();
	}

}

class ClientHandler extends Thread {
	DataInputStream dis;
	DataOutputStream dos;
	Socket s;
	List<note> notes;
	String[] colors;
	final int width;
	final int height;
	//final GUI g;
	
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, List<note> notes, String[] colors, int width, int height) {
		this.s = s;
		this.dis = dis;
		this.dos = dos;
		this.notes = notes;
		this.colors = colors;
		this.width = width;
		this.height = height;
	}
	/*public void POST(note n) {
		g.paint(null, n);
	}*/
	@Override
	public void run() {
		String received;
		String toreturn;
		try {
			dos.writeUTF("available colors: " + Arrays.toString(colors) + "\ndimensions: (width: " + this.width + ", height: " + this.height + ")");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			try {
                //dos.writeUTF("looking for response: ");
				System.out.println(s);
	            // receive the answer from client
				for(note n1 : notes) {
					n1.printNote();
				}
	            received = dis.readUTF();
	            System.out.println(received);
	            String[] data = received.split(" ");
	            System.out.println(Arrays.toString(data));
	            
                switch (data[0]) {
                
	                case "POST" :
	                	int x = Integer.parseInt(data[1]);
	                	int y = Integer.parseInt(data[2]);
	                	int noteHeight = Integer.parseInt(data[3]);
	                	int noteWidth = Integer.parseInt(data[4]);
	                	String colour = data[5].toLowerCase();
	                	String msg = received.substring(received.lastIndexOf(data[5])+data[5].length()+1, received.length());
	                	if(Arrays.toString(this.colors).contains(colour)) {           		
		                	if((x >= 0 && x <= this.width) && (y >= 0 && y <= this.height)){
			                	note n = new note(x,y,noteHeight,noteWidth,colour,msg);
			                	notes.add(n);
			                	
			                	dos.writeUTF("posted note");
		                	}
		                	else {
		                		dos.writeUTF("note out of board range...");
		                	}
	                	}
	                	else {
	                		dos.writeUTF("color not valid...");
	                	}
	                    break;
	                    
	                case "GET" :
	                	String[] contains = {"",""};
	                	String color = "";
	                	String refersTo = "";
	                	String[] getData = received.split("((?<=contains=)|(?=contains=))|((?<=refersTo=)|(?=refersTo=))|((?<=color=)|(?=color=))");
	                	System.out.println("getData: " + Arrays.toString(getData) + " length:"+getData.length);
	                	String[] constraints = {null, null, null};
	                	for(int i = 1; i < getData.length-1; i = i+2) {
	                		System.out.println(getData[i]);
	                		if(getData[i].equals("contains=")) {
	                			contains = getData[i+1].trim().split(" ", 2);
	                			System.out.println(Arrays.toString(contains));
	                			constraints[0] = "contains";
	                		}
	                		else if(getData[i].equals("refersTo=")){
	                			refersTo = getData[i+1].trim();
	                			constraints[1] = "refersTo";
	                		}
	                		else if(getData[i].equals("color=")){
	                			color = getData[i+1].trim();
	                			constraints[2] = "color";
	                			
	                		}
	                		/*else if(req.contains("GET")){
	                			System.out.println("getting notes...");
	                		}*/
	                		else {
	    		                System.out.println("invalid request");
	    		                dos.writeUTF("invalid request");
	    	                    break;
	                		}
	                	}
	                	List<note> sendNotes = new ArrayList<note>();
	                	if (constraints[0] == "contains") {
			                for(note i: notes) {
			                	 if (i.x == Integer.parseInt(contains[0]) && i.y == Integer.parseInt(contains[1])){
			                		sendNotes.add(i);
			                	}
			                }
	                	}
		                if (constraints[1] == "refersTo") {
		                	if (sendNotes.isEmpty()) {
				                for(note i: notes) {
				                	 if (i.msg.contains(refersTo)){
				                		sendNotes.add(i);
				                	}
				                }
		                	}
		                	else {
		                		for(note i: sendNotes) {
				                	 if (i.msg.contains(refersTo) == false){
				                		sendNotes.remove(i);
				                	}
		                		}
		                	}
		                }
		                if (constraints[2] == "color") {
		                	if (sendNotes.isEmpty()) {
				                for(note i: notes) {
				                	 if (i.colour.equals(color)){
				                		sendNotes.add(i);
				                	}
				                }
		                	}
		                	else {
		                		for(note i: sendNotes) {
				                	 if (i.colour.equals(color) == false){
				                		sendNotes.remove(i);
				                	}
		                		}
		                	}
		                }
	                	if(constraints[0] == null && constraints[1] == null && constraints[2] == null) {
	                		if(data.length == 1) {
	                			sendNotes = notes;
	                		}
	                		else if(data[1].equals("PIN")){
				                for(note i: notes) {
				                	 if (i.status == "pinned"){
				                		sendNotes.add(i);
				                	}
				                } 
	                		}
	                		else {
	    		                System.out.println("invalid request...");
	    		                dos.writeUTF("invalid request...");
	    	                    break;
	                		}
	                	}
		                String[] arrNotes = note.sendNotes(sendNotes);
		                System.out.println("notes to client: "+ Arrays.toString(arrNotes));
		                dos.writeUTF(Arrays.toString(arrNotes));
	                    break;
	                      
	                case "CLEAR" :
	                	notes.clear();
	                	//g.CLEAR();
	                	dos.writeUTF("cleared notes");
	                    break;
	                
	                case "SHAKE" :
	                	Iterator<note> noteIter = notes.iterator();
	                	while(noteIter.hasNext()) {
	                		note s = noteIter.next();
	                		if(s.status == "unpinned") {
	                			noteIter.remove();
	                		}
	                	}
	                	dos.writeUTF("removed non-pinned notes");
	                    break;
	                    
	                case "PIN" :
	                	x = Integer.parseInt(data[1]);
	                	y = Integer.parseInt(data[2]);
	                	for(note i : notes) {
	                		if(i.coordInNote(x, y)) {
		                		if(i.status == "unpinned") {
		                			i.status = "pinned";
		                		}
		                		i.pin++;
	                		}
	                	}
	                	dos.writeUTF("pinned notes");
	                    break;
	                    
	                case "UNPIN" :
	                	x = Integer.parseInt(data[1]);
	                	y = Integer.parseInt(data[2]);
	                	for(note i : notes) {
	                		if(i.coordInNote(x, y) && i.status == "pinned") {
	                			i.pin--;
		                		if(i.pin == 0) {
		                			i.status = "unpinned";
		                		}
		                	}
	                		
	                	}
	                	dos.writeUTF("unpinned notes");
	                    break;
	                    
	                case "DISCONNECT" :
	                    System.out.println("Client " + this.s + " sends exit...");
	                    System.out.println("Closing this connection.");
	                    //this.dis.close();
	                    //this.dos.close();
	                    this.s.close();
	                    System.out.println("Connection closed");
	                	//dos.writeUTF("cleared notes");
	                    break;
	                      
	                default:
	                    dos.writeUTF("Invalid input");
	                    break;
            }
                if (this.s.isClosed()) {;
	            	break;
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		try {
			this.dis.close();
			this.dos.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
