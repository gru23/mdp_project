package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;

import exceptions.ServerError;
import models.Part;
import services.SparePartService;
import utils.NumericFilter;

public class SparePartsPanel extends JPanel {
	private static final long serialVersionUID = -3164062891814248832L;

	private JTable partsTable;
	
	private SparePartService partsService;
	
	private ArrayList<Part> parts;
	
	private JTextField codeFld;
	private JTextField priceFld;
	private JTextField quantityFld;
	private JTextField nameFld;
	private JTextField manufacturerFld;
	private JTextField descritpionFld;
	private JButton cleanBtn;
	private JButton submitBtn;
	
	public SparePartsPanel() {
		super(new BorderLayout());
		this.partsService = new SparePartService();
		
		partsTable = new JTable(getTable());
		
		JScrollPane tableScrollPane = new JScrollPane(partsTable);
		tableScrollPane.getViewport().setBackground(Color.WHITE);
		tableScrollPane.setBackground(Color.WHITE);
		partsTable = formatTable(partsTable);
		
		initializeDeleteButton();
		initializeRowClick();
		
		add(initializeSubmitPanel(), BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
	}
	
	public void refreshTable() {
	    DefaultTableModel newModel = getTable();
	    partsTable.setModel(newModel);
	    formatTable(partsTable);
	}
	
	private DefaultTableModel getTable() {
		String[] columnNames = {"#", "Code", "Name", "Manufacturer", "Price (€)", "Quantity", "Description", " "};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        parts = getParts();
        int counter = 1;
        ImageIcon icon = new ImageIcon("icons/icons8-delete-16.png");
        
        for (Part p : parts) {
            Object[] row = {counter++ + ".", p.getCode(), p.getName(), p.getManufacturer(), p.getPrice(), 
            		p.getQuantity(), p.getDescription(), icon};
            model.addRow(row);
        }
        return model;
	}
	
	private JTable formatTable(JTable partsTable) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < partsTable.getColumnCount() - 1; i++) {
			partsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		// Zero column (Order number)
		TableColumn orderNumberColumn = partsTable.getColumnModel().getColumn(0);
		orderNumberColumn.setPreferredWidth(30);
		orderNumberColumn.setMaxWidth(35);
		
		// First column (Code)
		TableColumn codeColumn = partsTable.getColumnModel().getColumn(1);
		codeColumn.setPreferredWidth(100);
		codeColumn.setMinWidth(90);
		codeColumn.setMaxWidth(120);

		// Second column (Name)
		TableColumn nameColumn = partsTable.getColumnModel().getColumn(2);
		nameColumn.setPreferredWidth(160);
//		nameColumn.setMaxWidth(200);

		// Third column (Manufacturer)
		TableColumn manufacturerColumn = partsTable.getColumnModel().getColumn(3);
		manufacturerColumn.setPreferredWidth(150);
		manufacturerColumn.setMaxWidth(200);

		// Fourth column (Price)
		TableColumn priceColumn = partsTable.getColumnModel().getColumn(4);
//		statusColumn.setCellRenderer(new AppointmentStatusCellRenderer());
//		statusColumn.setCellEditor(new AppointmentStatusComboBoxEditor(partsTable, parts));
		priceColumn.setPreferredWidth(90);
		priceColumn.setMaxWidth(100);
		
		// Fifth column (Quantity)
		TableColumn quantityColumn = partsTable.getColumnModel().getColumn(5);
//		quantityColumn.setCellEditor(new CommentCellEditor(partsTable, parts));
		quantityColumn.setMinWidth(75);
		quantityColumn.setPreferredWidth(80);
		quantityColumn.setMaxWidth(80);
		
		// Sixth column (Description)
		TableColumn descriptionColumn = partsTable.getColumnModel().getColumn(6);
//		descriptionColumn.setCellEditor(new CommentCellEditor(partsTable, parts));
		descriptionColumn.setPreferredWidth(120);
		
//		partsTable.getColumnModel().getColumn(6).setMaxWidth(40);
		
		// Seventh column (Delete)
		partsTable.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
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
		TableColumn deleteColumn = partsTable.getColumnModel().getColumn(7);
		deleteColumn.setMinWidth(30);
		deleteColumn.setPreferredWidth(30);
		deleteColumn.setMaxWidth(40);
		
		partsTable.getTableHeader().setBackground(new Color(0, 120, 215));
		partsTable.getTableHeader().setForeground(Color.WHITE); 
		partsTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
		partsTable.setRowHeight(24);
		partsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
		
		return partsTable;
	}
	
