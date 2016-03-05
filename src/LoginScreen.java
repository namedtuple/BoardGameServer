import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginScreen extends JFrame implements ActionListener{

	private JButton loginBtn;
	private JTextField usernameField;
	private JPasswordField passwordField;
	JLabel usernameLabel, passwordLabel, programLogo;
	
	public LoginScreen(String name){
		super(name);
		createLoginButton();
		createUsernameLabel();
		createPasswordLabel();
		createUsernameField();
		createPasswordField();
		createLogo();
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(500, 240);
		this.setVisible(true);
	}

	public void createLoginButton(){
		loginBtn = new JButton("Login");	
		loginBtn.addActionListener(this);
		loginBtn.setBounds(190,155,100,20);
		this.add(loginBtn);
	}
	
	public void createLogo(){
		try {
			BufferedImage logoImage = ImageIO.read(new File("img/logo.png"));
			programLogo = new JLabel();
			programLogo.setBounds(190, 10, 100, 100);
			Image scaledImage = logoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			programLogo.setIcon(new ImageIcon(scaledImage));
			this.add(programLogo);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createUsernameLabel(){
		usernameLabel = new JLabel("Username: ");
		usernameLabel.setBounds(10,110,120,20);
		this.add(usernameLabel);
	}
	
	public void createPasswordLabel(){
		passwordLabel = new JLabel("Password: ");
		passwordLabel.setBounds(10,130,120,20);
		this.add(passwordLabel);
	}
	
	public void createUsernameField(){
		usernameField = new JTextField();
		usernameField.setBounds(140, 110, 200, 20);
		this.add(usernameField);
	}
	
	public void createPasswordField(){
		passwordField = new JPasswordField();
		passwordField.setBounds(140,130,200,20);	
		this.add(passwordField);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		if(button == loginBtn){
			//insert logic for button press
		}
		
	}
}
