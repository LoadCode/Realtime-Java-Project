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
	public JTextField txt_port;
	public JTextField txt_setpoint_I;
	public JTextField txt_sample_time_I;
	public JTextField txt_setpoint_II;
	public JTextField txt_sample_time_II;
	public JTextField txt_kp_T;
	public JTextField txt_ki_T;
	public JTextField txt_kd_T;
	public JTextField txt_kp_F;
	public JTextField txt_ki_F;
	public JTextField txt_kd_F;
	public JButton btn_server_init;
	public JButton btn_server_stop;
	
	public Window()
	{
		this.setTitle("Real Time Control App");
		this.setBounds(600,440,800,550);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		/*Parameters panel*/
		JPanel parametersPanel = new JPanel();
		JLabel lbl_param_1  = new JLabel("Port Number");
		JLabel lbl_param_2  = new JLabel("Setpoint (v)");
		JLabel lbl_param_2_2  = new JLabel("Setpoint (v)");
		JLabel lbl_param_3  = new JLabel("Sample Time (s)");
		JLabel lbl_param_3_2  = new JLabel("Sample Time (s)");
		JLabel lbl_param_4  = new JLabel("<HTML><u>PID Controller I</u></HTML>");
		JLabel lbl_param_5  = new JLabel("Kp");
		JLabel lbl_param_6  = new JLabel("Ki");
		JLabel lbl_param_7  = new JLabel("Kd");
		JLabel lbl_param_8  = new JLabel("<HTML><u>PID Controller II</u></HTML>");
		JLabel lbl_param_9  = new JLabel("Kp");
		JLabel lbl_param_10 = new JLabel("Ki");
		JLabel lbl_param_11 = new JLabel("Kd");
		JLabel lbl_dummi_1    = new JLabel("        ");
		JLabel lbl_dummi_2    = new JLabel("        ");
		txt_port 		= new JTextField("16");
		txt_setpoint_I    = new JTextField("1");
		txt_sample_time_I = new JTextField("0.5");
		txt_setpoint_II    = new JTextField("1");
		txt_sample_time_II = new JTextField("0.5");
		txt_kp_T   = new JTextField("0");
		txt_ki_T   = new JTextField("0");
		txt_kd_T   = new JTextField("0");
		txt_kp_F   = new JTextField("0");
		txt_ki_F   = new JTextField("0");
		txt_kd_F   = new JTextField("0");
		txt_port.setHorizontalAlignment(JTextField.CENTER);
		txt_setpoint_I.setHorizontalAlignment(JTextField.CENTER);
		txt_sample_time_I.setHorizontalAlignment(JTextField.CENTER);
		txt_setpoint_II.setHorizontalAlignment(JTextField.CENTER);
		txt_sample_time_II.setHorizontalAlignment(JTextField.CENTER);
		txt_kp_T.setHorizontalAlignment(JTextField.CENTER);
		txt_ki_T.setHorizontalAlignment(JTextField.CENTER);
		txt_kd_T.setHorizontalAlignment(JTextField.CENTER);
		txt_kp_F.setHorizontalAlignment(JTextField.CENTER);
		txt_ki_F.setHorizontalAlignment(JTextField.CENTER);
		txt_kd_F.setHorizontalAlignment(JTextField.CENTER);
		btn_server_init = new JButton("Start Control");
		btn_server_stop	= new JButton("Stop Control");
		btn_server_init.setActionCommand("start");
		btn_server_stop.setActionCommand("stop");
		parametersPanel.setLayout(new GridLayout(14, 2,10,10));
		parametersPanel.add(lbl_param_1);
		parametersPanel.add(txt_port);
		parametersPanel.add(lbl_param_4);
		parametersPanel.add(lbl_dummi_1);
		parametersPanel.add(lbl_param_2);
		parametersPanel.add(txt_setpoint_I);
		parametersPanel.add(lbl_param_3);
		parametersPanel.add(txt_sample_time_I);
		parametersPanel.add(lbl_param_5);
		parametersPanel.add(txt_kp_T);
		parametersPanel.add(lbl_param_6);
		parametersPanel.add(txt_ki_T);
		parametersPanel.add(lbl_param_7);
		parametersPanel.add(txt_kd_T);
		parametersPanel.add(lbl_param_8);
		parametersPanel.add(lbl_dummi_2);
		parametersPanel.add(lbl_param_2_2);
		parametersPanel.add(txt_setpoint_II);
		parametersPanel.add(lbl_param_3_2);
		parametersPanel.add(txt_sample_time_II);
		parametersPanel.add(lbl_param_9);
		parametersPanel.add(txt_kp_F);
		parametersPanel.add(lbl_param_10);
		parametersPanel.add(txt_ki_F);
		parametersPanel.add(lbl_param_11);
		parametersPanel.add(txt_kd_F);
		parametersPanel.add(btn_server_init);
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
