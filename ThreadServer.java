import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFileChooser;

public class ThreadServer extends Thread
{
	private int usbPort;
	private final int SERVER_PORT = 34869;
	private double tsT;
	private double setpointT;
	private double kpT;
	private double kiT;
	private double kdT;
	private double tsF;
	private double setpointF;
	private double kpF;
	private double kiF;
	private double kdF;
	private int server_answer = 2958;
	private int valid_client_request = 45862;
	private Window gui;
	private ServerSocket server;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ControlThread threadTemp;
	private ControlThread threadFlow;
	
	
	public ThreadServer(Window _gui)
	{
		this.gui = _gui;
		this.usbPort = 16;
		this.tsT = 0.1;
		
		// Create server
		System.out.println("Starting Loggin thread");
		try 
		{
			this.server = new ServerSocket(this.SERVER_PORT);
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
			} 
			catch (IOException e) 
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
			} catch (IOException e) 
			{
				System.out.println("No se pudo escribir valor al cliente");
				e.printStackTrace();
				return;
			}
			finally
			{
				if(client_request == this.valid_client_request)
				{
					System.out.println("Valid request value");
					try {
						out.writeInt(this.server_answer);
					} catch (IOException e) {
						System.out.println("No se pudo escribir valor al cliente");
						e.printStackTrace();
						return;
					}
				}
				else
				{
					System.out.println("Invalid request value");
					try 
					{
						out.writeInt(this.server_answer+1);
					} catch (IOException e) 
					{
						System.out.println("No se pudo escribir valor al cliente");
						e.printStackTrace();
						return;
					}
				}
			}// finally 
		}//finally
	}//ThreadServer
	
	public void terminate()
	{
		// llamar al terminate de cada hilo controlador y venir a capturar sus datos
		threadTemp.terminate();
		threadFlow.terminate();
		System.out.format("La cantidad de elementos es: %d\n", this.gui.output_I.getItemCount());
		
		// option to save the captured data
		double[][] logged_signal_I = this.gui.output_I.toArray();
		double[][] control_signal_I = this.gui.input_I.toArray();
		double[][] setpoint_signal_I = this.gui.setpoint_I.toArray();
		double[][] logged_signal_II = this.gui.output_II.toArray();
		double[][] control_signal_II = this.gui.input_II.toArray();
		double[][] setpoint_signal_II = this.gui.setpoint_II.toArray();
		
		// Dialogo para seleccionar donde almacenar el archivo
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Indicar el archivo a guardar");
		int userSelection = fileChooser.showSaveDialog(this.gui);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) 
		{
		    File fileToSave = fileChooser.getSelectedFile();
		    FileManager fileSaving = new FileManager(fileToSave.getAbsolutePath());
			fileSaving.save_pid_signals(setpoint_signal_I, logged_signal_I, control_signal_I, setpoint_signal_II, logged_signal_II, control_signal_II);
			System.out.println("Archivo guardado");
		}
		else
		{
			System.out.println("Datos descartados");
		}
		
	}
	
	
	
	
	public void run()
	{
		// get user parameters
		this.usbPort = Integer.parseInt(this.gui.txt_port.getText());
		this.tsT = Double.parseDouble(this.gui.txt_sample_time_I.getText());
		this.setpointT = Double.parseDouble(this.gui.txt_setpoint_I.getText());
		this.kpT = Double.parseDouble(this.gui.txt_kp_T.getText());
		this.kiT = Double.parseDouble(this.gui.txt_ki_T.getText());
		this.kdT = Double.parseDouble(this.gui.txt_kd_T.getText());
		this.tsF = Double.parseDouble(this.gui.txt_sample_time_II.getText());
		this.setpointF = Double.parseDouble(this.gui.txt_setpoint_II.getText());
		this.kpF = Double.parseDouble(this.gui.txt_kp_F.getText());
		this.kiF = Double.parseDouble(this.gui.txt_ki_F.getText());
		this.kdF = Double.parseDouble(this.gui.txt_kd_F.getText());
		
		// Send parámeters usbPort Ts, setpoint, & PID parameters
		try
		{
			out.writeInt(this.usbPort);
			out.writeDouble(this.tsT);
			out.writeDouble(this.setpointT);
			out.writeDouble(this.kpT);
			out.writeDouble(this.kiT);
			out.writeDouble(this.kdT);
			out.writeDouble(this.tsF);
			out.writeDouble(this.setpointF);
			out.writeDouble(this.kpF);
			out.writeDouble(this.kiF);
			out.writeDouble(this.kdF);
		}
		catch (IOException e) 
		{
			System.out.println("Error al enviar parámetros usbPort, Ts");
			e.printStackTrace();
			return;
		}
		
		// crear un par de hilos por cada controlador
		// Control temperatura
		try
		{
			this.socket = server.accept();
			threadTemp = new ControlThread(this.gui, this.socket, this.setpointT, 1);
			threadTemp.start();
		}
		catch(Exception e)
		{
	        e.printStackTrace();
	        System.out.println("Connection Error: creating the temp thread");
		}
		
		// Control temperatura
		try
		{
			this.socket = server.accept();
			threadFlow = new ControlThread(this.gui, this.socket, this.setpointF, 2);
			threadFlow.start();
		}
		catch(Exception e)
		{
	        e.printStackTrace();
	        System.out.println("Connection Error: creating the flow thread");
		}
		
		
		//Esperando a que los hilos terminen
		try 
		{
			threadTemp.join();
			threadFlow.join();
		} catch (InterruptedException e2) 
		{
			e2.printStackTrace();
		}
		
		// cerrando el socket principal, los demás sockets se deben cerrar dentro de los respectivos hilos
		try 
		{
			System.out.println("Esperando para cerrar los sockets");
			Thread.sleep(2000);
		} catch (InterruptedException e1) 
		{
			System.out.println("Error esperando para cerrar los sockets");
			e1.printStackTrace();
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
		System.out.println("Socket principal cerrado exitosamente & hilo terminado");
	}
}
