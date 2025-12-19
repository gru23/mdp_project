package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import chat.GroupChatReceiver;
import chat.UnicastChatReceiver;
import chat.UnicastChatSender;
import enums.Screen;


public class MainWindow extends JFrame {
	private static final long serialVersionUID = 4919451771802939504L;

	private JPanel currentPanel;

	private JLabel clientsLink;
	private JLabel chatLink;
	private JLabel appointmentsLink;
	private JLabel sparePartsLink;
	private JLabel ordersLink;
	private JLabel exitLink;
	
	private ClientsPanel clientsPanel;
	private ChatPanel chatPanel;
	private AppointmentsPanel appointmentsPanel;
	private SparePartsPanel sparePartsPanel;
	private OrdersPanel ordersPanel;
	
	private static final String clientsLinkName = "Clients";
	private static final String chatLinkName = "Chat";
	private static final String appointmentsLinkName = "Appointments ";
	private static final String sparePartsLinkName = "Spare Parts ";
	private static final String ordersLinkName = "Orders ";
	
	
	public MainWindow() {
		setLayout(new BorderLayout());
		
		try {
			InetAddress addr = InetAddress.getByName("127.0.0.1");
	        Socket sock = new Socket(addr, 15000);//new Socket(addr, TCP_PORT);
	        

			PrintWriter out = new PrintWriter(
			    new BufferedWriter(
			        new OutputStreamWriter(sock.getOutputStream())
			    ), true
			);
			
			// HANDSHAKE – SAMO JEDNOM
			out.println("MDP Servicer");
	        
	        UnicastChatReceiver ucr = new UnicastChatReceiver(sock);
			GroupChatReceiver gcr = new GroupChatReceiver("MDP Servicer");
			
			clientsPanel = new ClientsPanel();
			chatPanel = new ChatPanel(initializeUnicastSender());
			appointmentsPanel = new AppointmentsPanel();
			sparePartsPanel = new SparePartsPanel();
			ordersPanel = new OrdersPanel();
			
			ucr.setChatPanel(chatPanel);
			gcr.setChatPanel(chatPanel);
			ucr.start();
			gcr.start();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		setSize(900, 600);
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
		attachChatAction();
		attachSparePartsAction();
		attachOrdersAction();
		attachExitAction();
		
		header.add(clientsLink);
		header.add(chatLink);
		header.add(appointmentsLink);
		header.add(sparePartsLink);
		header.add(ordersLink);
		header.add(exitLink);
		
		return header;
	}
	
	private void initializeLinks() {
		ImageIcon clientsIcon = new ImageIcon("icons\\icons8-clients-36.png");
		clientsLink = new JLabel(clientsLinkName, clientsIcon, JLabel.LEFT);
		clientsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon chatIcon = new ImageIcon("icons\\icons8-chat-36.png");
		chatLink = new JLabel(chatLinkName, chatIcon, JLabel.LEFT);
		chatLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon appointmentsIcon = new ImageIcon("icons\\icons8-appointments-36.png");
		appointmentsLink = new JLabel(appointmentsLinkName, appointmentsIcon, JLabel.LEFT);
		appointmentsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon sparePartsIcon = new ImageIcon("icons\\icons8-gear-36.png");
		sparePartsLink = new JLabel(sparePartsLinkName, sparePartsIcon, JLabel.LEFT);
		sparePartsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon ordersIcon = new ImageIcon("icons\\icons8-order-36.png");
		ordersLink = new JLabel(ordersLinkName, ordersIcon, JLabel.LEFT);
		ordersLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		ImageIcon exitIcon = new ImageIcon("icons\\icons8-exit-36.png");
		exitLink = new JLabel("Exit", exitIcon, JLabel.LEFT);
		exitLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		underlineSelectedLink(Screen.CLIENTS);
	}
	
	private UnicastChatSender initializeUnicastSender() {
	    final int TCP_PORT = 15000;
	    UnicastChatSender sender = null;

	    try {
	        InetAddress addr = InetAddress.getByName("127.0.0.1");
	        Socket sock = new Socket(addr, TCP_PORT);
	        sender = new UnicastChatSender(sock, "MDP Servicer");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return sender;
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
	
	private void attachChatAction() {
		chatLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	underlineSelectedLink(Screen.CHAT);
                changeCurrentScreen(Screen.CHAT);
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
	
	private void attachOrdersAction() {
		ordersLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	ordersPanel.refreshTable();
            	underlineSelectedLink(Screen.ORDERS);
                changeCurrentScreen(Screen.ORDERS);
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
		else if(screen == Screen.CHAT)
			newCurrentPanel = chatPanel;
		else if(screen == Screen.CLIENTS)
			newCurrentPanel = clientsPanel;
		else if(screen == Screen.SPARE_PARTS)
			newCurrentPanel = sparePartsPanel;
		else 
			newCurrentPanel = ordersPanel;
		
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
		else if(Screen.CHAT == selected) {
			chatLink.setForeground(Color.BLUE);
			chatLink.setText("<html><u>" + chatLinkName + "</u></html>");
		}
		else if(Screen.APPOINTMENTS == selected) {
			appointmentsLink.setForeground(Color.BLUE);
			appointmentsLink.setText("<html><u>" + appointmentsLinkName + "</u></html>");
		}
		else if(Screen.SPARE_PARTS == selected) {
			sparePartsLink.setForeground(Color.BLUE);
			sparePartsLink.setText("<html><u>" + sparePartsLinkName + "</u></html>");
		}
		else if(Screen.ORDERS == selected) {
			ordersLink.setForeground(Color.BLUE);
			ordersLink.setText("<html><u>" + ordersLinkName + "</u></html>");
		}
	}
	
	private void deselectAllLinks() {
		appointmentsLink.setForeground(Color.BLACK);
		appointmentsLink.setText(appointmentsLinkName);
		clientsLink.setForeground(Color.BLACK);
		clientsLink.setText(clientsLinkName);
		chatLink.setForeground(Color.BLACK);
		chatLink.setText(chatLinkName);
		sparePartsLink.setForeground(Color.BLACK);
		sparePartsLink.setText(sparePartsLinkName);
		ordersLink.setForeground(Color.BLACK);
		ordersLink.setText(ordersLinkName);
	}

	private void setPageFont() {
        Font defaultLabelFont = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Label.font", defaultLabelFont);
    }
}
