package com.tigerisland.settings;

import com.tigerisland.client.OfflineDeck;
import com.tigerisland.game.pieces.Color;
import com.tigerisland.game.player.Player;
import com.tigerisland.game.player.PlayerSet;
import com.tigerisland.game.player.PlayerType;

public class GameSettings {

    private final PlayerType BEST_AI_TYPE;
    private final PlayerType TEST_AGAINST_TYPE = PlayerType.SAFEAI;

    private GlobalSettings globalSettings;
    private OfflineDeck offlineDeck;
    private PlayerSet playerSet;

    private String gameID = "A";
    private String moveID = "1";

    public GameSettings() {
        this.globalSettings = setPlayerIDsIfNull(new GlobalSettings());
        this.BEST_AI_TYPE = GlobalSettings.defaultAItype;
        this.playerSet = new PlayerSet(globalSettings);
        constructPlayerSet();
    }

    public GameSettings(GlobalSettings settings) {
        this.globalSettings = setPlayerIDsIfNull(settings);
        this.BEST_AI_TYPE = globalSettings.getAIType();
        this.playerSet = new PlayerSet(globalSettings);
        constructPlayerSet();
        setDeck();
    }

    public GameSettings(GameSettings gameSettings) {
        this.gameID = gameSettings.gameID;
        this.moveID = gameSettings.moveID;

        this.BEST_AI_TYPE = gameSettings.BEST_AI_TYPE;

        this.globalSettings = gameSettings.getGlobalSettings();
        this.offlineDeck = new OfflineDeck(gameSettings.getOfflineDeck());
        this.playerSet = new PlayerSet(gameSettings.getPlayerSet());
    }

    private GlobalSettings setPlayerIDsIfNull(GlobalSettings globalSettings) {
        if(globalSettings.getServerSettings().getPlayerID() == null) {
            globalSettings.getServerSettings().setPlayerID("1");
        }
        if(globalSettings.getServerSettings().getOpponentID() == null) {
            globalSettings.getServerSettings().setOpponentID("2");
        }
        return globalSettings;
    }

    public void setDeck() {
        offlineDeck = new OfflineDeck();
        if(globalSettings.getServerSettings().offline) {
            offlineDeck.createOfflineDeck();
        }
    }

    public void constructPlayerSet() {
        String ourPlayerID = globalSettings.getServerSettings().getPlayerID();
        PlayerType ourPlayerType = BEST_AI_TYPE;

        String opponentPlayerID = globalSettings.getServerSettings().getOpponentID();
        PlayerType opponentPlayerType = PlayerType.SERVER;

        if(globalSettings.manualTesting) {
            ourPlayerType = PlayerType.HUMAN;
        }

        if(globalSettings.getServerSettings().offline) {
            opponentPlayerType = TEST_AGAINST_TYPE;
        }

        Player ourPlayer = new Player(Color.WHITE, ourPlayerID, ourPlayerType);
        Player opponentPlayer = new Player(Color.BLACK, opponentPlayerID, opponentPlayerType);

        playerSet.getPlayerList().put(ourPlayerID, ourPlayer);
        playerSet.getPlayerList().put(opponentPlayerID, opponentPlayer);

    }

    public OfflineDeck getOfflineDeck() {
        return offlineDeck;
    }

    public PlayerSet getPlayerSet() {
        return playerSet;
    }

    public GlobalSettings getGlobalSettings() {
        return globalSettings;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getGameID() {
        return gameID;
    }

    public void setMoveID(String moveID) {
        this.moveID = moveID;
    }

    public String getMoveID() {
        return moveID;
    }

}
