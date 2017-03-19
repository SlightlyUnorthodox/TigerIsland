package com.tigerisland;

import java.util.ArrayList;

public class Game {

    protected GameSettings gameSettings;
    protected ArrayList<Player> players;
    protected Board board;
    protected Rules rules;
    private int currentPlayerIndex;

    public Game(GameSettings gameSettings){
        this.gameSettings = gameSettings;
        board = new Board();
        players = new ArrayList<Player>();
        rules = new Rules();
        for(int player = 0; player < this.gameSettings.globalSettings.playerCount; player++){
            players.add(player, new Player(Color.values()[player]));
        }
    }

    public void start() {

    }

    public boolean noValidMoves(){
        Player currentPlayer = players.get(currentPlayerIndex);
        if(currentPlayer.getPieceSet().getNumberOfVillagersRemaining() == 0){

        }
        return false;
    }

    private boolean playerIsOutOfPieces(){
        for(Player player : players){
            if(player.getPieceSet().inventoryEmpty()){
                return true;
            }
        }
        return false;
    }

}
