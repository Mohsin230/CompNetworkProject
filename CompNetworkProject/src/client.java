import java.net.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class client {
	// Import socket module
	
	//run with: >java server 8989 200 100 red white green yellow
	public static void main(String[] args) throws IOException{
		GUI g = new GUI();
		
		System.out.println("waiting for connection...");
		
		//System.out.println("connected");
		/*Socket s = new Socket("localhost", 8989);
		
		DataInputStream dis = new DataInputStream(s.getInputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		
		
		//setting up interface for asking client for server request
		Scanner clientReader = new Scanner(System.in);
		//String sentence = "";
		//String data = "";
		
		String[] messageOptions = {"POST", "GET", "PIN", "UNPIN", "CLEAR", "SHAKE", "DISCONNECT"};
		
		//enter data to server
		//
		while(true) {
			for(String i : messageOptions) { //prints options
				System.out.println(i);
			}
			System.out.println(dis.readUTF());
            //System.out.println("waiting for response: ");
            String tosend = clientReader.nextLine();
            dos.writeUTF(tosend);
            
			//System.out.println("waiting for response: ");
			//sentence = clientReader.nextLine();
			
            if(tosend.equals("DISCONNECT")) {
                System.out.println("Closing this connection : " + s);
                s.close();
                System.out.println("Connection closed");
                break;
            }
            String received = dis.readUTF();
            System.out.println(received);			
			
		}
        clientReader.close();
        dis.close();
        dos.close();
	}*/
	}
	
}

class GUI {
	note n;
	boolean conn;
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	String tosend = "";
	
