package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import enums.AppointmentType;
import exceptions.InvalidLoginException;
import models.Appointment;
import models.requests.AppointmentRequest;
import services.ClientService;

public class AppointmentsPanel extends JPanel {
	private static final long serialVersionUID = -4651347449715661088L;
	
	private JSpinner dateSpinner;
	private JSpinner timeSpinner;
	
	private JRadioButton regularServiceButton;
	private JRadioButton repairButton;
	
	private JTextArea errorLabel;
	
	private JTable appointmentTable;
	
	private ClientService service;
	
	public AppointmentsPanel() {
		super(new BorderLayout());
		service = new ClientService();
        setBackground(Color.WHITE);

        JPanel reservationPanel = new JPanel();
        reservationPanel.setBackground(Color.WHITE);
        reservationPanel.setLayout(new BorderLayout());
        

        reservationPanel.add(initializeTextPanel(), BorderLayout.NORTH);
        reservationPanel.add(initializeInputPanel(), BorderLayout.CENTER);
        reservationPanel.add(initializeConfirmPanel(), BorderLayout.SOUTH);

        appointmentTable = new JTable(getTable());
        
        JScrollPane tableScrollPane = new JScrollPane(appointmentTable);
        
        appointmentTable = formatTable(appointmentTable);
        

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, reservationPanel, tableScrollPane);
        splitPane.setDividerLocation(125);
        splitPane.setResizeWeight(0.0);
        splitPane.setBorder(null);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(1);

        add(splitPane, BorderLayout.CENTER);
	}
	
	private JTextArea getAppointmentText() {
		JTextArea appointmentText = new JTextArea("For making a new appointment, please choose date, time and "
				+ "type of service, and then confirm appointment. Bottom table will show you status of appointment.");
		appointmentText.setFont(new Font("Arial", Font.PLAIN, 15));
		appointmentText.setLineWrap(true);
		appointmentText.setWrapStyleWord(true);
		appointmentText.setEditable(false);
		appointmentText.setBackground(getBackground());
		return appointmentText;
	}
	
	private void addTypeOfServiceButtons(JPanel reservationPanel) {
        regularServiceButton = new JRadioButton("Regular Service");
        repairButton = new JRadioButton("Repair");
        regularServiceButton.setBackground(Color.WHITE);
        repairButton.setBackground(Color.WHITE);

        ButtonGroup serviceTypeGroup = new ButtonGroup();
        serviceTypeGroup.add(regularServiceButton);
        serviceTypeGroup.add(repairButton);

        regularServiceButton.setSelected(true);
        reservationPanel.add(regularServiceButton);
        reservationPanel.add(repairButton);
	}
	
	private JPanel initializeTextPanel() {
		JPanel textPanel = new JPanel();
//		textPanel.setLayout(new FlowLayout());
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.setBackground(Color.WHITE);
		textPanel.add(getAppointmentText());
		
		errorLabel = new JTextArea("");
		errorLabel.setFont(new Font("Arial", Font.PLAIN, 15));
	    errorLabel.setForeground(Color.RED);
	    textPanel.add(errorLabel);

		return textPanel;
	}
	
	private JPanel initializeInputPanel() {
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		inputPanel.setBackground(Color.WHITE);
		inputPanel.add(new JLabel("Date:"));
		inputPanel.add(getDateSpinner());
		inputPanel.add(Box.createHorizontalStrut(20));
		inputPanel.add(new JLabel("Time:"));
		inputPanel.add(getTimeSpinner());
		inputPanel.add(Box.createHorizontalStrut(20));
		inputPanel.add(new JLabel("Type:"));
		addTypeOfServiceButtons(inputPanel);
		return inputPanel;
	}
	
	private JPanel initializeConfirmPanel() {
		JPanel confirmPanel = new JPanel();
		confirmPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
		confirmPanel.setBackground(Color.WHITE);
		
		JButton confirmBtn = new JButton("Confirm");
		confirmBtn.addActionListener(this::onConfirmClicked);
	    
		confirmPanel.add(confirmBtn);
		return confirmPanel;
	}
	
	private void onConfirmClicked(ActionEvent e) {
	    String date = getSpinnerDate();
	    String time = getSpinnerTime();
	    
	    AppointmentType type = regularServiceButton.isSelected()
	            ? AppointmentType.REGULAR_SERVICE
	            : AppointmentType.REPAIR;
	    AppointmentRequest request = new AppointmentRequest(date, time, type);

	     try {
	    	 service.makeAppointment(request);
	    	 DefaultTableModel newModel = getTable();
	    	 appointmentTable.setModel(newModel);
	    	 formatTable(appointmentTable);
	    	 errorLabel.setText("");
	     } catch(InvalidLoginException ex) {
//	    	 errorLabel.setText("Selected time slot is already occupied.");
	    	 errorLabel.setText(ex.getMessage().split(":")[1]);
	    	 ex.printStackTrace();
	     }
	}
	
	private ArrayList<Appointment> getAppointments() {
		try {
			return service.appointmentsHistory();
		} catch (InvalidLoginException e) {
			e.printStackTrace();
		}
		return new ArrayList<Appointment>();
	}
	
	private DefaultTableModel getTable() {
		String[] columnNames = {"Date", "Time", "Service Type", "Status", "Comment"};
//        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ArrayList<Appointment> appointments = getAppointments();
        for (Appointment a : appointments) {
            Object[] row = {a.getDate(), a.getTime(), a.getType().getDisplayName(), 
            		a.getStatus().getDisplayName(), a.getComment()};
            model.addRow(row);
        }
        return model;
	}
	
	private JTable formatTable(JTable appointmentTable) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < appointmentTable.getColumnCount() - 1; i++) {
		    appointmentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		TableColumn dateColumn = appointmentTable.getColumnModel().getColumn(0);
		dateColumn.setPreferredWidth(120); // željena širina
		dateColumn.setMinWidth(80);        // minimalna širina
		dateColumn.setMaxWidth(140);       // maksimalna širina

		// Druga kolona (Time)
		TableColumn timeColumn = appointmentTable.getColumnModel().getColumn(1);
		timeColumn.setPreferredWidth(100);
		timeColumn.setMaxWidth(120);

		// Treća kolona (Service Type)
		TableColumn typeColumn = appointmentTable.getColumnModel().getColumn(2);
		typeColumn.setPreferredWidth(170);
		typeColumn.setMaxWidth(270);

		// Četvrta kolona (Status)
		TableColumn statusColumn = appointmentTable.getColumnModel().getColumn(3);
		statusColumn.setPreferredWidth(120);
		statusColumn.setMaxWidth(140);
		
		appointmentTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
		appointmentTable.setRowHeight(20);
		appointmentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));


		
		return appointmentTable;
	}

	private JSpinner getDateSpinner() {
		SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        return dateSpinner;
	}
	
	private JSpinner getTimeSpinner() {
		SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        timeSpinner = new JSpinner(timeModel);
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));
        return timeSpinner;
	}
	
	private String getSpinnerDate() {
	    Date date = (Date) dateSpinner.getValue();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    return sdf.format(date);
	}

	private String getSpinnerTime() {
	    Date time = (Date) timeSpinner.getValue();
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    return sdf.format(time);
	}

}
