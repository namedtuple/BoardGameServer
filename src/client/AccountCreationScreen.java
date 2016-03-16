package client;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AccountCreationScreen extends JPanel implements ActionListener, KeyListener {

	private JButton submitBtn;
	private JTextField usernameField, genderField, countryField;
	private JPasswordField passwordField;
	private JLabel usernameLabel, passwordLabel, genderLabel, countryLabel, programLogo;
    private GUI gui;

	public AccountCreationScreen(GUI gui) {
        this.gui = gui;
		createSubmitButton();
		createUsernameLabel();
		createPasswordLabel();
		createGenderLabel();
		createCountryLabel();
		createUsernameField();
		createPasswordField();
		createGenderField();
		createCountryField();
		createLogo();
		this.setLayout(null);

		this.setSize(500, 240);
		this.setVisible(true);
	}

	private void createSubmitButton(){
		submitBtn = new JButton("Submit");
		submitBtn.addActionListener(this);
		submitBtn.addKeyListener(this);
        submitBtn.setBounds(190,195,100,20);
		this.add(submitBtn);
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
	private void createGenderLabel(){
		genderLabel = new JLabel("Gender: ");
		genderLabel.setBounds(10,150,120,20);
		this.add(genderLabel);
	}

	private void createCountryLabel(){
		countryLabel = new JLabel("Country: ");
		countryLabel.setBounds(10,170,120,20);
		this.add(countryLabel);
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

	private void createGenderField() {
		genderField = new JTextField();
        genderField.addKeyListener(this);
		genderField.setBounds(140, 150, 200, 20);
		this.add(genderField);
	}

	private void createCountryField(){
		countryField = new JTextField();
        countryField.addKeyListener(this);
		countryField.setBounds(140,170,200,20);
		this.add(countryField);
	}

	public String getUsername(){
		return usernameField.getText();
	}

	public String getPassword() {
		return String.valueOf(passwordField.getPassword());
	}

	public String getGender(){
		return usernameField.getText();
	}

	public String getCountry() {
		return String.valueOf(passwordField.getPassword());
	}

    // ActionListener
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		if (button == submitBtn){
            attemptAccountCreation();
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
            attemptAccountCreation();
        }
    }

    // Helper method called by clicking Login button or pressing Enter
    private void attemptAccountCreation() {
        if (!getUsername().equals("") && !getPassword().equals("") && !getGender().equals("") && !getCountry().equals("") ) {
            gui.handleRequest(new Request(Command.CREATING_ACCOUNT, getUsername() + " " + getPassword() + " " + getGender() + " " + getCountry()));
        }
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        genderField.setText("");
        countryField.setText("");
    }
}
