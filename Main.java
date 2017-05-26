import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener
{
	private static ThreadClient myThread;
	private static boolean init_client;
	public static void main(String []args)
	{
		Window root = new Window();
		init_client = true;
		root.btn_server_start.addActionListener(new Main());
		root.btn_server_stop.addActionListener(new Main());
		myThread = new ThreadClient(root);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("start"))
		{
			if(init_client)
			{
				System.out.println("Iniciando el Cliente");
				myThread.start();
				init_client = false;
			}
			else
			{
				System.out.println("Enviando nuevos valores");
				myThread.dataFlag = 1;
			}
		}
		else if(e.getActionCommand().equals("stop"))
		{
			System.out.println("Finalizando el Cliente");
			myThread.terminate();
		}
   	}
}
