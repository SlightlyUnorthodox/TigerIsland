package com.tigerisland;

import com.tigerisland.game.board.Tile;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DeckTest{

    private Deck deck;
    ArrayList<Tile> baseDeck;
    ArrayList<Tile> altDeck;

    @Before
    public void createDeck() {
        this.deck = new Deck();
        deck.createOfflineDeck();
        this.baseDeck = deck.tileDeck;
    }

    @Test
    public void testCanCreateDeck() {
        assertTrue(deck != null);
    }

    @Test
    public void testCanCreateDeckViaCopyConstructor() {
        Deck deckCopy = new Deck(deck);
        assertTrue(deckCopy != deck);
    }

    @Test
    public void testCanCreateNonEmptyDeck() {
        assertTrue(deck.getDeckSize() > 0);
    }

    @Test
    public void testCanCreateDeckOfSize48() {
        assertTrue(deck.getDeckSize() == 48);
    }

    @Test
    public void testCanShuffleDeckOnceCreated() {
        String baseDeckString = Arrays.toString(deck.tileDeck.toArray());
        deck.shuffleDeck();
        String altDeckString = Arrays.toString(deck.tileDeck.toArray());
        assertNotEquals(baseDeckString, altDeckString);
    }

    @Test
    public void testEachTileHasUniqueId() {
        int deckSize = deck.getDeckSize();

        Set<String> listOfUniqueIDs = new HashSet<String>();

        for (Tile tile : deck.tileDeck) {
            listOfUniqueIDs.add(tile.getTileID());
        }

        assertTrue(deckSize == listOfUniqueIDs.size());
    }

    @Test
    public void testCanDrawTile() {
        int deckSize = deck.getDeckSize();
        deck.drawTile();
        assertTrue(deckSize - 1 == deck.getDeckSize());
    }
}