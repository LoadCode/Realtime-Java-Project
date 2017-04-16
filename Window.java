import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;
	protected JButton btn_server_init;
	protected JButton btn_server_stop;
	
	public Window()
	{
		this.setTitle("Simple Server");
		this.setBounds(200,200,300,200);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel main = new JPanel();
		main.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btn_server_init = new JButton("Iniciar Servidor");
		btn_server_stop = new JButton("Detener Servidor");
		btn_server_init.setActionCommand("start");
		btn_server_stop.setActionCommand("stop");
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		container.add(btn_server_init);
		container.add(btn_server_stop);
		main.add(container);
		this.getContentPane().add(main, BorderLayout.CENTER);		
		this.setVisible(true);
	}
}