	public GUI() {
		//this.n = null;
		this.conn = false;
		Border border = BorderFactory.createLineBorder(Color.green,3);
		
		JPanel panel = new JPanel(new GridLayout(3,3));
		
		JLabel label = new JLabel();
		label.setText(tosend);
		label.setBorder(border);
		label.setVerticalAlignment(JLabel.CENTER);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(250,350,200,50);
		
		JLabel labelIP = new JLabel();
		labelIP.setText("Server ip: ");
		//labelIP.setBorder(border);
		labelIP.setVerticalAlignment(JLabel.CENTER);
		labelIP.setHorizontalAlignment(JLabel.CENTER);
		labelIP.setBounds(50,0,200,50);
		
		JLabel labelPort = new JLabel();
		labelPort.setText("Port: ");
		//labelIP.setBorder(border);
		labelPort.setVerticalAlignment(JLabel.CENTER);
		labelPort.setHorizontalAlignment(JLabel.CENTER);
		labelPort.setBounds(250,0,200,50);
		
		JTextField serverip = new JTextField();
		//serverip.setText("Server ip: ");
		serverip.setBorder(border);
		serverip.setBounds(50,50,200,50);
		
		JTextField socketPort = new JTextField();
		//socketPort.setText("Port: ");
		socketPort.setBorder(border);
		socketPort.setBounds(250,50,200,50);
		
		JTextArea serverOutput = new JTextArea();
		serverOutput.setText("Server response: ");
		serverOutput.setBorder(border);
		serverOutput.setBounds(50, 400, 200, 50);
		
		JTextArea postTextArea = new JTextArea();
		postTextArea.setText("");
		postTextArea.setBorder(border);
		postTextArea.setBounds(200,220,200,30);
		
		JLabel postLabel = new JLabel();
		postLabel.setText("POST note text field:");
		postLabel.setVerticalAlignment(JLabel.CENTER);
		postLabel.setHorizontalAlignment(JLabel.CENTER);
		postLabel.setBounds(200,200,200,20);
		
		JButton connButton = new JButton("CONNECT/DISCONNECT");
		connButton.setBounds(100,150,300,50);
		connButton.setBorder(border);
		connButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {	
				if(conn == false) {
					try {
						conn = true;
						System.out.println("connection button pressed!");
						
						//String port = JOptionPane.showInputDialog(null,"Enter port");
						s = new Socket(serverip.getText(), Integer.parseInt(socketPort.getText()));
						
						dis = new DataInputStream(s.getInputStream());
						dos = new DataOutputStream(s.getOutputStream());
						System.out.println("connected");
						String serverData = dis.readUTF();
						System.out.println(serverData);
						serverOutput.setText("server output: " + serverData);
						
					}catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else {
					System.out.println("Disconnect button pressed!");

				    try {
				    	dos.writeUTF("DISCONNECT");
						dis.close();
					    dos.close();
					    s.close();
					    
					    conn = false;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    
				}
			}
		});
		JButton send = new JButton("Send to Server");
		send.setBounds(250,400,200,50);
		send.setBorder(border);
		send.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(conn == false) {
					System.out.println("not connected");
				}
				else {
					try {
						System.out.println("sending to server: " + label.getText());
			            dos.writeUTF(label.getText());
			            
			            String received = dis.readUTF();
			            System.out.println(received);
			            serverOutput.setText("server response: " + received);
					}
			        catch (IOException e1){
			        	e1.printStackTrace();
			        }
				}
			}
		});
		/*JButton disButton = new JButton("DISCONNECT");
		disButton.setBounds(200,100,100,50);
		disButton.setBorder(border);*/
		
		JButton postButton = new JButton("POST");
		postButton.setBounds(100,200,100,50);
		postButton.setBorder(border);
		postButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//String tosend = JOptionPane.showInputDialog(null,"Enter note");
				String tosend = postTextArea.getText();
				label.setText("POST " + tosend);
			}
		});
		
		
		JButton getButton = new JButton("GET");
		getButton.setBounds(100,250,100,50);
		getButton.setBorder(border);
		getButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String tosend = JOptionPane.showInputDialog(null,"GET request");
				label.setText("GET " + tosend);
				//attempt at each element contain, color, refersTo being input dialogs
				/*String contains = String.format("contains=%s %s",JOptionPane.showInputDialog(null,"contains x"), JOptionPane.showInputDialog(null,"contains y"));
				String color = String.format("color=%s",JOptionPane.showInputDialog(null,"what color?"));
				String refersTo = String.format("refersTo=%s",JOptionPane.showInputDialog(null,"refer to substring"));
				String[] getConstraints = new String[3];
				int getIndex = 0;
				if(contains.equals("contains= ") == false) {
					getConstraints[getIndex] = contains;
					getIndex++;
				}
				if(color.equals("color=") == false) {
					getConstraints[getIndex] = color;
					getIndex++;
				}
				if(refersTo.equals("refersTo=") == false) {
					getConstraints[getIndex] = refersTo;
					getIndex++;
				}
				for(String word: getConstraints) {
					tosend = tosend + word + " ";
				}
				label.setText("GET " + tosend.substring(0, tosend.length() -1));*/
			}
		});
		
		JButton pinButton = new JButton("PIN");
		pinButton.setBounds(150,300,100,50);
		pinButton.setBorder(border);
		pinButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//String tosend = JOptionPane.showInputDialog(null,"Enter PIN request");
				String coordx = JOptionPane.showInputDialog(null,"Enter x coordinate");
				String coordy = JOptionPane.showInputDialog(null,"Enter y coordinate");
				label.setText("PIN " + coordx + " " + coordy);
			}
		});
		
		JButton unpinButton = new JButton("UNPIN");
		unpinButton.setBounds(150,350,100,50);
		unpinButton.setBorder(border);
		unpinButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String coordx = JOptionPane.showInputDialog(null,"Enter x coordinate");
				String coordy = JOptionPane.showInputDialog(null,"Enter y coordinate");
				label.setText("UNPIN " + coordx + " " + coordy);
			}
		});
		
		JButton clearButton = new JButton("CLEAR");
		clearButton.setBounds(50,300,100,50);
		clearButton.setBorder(border);
		clearButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String tosend = "CLEAR";
				label.setText(tosend);
			}
		});
		
		JButton shakeButton = new JButton("SHAKE");
		shakeButton.setBounds(50,350,100,50);
		shakeButton.setBorder(border);
		shakeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String tosend = "SHAKE";
				label.setText(tosend);
			}
		});
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.add(panel);
		frame.add(label);
		frame.add(connButton);
		//frame.add(disButton);
		frame.add(postButton);
		frame.add(getButton);
		frame.add(send);
		frame.add(getButton);
		frame.add(pinButton);
		frame.add(unpinButton);
		frame.add(shakeButton);
		frame.add(clearButton);
		frame.add(serverOutput);
		frame.add(serverip);
		frame.add(socketPort);
		frame.add(labelIP);
		frame.add(labelPort);
		frame.add(postTextArea);
		frame.add(postLabel);
		//frame.pack();
	}
	

	
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(n.x, n.y, n.width, n.height);
		g.setColor(Color.BLACK);
		g.drawString(n.msg, n.x, n.y);
		
	}
	public void POST(note n) {
		this.n = n;
		System.out.println(n.msg);
	}
	public void PrintOnScreen(String data) {
		JTextField myOutput = new JTextField(16);
		myOutput.setText(data);
		
	}
	
	public void GET(String data) {
		
	}
	public void CLEAR() {
		
	}
	
}
/*python implementation for reference
 *  from socket import * 
	import sys # In order to terminate the program

	serverName = 'localhost'
	# Assign a port number
	serverPort = 6789

	# Bind the socket to server address and server port
	clientSocket = socket(AF_INET, SOCK_STREAM)

	clientSocket.connect((serverName, serverPort))
	sentence = input(' Input lower case sentence: ')
	clientSocket. send(sentence.encode())
	modifiedSentence = clientSocket.recv(1024)

	print('From server: ', modifiedSentence.decode())
	clientSocket.close()
	*/
