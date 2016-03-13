package client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LobbyScreen extends JPanel implements ActionListener{


	//method variables
	private JComboBox gameSelection;
	private String currentGameSelection;
	private String[] gameList = {"Tic Tac Toe", "Chutes and Ladders", "Checkers"};
	private JButton newGameButton,joinGameButton;
	private JPanel selectionPanel;
	private DefaultListModel waitList; //changes made to this will update GUI waiting list
	private JList uiList;
    private GUI gui;

	public LobbyScreen(GUI gui)
	{
		super(new BorderLayout());
        this.gui = gui;
		waitList = new DefaultListModel();

		createSelectionPanel();
		createJoinGameButton();
		createUIList();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(uiList);

		add(selectionPanel,BorderLayout.NORTH);
		add(scrollPane,BorderLayout.CENTER);
		add(joinGameButton,BorderLayout.SOUTH);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == gameSelection)
		{
			JComboBox selection = (JComboBox) e.getSource();
			currentGameSelection = (String) selection.getSelectedItem();
		}
		else if(e.getSource() == newGameButton)
		{
            gui.toBoard();
			//create a new game instance depending on what game is selected
		}
		else if(e.getSource() == joinGameButton)
		{
			//join an opponents game based on what opponent is chosen
			String userToJoin = (String) uiList.getSelectedValue();
			gui.getClient().send("JOIN " + userToJoin);
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
		joinGameButton = new JButton("Join Opponent's Game");
		joinGameButton.addActionListener(this);
		joinGameButton.setEnabled(false);
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
		newGameButton = new JButton("Start New Game");
		newGameButton.addActionListener(this);
		selectionPanel.add(newGameButton);
	}

	//call this to add a player to the lobby
	public void addToWaitList(String player){
		waitList.addElement(player);
	}

	//call this to remove from the lobby
	public void removeFromWaitList(String player){
		waitList.removeElement(player);
	}

	class playerSelectionListener implements ListSelectionListener
	{
	    public void valueChanged(ListSelectionEvent e) {
	        if (e.getValueIsAdjusting() == false) {
	            joinGameButton.setEnabled(true);
	            }
	        }

	}
}
