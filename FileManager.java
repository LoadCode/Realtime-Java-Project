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
	
	public void save_array(double [][]process_signal, double [][]control_signal)
	{
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		try
		{
			fileWriter = new FileWriter(this.fileName, true);
			bufferedWriter = new BufferedWriter(fileWriter);
			printWriter = new PrintWriter(bufferedWriter);
			// formato del archivo ( time_signal, process_signal, control_signal )
			for(int i = 0; i < process_signal[0].length; i++)
				bufferedWriter.write(String.format("%f\t%f\t%f\n", process_signal[0][i], process_signal[1][i], control_signal[1][i]));
				
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
		}
	}
}
