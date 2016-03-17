package client;
import shared.Command;
import shared.Request;


import javax.swing.*;
import java.awt.*;


/**
 * Created by Joshua on 3/16/2016.
 */
public class ProfileScreen extends JPanel{

    private JButton closeButton;
    private JLabel genderLabel, countryLabel,winLabel,lossLabel,usernameLabel;
    private GUI gui;
    private String[] userProfile;

    public ProfileScreen(GUI gui,String profileInfo){

        this.userProfile = profileInfo.split(",");
        this.gui = gui;
        this.setBorder(BorderFactory.createTitledBorder("User Profile"));
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        createUsernameLabel();
        createGenderLabel();
        createCountryLabel();
        createWinLabel();
        createLossLabel();
        this.setVisible(true);

        //throw the panel onto a JFrame

        JFrame frame = new JFrame();
        frame.add(this);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(200,200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void createUsernameLabel(){
        usernameLabel = new JLabel(userProfile[0]);
        usernameLabel.setFont(new Font("Serif", Font.BOLD, 22));
        add(usernameLabel);
    }
    private void createGenderLabel()
    {
        genderLabel = new JLabel("Gender: " + userProfile[1]);
        genderLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        add(genderLabel);
    }

    private void createCountryLabel(){
        countryLabel = new JLabel("Country: " + userProfile[2]);
        countryLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        add(countryLabel);
    }

    private void createWinLabel() {
        winLabel = new JLabel("Wins: " + userProfile[3]);
        winLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        add(winLabel);
    }

    private void createLossLabel(){
        lossLabel = new JLabel("Losses: " + userProfile[4]);
        lossLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        add(lossLabel);
    }

}
