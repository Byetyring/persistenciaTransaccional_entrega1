package Sockets;

import java.io.*;
import java.net.*;

public class Server {

	private ServerSocket serversckt;
	
	public Server(ServerSocket serversckt) {
		this.serversckt = serversckt;
		
	}
	
	public void startServer() {
		try {
			while(!serversckt.isClosed()) {
				
				Socket socket = serversckt.accept();
				
				System.out.println("Un nuevo cliente se conecto");
				ClientsControler cliente = new ClientsControler(socket);
				
				Thread hilo1 = new Thread(cliente);
				hilo1.start();
			} 
			
		} catch (IOException e) {
			
		}
	}
	
	public void closeServerSocket() {
		
		try {
			if (serversckt != null) {
				serversckt.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket skservidor = new ServerSocket(5000);
		Server servidor = new Server(skservidor);
		servidor.startServer();
		
	}
}
