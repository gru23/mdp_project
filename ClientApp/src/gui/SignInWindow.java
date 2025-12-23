package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import exceptions.InvalidLoginException;
import services.AuthService;

public class SignInWindow extends JFrame {
	private static final long serialVersionUID = -9822020554247709L;
	
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JLabel registrationLink;
	private JButton loginButton;
	
	private AuthService service;
	
	
	public SignInWindow() {
		super("Sign in");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(300, 200);
	    setLocationRelativeTo(null);
	    setResizable(false);
	    setPageFont();
	    
	    service = new AuthService();
	    initializeRegistrationLink();

	    JPanel panel = new JPanel(new GridBagLayout());
	    Insets left = new Insets(5, 40, 5, 0);
	    Insets right = new Insets(5, 5, 5, 40);
	     
	    GridBagConstraints gbc = new GridBagConstraints();
	    
	    gbc.insets = left; 
	    gbc.anchor = GridBagConstraints.WEST; 
	    gbc.fill = GridBagConstraints.HORIZONTAL; 
	     
	    // Username label
	    gbc.gridx = 0; 
	    gbc.gridy = 0; 
	    panel.add(new JLabel("Username"), gbc); 
	    
	    // Username field
	    gbc.gridx = 1; 
	    gbc.gridy = 0; 
	    gbc.weightx = 1; 
	    gbc.insets = right;
	    usernameField = new JTextField(12);
	    panel.add(usernameField, gbc); 
	    gbc.weightx = 0; 
	    
	    // Password label
	    gbc.gridx = 0; 
	    gbc.gridy = 1; 
	    gbc.insets = left;
	    panel.add(new JLabel("Password"), gbc); 
	    
	    // Password field
	    gbc.gridx = 1; 
	    gbc.gridy = 1; 
	    gbc.weightx = 1; 
	    gbc.insets = right;
	    passwordField = new JPasswordField(12);
	    panel.add(passwordField, gbc);
	    
	    gbc.gridx = 1;
	    gbc.gridy = 2;
	    gbc.weightx = 1;
	    gbc.insets = right;
	    gbc.fill = GridBagConstraints.NONE; 
	    gbc.anchor = GridBagConstraints.EAST;
	    panel.add(registrationLink, gbc);
	    
	    // Login button
	    loginButton = new JButton("Login");

	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gbc.gridwidth = 2;
	    gbc.insets = new Insets(10, 0, 0, 0);
	    gbc.anchor = GridBagConstraints.CENTER; 
	    gbc.fill = GridBagConstraints.NONE;

	    panel.add(loginButton, gbc);
	    
	    
	    attachLoginAction();
	    attachRegistrationAction();
	    add(panel);
	    setVisible(true);
	}
	
	private void setPageFont() {
        Font defaultLabelFont = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Label.font", defaultLabelFont);
    }
	
	private void attachLoginAction() {
        loginButton.addActionListener(e -> {
            try {
                service.login(getUsername(), getPassword());
                new MainWindow();
                SignInWindow.this.dispose();
            } catch (InvalidLoginException ex) {
            	usernameField.setText("");
            	passwordField.setText("");
                String errorMessage = ex.getMessage().split(":")[1];
                JOptionPane.showMessageDialog(
                        this,
                        errorMessage,
                        "Invalid Login",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
	
	private void attachRegistrationAction() {
		registrationLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegistrationWindow();
                SignInWindow.this.dispose();
            }
        });
    }
	
	private void initializeRegistrationLink() {
		registrationLink = new JLabel("Registration");
        registrationLink.setForeground(Color.BLUE.darker());
        registrationLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registrationLink.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	private String getUsername() {
        return usernameField.getText();
    }

    private String getPassword() {
        return new String(passwordField.getPassword());
    }
}
