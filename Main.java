import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener
{
	private static ThreadServer myThread;
	public static void main(String []args)
	{
		Window root = new Window();
		root.btn_server_init.addActionListener(new Main());
		root.btn_server_stop.addActionListener(new Main());
		myThread = new ThreadServer(root);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("start"))
		{
			System.out.println("Iniciando el servidor");
			myThread.start();
		}
		else if(e.getActionCommand().equals("stop"))
		{
			System.out.println("Parando el servidor");
			myThread.terminate();
			
		}
		
	}
}
