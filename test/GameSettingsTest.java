import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GameSettingsTest {

    private GameSettings gameSettings;
    private GlobalSettings globalSettings;

    @Before
    public void createGameSettings() {
        this.globalSettings = new GlobalSettings();
        this.gameSettings = new GameSettings(this.globalSettings);
    }

    @Test
    public void testCanCreateGameSettings() {
        assertTrue(gameSettings != null);
    }

    @Test
    public void testCanCreateGameSettingsWithDefaults() {
        gameSettings = new GameSettings();
        assertTrue(gameSettings != null);
    }

    @Test
    public void testCanCreateAndGetDeckOffline() {
        gameSettings.setDeck();
        assertTrue(gameSettings.getDeck() != null);
    }

    @Test
    public void testCanCreateAndGetPlayOrderOffline() {
        gameSettings.setPlayOrder();
        assertTrue(gameSettings.getPlayOrder() != null);
    }
}