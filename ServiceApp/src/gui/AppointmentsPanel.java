package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import exceptions.ServerError;
import models.Appointment;
import services.AppointmentService;

public class AppointmentsPanel extends JPanel {
	private static final long serialVersionUID = 5060117341949972554L;

	private JTable appointmentsTable;
	
	private AppointmentService appointmentService;
	
	private ArrayList<Appointment> appointments;

	public AppointmentsPanel() {
		super(new BorderLayout());
		this.appointmentService = new AppointmentService();
		
		appointmentsTable = new JTable(getTable());
		
		((DefaultTableModel) appointmentsTable.getModel()).addTableModelListener(e -> {
		    int row = e.getFirstRow();
		    int column = e.getColumn();

		    // Kolona 5 = comment
		    if (column == 5) {
		        String newComment = appointmentsTable.getValueAt(row, 5).toString();
		        Appointment ap = appointments.get(row);

		        ap.setComment(newComment);

		        try {
		            appointmentService.updateAppointment(ap.getId(), ap);
		        } catch (Exception ex) {
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(
		                this,
		                "Error updating comment: " + ex.getMessage(),
		                "Error",
		                JOptionPane.ERROR_MESSAGE
		            );
		        }
		    }
		});
		
		JScrollPane tableScrollPane = new JScrollPane(appointmentsTable);
		appointmentsTable = formatTable(appointmentsTable);
		initializeDeleteButton();
		
		add(tableScrollPane, BorderLayout.CENTER);
	}
	
	private DefaultTableModel getTable() {
		String[] columnNames = {"#", "Date", "Time", "Type", "Status", "Comment", " "};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5 || column == 6;
            }
        };
        appointments = getAppointments();
        int counter = 1;
        ImageIcon icon = new ImageIcon("icons/icons8-delete-16.png");
        
        for (Appointment a : appointments) {
            Object[] row = {counter++ + ".", a.getDate(), a.getTime(), a.getType().getDisplayName(), 
            		a.getStatus(), a.getComment(), icon};
            model.addRow(row);
        }
        return model;
	}
	
	private JTable formatTable(JTable appointmentsTable) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < appointmentsTable.getColumnCount() - 1; i++) {
			appointmentsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		// Zero column (Order number)
		TableColumn orderNumberColumn = appointmentsTable.getColumnModel().getColumn(0);
		orderNumberColumn.setPreferredWidth(30);
		orderNumberColumn.setMaxWidth(35);
		
		// First column (Date)
		TableColumn dateColumn = appointmentsTable.getColumnModel().getColumn(1);
		dateColumn.setPreferredWidth(100);
		dateColumn.setMinWidth(90);
		dateColumn.setMaxWidth(120);

		// Second column (Time)
		TableColumn timeColumn = appointmentsTable.getColumnModel().getColumn(2);
		timeColumn.setPreferredWidth(60);
		timeColumn.setMaxWidth(75);

		// Third column (Type)
		TableColumn typeColumn = appointmentsTable.getColumnModel().getColumn(3);
		typeColumn.setPreferredWidth(130);
		typeColumn.setMaxWidth(145);

		// Fourth column (Status)
		TableColumn statusColumn = appointmentsTable.getColumnModel().getColumn(4);
		statusColumn.setCellRenderer(new AppointmentStatusCellRenderer());
		statusColumn.setCellEditor(new AppointmentStatusComboBoxEditor(appointmentsTable, appointments));
		statusColumn.setPreferredWidth(120);
		statusColumn.setMaxWidth(130);
		
		// Fifth column (Comment)
		TableColumn commentColumn = appointmentsTable.getColumnModel().getColumn(5);
		commentColumn.setCellEditor(new CommentCellEditor(appointmentsTable, appointments));
		commentColumn.setPreferredWidth(120);
		
		// Sixth column (Delete)
		appointmentsTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value,
		            boolean isSelected, boolean hasFocus, int row, int column) {
		    	
		        JLabel label = new JLabel();
		        label.setHorizontalAlignment(SwingConstants.CENTER);

		        if (value instanceof ImageIcon) {
		            label.setIcon((ImageIcon) value);
		        }
		        return label;
		    }
		});
		appointmentsTable.getColumnModel().getColumn(6).setMaxWidth(40);
		
		appointmentsTable.getTableHeader().setBackground(new Color(0, 120, 215)); // plava boja
		appointmentsTable.getTableHeader().setForeground(Color.WHITE); 
		appointmentsTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
		appointmentsTable.setRowHeight(24);
		appointmentsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
		
		return appointmentsTable;
	}
	
	private void initializeDeleteButton() {
		appointmentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        int row = appointmentsTable.rowAtPoint(e.getPoint());
		        int col = appointmentsTable.columnAtPoint(e.getPoint());

		        // Kolona 6 je ikona
		        if (col == 6) {
		        	Appointment selectedAppointment = appointments.get(row);
		            
		        	int choice = JOptionPane.showConfirmDialog(
		            	AppointmentsPanel.this,
		                "Are you sure you want to delete selected appointment?",
		                "Confirm",
		                JOptionPane.YES_NO_OPTION
		            );

		            if (choice == JOptionPane.YES_OPTION) {
		            	try {
							appointmentService.deleteAppointment(selectedAppointment.getId());
							refreshTable();
						} catch (ServerError e1) {
							e1.printStackTrace();
						}
		            	JOptionPane.showMessageDialog(
		            			AppointmentsPanel.this, 
		            			"Appointment has been deleted", 
		            			"Info", 
		            			JOptionPane.INFORMATION_MESSAGE
		            	);
		            }
		        }
		    }
		});

	}
	
	private ArrayList<Appointment> getAppointments() {
		try {
			return appointmentService.getAllApointments();
		} catch (ServerError e) {
			e.printStackTrace();
		}
		return new ArrayList<Appointment>();
	}
	
	public void refreshTable() {
	    DefaultTableModel newModel = getTable();
	    appointmentsTable.setModel(newModel);
	    formatTable(appointmentsTable);
	}
}
