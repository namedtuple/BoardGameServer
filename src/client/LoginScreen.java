package client;
import shared.Command;
import shared.Request;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginScreen extends JPanel implements ActionListener, KeyListener {

	private JButton loginBtn, accountCreateBtn;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JLabel usernameLabel, passwordLabel, programLogo;
    private GUI gui;

	public LoginScreen(GUI gui) {
        this.gui = gui;
		createLoginButton();
		createAccountButton();
		createUsernameLabel();
		createPasswordLabel();
		createUsernameField();
		createPasswordField();
		createLogo();
		this.setLayout(null);

		this.setSize(500, 240);
		this.setVisible(true);
	}

	private void createLoginButton(){
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		loginBtn.addKeyListener(this);
        loginBtn.setBounds(170,155,140,20);
		this.add(loginBtn);
	}

	private void createAccountButton(){
		accountCreateBtn = new JButton("Create Account");
		accountCreateBtn.addActionListener(this);
		accountCreateBtn.addKeyListener(this);
		accountCreateBtn.setBounds(170,175,140,20);
		this.add(accountCreateBtn);
	}

	private void createLogo(){
		try {
			BufferedImage logoImage = ImageIO.read(new File("img/logo.png"));
			programLogo = new JLabel();
			programLogo.setBounds(190, 10, 100, 100);
			Image scaledImage = logoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			programLogo.setIcon(new ImageIcon(scaledImage));
			this.add(programLogo);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createUsernameLabel(){
		usernameLabel = new JLabel("Username: ");
		usernameLabel.setBounds(10,110,120,20);
		this.add(usernameLabel);
	}

	private void createPasswordLabel(){
		passwordLabel = new JLabel("Password: ");
		passwordLabel.setBounds(10,130,120,20);
		this.add(passwordLabel);
	}

	private void createUsernameField() {
		usernameField = new JTextField();
        usernameField.addKeyListener(this);
		usernameField.setBounds(140, 110, 200, 20);
		this.add(usernameField);
	}

	private void createPasswordField(){
		passwordField = new JPasswordField();
        passwordField.addKeyListener(this);
		passwordField.setBounds(140,130,200,20);
		this.add(passwordField);
	}


	private String getUsername(){
		return usernameField.getText();
	}

	private String getPassword() {
		return String.valueOf(passwordField.getPassword());
	}

    // ActionListener
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		if (button == loginBtn){
            attemptLogin();
		}
		if (button == accountCreateBtn){
            gui.handleRequest(new Request(Command.ACCOUNT_CREATION));
		}
	}

    // KeyListener
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER) {
            attemptLogin();
        }
    }

    // Helper method called by clicking Login button or pressing Enter
    private void attemptLogin() {
        if (!getUsername().equals("") && !getPassword().equals("")) {
            handleRequest(new Request(Command.LOGGING_IN, getUsername() + " " + getPassword()));
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public void handleRequest(Request request) {
        Command command = request.getCommand();
        switch(command) {
            case LOGGING_IN: case ACCOUNT_CREATION:
                gui.handleRequest(request);
                break;
            case LOGIN_SUCCESS:
                clearFields();
                break;
            default:
                break;
        }
    }

}
