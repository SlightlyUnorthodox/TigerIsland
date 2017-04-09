package com.tigerisland.game;

import com.tigerisland.GameSettings;
import com.tigerisland.InvalidMoveException;
import com.tigerisland.messenger.Message;
import com.tigerisland.messenger.MessageType;

import static java.lang.Thread.sleep;

public class Game implements Runnable {

    public String gameID;

    protected GameSettings gameSettings;
    protected Board board;
    protected Turn turnState;

    protected Boolean offline;

    private String ourPlayerID;

    private String moveID = "1";

    Player winner;
    Player loser;

    public Game(GameSettings gameSettings){
        this.ourPlayerID = gameSettings.getGlobalSettings().getServerSettings().getPlayerID();

        this.gameSettings = gameSettings;
        this.gameID = gameSettings.getGameID();
        this.board = new Board();
        this.offline = gameSettings.getGlobalSettings().getServerSettings().offline;
        this.turnState = new Turn(gameSettings, board);
    }

    public void run() {
        try {

            board.placeStartingTile();

            while (continuePlayingGame()) {
                sleep(1);
            }

            calculateResults();
            offlineGenerateGameOverEcho("GAME " + gameID + " OVER PLAYER " + winner.getPlayerID() + " " + winner.getScore().getScoreValue() + " PLAYER " + loser.getPlayerID() + " " + loser.getScore().getScoreValue());

        } catch (InterruptedException exception) {
            offlineGenerateGameOverEcho("GAME " + gameID + " MOVE " + turnState.getMoveID() + " PLAYER " + turnState.getCurrentPlayer().getPlayerID() + " FORFEITED: TIMEOUT");
            calculateResults();
            System.out.println("GAME " + gameID + " OVER PLAYER " + winner.getPlayerID() + " " + winner.getScore().getScoreValue() + " PLAYER " + loser.getPlayerID() + " " + loser.getScore().getScoreValue());

        } catch (InvalidMoveException exception) {
            offlineGenerateGameOverEcho("GAME " + gameID + " MOVE " + turnState.getMoveID() + " PLAYER " + turnState.getCurrentPlayer().getPlayerID() + " " + exception.getMessage());
            calculateResults();
            System.out.println("GAME " + gameID + " OVER PLAYER " + winner.getPlayerID() + " " + winner.getScore().getScoreValue() + " PLAYER " + loser.getPlayerID() + " " + loser.getScore().getScoreValue());

        } catch (IndexOutOfBoundsException exception) {
            // Catch out of tile case
            calculateResults();
            offlineGenerateGameOverEcho("GAME " + gameID + " OVER PLAYER " + winner.getPlayerID() + " " + winner.getScore().getScoreValue() + " PLAYER " + loser.getPlayerID() + " " + loser.getScore().getScoreValue());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void calculateResults() {
        winner = EndConditions.calculateWinner(gameSettings.getPlayerSet().getCurrentPlayer(), gameSettings.getPlayerSet().getPlayerList());
        loser = EndConditions.getLoser(winner, gameSettings.getPlayerSet().getPlayerList());
    }

    private Boolean continuePlayingGame() throws InterruptedException, InvalidMoveException {

        //if(gameSettings.getGlobalSettings().manualTesting) {
           // TextGUI.printMap(board);
        //}


        Boolean continueGame = true;

        if(Thread.currentThread().isInterrupted()) {
            return false;
        }

        if(offline) {
            mockMakeMoveMessage();
            alternateOurPlayerID();
        }

        checkForHaveAIPickAMove();

        continueGame = checkForMoveToProcess();

        if(gameOver()) {
            continueGame = false;
        }

        return continueGame;
    }

    protected void mockMakeMoveMessage() throws IndexOutOfBoundsException {

        String makeMoveMessage = "MAKE YOUR MOVE IN GAME " + gameSettings.getGameID();
        makeMoveMessage = makeMoveMessage + " WITHIN 1.5 SECONDS:";

        makeMoveMessage = makeMoveMessage + " MOVE " + moveID + " PLACE ";

        Tile newTile = gameSettings.getDeck().drawTile();

        String leftTerrain = newTile.getLeftHex().getHexTerrain().name();
        String rightTerrain = newTile.getRightHex().getHexTerrain().name();

        makeMoveMessage = makeMoveMessage + leftTerrain + "+" + rightTerrain;

        gameSettings.getGlobalSettings().inboundQueue.add(new Message(makeMoveMessage));
    }

    protected void alternateOurPlayerID() {
        String opponentPlayerID = gameSettings.getGlobalSettings().getServerSettings().getOpponentID();
        String truePlayerID = gameSettings.getGlobalSettings().getServerSettings().getPlayerID();
        if(ourPlayerID.equals(truePlayerID)) {
             ourPlayerID = opponentPlayerID;
        } else {
            ourPlayerID = truePlayerID;
        }
    }

    protected void incrementMoveID() {
        moveID = String.valueOf(Integer.parseInt(moveID)+ 1);
    }

    protected void checkForHaveAIPickAMove() throws InterruptedException {
        for(Message message : gameSettings.getGlobalSettings().inboundQueue) {
            if(message.getGameID() != null && message.getMessageType() != null) {
                if (message.getGameID().equals(gameID)) {
                    if (message.getMessageType() == MessageType.MAKEMOVE) {
                        message.setProcessed();
                        gameSettings.resetGameID(message.getGameID());
                        gameSettings.getPlayerSet().setCurrentPlayer(ourPlayerID);
                        pickMove(message);
                    }
                }
            }
        }
    }

    private void pickMove(Message message) {
        turnState.updateTurnInformation(message.getMoveID(), message.getTile(), ourPlayerID);
        turnState.getCurrentPlayer().getPlayerAI().pickTilePlacementAndBuildAction(turnState);
    }

    protected Boolean checkForMoveToProcess() throws InvalidMoveException, InterruptedException {
        for(Message message : gameSettings.getGlobalSettings().inboundQueue) {
            if(message.getGameID() != null && message.getMoveID() != null && message.getMessageType() != null) {
                if (message.getGameID().equals(gameID)) {
                    if (message.getMoveID().equals(moveID)) {
                        if (message.getMessageType().getSubtype().equals("BUILDACTION")) {
                            gameSettings.resetGameID(message.getGameID());
                            sendMockServerMessage(message);
                            return processMove(message);
                        }
                    }
                }
            }
        }
        return true;
    }

    private Boolean processMove(Message message) throws InvalidMoveException, InterruptedException {

        gameSettings.getPlayerSet().setCurrentPlayer(message.getCurrentPlayerID());
        gameSettings.setMoveID(message.getMoveID());

        turnState.processMove();

        turnState = Move.placeTile(turnState);

        if(EndConditions.noValidMoves(gameSettings.getPlayerSet().getCurrentPlayer(), board)) {
            throw new InvalidMoveException("LOST: UNABLE TO BUILD");
        }

        turnState = Move.takeBuildAction(turnState);

        if(EndConditions.playerIsOutOfPiecesOfTwoTypes(gameSettings.getPlayerSet().getCurrentPlayer())) {
            return false;
        }

        incrementMoveID();

        return true;

    }

    protected Boolean gameOver() {
        for(Message message : gameSettings.getGlobalSettings().inboundQueue) {
            if(message.getMessageType() != null) {
                if(message.getMessageType().getSubtype().equals(MessageType.GAMEOVER.getSubtype())) {
                    message.setProcessed();
                    return true;
                }
            }
        }

        return false;
    }

    private void sendMockServerMessage(Message message) {
        if(offline) {
            System.out.println("SERVER (Offline): " + message.message);
        }
    }

    private void offlineGenerateGameOverEcho(String message) {
        if(offline) {
            gameSettings.getGlobalSettings().outboundQueue.add(new Message(message));
        }
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public Board getBoard() {
        return board;
    }

    public String getGameID() {
        return gameID;
    }

}
