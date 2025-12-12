package gui;

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

import enums.Screen;


public class MainWindow extends JFrame {
	private static final long serialVersionUID = 4919451771802939504L;

	private JPanel currentPanel;

	private JLabel clientsLink;
	private JLabel appointmentsLink;
	private JLabel sparePartsLink;
	private JLabel exitLink;
	
	private ClientsPanel clientsPanel;
	private AppointmentsPanel appointmentsPanel;
	private SparePartsPanel sparePartsPanel;
	
	private static final String clientsLinkName = "Clients";
	private static final String appointmentsLinkName = "Appointments ";
	private static final String sparePartsLinkName = "Spare Parts ";
	
	
	public MainWindow() {
		setLayout(new BorderLayout());
		
		clientsPanel = new ClientsPanel();
		appointmentsPanel = new AppointmentsPanel();
		sparePartsPanel = new SparePartsPanel();
		
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null);

	    add(createHeader(), BorderLayout.NORTH);
	    currentPanel = clientsPanel;
	    add(currentPanel, BorderLayout.CENTER);
	    
		setVisible(true);
	}
	
	private JPanel createHeader() {
		JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 55, 35));
		setPageFont();	//ili setHeaderFont()?
		header.setBackground(Color.WHITE);

		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));//Color.BLUE

		
		initializeLinks();
		
		attachAppointmentsAction();
		attachClientsAction();
		attachSparePartsAction();
		attachExitAction();
		
		header.add(clientsLink);
		header.add(appointmentsLink);
		header.add(sparePartsLink);
		header.add(exitLink);
		
		return header;
	}
	
	private void initializeLinks() {
		ImageIcon clientsIcon = new ImageIcon("icons\\icons8-clients-36.png");
		clientsLink = new JLabel(clientsLinkName, clientsIcon, JLabel.LEFT);
		clientsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon appointmentsIcon = new ImageIcon("icons\\icons8-appointments-36.png");
		appointmentsLink = new JLabel(appointmentsLinkName, appointmentsIcon, JLabel.LEFT);
		appointmentsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon sparePartsIcon = new ImageIcon("icons\\icons8-gear-36.png");
		sparePartsLink = new JLabel(sparePartsLinkName, sparePartsIcon, JLabel.LEFT);
		sparePartsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon exitIcon = new ImageIcon("icons\\icons8-exit-36.png");
		exitLink = new JLabel("Exit", exitIcon, JLabel.LEFT);
		exitLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		underlineSelectedLink(Screen.CLIENTS);
	}
	
	private void attachClientsAction() {
		clientsLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	clientsPanel.refreshTable();
            	underlineSelectedLink(Screen.CLIENTS);
                changeCurrentScreen(Screen.CLIENTS);
            }
        });
    }
	
	private void attachAppointmentsAction() {
		appointmentsLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	appointmentsPanel.refreshTable();
            	underlineSelectedLink(Screen.APPOINTMENTS);
                changeCurrentScreen(Screen.APPOINTMENTS);
            }
        });
    }
	
	private void attachSparePartsAction() {
		sparePartsLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	sparePartsPanel.refreshTable();
            	underlineSelectedLink(Screen.SPARE_PARTS);
                changeCurrentScreen(Screen.SPARE_PARTS);
            }
        });
    }
	
	private void attachExitAction() {
		exitLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindow.this.dispose();
            }
        });
    }
	
	private void changeCurrentScreen(Screen screen) {
		JPanel newCurrentPanel;
		if(screen == Screen.APPOINTMENTS)
			newCurrentPanel = appointmentsPanel;
		else if(screen == Screen.CLIENTS)
			newCurrentPanel = clientsPanel;
		else
			newCurrentPanel = sparePartsPanel;
		
		remove(currentPanel);
		
		currentPanel = newCurrentPanel;
		add(currentPanel, BorderLayout.CENTER);
		
		currentPanel.revalidate();
		currentPanel.repaint();
	}
	
	private void underlineSelectedLink(Screen selected) {
		deselectAllLinks();
		if(Screen.CLIENTS == selected) {
			clientsLink.setForeground(Color.BLUE);
			clientsLink.setText("<html><u>" + clientsLinkName + "</u></html>");
		}
		else if(Screen.APPOINTMENTS == selected) {
			appointmentsLink.setForeground(Color.BLUE);
			appointmentsLink.setText("<html><u>" + appointmentsLinkName + "</u></html>");
		}
		else if(Screen.SPARE_PARTS == selected) {
			sparePartsLink.setForeground(Color.BLUE);
			sparePartsLink.setText("<html><u>" + sparePartsLinkName + "</u></html>");
		}
	}
	
	private void deselectAllLinks() {
		appointmentsLink.setForeground(Color.BLACK);
		appointmentsLink.setText(appointmentsLinkName);
		clientsLink.setForeground(Color.BLACK);
		clientsLink.setText(clientsLinkName);
		sparePartsLink.setForeground(Color.BLACK);
		sparePartsLink.setText(sparePartsLinkName);
	}

	private void setPageFont() {
        Font defaultLabelFont = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Label.font", defaultLabelFont);
    }
}
