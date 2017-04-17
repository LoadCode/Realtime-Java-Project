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
	final XYSeries dataLogged;
	final XYSeriesCollection collectionData;
	final JFreeChart display;
	public JTextField txt_port;
	public JTextField txt_acq_time;
	public JTextField txt_sample_time;
	public JTextField txt_step_time;
	public JTextField txt_file_name;
	public JButton btn_server_init;
	public JButton btn_server_stop;
	
	public Window()
	{
		this.setTitle("Real Time Control App");
		this.setBounds(300,440,500,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		/*Parameters panel*/
		JPanel parametersPanel = new JPanel();
		JLabel lbl_param_1 = new JLabel("Port Number");
		JLabel lbl_param_2 = new JLabel("Acquisition Time (s)");
		JLabel lbl_param_3 = new JLabel("Sample Time (s)");
		JLabel lbl_param_4 = new JLabel("Step Time (s)");
		JLabel lbl_param_5 = new JLabel("File Name");
		txt_port 		= new JTextField("16");
		txt_acq_time    = new JTextField("");
		txt_sample_time = new JTextField("0.5");
		txt_step_time   = new JTextField("15");
		txt_file_name   = new JTextField("");
		txt_port.setHorizontalAlignment(JTextField.CENTER);
		txt_acq_time.setHorizontalAlignment(JTextField.CENTER);
		txt_sample_time.setHorizontalAlignment(JTextField.CENTER);
		txt_step_time.setHorizontalAlignment(JTextField.CENTER);
		txt_file_name.setHorizontalAlignment(JTextField.CENTER);
		btn_server_init = new JButton("Start Acquisition");
		btn_server_stop	= new JButton("Save Data");
		btn_server_init.setActionCommand("start");
		btn_server_stop.setActionCommand("stop");
		parametersPanel.setLayout(new GridLayout(6, 2,10,10));
		parametersPanel.add(lbl_param_1);
		parametersPanel.add(txt_port);
		parametersPanel.add(lbl_param_2);
		parametersPanel.add(txt_acq_time);
		parametersPanel.add(lbl_param_3);
		parametersPanel.add(txt_sample_time);
		parametersPanel.add(lbl_param_4);
		parametersPanel.add(txt_step_time);
		parametersPanel.add(lbl_param_5);
		parametersPanel.add(txt_file_name);
		parametersPanel.add(btn_server_init);
		parametersPanel.add(btn_server_stop);
		mainPanel.add(parametersPanel, BorderLayout.EAST);
		this.getContentPane().add(mainPanel, BorderLayout.EAST);
		
		// Setup the Chart panel
		dataLogged = new XYSeries("Proocess Dynamics");
		dataLogged.add(0,0);
		collectionData = new XYSeriesCollection();
		collectionData.addSeries(dataLogged);
		display = ChartFactory.createXYLineChart("Realtime Plant Output",
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
