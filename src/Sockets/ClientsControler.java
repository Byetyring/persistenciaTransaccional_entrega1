package Sockets;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientsControler implements Runnable{
	
	public static ArrayList<ClientsControler> listaClientes = new ArrayList<>();
	private Socket socket;
	private BufferedReader lector;
	private BufferedWriter escritor;
	private String nombreUsuario;
	
	public ClientsControler(Socket socket) {
		try {
			this.socket = socket;
			this.escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.nombreUsuario = lector.readLine();
			listaClientes.add(this);
			broadcastMessage("SERVER: " + nombreUsuario + " su unio al chat");
			
		} catch(IOException e) {
			CerrarTodo(socket, lector, escritor);
		}
	}
	@Override
	public void run() {
		String mensaje;
		while (socket.isConnected()) {
			try {
				mensaje = lector.readLine();
				broadcastMessage(mensaje);
			} catch (IOException e){
				CerrarTodo(socket, lector, escritor);
				break;
			}
		}
		
	}
	
	public void broadcastMessage(String enviarMensaje) {
		for (ClientsControler cliente : listaClientes) {
			try {
				if(!cliente.nombreUsuario.equals(nombreUsuario)) {
					cliente.escritor.write(enviarMensaje);
					cliente.escritor.newLine();
					cliente.escritor.flush();
					
				}
			} catch(IOException e) {
				CerrarTodo(socket, lector, escritor);
			}
		}
	}
	
	public void EliminarCliente() {
		listaClientes.remove(this);
		broadcastMessage("SERVER: " + nombreUsuario + " abandono el chat");
	}
	
	public void CerrarTodo(Socket socket, BufferedReader lector, BufferedWriter escritor) {
		EliminarCliente();
		try {
			if (lector != null) {
				lector.close();
			}
			if (escritor != null ) {
				escritor.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.getMessage();
		}
	}

}