	private JPanel initializeSubmitPanel() {
		JPanel submitPanel = new JPanel(new BorderLayout());
//		submitPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		submitPanel.setBorder(BorderFactory.createTitledBorder("Add new part"));
		submitPanel.setBackground(Color.WHITE);

		
		codeFld = new JTextField(6);
		priceFld = new JTextField(4);
		quantityFld = new JTextField(3);
		nameFld = new JTextField(15);
		manufacturerFld = new JTextField(7);
		descritpionFld = new JTextField(34);
		
		((AbstractDocument) quantityFld.getDocument()).setDocumentFilter(new NumericFilter(false));
		((AbstractDocument) priceFld.getDocument()).setDocumentFilter(new NumericFilter(true));
		
		JPanel first = new JPanel(new FlowLayout(FlowLayout.CENTER, 9, 5));
		first.setBackground(Color.WHITE);
		first.add(new JLabel("Code:"));
		first.add(codeFld);
		first.add(Box.createHorizontalStrut(40));
		
		first.add(new JLabel("Price:"));
		first.add(priceFld);
		first.add(Box.createHorizontalStrut(40));
		
		first.add(new JLabel("Quantity:"));
		first.add(quantityFld);
		first.add(Box.createHorizontalStrut(40));
		
		JPanel second = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		second.setBackground(Color.WHITE);
		second.add(new JLabel("Name:"));
		second.add(nameFld);
		second.add(Box.createHorizontalStrut(40));
		
		second.add(new JLabel("Manufacturer:"));
		second.add(manufacturerFld);
		second.add(Box.createHorizontalStrut(40));
		
		JPanel third1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		third1.setBackground(Color.WHITE);
		third1.add(new JLabel("Descritpion:"));
		third1.add(descritpionFld);
		third1.add(Box.createHorizontalStrut(16));
		third1.add(new JLabel("  "));
		
		JPanel third2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		third2.setBackground(Color.WHITE);
		cleanBtn = new JButton("Clean ");
		cleanBtn.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        cleanForm();
		    }
		});
		third2.add(cleanBtn);
		third2.add(Box.createHorizontalStrut(16));
		submitBtn = new JButton("Submit");
		submitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		    	Part newPart = getEnteredPart();
		        cleanForm();
		        try {
		        	Optional<Part> existingPart = partsService.getPartByCode(newPart.getCode());
		        	if(existingPart.isPresent()) {
		        		partsService.updatePart(newPart.getCode(), newPart);
		        	}
		        	else {
		        		partsService.createPart(newPart);
		        	}
				} catch (ServerError e1) {
					e1.printStackTrace();
				}
		        refreshTable();
		    }
		});
		third2.add(submitBtn);
		
		JPanel third = new JPanel(new BorderLayout());
		third.setBackground(Color.WHITE);
		
		third.add(third1, BorderLayout.CENTER);
		third.add(third2, BorderLayout.SOUTH);
		
		submitPanel.add(first, BorderLayout.NORTH);
		submitPanel.add(second, BorderLayout.CENTER);
		submitPanel.add(third, BorderLayout.SOUTH);
		return submitPanel;
	}
	
	private void initializeDeleteButton() {
		partsTable.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        int row = partsTable.rowAtPoint(e.getPoint());
		        int col = partsTable.columnAtPoint(e.getPoint());

		        // Kolona 6 je ikona
		        if (col == 7) {
		        	Part selectedPart = parts.get(row);
		            
		        	int choice = JOptionPane.showConfirmDialog(
		            	SparePartsPanel.this,
		                "Are you sure you want to delete selected part?",
		                "Confirm",
		                JOptionPane.YES_NO_OPTION
		            );

		            if (choice == JOptionPane.YES_OPTION) {
		            	try {
		            		partsService.deletePart(selectedPart.getCode());
							refreshTable();
						} catch (ServerError e1) {
							e1.printStackTrace();
						}
		            	JOptionPane.showMessageDialog(
		            			SparePartsPanel.this, 
		            			"Part has been deleted", 
		            			"Info", 
		            			JOptionPane.INFORMATION_MESSAGE
		            	);
		            }
		        }
		    }
		});
	}
	
	private void initializeRowClick() {
	    partsTable.addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override
	        public void mouseClicked(java.awt.event.MouseEvent e) {
	            int row = partsTable.rowAtPoint(e.getPoint());
	            int col = partsTable.columnAtPoint(e.getPoint());
	            
	            if (col == 7)
	                return;

	            if (row >= 0 && row < parts.size()) {
	                Part selected = parts.get(row);
	                fillForm(selected);
	            }
	        }
	    });
	}

	
	private ArrayList<Part> getParts() {
		try {
			return partsService.getAllParts();
		} catch (ServerError e) {
			e.printStackTrace();
		}
		return new ArrayList<Part>();
	}
	
	private void fillForm(Part p) {
	    codeFld.setText(p.getCode());
	    nameFld.setText(p.getName());
	    manufacturerFld.setText(p.getManufacturer());
	    priceFld.setText(String.valueOf(p.getPrice()));
	    quantityFld.setText(String.valueOf(p.getQuantity()));
	    descritpionFld.setText(p.getDescription());
	}
	
	private void cleanForm() {
	    codeFld.setText("");
	    nameFld.setText("");
	    manufacturerFld.setText("");
	    priceFld.setText("");
	    quantityFld.setText("");
	    descritpionFld.setText("");
	}
	
	private Part getEnteredPart() {
		return new Part(codeFld.getText(), nameFld.getText(), manufacturerFld.getText(), 
				Double.valueOf(priceFld.getText()), Integer.valueOf(quantityFld.getText()), descritpionFld.getText());
	}
}
