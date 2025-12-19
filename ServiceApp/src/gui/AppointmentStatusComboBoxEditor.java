package gui;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import enums.AppointmentStatus;
import models.Appointment;
import services.AppointmentService;

class AppointmentStatusComboBoxEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 384147469031480665L;
	
	private JComboBox<String> comboBox;
	
	private JTable table;
	private ArrayList<Appointment> appointments;
    
    public AppointmentStatusComboBoxEditor(JTable table, ArrayList<Appointment> appointments) {
        super(new JComboBox<>());
        this.table = table;
        this.appointments = appointments;
        comboBox = (JComboBox<String>) getComponent();
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {

        comboBox.removeAllItems();

        String current = value.toString();

        switch (current) {
            case "WAITING":
                comboBox.addItem("WAITING");
                comboBox.addItem("CONFIRMED");
                comboBox.addItem("REJECTED");
                break;

            case "CONFIRMED":
                comboBox.addItem("CONFIRMED");
                comboBox.addItem("REPAIRED");
                break;

            case "REJECTED":
                comboBox.addItem("REJECTED");
                break;

            case "REPAIRED":
                comboBox.addItem("REPAIRED");
                break;
        }

        comboBox.setSelectedItem(current);
        return comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        String newStatus = (String) comboBox.getSelectedItem();

        int row = table.getEditingRow();

        if (row != -1) {
            Appointment ap = appointments.get(row);

            try {
                AppointmentService service = new AppointmentService();
                ap.setStatus(AppointmentStatus.valueOf(newStatus));
                service.updateAppointment(ap.getId(), ap);
                String message = "Appointment status has been updated. ";
                if(AppointmentStatus.REPAIRED == ap.getStatus())
                	message += "Invoice has been sent to client.";
                JOptionPane.showMessageDialog(
                        table,
                        message,
                        "Updated",
                        JOptionPane.INFORMATION_MESSAGE
                 );

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    table,
                    "Error updating appointment status: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return newStatus;
    }

}
