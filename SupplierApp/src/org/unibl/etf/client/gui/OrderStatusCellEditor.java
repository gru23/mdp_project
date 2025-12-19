package org.unibl.etf.client.gui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

import org.unibl.etf.orders.OrderStatus;

import java.awt.*;

public class OrderStatusCellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = -2374531656684559697L;
	
	private JComboBox<OrderStatus> comboBox;

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

		OrderStatus currentStatus = (OrderStatus) value;

		if (currentStatus != OrderStatus.WAITING) {
			return null;
		}

		comboBox = new JComboBox<>();
		comboBox.addItem(OrderStatus.APPROVED);
		comboBox.addItem(OrderStatus.NOT_APPROVED);
		comboBox.setSelectedIndex(-1);

		comboBox.addActionListener(e -> {
			stopCellEditing();
		});

		return comboBox;
	}

	@Override
	public Object getCellEditorValue() {
		return comboBox.getSelectedItem();
	}
}
