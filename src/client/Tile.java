package client;

import org.javatuples.Pair;
import shared.Command;
import shared.GameName;
import shared.Request;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tile extends JButton implements ActionListener {

    // Fields
    private static BoardScreen boardScreen;
    private static HashMap<String, ImageIcon> imageHashMap;
    private static List<Tile> instances = new ArrayList<>();
    private static String username;
    private static boolean waitingForSecondMove = true;
    private static String firstMove;
    private Pair<Integer, Integer> coordinates; //saves button coordinates
    private static GameName gameName;
    private static final String BOTH = "BOTH";

    // Methods
    public Tile(BoardScreen boardScreen, Pair<Integer, Integer> coordinates) {
        Tile.boardScreen = boardScreen;
        Tile.instances.add(this);
        this.coordinates = coordinates;
        this.addActionListener(this);
    }

    private static void loadImages(GameName gameName, String usernamePlayer1, String usernamePlayer2) {
        Tile.imageHashMap = new HashMap<>();
        try {
            if (gameName == GameName.TIC_TAC_TOE) {
                Tile.imageHashMap.put(usernamePlayer1, new ImageIcon(ImageIO.read(new File("img/x.png"))));
                System.out.println(usernamePlayer1 + " = X");
                Tile.imageHashMap.put(usernamePlayer2, new ImageIcon(ImageIO.read(new File("img/o.png"))));
                System.out.println(usernamePlayer2 + " = O");
            }
            else if (gameName == GameName.CHUTES_AND_LADDERS) {
                // TODO
                Tile.imageHashMap.put(usernamePlayer1, new ImageIcon(ImageIO.read(new File("img/chutes-red-10%.png"))));
                System.out.println(usernamePlayer1 + " = X");
                Tile.imageHashMap.put(usernamePlayer2, new ImageIcon(ImageIO.read(new File("img/chutes-black-10%.png"))));
                System.out.println(usernamePlayer2 + " = O");
                Tile.imageHashMap.put(BOTH, new ImageIcon(ImageIO.read(new File("img/chutes-black-and-red-10%.png"))));
            }
            else if (gameName == GameName.CHECKERS) {
                Tile.imageHashMap.put(usernamePlayer1, new ImageIcon(ImageIO.read(new File("img/checkers-black-12%.png"))));
                System.out.println(usernamePlayer1 + " = X");
                Tile.imageHashMap.put(usernamePlayer2, new ImageIcon(ImageIO.read(new File("img/checkers-red-12%.png"))));
                System.out.println(usernamePlayer2 + " = O");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * This will execute when a button object is clicked on the game board.
     * Gameboard will be properly updated to what is clicked as well.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        handleRequest(new Request(Command.MOVE, username + " " + coordinates.toString()));
    }

    private static ImageIcon chooseIcon(String piece) {
        return Tile.imageHashMap.get(piece);
    }

    private static Tile getTile(Pair<Integer, Integer> location) {
        Tile returnTile = null;
        for (Tile tile : Tile.instances) {
            if (tile.getCoordinates().equals(location)) {
                returnTile = tile;
                break;
            }
        }
        return returnTile;
    }

    private Pair<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    // Helper method to obtain position Pair from received String message
    @SuppressWarnings("Duplicates")
    private static Pair<Integer, Integer> extractPosition(Request request) {
        String message = request.getRequest();
        int i = message.indexOf('[');
        int j = message.indexOf(',');
        int k = message.indexOf(']');
        int x = Integer.parseInt(message.substring(i+1, j).trim());
        int y = Integer.parseInt(message.substring(j+1, k).trim());
        return Pair.with(x, y);
    }

    private void setupCheckers(Request request) {
        int length = 8;
        String[] tokens = request.getTokens();
        Command command = request.getCommand();
        Tile tile = null;
        String player1 = tokens[2];
        String player2 = tokens[4];

        int adj = 1;
        for (int yRow = 1; yRow <= 3; ++yRow) {
            for (int xCol = 1; xCol <= length; xCol+=2) {
                // Reds
                System.out.print("(" + (xCol+adj) + ", " + yRow + ")    ");
                tile = getTile(Pair.with(xCol + adj, yRow));
                tile.setIcon(chooseIcon(player2));
                tile.setDisabledIcon(chooseIcon(player2));
            }
            System.out.println("");
            adj = adj == 0 ? 1 : 0;
        }
        System.out.println("");

        adj = 0;
        for (int yRow = 6; yRow <= 8; ++yRow) {
            for (int xCol = 1; xCol <= length; xCol+=2) {
                // Blacks
                System.out.print("(" + (xCol+adj) + ", " + yRow + ")    ");
                tile = getTile(Pair.with(xCol + adj, yRow));
                tile.setIcon(chooseIcon(player1));
                tile.setDisabledIcon(chooseIcon(player1));
            }
            System.out.println("");
            adj = adj == 0 ? 1 : 0;
        }

    }

    public void handleRequest(Request request) {
        String[] tokens = request.getTokens();
        Command command = request.getCommand();
        Tile tile = null;
        switch (command) {
            case MOVE:
                if (gameName == GameName.CHECKERS) {
                    if (waitingForSecondMove) {
                        waitingForSecondMove = false;
                        firstMove = extractPosition(request).toString();
                        System.out.println("firstMove: " + firstMove);
                    }
                    else {
                        waitingForSecondMove = true;
                        String secondMove = extractPosition(request).toString();
                        System.out.println("secondMove: " + secondMove);
                        Tile.boardScreen.handleRequest(new Request(Command.MOVE, username + " " + firstMove + " " + secondMove));
                    }
                }
                else {
                    Tile.boardScreen.handleRequest(request);
                }

                break;
            case NEW_GAME:
                gameName = GameName.valueOf(tokens[1]);
                String player1 = tokens[2];
                String player2 = tokens[4];
                loadImages(gameName, player1, player2);
                if (gameName == GameName.CHECKERS) {
                    setupCheckers(request);
                }
                if (player1.equals(boardScreen.getUsername())) { // then i'm p1 and I should be X
                    Tile.username = player1;
                } else { // then i'm p2 and I should be O
                    Tile.username = player2;
                }
                break;
            case MOVE_TO:
                tile = getTile(extractPosition(request));
                tile.setIcon(chooseIcon(tokens[1]));
                tile.setDisabledIcon(chooseIcon(tokens[1]));
                tile.setEnabled(true);
                break;
            case REMOVE_FROM:
                tile = getTile(extractPosition(request));
                tile.setIcon(null);
                tile.setDisabledIcon(null);
                tile.setEnabled(true);
                break;
            case MOVE_BOTH_TO:
                tile = getTile(extractPosition(request));
                tile.setIcon(chooseIcon(BOTH));
                tile.setDisabledIcon(chooseIcon(BOTH));
                tile.setEnabled(true);
                break;
            case VICTORY: case DEFEAT: case TIE:
                Tile.boardScreen = null;
                Tile.imageHashMap = null;
                Tile.instances = new ArrayList<>();
                break;
        }
    }

}
