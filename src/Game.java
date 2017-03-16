import java.util.ArrayList;

public class Game {

    private GlobalSettings globalSettings;
    private ArrayList<Player> players;
    private Board board;
    private int currentPlayerIndex;

    public Game(GlobalSettings globalSettings){
        this.globalSettings = globalSettings;
        board = new Board();
        players = new ArrayList<Player>();
        for(int player = 0; player < this.globalSettings.playerCount; player++){
            players.add(player, new Player(Color.values()[player]));
        }
    }

    public void start() {

    }

    public boolean noValidMoves(){
        Player currentPlayer = players.get(currentPlayerIndex);
        if(currentPlayer.mustPlaceTotoro()){

        }
        return false;
    }

    public boolean playerIsOutOfPieces(){
        for(Player player : players){
            if(player.getPieceSet().inventoryEmpty()){
                return true;
            }
        }
        return false;
    }

}
