import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ControlThread extends Thread
{
	private int finish = 0;
	private double setpoint;
	private Window gui;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private volatile boolean alive = true;
	private int threadId;
	
	public ControlThread(Window _gui, Socket _socket, double _setpoint, int _threadId)
	{	
		this.socket = _socket;
		this.setpoint = _setpoint;
		this.gui = _gui;
		this.threadId = _threadId;
		try
		{
			this.out = new DataOutputStream(this.socket.getOutputStream());
			this.in  = new DataInputStream(this.socket.getInputStream());
			out.flush();
		}
		catch (IOException e) 
		{
			System.out.println("Error obteniendo los streams de un cliente controlador");
			e.printStackTrace();
			return;
		}
		System.out.println("Hilo controlador creado");
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
			if(this.threadId == 1)
			{
				this.gui.output_I.add(time_value, output_process_val);
				this.gui.input_I.add(time_value, input_process_val);
				this.gui.setpoint_I.add(time_value, this.setpoint);
			}
			else if(this.threadId == 2)
			{
				this.gui.output_II.add(time_value, output_process_val);
				this.gui.input_II.add(time_value, input_process_val);
				this.gui.setpoint_II.add(time_value, this.setpoint);
			}
		}
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
		} catch (IOException e) 
		{
			System.out.println("No se pudo cerrar el socket al dejar el hilo");
			e.printStackTrace();
		}
		System.out.println("Hilo controlador terminado");
	}
	
	
	public void terminate()
	{
		this.alive = false;
		this.finish = 1;
	}
	
}
