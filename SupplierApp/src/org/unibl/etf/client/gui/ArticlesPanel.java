package org.unibl.etf.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;

import org.unibl.etf.articles.Article;
import org.unibl.etf.client.sockets.SupplierClient;
import org.unibl.etf.utils.NumericFilter;

public class ArticlesPanel extends JPanel {
	private static final long serialVersionUID = -3164062891814248832L;

	private JTable articlesTable;
	
	private SupplierClient supplierClient;
	
	private ArrayList<Article> articles;
	
	private JTextField codeFld;
	private JTextField priceFld;
	private JTextField nameFld;
	private JTextField manufacturerFld;
	private JTextField urlFld;
	private JButton cleanBtn;
	private JButton submitBtn;
	
	public ArticlesPanel(SupplierClient supplierClient) {
		super(new BorderLayout());
		this.supplierClient = supplierClient;
		
		articlesTable = new JTable(getTable());
		
		JScrollPane tableScrollPane = new JScrollPane(articlesTable);
		tableScrollPane.getViewport().setBackground(Color.WHITE);
		tableScrollPane.setBackground(Color.WHITE);
		articlesTable = formatTable(articlesTable);
		
		initializeRowClick();
		
		add(initializeSubmitPanel(), BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
	}
	
	public void refreshTable() {
	    DefaultTableModel newModel = getTable();
	    articlesTable.setModel(newModel);
	    formatTable(articlesTable);
	}
	
	private DefaultTableModel getTable() {
		String[] columnNames = {"#", "Code", "Name", "Manufacturer", "Price (€)", "Image URL"};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        articles = supplierClient.getAllArticles();
        int counter = 1;
        
        for (Article a : articles) {
            Object[] row = {counter++ + ".", a.getCode(), a.getName(), a.getManufacturer(), a.getPrice(), a.getImage()};
            model.addRow(row);
        }
        return model;
	}
	
	private JTable formatTable(JTable articlesTable) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < articlesTable.getColumnCount() - 1; i++) {
			articlesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		// Zero column (Order number)
		TableColumn orderNumberColumn = articlesTable.getColumnModel().getColumn(0);
		orderNumberColumn.setPreferredWidth(35);
		orderNumberColumn.setMaxWidth(40);
		
		// First column (Code)
		TableColumn codeColumn = articlesTable.getColumnModel().getColumn(1);
		codeColumn.setPreferredWidth(100);
		codeColumn.setMinWidth(90);
		codeColumn.setMaxWidth(120);

		// Second column (Name)
		TableColumn nameColumn = articlesTable.getColumnModel().getColumn(2);
		nameColumn.setPreferredWidth(450);
		nameColumn.setMaxWidth(550);

		// Third column (Manufacturer)
		TableColumn manufacturerColumn = articlesTable.getColumnModel().getColumn(3);
		manufacturerColumn.setPreferredWidth(150);
		manufacturerColumn.setMaxWidth(200);

		// Fourth column (Price)
		TableColumn priceColumn = articlesTable.getColumnModel().getColumn(4);
		priceColumn.setPreferredWidth(90);
		priceColumn.setMaxWidth(100);
		
		// Sixth column (Description)
		TableColumn descriptionColumn = articlesTable.getColumnModel().getColumn(5);
		descriptionColumn.setPreferredWidth(120);
		
		
		articlesTable.getTableHeader().setBackground(new Color(0, 120, 215));
		articlesTable.getTableHeader().setForeground(Color.WHITE); 
		articlesTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
		articlesTable.setRowHeight(24);
		articlesTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
		
		return articlesTable;
	}
	
	private JPanel initializeSubmitPanel() {
		JPanel submitPanel = new JPanel(new BorderLayout());
		submitPanel.setBorder(BorderFactory.createTitledBorder("Add new article"));
		submitPanel.setBackground(Color.WHITE);

		
		codeFld = new JTextField(6);
		priceFld = new JTextField(4);
		nameFld = new JTextField(15);
		manufacturerFld = new JTextField(7);
		urlFld = new JTextField(34);
		
		((AbstractDocument) priceFld.getDocument()).setDocumentFilter(new NumericFilter(true));
		
		JPanel first = new JPanel(new FlowLayout(FlowLayout.CENTER, 9, 5));
		first.setBackground(Color.WHITE);
		first.add(new JLabel("Code:"));
		first.add(codeFld);
		first.add(Box.createHorizontalStrut(40));
		
		first.add(new JLabel("Price:"));
		first.add(priceFld);
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
		third1.add(new JLabel("Image URL:"));
		third1.add(urlFld);
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
		    	Article newPart = getEnteredArticle();
		        cleanForm();
		        supplierClient.addArticle(newPart);
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
	
	private void initializeRowClick() {
	    articlesTable.addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override
	        public void mouseClicked(java.awt.event.MouseEvent e) {

	            int row = articlesTable.rowAtPoint(e.getPoint());
	            int col = articlesTable.columnAtPoint(e.getPoint());

	            if (row < 0) return;

	            if (col == 5) {
	                Object value = articlesTable.getValueAt(row, col);

	                if (value != null) {
	                    String url = value.toString();

	                    StringSelection selection = new StringSelection(url);
	                    Toolkit.getDefaultToolkit()
	                           .getSystemClipboard()
	                           .setContents(selection, null);

	                    Point p = e.getLocationOnScreen();
	                    showCopiedPopup("URL copied!", p.x, p.y);
	                }
	                return;
	            }

	            if (row < articles.size()) {
	                Article selected = articles.get(row);
	                fillForm(selected);
	            }
	        }
	    });
	}

	
	private void fillForm(Article a) {
	    codeFld.setText(a.getCode());
	    nameFld.setText(a.getName());
	    manufacturerFld.setText(a.getManufacturer());
	    priceFld.setText(String.valueOf(a.getPrice()));
	    urlFld.setText(a.getImage());
	}
	
	private void cleanForm() {
	    codeFld.setText("");
	    nameFld.setText("");
	    manufacturerFld.setText("");
	    priceFld.setText("");
	    urlFld.setText("");
	}
	
	private Article getEnteredArticle() {
		return new Article(codeFld.getText(), nameFld.getText(), manufacturerFld.getText(), 
				Double.valueOf(priceFld.getText()), urlFld.getText());
	}
	
	private void showCopiedPopup(String text, int x, int y) {
	    JToolTip tip = new JToolTip();
	    tip.setTipText(text);
	    tip.setBackground(new Color(60, 60, 60));
	    tip.setForeground(Color.WHITE);
	    tip.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
	    tip.setFont(new Font("SansSerif", Font.PLAIN, 13));

	    Popup popup = PopupFactory.getSharedInstance()
	            .getPopup(this, tip, x + 10, y + 10);

	    popup.show();
	    new javax.swing.Timer(1500, e -> popup.hide()).start();
	}

}
