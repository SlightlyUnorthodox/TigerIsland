package com.tigerisland.game;

import com.tigerisland.InvalidMoveException;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BoardTest{

    private Board board;
    private Tile tile;
    private Location location;
    private ArrayList<PlacedHex> placedHexes;

    @Before
    public void createBoard() {
        this.board = new Board();
        this.tile = new Tile(Terrain.GRASSLANDS, Terrain.JUNGLE);
        this.location = new Location(0,0);
        this.placedHexes = new ArrayList<PlacedHex>();
    }

    @Test
    public void testCanCreateBoard() {
        assertTrue(board != null);
    }

    @Test
    public void testCreatedBoardHasPlacedHexes() {
        assertTrue(board.placedHexes != null);
    }

    @Test
    public void testCreatedBoardHasEdgeSpaces() {
        assertTrue(board.edgeSpaces != null);
    }

    @Test
    public void testLocationUnavailable() throws InvalidMoveException {
        Tile tile = new Tile(Terrain.GRASSLANDS, Terrain.JUNGLE);
        Location location = new Location(0,0);
        board.placeTile(tile, location,0);

        Location location2 = new Location(0,0);
        assertFalse(board.isAnAvailableEdgeSpace(location2));
    }

    @Test
    public void testHexAtLocation() throws InvalidMoveException {
        Tile tile = new Tile(Terrain.GRASSLANDS, Terrain.JUNGLE);
        Location location = new Location(0,0);
        board.placeTile(tile, location,0);

        Tile tile2 = new Tile(Terrain.GRASSLANDS, Terrain.JUNGLE);
        Location location2 = new Location(2,1);
        assertFalse(board.hexExistsAtLocation(location2));
    }

    @Test
    public void testCanPlaceTile() throws InvalidMoveException{
        board.placeTile(tile, location, 0);
    }

    @Test
    public void testUpdateSettlements(){
        Hex hex1 = new Hex("hex1", Terrain.LAKE);
        Hex hex2 = new Hex("hex2", Terrain.LAKE);
        Hex hex3 = new Hex("hex3", Terrain.LAKE);
        Hex hex4 = new Hex("hex4", Terrain.LAKE);

        hex1.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        hex2.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        hex3.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        hex4.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);

        Location loc1 = new Location(0,0);
        Location loc2 = new Location(0,-1);
        Location loc3 = new Location(0,1);
        Location loc4 = new Location(1,0);

        PlacedHex placedHex1 = new PlacedHex(hex1, loc1);
        PlacedHex placedHex2 = new PlacedHex(hex2, loc2);
        PlacedHex placedHex3 = new PlacedHex(hex3, loc3);
        PlacedHex placedHex4 = new PlacedHex(hex4, loc4);

        placedHexes.add(placedHex1);
        placedHexes.add(placedHex2);
        placedHexes.add(placedHex3);
        placedHexes.add(placedHex4);

        Settlement settlement = new Settlement(placedHex1, placedHexes);

        board.placedHexes = this.placedHexes;
        board.settlements.add(settlement);

        assertTrue(settlement.size() == 4 && !settlement.containsTotoro());

        Hex hex5 = new Hex("hex5", Terrain.LAKE);
        Hex hex6 = new Hex("hex6", Terrain.LAKE);

        hex5.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        hex6.addPiecesToHex(new Piece(Color.BLACK, PieceType.TOTORO), 1);

        Location loc5 = new Location(0,2);
        Location loc6 = new Location(2,0);

        PlacedHex placedHex5 = new PlacedHex(hex5, loc5);
        PlacedHex placedHex6 = new PlacedHex(hex6, loc6);

        placedHexes.add(placedHex5);
        placedHexes.add(placedHex6);

        board.updateSettlements();

        assertTrue(board.settlements.get(0).containsTotoro());

        assertTrue(board.settlements.get(0).getHexesInSettlement().size() == 6);

    }

    @Test
    public void testPlayerHasSettlementThatCouldAcceptTotoro(){
        Player player = new Player(Color.BLACK);

        Hex hex1 = new Hex("hex1", Terrain.LAKE);
        Hex hex2 = new Hex("hex2", Terrain.LAKE);
        Hex hex3 = new Hex("hex3", Terrain.LAKE);
        Hex hex4 = new Hex("hex4", Terrain.LAKE);

        hex1.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        hex2.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        hex3.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        hex4.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);

        Location loc1 = new Location(0,0);
        Location loc2 = new Location(0,-1);
        Location loc3 = new Location(0,1);
        Location loc4 = new Location(1,0);

        PlacedHex placedHex1 = new PlacedHex(hex1, loc1);
        PlacedHex placedHex2 = new PlacedHex(hex2, loc2);
        PlacedHex placedHex3 = new PlacedHex(hex3, loc3);
        PlacedHex placedHex4 = new PlacedHex(hex4, loc4);

        placedHexes.add(placedHex1);
        placedHexes.add(placedHex2);
        placedHexes.add(placedHex3);
        placedHexes.add(placedHex4);

        Settlement settlement = new Settlement(placedHex1, placedHexes);

        board.placedHexes = this.placedHexes;
        board.settlements.add(settlement);

        assertTrue(board.playerHasSettlementThatCouldAcceptTotoro(player) == false);

        Hex hex5 = new Hex("hex5", Terrain.LAKE);
        Hex hex6 = new Hex("hex6", Terrain.LAKE);

        hex5.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);


        Location loc5 = new Location(0,2);
        Location loc6 = new Location(2,0);

        PlacedHex placedHex5 = new PlacedHex(hex5, loc5);
        PlacedHex placedHex6 = new PlacedHex(hex6, loc6);

        placedHexes.add(placedHex5);
        placedHexes.add(placedHex6);

        board.updateSettlements();


        assertTrue(board.playerHasSettlementThatCouldAcceptTotoro(player) == true);

        hex6 = new Hex("hex6", Terrain.VOLCANO);

        assertFalse(board.playerHasSettlementThatCouldAcceptTotoro(player) == false);


    }




    private PlacedHex setUpSettlement(){
        Hex hex1 = new Hex("hex1", Terrain.LAKE);
        Hex hex2 = new Hex("hex2", Terrain.LAKE);
        Hex hex3 = new Hex("hex3", Terrain.LAKE);
        Hex hex4 = new Hex("hex4", Terrain.LAKE);

        hex1.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        hex2.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        hex3.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        hex4.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);

        Location loc1 = new Location(0,0);
        Location loc2 = new Location(0,-1);
        Location loc3 = new Location(0,1);
        Location loc4 = new Location(1,0);

        PlacedHex placedHex1 = new PlacedHex(hex1, loc1);
        PlacedHex placedHex2 = new PlacedHex(hex2, loc2);
        PlacedHex placedHex3 = new PlacedHex(hex3, loc3);
        PlacedHex placedHex4 = new PlacedHex(hex4, loc4);

        placedHexes.add(placedHex1);
        placedHexes.add(placedHex2);
        placedHexes.add(placedHex3);
        placedHexes.add(placedHex4);

        return placedHex1;
    }

    @Test
    public void testCanMakeSaveBoardCopyCheckPlacedHexes() throws InvalidMoveException {
        Board boardCopy = new Board(board);
        Tile tile = new Tile(Terrain.LAKE, Terrain.GRASSLANDS);
        Location location = new Location(0, 0);
        boardCopy.placeTile(tile, location, 0);

        assertTrue(boardCopy.hexExistsAtLocation(location) == true && board.hexExistsAtLocation(location) == false);
    }

    @Test
    public void testCanMakeSaveBoardCopyCheckSettlements() throws InvalidMoveException {
        Board boardCopy = new Board(board);

        Tile tile = new Tile(Terrain.LAKE, Terrain.GRASSLANDS);
        Location location = new Location(0, 0);
        boardCopy.placeTile(tile, location, 0);

        Player player = new Player(Color.BLACK);
        Location villageLocation = new Location(1, 0);
        boardCopy.createVillage(player, villageLocation);

        boardCopy.updateSettlements();

        assertTrue(boardCopy.settlements.size() == 1 && board.settlements.size() == 0);
    }

    @Test
    public void testCanMakeSaveBoardCopyCheckEdgesSpaces() throws InvalidMoveException {
        Board boardCopy = new Board(board);
        Tile tile = new Tile(Terrain.LAKE, Terrain.GRASSLANDS);
        Location location = new Location(0, 0);
        boardCopy.placeTile(tile, location, 0);

        assertTrue(boardCopy.edgeSpaces.size() != board.edgeSpaces.size());
    }

    @Test
    public void testCanSaveOriginalBoardWithCopy() throws InvalidMoveException {
        Board boardCopy = new Board(board);
        Tile tile = new Tile(Terrain.LAKE, Terrain.GRASSLANDS);
        Location location = new Location(0, 0);
        boardCopy.placeTile(tile, location, 0);

        board = boardCopy;

        assertTrue(board.hexExistsAtLocation(location) == true);
    }

   @Test
   public void testCanPlaceTigerOnValidHex(){
       Player player = new Player(Color.BLACK);
       PlacedHex placedHex1 = setUpSettlement();

       Hex targetHex = new Hex("hex6", Terrain.JUNGLE,3);
       Location targetLocation = new Location(0,-2);
       PlacedHex targetPlacedHex = new PlacedHex(targetHex, targetLocation);
       placedHexes.add(targetPlacedHex);
       Settlement settlement = new Settlement(placedHex1, placedHexes);

       int originalNumberOfTigersRemaining = player.getPieceSet().getNumberOfTigersRemaining();

       board.placedHexes = this.placedHexes;
       board.settlements.add(settlement);

       try {
           board.placeTiger(player, targetLocation);
       } catch (InvalidMoveException e) {
           assertFalse(e.getMessage() == "");
       }

       int finalNumberOfTigersRemaining = player.getPieceSet().getNumberOfTigersRemaining();

       assertTrue(finalNumberOfTigersRemaining == originalNumberOfTigersRemaining - 1);
   }

   @Test
   public void testCannotPlaceTigerOnNonexistentHex(){
       Player player = new Player(Color.BLACK);
       PlacedHex placedHex1 = setUpSettlement();

       Location targetLocation = new Location(0,-2);
       Settlement settlement = new Settlement(placedHex1, placedHexes);

       int originalNumberOfTigersRemaining = player.getPieceSet().getNumberOfTigersRemaining();

       board.placedHexes = this.placedHexes;
       board.settlements.add(settlement);

       try {
           board.placeTiger(player, targetLocation);
       } catch (InvalidMoveException e) {
           assertTrue(e.getMessage().equals("Target hex does not exist"));
       }

       int finalNumberOfTigersRemaining = player.getPieceSet().getNumberOfTigersRemaining();

       assertFalse(finalNumberOfTigersRemaining == originalNumberOfTigersRemaining - 1);
   }

   @Test
    public void testCannotPlaceTigerOnVolcano(){
       Player player = new Player(Color.BLACK);
       PlacedHex placedHex1 = setUpSettlement();

       Hex targetHex = new Hex("hex5", Terrain.VOLCANO, 3);
       Location targetLocation = new Location(0,-2);
       PlacedHex targetPlacedHex = new PlacedHex(targetHex, targetLocation);
       placedHexes.add(targetPlacedHex);
       Settlement settlement = new Settlement(placedHex1, placedHexes);

       int originalNumberOfVillagersRemaining = player.getPieceSet().getNumberOfVillagersRemaining();
       int originalNumberOfTigersRemaining = player.getPieceSet().getNumberOfTigersRemaining();

       board.placedHexes = this.placedHexes;
       board.settlements.add(settlement);

       try {
           board.placeTiger(player, targetLocation);
       } catch (InvalidMoveException e) {
           assertTrue(e.getMessage().equals("Cannot place a piece on a volcano hex"));
       }

       int finalNumberOfVillagersRemaining = player.getPieceSet().getNumberOfVillagersRemaining();
       int finalNumberOfTigersRemaining = player.getPieceSet().getNumberOfTigersRemaining();

       assertFalse(finalNumberOfTigersRemaining == originalNumberOfTigersRemaining - 1);
       assertTrue(finalNumberOfVillagersRemaining == originalNumberOfVillagersRemaining);
   }

   @Test
   public void testCannotPlaceTigerOnNonemptyHex(){
       Player player = new Player(Color.BLACK);
       PlacedHex placedHex1 = setUpSettlement();

       Hex targetHex = new Hex("hex5", Terrain.JUNGLE, 3);
       targetHex.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 3);
       Location targetLocation = new Location(0,-2);
       PlacedHex targetPlacedHex = new PlacedHex(targetHex, targetLocation);
       placedHexes.add(targetPlacedHex);
       Settlement settlement = new Settlement(placedHex1, placedHexes);

       int originalNumberOfVillagersRemaining = player.getPieceSet().getNumberOfVillagersRemaining();
       int originalNumberOfTigersRemaining = player.getPieceSet().getNumberOfTigersRemaining();

       board.placedHexes = this.placedHexes;
       board.settlements.add(settlement);

       try {
           board.placeTiger(player, targetLocation);
       } catch (InvalidMoveException e) {
           assertTrue(e.getMessage().equals("Target hex already contains piece(s)"));
       }

       int finalNumberOfVillagersRemaining = player.getPieceSet().getNumberOfVillagersRemaining();
       int finalNumberOfTigersRemaining = player.getPieceSet().getNumberOfTigersRemaining();

       assertFalse(finalNumberOfTigersRemaining == originalNumberOfTigersRemaining - 1);
       assertTrue(finalNumberOfVillagersRemaining == originalNumberOfVillagersRemaining);
   }

   @Test
    public void testCannotPlaceTigerOnHexOfHeightLessThanThree(){
       Player player = new Player(Color.BLACK);
       PlacedHex placedHex1 = setUpSettlement();

       Hex targetHex = new Hex("hex5", Terrain.JUNGLE, 2);
       Location targetLocation = new Location(0,-2);
       PlacedHex targetPlacedHex = new PlacedHex(targetHex, targetLocation);
       placedHexes.add(targetPlacedHex);
       Settlement settlement = new Settlement(placedHex1, placedHexes);

       int originalNumberOfVillagersRemaining = player.getPieceSet().getNumberOfVillagersRemaining();
       int originalNumberOfTigersRemaining = player.getPieceSet().getNumberOfTigersRemaining();

       board.placedHexes = this.placedHexes;
       board.settlements.add(settlement);

       try {
           board.placeTiger(player, targetLocation);
       } catch (InvalidMoveException e) {
           assertTrue(e.getMessage().equals("Cannot build Tiger playground on hex of level less than 3"));
       }

       int finalNumberOfVillagersRemaining = player.getPieceSet().getNumberOfVillagersRemaining();
       int finalNumberOfTigersRemaining = player.getPieceSet().getNumberOfTigersRemaining();

       assertFalse(finalNumberOfTigersRemaining == originalNumberOfTigersRemaining - 1);
       assertTrue(finalNumberOfVillagersRemaining == originalNumberOfVillagersRemaining);
   }

   @Test
    public void testCannotPlaceTigerOnSettlementSizeZero(){
       Player player = new Player(Color.BLACK);
       Hex targetHex = new Hex("hex1", Terrain.JUNGLE, 3);
       Location targetLocation = new Location(0,-2);
       PlacedHex targetPlacedHex = new PlacedHex(targetHex, targetLocation);
       placedHexes.add(targetPlacedHex);

       board.placedHexes = this.placedHexes;

       try {
           board.placeTiger(player, targetLocation);
       } catch (InvalidMoveException e) {
           assertTrue(e.getMessage().equals("Cannot place Tiger in a settlement of size smaller than 1"));
       }
   }

   @Test
    public void testCannotPlaceTigerOnSettlementWithTiger(){
       Player player = new Player(Color.BLACK);
       PlacedHex placedHex1 = setUpSettlement();
       Hex hex5 = new Hex("hex5", Terrain.LAKE,3);
       hex5.addPiecesToHex(new Piece(Color.BLACK, PieceType.TIGER), 1);
       Location loc5 = new Location(0,-2);
       PlacedHex placedHex5 = new PlacedHex(hex5, loc5);
       placedHexes.add(placedHex5);
       Hex hex6 = new Hex("hex5", Terrain.GRASSLANDS,4);
       Location loc6 = new Location(0,-3);
       PlacedHex emptyPlacedHexToTryToPlaceTigerOn = new PlacedHex(hex6, loc6);
       placedHexes.add(emptyPlacedHexToTryToPlaceTigerOn);
       Settlement settlement = new Settlement(placedHex1, placedHexes);


       board.placedHexes = this.placedHexes;
       board.settlements.add(settlement);

       try {
           board.placeTotoro(player, emptyPlacedHexToTryToPlaceTigerOn.getLocation()); //this says totoro. Should it say tiger?
       } catch (InvalidMoveException e) {
           assertTrue(e.getMessage().equals("Cannot place Tiger in a settlement already containing a Tiger"));
       }
   }

   @Test
    public void testSettlementLocationIsNotValid() {
        Player player = new Player(Color.BLACK);
        Location invalidSettlementLocation = new Location(0,1);

        try {
            board.isSettledLocationValid(player, invalidSettlementLocation);
        } catch (InvalidMoveException e) {
            assertTrue(e.getMessage().equals("This hex does not exist"));
        }
   }

   @Test
    public void testLocationDoesNotBelongInASettlement() {
        Player player = new Player(Color.BLACK);
        String tileID = "fakeTileID";
        Location loc = new Location(0,1);
        Hex emptyHex = new Hex(tileID, Terrain.GRASSLANDS);
        PlacedHex emptyPlacedHex = new PlacedHex(emptyHex, loc);
        placedHexes.add(emptyPlacedHex);
        board.placedHexes = this.placedHexes;

       try {
           board.isSettledLocationValid(player, loc);
       } catch (InvalidMoveException e) {
           assertTrue(e.getMessage().equals("This hex does not belong in a settlement"));
       }
   }

   @Test
    public void testSettlementLocationDoesNotBelongToPlayer() {
        Player player1 = new Player(Color.BLACK);
        String tileID = "fakeTileID";
        Location loc = new Location(0,1);
        Hex blackHex = new Hex(tileID, Terrain.GRASSLANDS);
        blackHex.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        PlacedHex blackPlacedHex = new PlacedHex(blackHex, loc);
        placedHexes.add(blackPlacedHex);
        board.placedHexes = this.placedHexes;

        Player player2 = new Player(Color.WHITE);

        try {
            board.isSettledLocationValid(player2, loc);
        } catch (InvalidMoveException e) {
            assertTrue(e.getMessage().equals("Settlement hex does not belong to the current player"));
        }
   }

   @Test
    public void testIsValidSettlementLocation() {
        Player player = new Player(Color.BLACK);
        String tileID = "fakeTileID";
        Location loc = new Location(0,1);
        Hex settledHex = new Hex(tileID, Terrain.LAKE);
        settledHex.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        PlacedHex blackSettledHex = new PlacedHex(settledHex, loc);
        placedHexes.add(blackSettledHex);
        board.placedHexes = this.placedHexes;
        Settlement settlement = new Settlement(blackSettledHex, placedHexes);

        try {
            settlement = board.isSettledLocationValid(player, loc);
        } catch (InvalidMoveException e) {
            assertTrue(settlement != null);
        }
   }
    @Test
    public void testSettlementLocationIsNotValidForExpansion() {
        Player player = new Player(Color.BLACK);
        Location invalidSettlementLocation = new Location(0,1);

        try {
            board.isSettledLocationValid(player, invalidSettlementLocation);
        } catch (InvalidMoveException e) {
            assertTrue(e.getMessage().equals("This hex does not exist"));
        }
    }

    @Test
    public void testLocationDoesNotBelongToASettlement() {
        Player player = new Player(Color.BLACK);
        String tileID = "fakeTileID";
        Location loc = new Location(0,1);
        Hex emptyHex = new Hex(tileID, Terrain.GRASSLANDS);
        PlacedHex emptyPlacedHex = new PlacedHex(emptyHex, loc);
        placedHexes.add(emptyPlacedHex);
        board.placedHexes = this.placedHexes;

        try {
            board.isSettledLocationValid(player, loc);
        } catch (InvalidMoveException e) {
            assertTrue(e.getMessage().equals("This hex does not belong in a settlement"));
        }
    }

    @Test
    public void testSettledLocationDoesNotBelongToPlayer() {
        Player player1 = new Player(Color.BLACK);
        String tileID = "fakeTileID";
        Location loc = new Location(0,1);
        Hex blackHex = new Hex(tileID, Terrain.GRASSLANDS);
        blackHex.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        PlacedHex blackPlacedHex = new PlacedHex(blackHex, loc);
        placedHexes.add(blackPlacedHex);
        board.placedHexes = this.placedHexes;

        Player player2 = new Player(Color.WHITE);

        try {
            board.isSettledLocationValid(player2, loc);
        } catch (InvalidMoveException e) {
            assertTrue(e.getMessage().equals("Settlement hex does not belong to the current player"));
        }
    }

    @Test
    public void testIsValidSettlementLocationExpansion() {
        Player player = new Player(Color.BLACK);
        String tileID = "fakeTileID";
        Location loc = new Location(0,1);
        Hex settledHex = new Hex(tileID, Terrain.LAKE);
        settledHex.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        PlacedHex blackSettledHex = new PlacedHex(settledHex, loc);
        placedHexes.add(blackSettledHex);
        board.placedHexes = this.placedHexes;
        Settlement settlement = new Settlement(blackSettledHex, placedHexes);
        board.settlements.add(settlement);

        try {
            settlement = board.isSettledLocationValid(player, loc);
        } catch (InvalidMoveException e) {
            assertFalse(settlement.size() != 1);
        }

    }

    @Test
    public void testInvalidHexLocation() {
        Location invalidLocation = new Location(0,0);

        try {
            board.isHexLocationValid(invalidLocation);
        } catch (InvalidMoveException e) {
            assertTrue(e.getMessage().equals("Target hex does not exist"));
        }
    }

    @Test
    public void testHexLocationAlreadyTaken() {
        Player player = new Player(Color.BLACK);
        String tileID = "fakeTileID";
        Location loc = new Location(0,1);
        Hex settledHex = new Hex(tileID, Terrain.LAKE);
        settledHex.addPiecesToHex(new Piece(Color.BLACK, PieceType.VILLAGER), 1);
        PlacedHex blackSettledHex = new PlacedHex(settledHex, loc);
        placedHexes.add(blackSettledHex);
        board.placedHexes = this.placedHexes;

        try {
            board.isHexLocationValid(loc);
        } catch (InvalidMoveException e) {
            assertTrue(e.getMessage().equals("Target hex already has pieces on it and cannot be expanded upon"));
        }
    }

    @Test
    public void testInvalidHexTerrain() {
        String tileID = "fakeTileID";
        Location loc = new Location(0,1);
        Hex volcanoHex = new Hex(tileID, Terrain.VOLCANO);
        PlacedHex volcanoPlacedHex = new PlacedHex(volcanoHex, loc);
        placedHexes.add(volcanoPlacedHex);
        board.placedHexes = this.placedHexes;

        try {
            board.isHexLocationValid(loc);
        } catch (InvalidMoveException e) {
            assertTrue(e.getMessage().equals("Cannot expand into a Volcano"));
        }
    }

    @Test
    public void testValidHexTerrain() {
        String tileID = "fakeTileID";
        Location loc = new Location(0,1);
        Hex validHex = new Hex(tileID, Terrain.LAKE);
        PlacedHex lakeHex = new PlacedHex(validHex, loc);
        placedHexes.add(lakeHex);
        board.placedHexes = this.placedHexes;
        Terrain lake = Terrain.GRASSLANDS;

        try {
            lake = board.isHexLocationValid(loc);
        } catch (InvalidMoveException e) {
            assertTrue(lake.getType() == "Lake");
        }
    }

    @Test
    public void testInvalidAdjacentLocations() {
        Location loc1 = new Location(0,0);
        Location loc2 = new Location(2,2);

        try {
            board.areAdjacentLocationsValid(loc1, loc2);
        } catch (InvalidMoveException e) {
            assertTrue(e.getMessage().equals("Hexes are not adjacent to one another"));
        }
    }

    @Test
    public void testValidAdjacentLocations() {
        Location loc1 = new Location(0,0);
        Location loc2 = new Location(1,0);

        try {
            board.areAdjacentLocationsValid(loc1, loc2);
        } catch (InvalidMoveException e) {
            assertTrue(e.getMessage().equals("Hexes are not adjacent to one another"));
        }
    }
}