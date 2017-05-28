import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFileChooser;


public class ThreadClient extends Thread
{
	private final int SERVER_PORT = 9090;
	public int dataFlag;
	public int finishFlag;
	private Window gui;
	private String SERVER_IP;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private volatile boolean alive;


	// Parámetros de control
	private double setpoint_temp;
	private double setpoint_flow;
	private double u_temp;
	private double u_flow;
	private double out_temp;
	private double out_flow;
	private double time_value;

	
	public ThreadClient(Window _gui)
	{
		this.gui = _gui;		
   	}//ThreadServer
	
	public void terminate()
	{
		this.finishFlag = 1;
		System.out.format("La cantidad de elementos es: %d\n", this.gui.output_I.getItemCount());
		
		// option to save the captured data
		double[][] logged_signal_I    = this.gui.output_I.toArray();
		double[][] control_signal_I   = this.gui.input_I.toArray();
		double[][] setpoint_signal_I  = this.gui.setpoint_I.toArray();
		double[][] logged_signal_II   = this.gui.output_II.toArray();
		double[][] control_signal_II  = this.gui.input_II.toArray();
		double[][] setpoint_signal_II = this.gui.setpoint_II.toArray();
		
		// Dialogo para seleccionar donde almacenar el archivo
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Indicar el archivo a guardar");
		int userSelection = fileChooser.showSaveDialog(this.gui);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) 
		{
		    File fileToSave = fileChooser.getSelectedFile();
		    String filePath = fileToSave.getAbsolutePath();
		    FileManager fileSaving = new FileManager(filePath.concat("_PID_I"));
			fileSaving.save_pid_signals(setpoint_signal_I, logged_signal_I, control_signal_I);
			filePath = fileToSave.getAbsolutePath();
			fileSaving = new FileManager(filePath.concat("_PID_II"));
			fileSaving.save_pid_signals(setpoint_signal_II, logged_signal_II, control_signal_II);
			System.out.println("Archivo guardado");
		}
		else
		{
			System.out.println("Datos descartados");
		}
		
	}//terminate
	
	public void run()
	{
		this.alive = true;
		this.dataFlag = 1;
		this.finishFlag = 0;

		// Crear cliente y establecer conexión
		try
		{
			this.SERVER_IP = this.gui.txt_server_ip.getText();
			socket = new Socket(SERVER_IP, SERVER_PORT);
			out = new DataOutputStream(socket.getOutputStream());
			in  = new DataInputStream(socket.getInputStream());
		}
		catch(IOException e)
		{
			System.out.println("No se pudo establecer la conexión con el servidor");
			e.printStackTrace();
			this.alive = false;
		}


		while(this.alive)
		{
			// Enviar Finish y Data Flags
			try
			{
				this.out.flush();
				this.out.writeInt(finishFlag);
				this.out.writeInt(dataFlag);
			}catch(IOException e)
			{
				System.out.println("Error enviando la señalización al servidor");
				e.printStackTrace();
				//this.alive = false;
			}
				

			if(finishFlag == 1)
			{
				// Terminar todo el hilo adecuadamente (cerrar la conexión)
				System.out.println("Cerrando la conexión");
				//this.alive = false;
			}
			else if(dataFlag == 1)
			{
				// Capturar parámetros de usuario
				setpoint_temp = Double.parseDouble(this.gui.txt_setpoint_temp.getText());
				setpoint_flow = Double.parseDouble(this.gui.txt_setpoint_flow.getText());

				// Enviar valores de los nuevos setpoints para los controladores
				try
   				{
					this.out.writeDouble(setpoint_temp);
					this.out.writeDouble(setpoint_flow);
				}
				catch(IOException e)
				{
					System.out.println("Error enviando valores de setpoint");
					e.printStackTrace();
					//this.alive = false;
				}
			 }
			dataFlag = 0;
			
			// Lectura de valores provenientes de la planta
			if(finishFlag == 0)
			{
				try
				{
					this.u_temp     = this.in.readDouble();
					this.u_flow     = this.in.readDouble();
					this.out_temp   = this.in.readDouble();
					this.out_flow   = this.in.readDouble();
					this.time_value = this.in.readDouble();
				} catch (IOException e)
				{
					System.out.println("No se pudo hacer la lectura de la señal o la transferencia del finish");
					e.printStackTrace();
					//this.alive = false;
				}
			}
			//System.out.println("u_temp="+u_temp+" u_flow="+u_flow+" outemp="+out_temp+" outFlow="+out_flow+" time="+time_value);
			// Actualizar gráficos con los nuevos valores
			this.UpdateChart();
	    }
		try 
		{
			System.out.println("Esperando para cerrar los sockets");
			Thread.sleep(4000);
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
		System.out.println("Socket principal cerrado exitosamente & hilo terminado");
	}// void run

	public void UpdateChart()
	{
		this.gui.output_I.add(time_value, this.out_temp);
		this.gui.input_I.add(time_value, this.u_temp);
		this.gui.setpoint_I.add(time_value, this.setpoint_temp);
        this.gui.output_II.add(time_value, this.out_flow);
		this.gui.input_II.add(time_value, this.u_flow);
		this.gui.setpoint_II.add(time_value, this.setpoint_flow);
	}//UpdateChart
}
