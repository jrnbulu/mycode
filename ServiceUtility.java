package com.Demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

public class ServiceUtility
{

	private JFrame utilityFrame = null;
	private JPanel North_panel,South_Panel,West_Panel = null;
	private JButton kill_button = null;
	private JComboBox service_Combobox = null;
	private DefaultComboBoxModel serviceComboBoxModel = new DefaultComboBoxModel();
	private String selected_service = null;
	private ButtonGroup btn_group = null;
	private JCheckBox south_chk_box = null;
	private JRadioButton radio_btn_single,radio_btn_multiple = null;
	private JLabel from_port,to_port ,combo_label,port_no,pid= null;
	private JTextField port_text1,port_text2,port_text3 = null;
	private JButton block_btn ,free_btn = null;
	private JTextArea port_area = null;
	private Process process = null;
	private JScrollPane scrollPane = null;
	private final static String newline = "\n";
	public ServiceUtility()
	{
		init();
	}
	public void init()
	{
		utilityFrame = new JFrame("Service page");
		utilityFrame.setLayout(new BorderLayout());
		utilityFrame.add(northPanel(),BorderLayout.NORTH);
		utilityFrame.add(southPanel(),BorderLayout.SOUTH);
		utilityFrame.add(westPanel(),BorderLayout.WEST);
		utilityFrame.setSize(500,500);
		utilityFrame.setVisible(true);
	}
	public JPanel southPanel()
	{
		South_Panel = new JPanel();
		South_Panel.setLayout(new GridBagLayout());
		GridBagConstraints gbConstant = new GridBagConstraints();
		South_Panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(12,12,0,12),BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED),"Blocking Port")));
		btn_group = new ButtonGroup();
		south_chk_box = new JCheckBox();
		south_chk_box.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				// TODO Auto-generated method stub
				boolean selected = south_chk_box.isSelected();
				enableSouthpanel(selected);
			}
		});
		radio_btn_single = new JRadioButton("Single");
		radio_btn_multiple = new JRadioButton("Multiple");
		radio_btn_single.setSelected(true);
		btn_group.add(radio_btn_single);
		btn_group.add(radio_btn_multiple);
		radio_btn_single.addItemListener(getradio_btn_Listener());
		radio_btn_multiple.addItemListener(getradio_btn_Listener());
		radio_btn_single.setSelected(true);
		from_port = new JLabel("From:");
		to_port = new JLabel(" To: ");
		port_text1 = new JTextField(5);
		port_text2 = new JTextField(5);
		port_text3 = new JTextField(5);
		port_text1.addKeyListener(getKeyListener(port_text1));
		port_text2.addKeyListener(getKeyListener(port_text2));
		port_text3.addKeyListener(getKeyListener(port_text3));
		block_btn = new JButton("Block");
		free_btn = new JButton("Free");
		
		gbConstant.gridx = 0;
		gbConstant.gridy = 0;
		South_Panel.add(south_chk_box,gbConstant);
		
		gbConstant.gridx = 1;
		gbConstant.gridy = 1;
		South_Panel.add(radio_btn_single,gbConstant);
		
		gbConstant.gridx = 2;
		gbConstant.gridy = 1;
		South_Panel.add(port_text1,gbConstant);
		
		gbConstant.gridx = 1;
		gbConstant.gridy = 3;
		South_Panel.add(radio_btn_multiple,gbConstant);
		
		gbConstant.gridx = 2;
		gbConstant.gridy = 3;
		South_Panel.add(from_port,gbConstant);
		
		gbConstant.gridx = 3;
		gbConstant.gridy = 3;
		South_Panel.add(port_text2,gbConstant);
		
		gbConstant.gridx = 4;
		gbConstant.gridy = 3;
		South_Panel.add(to_port,gbConstant);
		
		gbConstant.gridx = 5;
		gbConstant.gridy = 3;
		South_Panel.add(port_text3,gbConstant);
		
		gbConstant.gridx = 7;
		gbConstant.gridy = 4;
		South_Panel.add(block_btn,gbConstant);
		
		gbConstant.gridx = 8;
		gbConstant.gridy = 4;
		South_Panel.add(free_btn,gbConstant);
		
		south_chk_box.setSelected(false);
		enableSouthpanel(false);
		return South_Panel;
	}
	public JPanel northPanel()
	{
		North_panel = new JPanel();
		North_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		North_panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(12,12,0,12),BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED),null)));
		kill_button = new JButton("KILL");
		combo_label = new JLabel("Service Name: ");
		service_Combobox = new JComboBox(getServiceComboBoxModel());
		service_Combobox.setName("service_combobox");
		service_Combobox.setPreferredSize(new Dimension(200,20));
		service_Combobox.setMaximumSize(new Dimension(200,20));
		service_Combobox.setMinimumSize(new Dimension(200,20));
		service_Combobox.addItemListener(getComboItemListener());
		kill_button.addActionListener(getKillAction());
		North_panel.add(combo_label);
		North_panel.add(service_Combobox);
		North_panel.add(kill_button);
		return North_panel;
	}
	public JPanel westPanel()
	{
		West_Panel = new JPanel();
		West_Panel.setLayout(new GridBagLayout());
		GridBagConstraints gbConstant = new GridBagConstraints();
		West_Panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(12,12,0,12),BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED),"Service Details:")));
		
		port_area = new JTextArea();
		scrollPane = new JScrollPane(port_area);
		port_area.setEditable(false);
		
		port_area.setSize(new Dimension(100,250));
		port_area.setPreferredSize(new Dimension(200,200));
		port_area.setMaximumSize(new Dimension(200,200));
		port_area.setEnabled(false);
		
				
		gbConstant.gridx = 0;
		gbConstant.gridy = 0;
		West_Panel.add(scrollPane,gbConstant);
		//West_Panel.add(port_area);
		return West_Panel;
	}
	private DefaultComboBoxModel getServiceComboBoxModel()
    {
	    // TODO Auto-generated method stub
		serviceComboBoxModel.removeAllElements();
		List<String> allProcessList = getAllProcessList();
		if(null != allProcessList )
		{
			for(String process:allProcessList)
			{
				serviceComboBoxModel.addElement(process);
			}
			
		}
	    return serviceComboBoxModel;
    }
	private List<String> getAllProcessList()
    {
		BufferedReader input = null;
		List<String> processes = new ArrayList<String>();
		processes.add(new String(""));
		try
		{
			String line;
			Process process = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
			 input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while(null !=(line = input.readLine()))
			{
				if(!line.trim().equals(""))
				{
					//keep only the process name
					line = line.substring(1);
					processes.add(line.substring(0,line.indexOf('"')));
				}
			}
		}catch(Exception e)
		{
			if(null != input)
			{
				try
				{
					input.close();
				}catch(IOException ie)
				{
					
				}
			}
		}
	    // TODO Auto-generated method stub
	    return processes;
    }
	public ItemListener getComboItemListener()
	{
		return new ItemListener()
		{
			public void itemStateChanged(final ItemEvent event)
			{
				comboSelectedChangedAction(event);
			}
		};
		
		
	}
	public void comboSelectedChangedAction(ItemEvent event)
	{
		final ItemEvent f_event = event;
		if(event.getStateChange() == ItemEvent.SELECTED)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					port_area.setEnabled(true);
					port_area.setText("   PID      PORT_TYPE      PORT_NO");
				    selected_service = f_event.getItem().toString();
					System.out.println(selected_service);
					String pid = getProcessID(selected_service);
					String port = getUsedPort(pid);
					port_area.append(newline+"   "+pid+"       "+port);
				}
				});
			}
		}
	public String getProcessID(String service)
	{
		String OS = System.getProperty("os.name");
		BufferedReader br = null;
		String line ="";
		List<String> data = new ArrayList<String>();
			try
			{
				if(OS.startsWith("Windows"))
				{
				  process = Runtime.getRuntime().exec(System.getenv("windir")+"\\system32\\"+"tasklist.exe");
				}
				else
				{
					process =  Runtime.getRuntime().exec("ps -e");
				}
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while((line =br.readLine())!= null )
				{
					if(line.startsWith(service))
					{
						data = parseLine(line);
						if(null != data && 0 != data.size())
						{
							String pid = (String)data.get(1);
							return pid;
						}
					}
				}
			}
			catch(Exception e)
			{
				
			}
			
		return "";
	}
	public String getUsedPort(String pid)
	{
		BufferedReader b_reader = null;
		String line = "";
		List<String> port_data = new ArrayList<String>();
		try
		{
			process = Runtime.getRuntime().exec("netstat -ano");
			b_reader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line =b_reader.readLine())!= null )
			{
				if(line.endsWith(pid))
				{
					port_data = parseLine(line);
					if(null != port_data && 0 != port_data.size())
					{
						String port_type = (String)port_data.get(0);
						String port_no = (String)port_data.get(1);
						return port_type+"             "+port_no ;
					}
				}
			}
		}
		catch(Exception e)
		{
			
		}
		return "not accessible";
	}
	public List parseLine(String line)
	{
		StringTokenizer s_token = new StringTokenizer(line);
		List<String> token = new ArrayList<String>();
		while(s_token.hasMoreTokens())
		{
			token.add(s_token.nextToken());
		}
		return token;
	}
	public void enableSouthpanel(boolean selected)
	{
		South_Panel.setEnabled(selected);
		if(!selected)
		{
			radio_btn_single.setEnabled(selected);
			radio_btn_single.setSelected(!selected);
			radio_btn_multiple.setEnabled(selected);
			port_text1.setEnabled(selected);
			port_text2.setEnabled(selected);
			port_text3.setEnabled(selected);
			from_port.setEnabled(selected);
			to_port.setEnabled(selected);
			block_btn.setEnabled(selected);
			free_btn.setEnabled(selected);
		}
		else
		{
			radio_btn_single.setEnabled(selected);
			radio_btn_single.setSelected(selected);
			radio_btn_multiple.setEnabled(selected);
			port_text1.setEnabled(selected);
			block_btn.setEnabled(selected);
			free_btn.setEnabled(selected);
		}
		utilityFrame.getContentPane().validate();
		utilityFrame.getContentPane().repaint();
	}
	public ItemListener getradio_btn_Listener()
	{
		ItemListener item_Listener = new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(radio_btn_single.isSelected())
				{
					port_text1.setEnabled(true);
					port_text2.setText("");
					port_text3.setText("");
					from_port.setEnabled(false);
					to_port.setEnabled(false);
				}
				else if(radio_btn_multiple.isSelected())
				{
					port_text1.setEnabled(false);
					port_text2.setEnabled(true);
					port_text3.setEnabled(true);
					port_text1.setText("");
					from_port.setEnabled(true);
					to_port.setEnabled(true);
					
				}
			}
		};
		return item_Listener;
	}

	public ActionListener getKillAction()
	{
		return null;
	}
	public KeyListener getKeyListener(JTextField port)
	{
		final JTextField t_field = port;
		KeyListener k_listener = new KeyAdapter()
		{
			public void keyTyped(KeyEvent ke)
			{
				char c = ke.getKeyChar();
				if(!(Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE) ||(c==KeyEvent.VK_DELETE)))
				{
					ke.consume();
				}
				else
				{
					String text = t_field.getText();
					if(text.length()>=5)
					{
						ke.consume();
						t_field.setToolTipText("Please put nos in range 0 to 65355");
					}
				}
			}
		};
		return k_listener;
	}
	public static void main(String[] args)
	{
		ServiceUtility utility = new ServiceUtility();
		 System.out.println("We have " + " running threads. ");
	}

}
