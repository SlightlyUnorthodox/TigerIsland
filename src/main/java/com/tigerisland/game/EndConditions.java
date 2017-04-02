package com.tigerisland.game;

import java.util.ArrayList;

public final class EndConditions {

    public static boolean noEndConditionsAreMet(Player currentPlayer, Board board) {
        return !playerIsOutOfPiecesOfTwoTypes(currentPlayer) && !noValidMoves(currentPlayer, board);
    }

    public static boolean playerIsOutOfPiecesOfTwoTypes(Player currentPlayer){
        Boolean onlyOneTypeRemaining = false;
        PieceSet currentPlayerPieces = currentPlayer.getPieceSet();
        if(currentPlayerPieces.getNumberOfVillagersRemaining() == 0 && currentPlayerPieces.getNumberOfTotoroRemaining() == 0){
            onlyOneTypeRemaining = true;
        } else if(currentPlayerPieces.getNumberOfVillagersRemaining() == 0 && currentPlayerPieces.getNumberOfTigersRemaining() == 0){
            onlyOneTypeRemaining = true;
        } else if(currentPlayerPieces.getNumberOfTigersRemaining() == 0 && currentPlayerPieces.getNumberOfTotoroRemaining() == 0){
            onlyOneTypeRemaining = true;
        }
        return onlyOneTypeRemaining;
    }

    public static boolean noValidMoves(Player currentPlayer, Board board){
        if(noMoreVillagers(currentPlayer) && cantPlayTotoro(currentPlayer, board) && cantPlayTiger(currentPlayer, board)){
            return true;
        }
        return false;
    }

    private static boolean noMoreVillagers(Player currentPlayer){
        return currentPlayer.getPieceSet().getNumberOfVillagersRemaining() == 0;
    }

    private static boolean cantPlayTotoro(Player currentPlayer, Board board){

        if(currentPlayer.getPieceSet().getNumberOfTotoroRemaining() == 0){
            return true;
        } else {
            return !board.playerHasSettlementThatCouldAcceptTotoro(currentPlayer);
        }
    }

    private static boolean cantPlayTiger(Player currentPlayer, Board board) {

        if(currentPlayer.getPieceSet().getNumberOfTigersRemaining() == 0) {
            return true;
        } else {
            return !board.playerHasSettlementThatCouldAcceptTiger(currentPlayer);
        }
    }

    public static Player calculateWinner(Player currentPlayer, ArrayList<Player> playerList) {

        if(playerIsOutOfPiecesOfTwoTypes(currentPlayer) == false) {
            playerList.remove(currentPlayer);
        }

        Player winner = getPlayerWithHighestScore(currentPlayer, playerList);

        return winner;
    }

    private static Player getPlayerWithHighestScore(Player currentPlayer, ArrayList<Player> playerList) {
        int highestScore = -1;
        Player winner = currentPlayer;

        for(Player player : playerList) {
            if(player.getScore().getScoreValue() > highestScore) {
                winner = player;
                highestScore = player.getScore().getScoreValue();
            } else if (player.getScore().getScoreValue() == highestScore) {
                winner = totoroCountTiebreaker(currentPlayer, playerList);
            }
        }
        return winner;
    }

    private static Player totoroCountTiebreaker(Player currentPlayer, ArrayList<Player> playerList) {
        int minRemainingTotoros = 4;
        Player winner = currentPlayer;

        for(Player player : playerList) {
            if(player.getPieceSet().getNumberOfTotoroRemaining() < minRemainingTotoros) {
                winner = player;
                minRemainingTotoros = player.getPieceSet().getNumberOfTotoroRemaining();
            } else if (player.getPieceSet().getNumberOfTotoroRemaining() == minRemainingTotoros) {
                winner = tigerCountTieBreaker(currentPlayer, playerList);
            }
        }
        return winner;
    }

    private static Player tigerCountTieBreaker(Player currentPlayer, ArrayList<Player> playerList) {
        int minRemainingTigers = 3;
        Player winner = currentPlayer;

        for(Player player : playerList) {
            if(player.getPieceSet().getNumberOfTigersRemaining() < minRemainingTigers) {
                winner = player;
                minRemainingTigers = player.getPieceSet().getNumberOfTigersRemaining();
            } else if (player.getPieceSet().getNumberOfTigersRemaining() == minRemainingTigers) {
                winner = villagerCountTieBreaker(currentPlayer, playerList);
            }
        }
        return winner;
    }

    private static Player villagerCountTieBreaker(Player currentPlayer, ArrayList<Player> playerList) {
        int minRemainingVillagers = 21;
        Player winner = currentPlayer;

        for(Player player : playerList) {
            if(player.getPieceSet().getNumberOfVillagersRemaining() < minRemainingVillagers) {
                winner = player;
                minRemainingVillagers = player.getPieceSet().getNumberOfVillagersRemaining();
            }
        }
        return winner;
    }
}
