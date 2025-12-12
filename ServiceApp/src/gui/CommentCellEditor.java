package gui;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.util.ArrayList;
import models.Appointment;
import services.AppointmentService;

public class CommentCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 2115021322015056371L;
	
	private JTextField textField;
    private JTable table;
    private ArrayList<Appointment> appointments;

    public CommentCellEditor(JTable table, ArrayList<Appointment> appointments) {
        super(new JTextField());
        this.table = table;
        this.appointments = appointments;
        textField = (JTextField) getComponent();
    }

    @Override
    public Object getCellEditorValue() {
        String newComment = textField.getText();

        int row = table.getEditingRow();
        if (row != -1) {
            Appointment ap = appointments.get(row);

            try {
                AppointmentService service = new AppointmentService();
                ap.setComment(newComment);
                service.updateAppointment(ap.getId(), ap);
                JOptionPane.showMessageDialog(
                        table,
                        "Appointment comment has been updated.",
                        "Updated",
                        JOptionPane.INFORMATION_MESSAGE
                 );

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    table,
                    "Error updating appointment comment: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }

        return newComment;
    }
}

