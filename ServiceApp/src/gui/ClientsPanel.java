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
import models.Client;
import services.ClientService;

public class ClientsPanel extends JPanel {
	private static final long serialVersionUID = -625255177917879618L;

	private JTable clientsTable;
	
	private ClientService clientService;

	public ClientsPanel() {
		super(new BorderLayout());
		this.clientService = new ClientService();
		
		clientsTable = new JTable(getTable());
		
		JScrollPane tableScrollPane = new JScrollPane(clientsTable);
		clientsTable = formatTable(clientsTable);
		initializeDeleteButton();
		
		add(tableScrollPane, BorderLayout.CENTER);
	}
	
	private DefaultTableModel getTable() {
		String[] columnNames = {"#", "First name", "Last name", "Username", "Address", "Phone", "E-mail", "Status", " "};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        ArrayList<Client> clients = getClients();
        int counter = 1;
        ImageIcon icon = new ImageIcon("icons/icons8-delete-16.png");
        
        for (Client c : clients) {
            Object[] row = {counter++ + ".", c.getFirstName(), c.getLastName(), c.getUsername(), c.getAddress(), c.getPhoneNumber(),
            		c.getEmail(), c.getStatus(), icon};
            model.addRow(row);
        }
        return model;
	}
	
	private JTable formatTable(JTable clientsTable) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < clientsTable.getColumnCount() - 1; i++) {
			clientsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		// Zero column (Order number)
		TableColumn orderNumberColumn = clientsTable.getColumnModel().getColumn(0);
		orderNumberColumn.setPreferredWidth(20);
		orderNumberColumn.setMaxWidth(30);
		
		// First column (First name)
		TableColumn firstNameColumn = clientsTable.getColumnModel().getColumn(1);
		firstNameColumn.setPreferredWidth(120);
		firstNameColumn.setMinWidth(80);
		firstNameColumn.setMaxWidth(140);

		// Second column (Last name)
		TableColumn lastNameColumn = clientsTable.getColumnModel().getColumn(2);
		lastNameColumn.setPreferredWidth(120);
		lastNameColumn.setMaxWidth(140);

		// Third column (Username)
		TableColumn usernameColumn = clientsTable.getColumnModel().getColumn(3);
		usernameColumn.setPreferredWidth(120);
		usernameColumn.setMaxWidth(140);

		// Fourth column (Address)
		TableColumn addressColumn = clientsTable.getColumnModel().getColumn(4);
		addressColumn.setPreferredWidth(320);
		addressColumn.setMaxWidth(340);
		
		// Fifth column (Phone)
		TableColumn phoneNumberColumn = clientsTable.getColumnModel().getColumn(5);
		phoneNumberColumn.setPreferredWidth(120);
		phoneNumberColumn.setMaxWidth(140);
		
		// Sixth column (E-mail)
		TableColumn emailColumn = clientsTable.getColumnModel().getColumn(6);
		emailColumn.setPreferredWidth(190);
		emailColumn.setMaxWidth(200);
		
		// Seventh column (Status)
		TableColumn statusColumn = clientsTable.getColumnModel().getColumn(7);
		statusColumn.setCellRenderer(new StatusCellRenderer());
		statusColumn.setCellEditor(new StatusComboBoxEditor(clientsTable));
		
		
		// Eight column (Delete)
		clientsTable.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
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
		clientsTable.getColumnModel().getColumn(8).setMaxWidth(40);
		
		clientsTable.getTableHeader().setBackground(new Color(0, 120, 215));
		clientsTable.getTableHeader().setForeground(Color.WHITE);
		clientsTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
		clientsTable.setRowHeight(24);
		clientsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
		
		return clientsTable;
	}
	
	private void initializeDeleteButton() {
		clientsTable.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        int row = clientsTable.rowAtPoint(e.getPoint());
		        int col = clientsTable.columnAtPoint(e.getPoint());

		        if (col == 8) {
		            String username = clientsTable.getValueAt(row, 3).toString();

		            int choice = JOptionPane.showConfirmDialog(
		                ClientsPanel.this,
		                "Are you sure you want to delete user \"" + username + "\"?",
		                "Confirm",
		                JOptionPane.YES_NO_OPTION
		            );

		            if (choice == JOptionPane.YES_OPTION) {
		            	try {
		            		String clientId = clientService.getClientByUsername(username).getId();
							clientService.deleteClient(clientId);
							refreshTable();
						} catch (ServerError e1) {
							e1.printStackTrace();
							System.out.println(e1.getMessage());
						}
		            	JOptionPane.showMessageDialog(
		            			ClientsPanel.this, 
		            			"User \"" + username + "\" has been deleted", 
		            			"Info", 
		            			JOptionPane.INFORMATION_MESSAGE
		            	);
		            }
		        }
		    }
		});

	}
	
	private ArrayList<Client> getClients() {
		try {
			return clientService.getAllClients();
		} catch (ServerError e) {
			e.printStackTrace();
		}
		return new ArrayList<Client>();
	}
	
	public void refreshTable() {
	    DefaultTableModel newModel = getTable();
	    clientsTable.setModel(newModel);
	    formatTable(clientsTable);
	}

}
