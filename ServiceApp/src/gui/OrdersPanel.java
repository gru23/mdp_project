package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import exceptions.ServerError;
import models.Order;
import services.SupplierService;

public class OrdersPanel extends JPanel {
	private static final long serialVersionUID = -7986709115218958062L;

	private JTable ordersTable;
	
	private ArrayList<Order> orders;
	
	private SuppliersPanel suppliersPanel;
	private SupplierService supplierService;
	
	public OrdersPanel() {
		super(new BorderLayout());
		this.supplierService = new SupplierService();
		
		ordersTable = new JTable(getOrdersTable());
		
		JScrollPane tableScrollPane = new JScrollPane(ordersTable);
		tableScrollPane.getViewport().setBackground(Color.WHITE);
		tableScrollPane.setBackground(Color.WHITE);
		ordersTable = formatOrdersTable(ordersTable);
		
//		initializeRowClick();
		
		suppliersPanel = new SuppliersPanel(this);
		
		JSplitPane splitPane = new JSplitPane(
			    JSplitPane.VERTICAL_SPLIT,
			    tableScrollPane,
			    suppliersPanel
			);
		
		splitPane.setResizeWeight(0.35);
		splitPane.setDividerSize(5);
		splitPane.setEnabled(false);
		
		
//		add(initializeSubmitPanel(), BorderLayout.NORTH);
		add(splitPane, BorderLayout.CENTER);
	}
	
	public void addOrder(Order newOrder) {
//		orders.add(newOrder);
		try {
			supplierService.order(newOrder);
			refreshTable();
		} catch (ServerError e) {
			e.printStackTrace();
		}
	}
	
	public void refreshTable() {
	    DefaultTableModel newModel = getOrdersTable();
	    ordersTable.setModel(newModel);
	    formatOrdersTable(ordersTable);
	    suppliersPanel.initializeSuppliers();
	}
	
	private DefaultTableModel getOrdersTable() {
		String[] columnNames = {"#", "Date", "Id", "Supplier", "Status"};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		int counter = 1;
		orders = getAllOrders();
		for(Order o : orders) {
			String date = LocalDate
			        .parse(o.getDate())
			        .format(DateTimeFormatter.ofPattern("dd. MMM yyyy."));

			Object[] row = {counter++ + ".", date, o.getId(), o.getSupplier(), o.getStatus()};
			model.addRow(row);
		}
		return model;
	}
	
	private JTable formatOrdersTable(JTable ordersTable) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < ordersTable.getColumnCount(); i++) {
			ordersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		// Zero column (Order number)
		TableColumn orderNumberColumn = ordersTable.getColumnModel().getColumn(0);
		orderNumberColumn.setPreferredWidth(35);
		orderNumberColumn.setMaxWidth(45);
				
		// First column (Date)
		TableColumn dateColumn = ordersTable.getColumnModel().getColumn(1);
		dateColumn.setPreferredWidth(120);
		dateColumn.setMinWidth(90);
		dateColumn.setMaxWidth(140);
		
		// Second column (Id)
		TableColumn idColumn = ordersTable.getColumnModel().getColumn(2);
		idColumn.setPreferredWidth(120);
		idColumn.setMinWidth(90);
		
		// Third column (Supplier)
		TableColumn supplierColumn = ordersTable.getColumnModel().getColumn(3);
		supplierColumn.setMinWidth(60);
		supplierColumn.setPreferredWidth(120);
		supplierColumn.setMaxWidth(140);

		// Fourth column (Status)
		TableColumn statusColumn = ordersTable.getColumnModel().getColumn(3);
		statusColumn.setMinWidth(220);
		statusColumn.setPreferredWidth(240);
		statusColumn.setMaxWidth(260);
		
		ordersTable.getTableHeader().setBackground(new Color(0, 120, 215));
		ordersTable.getTableHeader().setForeground(Color.WHITE); 
		ordersTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
		ordersTable.setRowHeight(24);
		ordersTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
		
		return ordersTable;
	}
	
	private ArrayList<Order> getAllOrders() {
		try {
			return supplierService.getAllOrders();
		} catch (ServerError e) {
			e.printStackTrace();
		}
		return new ArrayList<Order>();
	}
}
