package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import exceptions.ServerError;
import models.Order;
import models.OrderingArticle;
import models.Supplier;
import services.SupplierService;

public class SuppliersPanel extends JPanel {
	private static final long serialVersionUID = 2387402530293966111L;
	
	private JTable articlesTable;
	
	private JComboBox<String> suppliersCB;
	
	private SupplierService supplierService;
	
	private ArrayList<Supplier> suppliers;
	private ArrayList<OrderingArticle> currentArticles;
	
	private OrdersPanel ordersPanel;
	
	public SuppliersPanel(OrdersPanel ordersPanel) {
		super(new BorderLayout());
		this.supplierService = new SupplierService();
		this.suppliersCB = new JComboBox<String>();
		this.ordersPanel = ordersPanel;
		
		initializeSuppliers();
		
		articlesTable = new JTable(getArticlesTable());
		JScrollPane tableScrollPane = new JScrollPane(articlesTable);
		tableScrollPane.getViewport().setBackground(Color.WHITE);
		tableScrollPane.setBackground(Color.WHITE);
		articlesTable = formatTable(articlesTable);
		
		add(initializeTopPanel(), BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
	}
	
	public void initializeSuppliers() {
		try {
			suppliers = supplierService.getAllSuppliers();
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(
					suppliers.stream()
		                          .map(Supplier::getName)
		                          .toArray(String[]::new)
		    );
		    suppliersCB.setModel(model);
		    suppliersCB.addActionListener(e -> refreshTable());
		} catch (ServerError e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public JPanel initializeTopPanel() {
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		
		JButton orderBtn = new JButton("Order");
		orderBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	Order newOrder = new Order(
            			suppliersCB.getSelectedItem().toString(), 
            			getOrderedArticles()
            	);
            	System.out.println(newOrder);
            	if(newOrder.getArticles().size() > 0) {
	            	ordersPanel.addOrder(newOrder);
            	}
            }
        });
		
		topPanel.add(suppliersCB);
		topPanel.add(orderBtn);
		return topPanel;
	}
	
	private DefaultTableModel getArticlesTable() {
		String[] columnNames = {"#", "Code", "Name", "Manufacturer", "Price (€)", "Quantity"};
	    DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

	    Object selected = suppliersCB.getSelectedItem();
	    if (selected == null || suppliers == null) {
	        return model;
	    }

	    String currentSupplier = selected.toString();

	    Supplier selectedSupplier = null;
	    for (Supplier s : suppliers) {
	        if (currentSupplier.equals(s.getName())) {
	            selectedSupplier = s;
	            break;
	        }
	    }
	    if (selectedSupplier == null)
	        return model;

	    currentArticles = selectedSupplier.getArticles();
		int counter = 1;
		for(OrderingArticle o : currentArticles) {
			Object[] row = {counter++ + ".", o.getCode(), o.getName(), o.getManufacturer(), o.getPrice(), o.getQuanity()};
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
		orderNumberColumn.setMaxWidth(45);
				
		// First column (Code)
		TableColumn codeColumn = articlesTable.getColumnModel().getColumn(1);
		codeColumn.setPreferredWidth(100);
		codeColumn.setMinWidth(90);

		// Second column (Name)
		TableColumn nameColumn = articlesTable.getColumnModel().getColumn(2);
		nameColumn.setPreferredWidth(400);

		// Third column (Manufacturer)
		TableColumn manufacturerColumn = articlesTable.getColumnModel().getColumn(3);
		manufacturerColumn.setPreferredWidth(150);
		manufacturerColumn.setMaxWidth(200);
		
		// Fourth column (Price)
		TableColumn priceColumn = articlesTable.getColumnModel().getColumn(4);
		priceColumn.setPreferredWidth(100);
		priceColumn.setMaxWidth(120);
		
		// Fifth column (Quantity)
		TableColumn quantityColumn = articlesTable.getColumnModel().getColumn(5);
		quantityColumn.setPreferredWidth(80);
		quantityColumn.setMaxWidth(100);
		quantityColumn.setCellEditor(new SpinnerCellEditor());
		
		articlesTable.getTableHeader().setBackground(new Color(0, 120, 215));
		articlesTable.getTableHeader().setForeground(Color.WHITE); 
		articlesTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
		articlesTable.setRowHeight(24);
		articlesTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
		
		return articlesTable;
	}

	private void refreshTable() {
	    DefaultTableModel model = getArticlesTable();
	    articlesTable.setModel(model);
	    formatTable(articlesTable);
	}
	
	private ArrayList<OrderingArticle> getOrderedArticles() {
		ArrayList<OrderingArticle> ordered = new ArrayList<>();

	    for (int i = 0; i < articlesTable.getRowCount(); i++) {
	        Object val = articlesTable.getValueAt(i, 5);
	        int quantity = 0;

	        if (val instanceof Number) {
	            quantity = ((Number) val).intValue();
	        } else if (val instanceof String) {
	            try {
	                quantity = Integer.parseInt((String) val);
	            } catch (NumberFormatException e) {
	                quantity = 0;
	            }
	        }

	        if (quantity > 0) {
	            OrderingArticle article = currentArticles.get(i);
	            OrderingArticle orderedArticle = new OrderingArticle(
	                article.getCode(),
	                article.getName(),
	                article.getManufacturer(),
	                article.getPrice(),
	                quantity
	            );
	            ordered.add(orderedArticle);
	        }
	    }
	    refreshTable();
	    return ordered;
	}
}
