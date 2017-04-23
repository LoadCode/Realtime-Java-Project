import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager 
{
	private String fileName;
	public FileManager(String _fileName)
	{
		this.fileName = _fileName;
	}
	
	
	public void save_pid_signals(double [][]setpointI, 
								 double [][]signalI, 
								 double [][]controlI)
	{
		/*
		 * Este método genera una archivo con cuatro columnas que corresponden a:
		 *    datos_tiempo    datos_setpoint	datos_control	salida_planta_I
		 * */
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		try
		{
			fileWriter = new FileWriter(this.fileName, true);
			bufferedWriter = new BufferedWriter(fileWriter);
			printWriter = new PrintWriter(bufferedWriter);
			for(int i = 0; i < signalI[0].length; i++)                                 //time        //setpoint     //control      //first plant's output    
				bufferedWriter.write(String.format("%f\t%f\t%f\t%f\n", signalI[0][i], setpointI[1][i], controlI[1][i], signalI[1][i]));
				
		}
		catch(IOException e)
		{
			System.out.println("Error escribiendo el archivo");
		}
		finally
		{
			try
			{
				if(bufferedWriter != null)
				{
					bufferedWriter.close();
					printWriter.close();
					fileWriter.close();
				}
			}
			catch (IOException e)
			{
				
			}
		} //finally
	}// save_pid_signals
	
	
	public void save_array(double [][]process_signal, double [][]stimuli)
	{
		/*
		 * Este método genera una archivo con tres columnas que corresponden a:
		 *  datos_tiempo    datos_señal_salida_planta     datos_señal_entrada_planta
		 * 
		 * */
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		try
		{
			fileWriter = new FileWriter(this.fileName, true);
			bufferedWriter = new BufferedWriter(fileWriter);
			printWriter = new PrintWriter(bufferedWriter);
			for(int i = 0; i < process_signal[0].length; i++)       //time data           //plant's output       //stimuli signal (e.g. step)
				bufferedWriter.write(String.format("%f\t%f\t%f\n", process_signal[0][i], process_signal[1][i], stimuli[1][i]));
				
		}
		catch(IOException e)
		{
			System.out.println("Error escribiendo el archivo");
		}
		finally
		{
			try
			{
				if(bufferedWriter != null)
				{
					bufferedWriter.close();
					printWriter.close();
					fileWriter.close();
				}
			}
			catch (IOException e)
			{
				
			}
		} //finally
	}//save_array
} // class FileManager
