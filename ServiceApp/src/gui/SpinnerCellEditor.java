package gui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

public class SpinnerCellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1969238391031735653L;
	private final JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

	public SpinnerCellEditor() {
		((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
	}

	@Override
	public Object getCellEditorValue() {
		return spinner.getValue();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

		int val = (value instanceof Integer) ? (Integer) value : 0;
		spinner.setValue(val);
		return spinner;
	}
}
