import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;



public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;
	final XYSeries output_I;
	final XYSeries input_I;
	final XYSeries setpoint_I;
	final XYSeries output_II;
	final XYSeries input_II;
	final XYSeries setpoint_II;
	final XYSeriesCollection collectionData;
	final JFreeChart display;
	public JTextField txt_server_ip;
	public JButton btn_server_start;
	public JButton btn_server_stop;
	
	public Window()
	{
		this.setTitle("Real Time Control Viewer App");
		this.setBounds(600,440,800,550);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		/*Parameters panel*/
		JPanel parametersPanel = new JPanel();
		JLabel lbl_server_ip    = new JLabel("Server IP");
		txt_server_ip = new JTextField("192.168.");
		btn_server_init = new JButton("Start Session");
		btn_server_stop	= new JButton("Stop Session");
		btn_server_init.setActionCommand("start");
		btn_server_stop.setActionCommand("stop");
		parametersPanel.setLayout(new GridLayout(2, 2,10,10));
		parametersPanel.add(lbl_server_ip);
		parametersPanel.add(txt_server_ip);
		parametersPanel.add(btn_server_start);
		parametersPanel.add(btn_server_stop);
		mainPanel.add(parametersPanel, BorderLayout.EAST);
		this.getContentPane().add(mainPanel, BorderLayout.EAST);
		
		// Setup the Chart panel
		input_I    = new XYSeries("PID I Signal");
		output_I   = new XYSeries("Process Output I");
		setpoint_I = new XYSeries("Setpoint I");
		input_II    = new XYSeries("PID II Signal");
		output_II   = new XYSeries("Process Output II");
		setpoint_II = new XYSeries("Setpoint II");
		input_I.clear();
		output_I.clear();
		setpoint_I.clear();
		input_II.clear();
		output_II.clear();
		setpoint_II.clear();
		collectionData = new XYSeriesCollection();
		collectionData.addSeries(input_I);
		collectionData.addSeries(output_I);
		collectionData.addSeries(setpoint_I);
		collectionData.addSeries(input_II);
		collectionData.addSeries(output_II);
		collectionData.addSeries(setpoint_II);
		display = ChartFactory.createXYLineChart("Realtime Process Dynamics",
												 "Time",
												 "Amplitude",
												 collectionData,
												 PlotOrientation.VERTICAL,
												 true,
												 true,
												 false);
		JPanel rt_panel = new ChartPanel(display);
		this.getContentPane().add(rt_panel, BorderLayout.CENTER);
		this.setVisible(true);
	}
}
