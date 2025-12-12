package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import exceptions.InvalidLoginException;
import models.Client;
import models.Vehicle;
import models.requests.ClientRequest;
import services.AuthService;

public class RegistrationWindow extends JFrame {
    private static final long serialVersionUID = -5469244757196123605L;

    private final int LEFT_SIDE_FIELD_DISTANCE_FROM_PANEL = -30;
    private final int RIGHT_SIDE_FIELD_DISTANCE_FROM_PANEL = 90;
    private final int VERTICAL_SPACE_BETWEEN = 35;
    private final int JTEXT_FIELD_SIZE = 10;
    private final int YEAR_JTEXT_FIELD_SIZE = 4;
    
    private AuthService authService = new AuthService();

    private JPanel panel;

    private JTextField firstNameField, lastNameField;
    private JTextField usernameField;

    private JPasswordField passwordField;

    private JTextField phoneField, emailField;
    private JTextField addressField;

    private JSeparator separator, separatorVehicle;

    private JTextField manufacturerField, modelField;
    private JTextField yearField, plateField;

    private JPanel buttonPanel;
    private JButton registerButton;
    private JButton backButton;


    public RegistrationWindow() {
        super("Account registration");
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(440, 350);
        setResizable(false);
        setLocationRelativeTo(null);

        setPageFont();

        initializeMainPanel();
        initializeButtonPanel();
        attachRegisterAction();
        attachBackAction();

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void initializeMainPanel() {
        panel = new JPanel();

        SpringLayout spring = new SpringLayout();
        panel.setLayout(spring);

        JLabel firstNameLabel = new JLabel("First name");
        firstNameField = new JTextField(JTEXT_FIELD_SIZE);

        JLabel lastNameLabel = new JLabel("Last name ");
        lastNameField = new JTextField(JTEXT_FIELD_SIZE);

        JLabel usernameLabel = new JLabel("Username");
        usernameField = new JTextField(JTEXT_FIELD_SIZE);

        JLabel passwordLabel = new JLabel("Password ");
        passwordField = new JPasswordField(JTEXT_FIELD_SIZE);

        JLabel phoneLabel = new JLabel("Phone");
        phoneField = new JTextField(JTEXT_FIELD_SIZE);

        ((AbstractDocument) phoneField.getDocument()).setDocumentFilter(new DocumentFilter() {
            private final int MAX_LENGTH = 15;

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(currentText).insert(offset, string).toString();

                if (newText.matches("\\+?\\d*") && newText.length() <= MAX_LENGTH) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(currentText).replace(offset, offset + length, text).toString();

                if (newText.matches("\\+?\\d*") && newText.length() <= MAX_LENGTH) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        JLabel emailLabel = new JLabel("E-mail");
        emailField = new JTextField(JTEXT_FIELD_SIZE);

        JLabel addressLabel = new JLabel("Address");
        addressField = new JTextField(JTEXT_FIELD_SIZE);

        JLabel personalInfoLabel = new JLabel("Personal info");
        personalInfoLabel.setFont(new Font("Monospaced", Font.PLAIN, 8));
        separator = new JSeparator(SwingConstants.HORIZONTAL);

        JLabel vehicleInfoLabel = new JLabel("Vehicle info");
        vehicleInfoLabel.setFont(new Font("Monospaced", Font.PLAIN, 8));
        separatorVehicle = new JSeparator(SwingConstants.HORIZONTAL);

        JLabel manufacturerLabel = new JLabel("Manufacturer");
        manufacturerField = new JTextField(JTEXT_FIELD_SIZE);

        JLabel modelLabel = new JLabel("Model");
        modelField = new JTextField(JTEXT_FIELD_SIZE);

        JLabel yearLabel = new JLabel("Year");
        yearField = new JTextField(YEAR_JTEXT_FIELD_SIZE);

        ((AbstractDocument) yearField.getDocument()).setDocumentFilter(new DocumentFilter() {
            private final int MAX_LENGTH = 4;

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d+")) {
                    String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String newText = new StringBuilder(currentText).insert(offset, string).toString();
                    if (newText.length() <= MAX_LENGTH) {
                        super.insertString(fb, offset, string, attr);
                    }
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                if (text.matches("\\d+")) {
                    String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String newText = new StringBuilder(currentText).replace(offset, offset + length, text).toString();
                    if (newText.length() <= MAX_LENGTH) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            }
        });

        JLabel plateLabel = new JLabel("Reg. plate");
        plateField = new JTextField(JTEXT_FIELD_SIZE);


        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(emailLabel);
        panel.add(emailField);

        panel.add(addressLabel);
        panel.add(addressField);

        panel.add(personalInfoLabel);
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));

        panel.add(vehicleInfoLabel);
        panel.add(separatorVehicle);
        panel.add(Box.createVerticalStrut(10));

        panel.add(manufacturerLabel);
        panel.add(manufacturerField);
        panel.add(modelLabel);
        panel.add(modelField);

        panel.add(yearLabel);
        panel.add(yearField);
        panel.add(plateLabel);
        panel.add(plateField);

        // SEPARATOR
        spring.putConstraint(SpringLayout.WEST, personalInfoLabel, 15, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.NORTH, personalInfoLabel, 13, SpringLayout.NORTH, panel);

        spring.putConstraint(SpringLayout.WEST, separator, 85, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.EAST, separator, -15, SpringLayout.EAST, panel);
        spring.putConstraint(SpringLayout.NORTH, separator, 20, SpringLayout.NORTH, panel);

        // FIRST ROW (first name and last name)
        spring.putConstraint(SpringLayout.EAST, firstNameLabel, -6, SpringLayout.WEST, firstNameField);
        spring.putConstraint(SpringLayout.NORTH, firstNameLabel, 10, SpringLayout.SOUTH, separator);

        spring.putConstraint(SpringLayout.WEST, firstNameField, 90, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.NORTH, firstNameField, 10, SpringLayout.SOUTH, separator);

        spring.putConstraint(SpringLayout.EAST, lastNameLabel, -6, SpringLayout.WEST, lastNameField);
        spring.putConstraint(SpringLayout.NORTH, lastNameLabel, 10, SpringLayout.SOUTH, separator);

        spring.putConstraint(SpringLayout.EAST, lastNameField, LEFT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.EAST, panel);
        spring.putConstraint(SpringLayout.NORTH, lastNameField, 10, SpringLayout.SOUTH, separator);


        // SECOND ROW (username and password)
        spring.putConstraint(SpringLayout.EAST, usernameLabel, -6, SpringLayout.WEST, usernameField);
        spring.putConstraint(SpringLayout.NORTH, usernameLabel, 35, SpringLayout.NORTH, firstNameLabel);

        spring.putConstraint(SpringLayout.WEST, usernameField, RIGHT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.NORTH, usernameField, VERTICAL_SPACE_BETWEEN, SpringLayout.NORTH, firstNameField);

        spring.putConstraint(SpringLayout.EAST, passwordLabel, -6, SpringLayout.WEST, passwordField);
        spring.putConstraint(SpringLayout.NORTH, passwordLabel, VERTICAL_SPACE_BETWEEN, SpringLayout.NORTH, lastNameLabel);

        spring.putConstraint(SpringLayout.EAST, passwordField, LEFT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.EAST, panel);
        spring.putConstraint(SpringLayout.NORTH, passwordField, VERTICAL_SPACE_BETWEEN, SpringLayout.NORTH, lastNameField);


        // THIRD ROW (phone and email)
        spring.putConstraint(SpringLayout.EAST, phoneLabel, -6, SpringLayout.WEST, phoneField);
        spring.putConstraint(SpringLayout.NORTH, phoneLabel, 35, SpringLayout.NORTH, usernameLabel);

        spring.putConstraint(SpringLayout.WEST, phoneField, RIGHT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.NORTH, phoneField, 35, SpringLayout.NORTH, usernameField);

        spring.putConstraint(SpringLayout.EAST, emailLabel, -6, SpringLayout.WEST, passwordField);
        spring.putConstraint(SpringLayout.NORTH, emailLabel, VERTICAL_SPACE_BETWEEN, SpringLayout.NORTH, passwordLabel);

        spring.putConstraint(SpringLayout.EAST, emailField, LEFT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.EAST, panel);
        spring.putConstraint(SpringLayout.NORTH, emailField, VERTICAL_SPACE_BETWEEN, SpringLayout.NORTH, passwordField);

        // FOURTH ROW (address)
        spring.putConstraint(SpringLayout.EAST, addressLabel, -6, SpringLayout.WEST, addressField);
        spring.putConstraint(SpringLayout.NORTH, addressLabel, 35, SpringLayout.NORTH, emailLabel);

        spring.putConstraint(SpringLayout.WEST, addressField, RIGHT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.EAST, addressField, LEFT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.EAST, panel);
        spring.putConstraint(SpringLayout.NORTH, addressField, VERTICAL_SPACE_BETWEEN, SpringLayout.NORTH, emailField);

        // VEHICLE SEPARATOR
        spring.putConstraint(SpringLayout.WEST, vehicleInfoLabel, 15, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.NORTH, vehicleInfoLabel, 13, SpringLayout.SOUTH, addressField);

        spring.putConstraint(SpringLayout.WEST, separatorVehicle, 85, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.EAST, separatorVehicle, -15, SpringLayout.EAST, panel);
        spring.putConstraint(SpringLayout.NORTH, separatorVehicle, 20, SpringLayout.SOUTH, addressField);

        // FIRST ROW (manufacturer and model)
        spring.putConstraint(SpringLayout.EAST, manufacturerLabel, -6, SpringLayout.WEST, manufacturerField);
        spring.putConstraint(SpringLayout.NORTH, manufacturerLabel, 10, SpringLayout.NORTH, separatorVehicle);

        spring.putConstraint(SpringLayout.WEST, manufacturerField, RIGHT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.NORTH, manufacturerField, 10, SpringLayout.NORTH, separatorVehicle);

        spring.putConstraint(SpringLayout.EAST, modelLabel, -6, SpringLayout.WEST, modelField);
        spring.putConstraint(SpringLayout.NORTH, modelLabel, 10, SpringLayout.NORTH, separatorVehicle);

        spring.putConstraint(SpringLayout.EAST, modelField, LEFT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.EAST, panel);
        spring.putConstraint(SpringLayout.NORTH, modelField, 10, SpringLayout.NORTH, separatorVehicle);

        // SECOND ROW (year and registration plate)
        spring.putConstraint(SpringLayout.EAST, yearLabel, -6, SpringLayout.WEST, yearField);
        spring.putConstraint(SpringLayout.NORTH, yearLabel, 35, SpringLayout.NORTH, modelField);

        spring.putConstraint(SpringLayout.WEST, yearField, RIGHT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.NORTH, yearField, VERTICAL_SPACE_BETWEEN, SpringLayout.NORTH, modelField);

        spring.putConstraint(SpringLayout.EAST, plateLabel, -6, SpringLayout.WEST, plateField);
        spring.putConstraint(SpringLayout.NORTH, plateLabel, VERTICAL_SPACE_BETWEEN, SpringLayout.NORTH, modelField);

        spring.putConstraint(SpringLayout.EAST, plateField, LEFT_SIDE_FIELD_DISTANCE_FROM_PANEL, SpringLayout.EAST, panel);
        spring.putConstraint(SpringLayout.NORTH, plateField, VERTICAL_SPACE_BETWEEN, SpringLayout.NORTH, modelField);
    }

    private void initializeButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        registerButton = new JButton("Register");
        backButton = new JButton("   Back   ");
        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);
    }

    private void setPageFont() {
        Font defaultLabelFont = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Label.font", defaultLabelFont);
    }
    
    private void attachRegisterAction() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientRequest request = new ClientRequest(
                        firstNameField.getText(),
                        lastNameField.getText(),
                        usernameField.getText(),
                        new String(passwordField.getPassword()),
                        addressField.getText(),
                        phoneField.getText(),
                        emailField.getText()
                );
                Vehicle vehicleRequest = new Vehicle(
                		manufacturerField.getText(), 
                		modelField.getText(), 
                		Integer.valueOf(yearField.getText()), 
                		plateField.getText()
                );

                Client client;
				try {
					client = authService.registration(request, vehicleRequest);
					if (client != null) {
						new SignInWindow();
		                RegistrationWindow.this.dispose();
		                JOptionPane.showMessageDialog(
		                	    RegistrationWindow.this,
		                	    "Registration completed successfully. Servicer needs to confirm registration.",
		                	    "Success",
		                	    JOptionPane.INFORMATION_MESSAGE
		                	);
	                } else {
	                    JOptionPane.showMessageDialog(RegistrationWindow.this,
	                            "Registracija failed!", "Error", JOptionPane.ERROR_MESSAGE);
	                }
				} catch (InvalidLoginException e1) {
					e1.printStackTrace();
				}
            }
        });
    }
    
    private void attachBackAction() {
    	backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	new SignInWindow();
                RegistrationWindow.this.dispose();
            }
        });
    }
}
