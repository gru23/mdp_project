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
import utils.AppSession;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 4919451771802939504L;

	private JPanel currentPanel;

	private JLabel appointmentsLink;
	private JLabel chatLink;
	private JLabel logoutLink;
	
	private AppointmentsPanel appointmentsPanel;
	private ChatPanel chatPanel;
	
	public MainWindow() {
		setLayout(new BorderLayout());
		setTitle(AppSession.getInstance().getCurrentClient().getUsername());
		
		appointmentsPanel = new AppointmentsPanel();
		chatPanel = new ChatPanel();
		
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null);

	    add(createHeader(), BorderLayout.NORTH);
	    currentPanel = appointmentsPanel;
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
		attachChatAction();
		attachLogoutAction();
		
		header.add(appointmentsLink);
		header.add(chatLink);
		header.add(logoutLink);
		
		return header;
	}
	
	private void initializeLinks() {
		ImageIcon appointmentsIcon = new ImageIcon("icons\\icons8-car-service-36.png");
		appointmentsLink = new JLabel("Appointments", appointmentsIcon, JLabel.LEFT);
		appointmentsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon chatIcon = new ImageIcon("icons\\icons8-chat-36.png");
		chatLink = new JLabel("Chat ", chatIcon, JLabel.LEFT);
		chatLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon logoutIcon = new ImageIcon("icons\\icons8-logout-36.png");
		logoutLink = new JLabel("Logout ", logoutIcon, JLabel.LEFT);
		logoutLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		underlineSelecteLink(Screen.APPOINTMENTS);
	}
	
	private void attachAppointmentsAction() {
		appointmentsLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	appointmentsPanel.refreshTable();
            	underlineSelecteLink(Screen.APPOINTMENTS);
                changeCurrentScreen(Screen.APPOINTMENTS);
            }
        });
    }
	
	private void attachChatAction() {
		chatLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	underlineSelecteLink(Screen.CHAT);
                changeCurrentScreen(Screen.CHAT);
            }
        });
    }
	
	private void attachLogoutAction() {
		logoutLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	AppSession.getInstance().logout();
                new SignInWindow();
                MainWindow.this.dispose();
            }
        });
    }
	
	private void changeCurrentScreen(Screen screen) {
		JPanel newCurrentPanel = Screen.APPOINTMENTS == screen ? appointmentsPanel : chatPanel;
		remove(currentPanel);
		
		currentPanel = newCurrentPanel;
		add(currentPanel, BorderLayout.CENTER);
		
		currentPanel.revalidate();
		currentPanel.repaint();
	}
	
	private void setPageFont() {
        Font defaultLabelFont = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Label.font", defaultLabelFont);
    }
	
	private void underlineSelecteLink(Screen selected) {
		if(Screen.APPOINTMENTS == selected) {
			appointmentsLink.setForeground(Color.BLUE);
			appointmentsLink.setText("<html><u>Appointments </u></html>");
			chatLink.setForeground(Color.BLACK);
			chatLink.setText("Chat ");
		}
		else if(Screen.CHAT == selected) {
			appointmentsLink.setForeground(Color.BLACK);
			appointmentsLink.setText("Appointments ");
			chatLink.setForeground(Color.BLUE);
			chatLink.setText("<html><u>Chat </u></html>");
		}
	}

}
