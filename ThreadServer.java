import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer extends Thread
{
	private int usbPort;
	private int nsamples;
	private int ts;
	private int server_answer = 2958;
	private int valid_client_request = 45862;
	private int finish = 0;
	private Window gui;
	private ServerSocket server;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private volatile boolean alive = true;
	
	public ThreadServer(Window gui)
	{
		this.gui = gui;
		this.usbPort = 16;
		this.nsamples = 50;
		this.ts = 1;
		// Create server
		System.out.println("Stating Loggin thread");
		try 
		{
			this.server = new ServerSocket(2222);
			this.socket = server.accept();
		} 
		catch (IOException e) 
		{
			System.out.println("problema creando el servidor Java");
			e.printStackTrace();
			return;
		}
		finally
		{
			System.out.println("Servidor creado con éxito");
			try 
			{
				this.out = new DataOutputStream(this.socket.getOutputStream());
				this.in  = new DataInputStream(this.socket.getInputStream());
				out.flush();
			} catch (IOException e) 
			{
				System.out.println("Error obteniendo los streams");
				e.printStackTrace();
				return;
			}
			
			// Establecemos conexión
			int client_request = 1;
			try 
			{
				client_request = this.in.readInt();
			} catch (IOException e) {
				System.out.println("No se pudo escribir valor al cliente");
				e.printStackTrace();
				return;
			}finally
			{
				if(client_request == this.valid_client_request)
				{
					System.out.println("Valid request value");
					try {
						out.writeInt(this.server_answer);
						//out.writeInt(this.usbPort);
						out.writeInt(this.ts);
					} catch (IOException e) {
						System.out.println("No se pudo escribir valor al cliente");
						e.printStackTrace();
						return;
					}
				}
				else
				{
					System.out.println("Invalid request value");
					try {
						out.writeInt(this.server_answer+1);
					} catch (IOException e) {
						System.out.println("No se pudo escribir valor al cliente");
						e.printStackTrace();
						return;
					}
				}
				System.out.println("client_request = "+String.valueOf(client_request));
			}// finally 
			
		}//finally
		
		
	}//ThreadServer
	
	public void terminate()
	{
		this.alive = false;
		this.finish = 1;
	}
	
	public void run()
	{
		double i = 0.0;
		while(this.alive)
		{
			try {
				i = this.in.readDouble();
				out.writeInt(this.finish);
			} catch (IOException e) 
			{
				System.out.println("No se pudo hacer la lectura de la señal o la transferencia del finish");
				e.printStackTrace();
			}
			System.out.println("contando: "+String.valueOf(i));
		}
		try 
		{
			this.out.close();
			this.in.close();
			this.socket.close();
			this.server.close();
		} catch (IOException e) 
		{
			System.out.println("No se pudo cerrar el socket al dejar el hilo");
			e.printStackTrace();
		}
		System.out.println("Socket cerrado exitosamente & hilo terminado");
	}
}
