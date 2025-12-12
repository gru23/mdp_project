package gui;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import enums.AccountStatus;
import models.Client;
import services.ClientService;


class StatusComboBoxEditor extends DefaultCellEditor {
	private static final long serialVersionUID = -7511967818186438155L;
	
	private JComboBox<String> comboBox;
	private JTable table;

    public StatusComboBoxEditor() {
        super(new JComboBox<>());
        comboBox = (JComboBox<String>) getComponent();
    }
    
    public StatusComboBoxEditor(JTable table) {
        super(new JComboBox<>());
        this.table = table;
        comboBox = (JComboBox<String>) getComponent();
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {

        comboBox.removeAllItems();
        String current = value.toString();

        if (current.equals("UNAPPROVED")) {
            comboBox.addItem("UNAPPROVED");
            comboBox.addItem("APPROVED");
        } else if (current.equals("APPROVED")) {
            comboBox.addItem("APPROVED");
            comboBox.addItem("BLOCKED");
        } else if (current.equals("BLOCKED")) {
            comboBox.addItem("APPROVED");
            comboBox.addItem("BLOCKED");
        }

        comboBox.setSelectedItem(current);
        return comboBox;
    }
    
    @Override
    public Object getCellEditorValue() {
        String newStatus = (String) comboBox.getSelectedItem();

        int row = table.getEditingRow();
        if (row != -1) {
            String username = table.getValueAt(row, 3).toString();

            try {
                ClientService clientService = new ClientService();
                Client client = clientService.getClientByUsername(username);
                client.setStatus(AccountStatus.valueOf(newStatus));
                clientService.updateClient(client.getId(), client);
                JOptionPane.showMessageDialog(
                        table,
                        "Status has been updated for client " + client.getUsername(),
                        "Update",
                        JOptionPane.INFORMATION_MESSAGE
                    );
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    table,
                    "Error updating status: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return newStatus;
    }


}
