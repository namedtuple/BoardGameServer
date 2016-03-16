package client;

import shared.GameName;
import shared.Command;
import shared.Request;

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
	private GameName currentGameSelection;
    private HashMap<GameName, List<String>> lobbyMap;
    private String[] gameList = GameName.getAllFriendlyNames();

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
        lobbyMap.put(GameName.TIC_TAC_TOE, new ArrayList<>());
        lobbyMap.put(GameName.CHUTES_AND_LADDERS, new ArrayList<>());
        lobbyMap.put(GameName.CHECKERS, new ArrayList<>());

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
                requestWaitlist();
            }
        }
		else if(e.getSource() == logoutButton)
		{
			//create a new game instance depending on what game is selected
            handleRequest(new Request(Command.LOGOUT));
            handleRequest(new Request(Command.DISCONNECTED));
		}
		else if(e.getSource() == challengeOpponentButton)
		{
            String userToJoin = (String) uiList.getSelectedValue();
			//join an opponents game based on what opponent is chosen
            handleRequest(new Request(Command.JOIN, username + " " + userToJoin));
		}

	}

	private void createUIList(){
		uiList = new JList(waitList);
        uiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        uiList.addListSelectionListener(new playerSelectionListener());
        uiList.setVisibleRowCount(5);
        uiList.setFixedCellHeight(44);
	}

	private void createJoinGameButton(){
		challengeOpponentButton = new JButton("Challenge Opponent");
		challengeOpponentButton.addActionListener(this);
		challengeOpponentButton.setEnabled(false);
	}

	private void createSelectionPanel(){
		selectionPanel = new JPanel();
		selectionPanel.setLayout(new BoxLayout(selectionPanel,BoxLayout.LINE_AXIS));
		createGameSelectionBox();
		selectionPanel.add(Box.createHorizontalStrut(5));
		selectionPanel.add(Box.createHorizontalStrut(5));
		createnewGameButton();
		selectionPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}

	private void createGameSelectionBox(){
		gameSelection = new JComboBox(gameList);
		gameSelection.addActionListener(this);
		gameSelection.setSelectedIndex(0);
		currentGameSelection = GameName.getGameName(gameList[0]);
		selectionPanel.add(gameSelection);
	}

	private void createnewGameButton(){
		logoutButton = new JButton("Log out");
		logoutButton.addActionListener(this);
		selectionPanel.add(logoutButton);
	}

	//call this to add a player to the lobby
	private void addToWaitList(String player){
		waitList.addElement(player);
	}

    private void addAllToWaitList(String request) {

        String[] splitMsg = request.split(" ");

        // update (model) list first:
        GameName gameName = GameName.valueOf(splitMsg[1]);
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
	private void removeFromWaitList(String player){
		waitList.removeElement(player);
	}

 	private void requestWaitlist(){
 		waitList.clear(); //clear contents
        currentGameSelection = GameName.getGameName((String) gameSelection.getSelectedItem());
 		handleRequest(new Request(Command.GOTO_LOBBY, currentGameSelection.toString()));
 	}

	class playerSelectionListener implements ListSelectionListener
	{
	    public void valueChanged(ListSelectionEvent e) {
	        if (e.getValueIsAdjusting() == false) {
	            challengeOpponentButton.setEnabled(true);
	            }
	        }

	}

    public void handleRequest(Request request) {
        String[] tokens = request.getTokens();
        Command command = request.getCommand();
        switch(command) {
            case LOGOUT: case DISCONNECTED: case JOIN: case GOTO_LOBBY:
                gui.handleRequest(request);
                break;
            case LOGIN_SUCCESS:
                username = tokens[1];
                requestWaitlist();
                break;
            case VICTORY: case DEFEAT: case TIE:
                requestWaitlist();
                break;
            case LOBBY:
                addAllToWaitList(request.getRequest()); // TODO
                break;
            default:
                break;
        }
    }
}
