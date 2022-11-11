package Sockets;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	
	private Socket socket;
	private BufferedReader lector;
	private BufferedWriter escritor;
	private String nombreUsuario;	
	
	public Client(Socket socket, String nombreUsuario) {
		try {
			this.socket = socket;
			this.escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.nombreUsuario = nombreUsuario;
		} catch (IOException e) {
			CerrarTodo(socket, escritor, lector);
		}
	}
	public void EnviarMensaje() {
		try {
			escritor.write(nombreUsuario);
			escritor.newLine();
			escritor.flush();
			
			Scanner input = new Scanner(System.in);
			while (socket.isConnected()) {
				String MensajeParaEnviar = input.nextLine();
				escritor.write(nombreUsuario + ": " + MensajeParaEnviar);
				escritor.newLine();
				escritor.flush();
			}
		} catch (IOException e) {
			CerrarTodo(socket, escritor, lector);
		}
	}
	public void EscucharMensaje() {
		new Thread(new Runnable() {
			public void run() {
				String mjsDelGrupo;
				while (socket.isConnected()) {
					try {
						mjsDelGrupo = lector.readLine();
						System.out.println(mjsDelGrupo);
					} catch (IOException e) {
						CerrarTodo(socket, escritor, lector);
					}
				}
			}
		}).start();
	}
	public void CerrarTodo(Socket socket, BufferedWriter escritor, BufferedReader lector) {
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
	public static void main(String[] args) throws UnknownHostException, IOException{
		Scanner input = new Scanner(System.in);
		System.out.println("Nombre: ");
		String nombre = input.nextLine();
		System.out.println("IP: ");
		String IP = input.nextLine();
		System.out.println("Puerto de conexion: ");
		int puerto = input.nextInt();
		Socket socket = new Socket(IP, puerto);
		Client cliente = new Client(socket, nombre);
		cliente.EscucharMensaje();
		cliente.EnviarMensaje();
		input.close();
	}
}
