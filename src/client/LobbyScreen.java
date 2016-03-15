package client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LobbyScreen extends JPanel implements ActionListener{


	//method variables
	private JComboBox gameSelection;
	private String currentGameSelection;
    private HashMap<String, List<String>> lobbyMap;
	private String[] gameList = {"Tic-Tac-Toe", "Chutes-and-Ladders", "Checkers"};
	private JButton logoutButton, challengeOpponentButton;
	private JPanel selectionPanel;
	private DefaultListModel waitList; //changes made to this will update GUI waiting list
	private JList uiList;
    private GUI gui;
    private String username;

	public LobbyScreen(GUI gui)
	{
		super(new BorderLayout());
        this.gui = gui;

        lobbyMap = new HashMap<>();
        lobbyMap.put("Tic-Tac-Toe", new ArrayList<>());
        lobbyMap.put("Chutes-and-Ladders", new ArrayList<>());
        lobbyMap.put("Checkers", new ArrayList<>());

		waitList = new DefaultListModel();
        setVisible(false);

		createSelectionPanel();
		createJoinGameButton();
		createUIList();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(uiList);

		add(selectionPanel,BorderLayout.NORTH);
		add(scrollPane,BorderLayout.CENTER);
		add(challengeOpponentButton,BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == gameSelection)
		{
            if (this.isVisible()) {
                System.out.println("ABOUT TO CALL LobbyScreen.requestWaitlist() from actionPerformed()");
                requestWaitlist();
            }
        }
		else if(e.getSource() == logoutButton)
		{
			//create a new game instance depending on what game is selected
            gui.handleRequest(new Request(Command.LOGOUT));
            gui.handleRequest(new Request(Command.DISCONNECTED));
		}
		else if(e.getSource() == challengeOpponentButton)
		{
            String userToJoin = (String) uiList.getSelectedValue();
			//join an opponents game based on what opponent is chosen
            gui.handleRequest(new Request(Command.JOIN, userToJoin));
		}

	}

	public void createUIList(){
		uiList = new JList(waitList);
        uiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        uiList.addListSelectionListener(new playerSelectionListener());
        uiList.setVisibleRowCount(5);
        uiList.setFixedCellHeight(44);
	}

	public void createJoinGameButton(){
		challengeOpponentButton = new JButton("Challenge Opponent");
		challengeOpponentButton.addActionListener(this);
		challengeOpponentButton.setEnabled(false);
	}

	public void createSelectionPanel(){
		selectionPanel = new JPanel();
		selectionPanel.setLayout(new BoxLayout(selectionPanel,BoxLayout.LINE_AXIS));
		createGameSelectionBox();
		selectionPanel.add(Box.createHorizontalStrut(5));
		selectionPanel.add(Box.createHorizontalStrut(5));
		createnewGameButton();
		selectionPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}

	public void createGameSelectionBox(){
		gameSelection = new JComboBox(gameList);
		gameSelection.addActionListener(this);
		gameSelection.setSelectedIndex(0);
		currentGameSelection = gameList[0];
		selectionPanel.add(gameSelection);
	}

	public void createnewGameButton(){
		logoutButton = new JButton("Log out");
		logoutButton.addActionListener(this);
		selectionPanel.add(logoutButton);
	}

	//call this to add a player to the lobby
	public void addToWaitList(String player){
		waitList.addElement(player);
	}

    public void addAllToWaitList(String request) {

        String[] splitMsg = request.split(" ");

        // update (model) list first:
        String gameName = splitMsg[1];
        List<String> lobbyList = lobbyMap.get(gameName);
        lobbyList.clear();
        for (int i=2; i<splitMsg.length; ++i) {
            lobbyList.add(splitMsg[i]);
        }

        Collections.sort(lobbyList);


        // Now update the GUI list if necessary.
        if (gameName.equals(currentGameSelection)) {
            waitList.clear();
            List<String> currentlySelectedLobby = lobbyMap.get(currentGameSelection);
            for (String playerName : currentlySelectedLobby) {
                if (!playerName.equals(username)) { // don't add own username to lobby list
                    addToWaitList(playerName);
                }
            }
        }
    }

	//call this to remove from the lobby
	public void removeFromWaitList(String player){
		waitList.removeElement(player);
	}

 	public void requestWaitlist(){
 		waitList.clear(); //clear contents
 		currentGameSelection = (String) gameSelection.getSelectedItem();
 		gui.handleRequest(new Request(Command.GOTO_LOBBY, currentGameSelection));
 	}

	class playerSelectionListener implements ListSelectionListener
	{
	    public void valueChanged(ListSelectionEvent e) {
	        if (e.getValueIsAdjusting() == false) {
	            challengeOpponentButton.setEnabled(true);
	            }
	        }

	}

    public void setUsername(String username) {
        this.username = username;
    }
}
