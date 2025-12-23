package org.unibl.etf.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.unibl.etf.client.enums.Screen;
import org.unibl.etf.client.sockets.SupplierClient;
import org.unibl.etf.utils.AppSession;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 4919451771802939504L;
	
	private SupplierClient supplierClient;

	private JPanel currentPanel;

	private JLabel sparePartsLink;
	private JLabel ordersLink;
	private JLabel exitLink;
	
	private ArticlesPanel articlesPanel;
	private OrdersPanel ordersPanel;
	
	private static final String articlesLinkName = "Articles ";
	private static final String ordersLinkName = "Orders ";
	
	
	public MainWindow(SupplierClient supplierClient) {
		setLayout(new BorderLayout());
		this.supplierClient = supplierClient;
		
		String title = AppSession.getInstance().getSupplierName();
		setTitle(title);
	    articlesPanel = new ArticlesPanel(supplierClient);
	    ordersPanel = new OrdersPanel(supplierClient);
	    supplierClient.setArticlesPanel(articlesPanel);
	    supplierClient.setOrdersPanel(ordersPanel);
		
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null);

	    add(createHeader(), BorderLayout.NORTH);
	    currentPanel = articlesPanel;
	    add(currentPanel, BorderLayout.CENTER);
	    
		setVisible(true);
		
		supplierClient.startListening();
	}
	
	private JPanel createHeader() {
		JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 55, 35));
		setPageFont();	//ili setHeaderFont()?
		header.setBackground(Color.WHITE);

		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));//Color.BLUE

		
		initializeLinks();
		
		attachSparePartsAction();
		attachOrdersAction();
		attachExitAction();
		
		header.add(sparePartsLink);
		header.add(ordersLink);
		header.add(exitLink);
		
		return header;
	}
	
	private void initializeLinks() {
		ImageIcon sparePartsIcon = new ImageIcon("icons\\icons8-gear-36.png");
		sparePartsLink = new JLabel(articlesLinkName, sparePartsIcon, JLabel.LEFT);
		sparePartsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon ordersIcon = new ImageIcon("icons\\icons8-order-36.png");
		ordersLink = new JLabel(ordersLinkName, ordersIcon, JLabel.LEFT);
		ordersLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon exitIcon = new ImageIcon("icons\\icons8-exit-36.png");
		exitLink = new JLabel("Exit", exitIcon, JLabel.LEFT);
		exitLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		underlineSelectedLink(Screen.SPARE_PARTS);
	}
	
	private void attachSparePartsAction() {
		sparePartsLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//            	articlesPanel.refreshTable();
            	supplierClient.getAllArticles();
            	underlineSelectedLink(Screen.SPARE_PARTS);
                changeCurrentScreen(Screen.SPARE_PARTS);
            }
        });
    }
	
	private void attachOrdersAction() {
		ordersLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//ordersPanel.refreshTable();
				underlineSelectedLink(Screen.ORDERS);
				changeCurrentScreen(Screen.ORDERS);
			}
		});
	}
	
	private void attachExitAction() {
		exitLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//            	supplierClient.shutdown();
            	System.exit(0);
//                MainWindow.this.dispose();
            }
        });
    }
	
	private void changeCurrentScreen(Screen screen) {
		JPanel newCurrentPanel;
		if(screen == Screen.ORDERS)
			newCurrentPanel = ordersPanel;
		else
			newCurrentPanel = articlesPanel;
		
		remove(currentPanel);
		
		currentPanel = newCurrentPanel;
		add(currentPanel, BorderLayout.CENTER);
		
		currentPanel.revalidate();
		currentPanel.repaint();
	}
	
	private void underlineSelectedLink(Screen selected) {
		deselectAllLinks();
		if(Screen.ORDERS == selected) {
			ordersLink.setForeground(Color.BLUE);
			ordersLink.setText("<html><u>" + ordersLinkName + "</u></html>");
		}
		else if(Screen.SPARE_PARTS == selected) {
			sparePartsLink.setForeground(Color.BLUE);
			sparePartsLink.setText("<html><u>" + articlesLinkName + "</u></html>");
		}
	}
	
	private void deselectAllLinks() {
		sparePartsLink.setForeground(Color.BLACK);
		sparePartsLink.setText(articlesLinkName);
		ordersLink.setForeground(Color.BLACK);
		ordersLink.setText(ordersLinkName);
	}

	private void setPageFont() {
        Font defaultLabelFont = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Label.font", defaultLabelFont);
    }
}
