package server;

public abstract class AbstractBoardTile {
    private ServerThread occupant1;
    private ServerThread occupant2;
    private Pair<Integer, Integer> coordinates;
    // Methods
    public AbstractBoardTile(Pair<Integer, Integer> coordinates) {
        this.coordinates = coordinates;
    }
    public void addOccupant(ServerThread serverThread) {
        this.occupant1 = serverThread;
    }
    public ServerThread getOccupant() {
        return occupant1;
    }
   public void setOccupant(ServerThread serverThread) {
        occupant1 = serverThread;
   }

}
