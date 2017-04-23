import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ControlThread extends Thread
{
	private int usbPort;
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
	private int finish = 0;
	private Window gui;
	private ServerSocket server;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private volatile boolean alive = true;
	
	public ControlThread(Window _gui)
	{
		this.gui = _gui;
		this.alive = true;
		// Create server
		System.out.println("Starting Loggin thread");
		try 
		{
			this.server = new ServerSocket(54067);
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
		}
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
	}
	
	public void run()
	{
		double output_process_val = 0;
		double input_process_val = 0;
		double time_value = 0;
		
		while(this.alive)
		{
			try
			{
				output_process_val = this.in.readDouble();
				input_process_val  = this.in.readDouble();
				time_value = this.in.readDouble();
				out.writeInt(this.finish);
			} catch (IOException e)
			{
				System.out.println("No se pudo hacer la lectura de la señal o la transferencia del finish");
				e.printStackTrace();
			}
			this.gui.output_I.add(time_value, output_process_val);
			this.gui.input_I.add(time_value, input_process_val);
			this.gui.setpoint_I.add(time_value, this.setpointT);
		}
	}
}
