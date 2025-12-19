package org.unibl.etf.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.unibl.etf.orders.Order;
import org.unibl.etf.orders.OrderStatus;

public class OrdersPanel extends JPanel {
	private static final long serialVersionUID = 2136472882291460805L;

	private JTable ordersTable;	
	private ArrayList<Order> orders;	//mozda ipak cuvati narudzbe ovdje u memoriji
	
	public OrdersPanel() {
		super(new BorderLayout());
		orders = new ArrayList<Order>();
		orders.add(new Order("31616dde-5f43-48ca-ba08-355f44f7405a", "MDP Servicer", "18.12.2025. ", OrderStatus.WAITING, null));
		
		ordersTable = new JTable(getTable());
		attachTableModelListener();
		
		
		JScrollPane tableScrollPane = new JScrollPane(ordersTable);
		tableScrollPane.getViewport().setBackground(Color.WHITE);
		tableScrollPane.setBackground(Color.WHITE);
		ordersTable = formatTable(ordersTable);
		
		add(tableScrollPane, BorderLayout.CENTER);
	}
	
	public void addOrder(Order newOrder) {
		System.out.println("ADD ORDER METODA");
		orders.add(newOrder);
		refreshTable();
	}
	
	public void refreshTable() {
	    DefaultTableModel newModel = getTable();
	    ordersTable.setModel(newModel);
	    attachTableModelListener();
	    formatTable(ordersTable);
	}
	
	private DefaultTableModel getTable() {
		String[] columnNames = {"#", "Customer", "Id", "Date", "Status"};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        if (column != 4) return false;

		        OrderStatus status = (OrderStatus) getValueAt(row, column);
		        return status == OrderStatus.WAITING;
		    }
        };
        int counter = 1;
        for (Order o : orders) {
            Object[] row = {counter++ + ".", "MDP Servicer", o.getId(), o.getDate(), o.getStatus()};
            model.addRow(row);
        }
        return model;
	}
	
	private JTable formatTable(JTable articlesTable) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < articlesTable.getColumnCount(); i++) {
			articlesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		// Zero column (Order number)
		TableColumn orderNumberColumn = articlesTable.getColumnModel().getColumn(0);
		orderNumberColumn.setPreferredWidth(35);
		orderNumberColumn.setMaxWidth(40);

		// First column (Name)
		TableColumn nameColumn = articlesTable.getColumnModel().getColumn(1);
		nameColumn.setPreferredWidth(150);
		nameColumn.setMaxWidth(200);
		
		// Second column (Id)
		TableColumn idColumn = articlesTable.getColumnModel().getColumn(2);
		idColumn.setPreferredWidth(100);
		idColumn.setMinWidth(90);

		// Third column (Date)
		TableColumn dateColumn = articlesTable.getColumnModel().getColumn(3);
		dateColumn.setPreferredWidth(150);
		dateColumn.setMaxWidth(200);

		// Fourth column (Status)
		TableColumn statusColumn = articlesTable.getColumnModel().getColumn(4);
		statusColumn.setCellEditor(new OrderStatusCellEditor());
		statusColumn.setPreferredWidth(220);
		statusColumn.setMaxWidth(280);
		
		articlesTable.getTableHeader().setBackground(new Color(0, 120, 215));
		articlesTable.getTableHeader().setForeground(Color.WHITE); 
		articlesTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
		articlesTable.setRowHeight(24);
		articlesTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
		
		return articlesTable;
	}
	
	private void attachTableModelListener() {
	    ordersTable.getModel().addTableModelListener(e -> {
	        if (e.getType() != javax.swing.event.TableModelEvent.UPDATE)
	            return;

	        int row = e.getFirstRow();
	        int col = e.getColumn();

	        if (col == 4 || col == javax.swing.event.TableModelEvent.ALL_COLUMNS) {

	            OrderStatus newStatus = (OrderStatus) ordersTable.getValueAt(row, 4);

	            String orderId = ordersTable.getValueAt(row, 2).toString();

	            System.out.println(
	                "STATUS PROMIJENJEN: orderId=" +
	                orderId + ", novi status=" + newStatus
	            );

	            Order order = orders.stream().filter(o -> orderId.equals(o.getId())).findFirst().get();
	            order.setStatus(newStatus);
	            // orderService.updateOrderStatus(orderId, newStatus);
	        }
	    });
	}

}
