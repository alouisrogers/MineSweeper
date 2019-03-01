import java.util.*;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//Examples class for tests
class ExamplesMineSweeper {

  //EFFECT: prints the contents of the given array list
  <T> void printArray(ArrayList<T> a) {
    System.out.println("\nBombs");

    for (T item : a) {
      System.out.println(item);
    }
  } 

  //EFFECT: prints the data about each cell for testing purposes
  void printCellData(ArrayList<Cell> clist) {

    for (Cell cell : clist) {
      System.out.println("Bomb: " + cell.bomb + ", "
          + "BombAround: " + cell.adjacentBombs);
    }
  }

  //------------------------------------------------------------
  //Initialization for variables used in tests
  MineSweeper mineSweeper;
  MineSweeper mineSweeperInput = new MineSweeper(4, 4, 8);
  Cell cell;
  Cell cellB;
  ArrayList<Integer> bombList;
  ArrayList<ArrayList<Cell>> grid;

  ArrayList<Cell> row0;
  ArrayList<Cell> row1;
  ArrayList<Cell> row2;
  ArrayList<Cell> row3;

  ArrayList<ArrayList<Cell>> testGrid;

  ArrayList<ArrayList<Cell>> smallGrid;
  MineSweeper smallMinesweeper;
  //EFFECT: initializes the mineSweeper variable with values;

  Cell cell0 = new Cell(false);
  Cell cell1 = new Cell(true);
  Cell cell2 = new Cell(true);
  Cell cell3 = new Cell(false);

  MineSweeper emptyMinesweeper;

  MineSweeper smallMinesweeper2;

  MineSweeper testFloodMinesweeper;

  void initMineSweeper() {
    this.mineSweeper = new MineSweeper();
    this.mineSweeper.makeBombList(new Random(0));
    this.bombList = this.mineSweeper.bombList;
    this.cell = new Cell(false);
    this.cellB = new Cell(true);
    this.mineSweeper.grid = this.mineSweeper.fillCells();
    this.grid = this.mineSweeper.grid; 

    row0 = new ArrayList<Cell>(Arrays.asList(cell, cell, cellB, cellB));
    row1 = new ArrayList<Cell>(Arrays.asList(cellB, cellB, cell, cell));
    row2 = new ArrayList<Cell>(Arrays.asList(cellB, cell, cell, cellB));
    row3 = new ArrayList<Cell>(Arrays.asList(cell, cell, cellB, cellB));

    testGrid = new ArrayList<ArrayList<Cell>>(Arrays.asList(row0, row1, row2, row3));

    smallMinesweeper = new MineSweeper(2, 2, 2);
    this.smallMinesweeper.makeBombList(new Random(0));
    this.smallMinesweeper.grid = 
        this.smallMinesweeper.initializeCells(this.smallMinesweeper.fillCells());
    smallGrid = this.smallMinesweeper.grid;

    //for image testing, copy of smallMinesweeper

    smallMinesweeper2 = new MineSweeper(2, 2, 2);
    this.smallMinesweeper2.makeBombList(new Random(0));
    this.smallMinesweeper2.grid = 
        this.smallMinesweeper2.initializeCells(this.smallMinesweeper2.fillCells());


    //to test flood small minesweeper with few bombs
    this.testFloodMinesweeper = new MineSweeper(4, 4, 2);
    this.testFloodMinesweeper.makeBombList(new Random(0));
    this.testFloodMinesweeper.grid =
        this.testFloodMinesweeper.initializeCells(this.testFloodMinesweeper.fillCells());

    //Initializing tester neighbor list to test initializeCells

    cell0.adjacentBombs = 2;

    cell1.adjacentBombs = 1;

    cell2.adjacentBombs = 1;

    cell3.adjacentBombs = 2;

    cell0.neighbor = new ArrayList<Cell>(Arrays.asList(cell1, cell2, cell3));
    cell1.neighbor = new ArrayList<Cell>(Arrays.asList(cell0, cell2, cell3));
    cell2.neighbor = new ArrayList<Cell>(Arrays.asList(cell0, cell1, cell3));
    cell3.neighbor = new ArrayList<Cell>(Arrays.asList(cell0, cell1, cell2));

    //empty mineSweeper
    emptyMinesweeper = new MineSweeper(0, 0, 0);
  }

  //-----------------------------------------------------------
  //Tests for MineSweeper class methods

  void testMineSweeperConstructor(Tester t) {
    MineSweeper testM = new MineSweeper(4, 4, 7);
    t.checkExpect(testM.bombs, 7);
    t.checkExpect(testM.gridHeight, 4);
    t.checkExpect(testM.gridWidth, 4);
    t.checkExpect(testM.bombList.size(), 7);
    t.checkExpect(testM.grid, testM.initializeCells(testM.fillCells()));
  }

  void testFillCells(Tester t) {
    this.initMineSweeper();
    this.printCellData(this.mineSweeper.flatten(this.grid));
    System.out.println("\ntestGrid");
    this.printCellData(this.mineSweeper.flatten(this.testGrid));

    t.checkExpect(this.grid, testGrid);
  }

  void testInitializeCells(Tester t) {
    this.initMineSweeper();

    t.checkExpect(this.smallGrid.get(0).get(0).adjacentBombs, 2);
    t.checkExpect(this.smallGrid.get(0).get(1).adjacentBombs, 1);

    t.checkExpect(this.smallGrid.get(0).get(1).neighbor, this.cell2.neighbor);
  }

  void testFlatten(Tester t) {
    this.initMineSweeper();
    MineSweeper flattenTest = new MineSweeper();

    ArrayList<Cell> test1D = new ArrayList<Cell>(Arrays.asList(cell, cell, cellB, cell));
    ArrayList<ArrayList<Cell>> testerArray = 
        new ArrayList<ArrayList<Cell>>(Arrays.asList(test1D, test1D, test1D, test1D));

    ArrayList<Cell> finalArray = 
        new ArrayList<Cell>(Arrays.asList(
            cell, cell, cell, cell, cell, cell, cell, cell, 
            cellB, cellB, cellB, cellB, cell, cell, cell, cell));
    t.checkExpect(flattenTest.flatten(testerArray), finalArray);
    t.checkExpect(this.emptyMinesweeper.flatten(new ArrayList<ArrayList<Cell>>()), 
        new ArrayList<Cell>());


  }

  void testMakeCellList(Tester t) {
    this.initMineSweeper();

    ArrayList<Integer> finalArray = new ArrayList<Integer>(
        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));

    t.checkExpect(this.mineSweeper.makeCellList(), finalArray);
  }

  void testMakeBombList(Tester t) {
    this.initMineSweeper();
    this.printArray(this.bombList);
    t.checkExpect(this.bombList.contains(14), true);
    t.checkExpect(this.bombList.get(2), 12);
    t.checkExpect(this.bombList.contains(6), false);
  }

  void testDrawCells(Tester t) {
    this.initMineSweeper();
    WorldImage row1 = new BesideImage(
        new BesideImage(new EmptyImage(), cell0.drawCell()), cell1.drawCell());
    WorldImage row2 = new BesideImage(
        new BesideImage(new EmptyImage(), cell2.drawCell()), cell3.drawCell());

    WorldImage finalGrid = new AboveImage(new AboveImage(new EmptyImage(), row1), row2);

    t.checkExpect(smallMinesweeper.drawCells(), finalGrid);
    t.checkExpect(emptyMinesweeper.drawCells(), new EmptyImage());
  }

  void testGameOver(Tester t) {
    this.initMineSweeper();

    //if called on an empty board, it should win, although this will not trigger without a click
    t.checkExpect(this.emptyMinesweeper.gameWin(), true);

    //initial setup of smallMinesweeper should not be won
    t.checkExpect(this.smallMinesweeper.gameWin(), false);

    //click and flag correct cells in smallMinesweeper
    this.smallMinesweeper.grid.get(0).get(0).click();
    this.smallMinesweeper.grid.get(0).get(1).flag();
    this.smallMinesweeper.grid.get(1).get(0).flag();
    this.smallMinesweeper.grid.get(1).get(1).click();

    t.checkExpect(this.smallMinesweeper.gameWin(), true);

  }

  void testMakeScene(Tester t) {
    this.initMineSweeper();

    WorldScene finalScene = new WorldScene(50, 50);
    finalScene.placeImageXY(this.smallMinesweeper.drawCells(), 25, 25);

    t.checkExpect(this.smallMinesweeper.makeScene(), finalScene);



    WorldScene winScene = new WorldScene(50, 50);

    WorldImage testRestart = new OverlayImage(
        new TextImage("Click Anywhere To Restart", 25 / 10, 
            FontStyle.BOLD, Color.BLACK),
        new RectangleImage(25 * (3 / 4), 
            25, OutlineMode.SOLID, Color.WHITE));

    WorldImage win = new TextImage("You Won", 25 * 0.375, 
        FontStyle.BOLD, Color.GREEN);

    WorldImage gameWinHeader = new OverlayImage(
        win,
        new RectangleImage(25 * (1 / 4), 
            25, OutlineMode.OUTLINE, Color.BLACK));

    WorldImage gameWinImg = new AboveImage(gameWinHeader, testRestart);

    winScene.placeImageXY(gameWinImg, 25, 25);

    //set small minesweeper values to true
    this.smallMinesweeper.grid.get(0).get(0).click();
    this.smallMinesweeper.grid.get(0).get(1).flag();
    this.smallMinesweeper.grid.get(1).get(0).flag();
    this.smallMinesweeper.grid.get(1).get(1).click();

    //sets the game to win
    this.smallMinesweeper.gameWin = this.smallMinesweeper.gameWin();

    t.checkExpect(this.smallMinesweeper.makeScene(), winScene);

    //tests for lose scene using copy of smallMinesweeper initialized in init method    

    this.smallMinesweeper2.grid.get(0).get(1).click();

    this.smallMinesweeper2.gameOver = this.smallMinesweeper2.grid.get(0).get(1).gameOver();

    WorldScene loseScene = new WorldScene(50, 50);
    WorldImage lose = new TextImage("Game Over", 25 * 0.375, 
        FontStyle.BOLD, Color.RED);
    WorldImage gameLoseHeader = new OverlayImage(
        lose,
        new RectangleImage(25 * (1 / 4), 
            25, OutlineMode.OUTLINE, Color.BLACK));
    WorldImage gameLoseImg = new AboveImage(gameLoseHeader, testRestart);
    loseScene.placeImageXY(gameLoseImg, 25, 25);

    t.checkExpect(this.smallMinesweeper2.makeScene(), loseScene);

  }

  void testOnMouseClicked(Tester t) {
    this.initMineSweeper();

    //test clicking
    this.smallMinesweeper.onMouseClicked(new Posn(20, 20), "LeftButton");
    t.checkExpect(this.smallMinesweeper.grid.get(0).get(0).isClicked, true);
    t.checkExpect(this.smallMinesweeper.grid.get(0).get(0).isFlagged, false);

    //test flagging
    this.smallMinesweeper.onMouseClicked(new Posn(40, 40), "RightButton");
    t.checkExpect(this.smallMinesweeper.grid.get(1).get(1).isClicked, false);
    t.checkExpect(this.smallMinesweeper.grid.get(1).get(1).isFlagged, true);

    //reinitialize minesweeper
    this.initMineSweeper();

    //test unflagging
    this.smallMinesweeper.onMouseClicked(new Posn(40, 40), "RightButton");
    this.smallMinesweeper.onMouseClicked(new Posn(40, 40), "RightButton");
    t.checkExpect(this.smallMinesweeper.grid.get(1).get(1).isClicked, false);
    t.checkExpect(this.smallMinesweeper.grid.get(1).get(1).isFlagged, false);

    //reinit minesweeper
    this.initMineSweeper();

    //can't click after flagging
    this.smallMinesweeper.onMouseClicked(new Posn(40, 40), "RightButton");
    this.smallMinesweeper.onMouseClicked(new Posn(40, 40), "LeftButton");
    t.checkExpect(this.smallMinesweeper.grid.get(1).get(1).isClicked, false);
    t.checkExpect(this.smallMinesweeper.grid.get(1).get(1).isFlagged, true);


    this.initMineSweeper();

    this.smallMinesweeper.onMouseClicked(new Posn(40, 20), "RightButton");
    this.smallMinesweeper.onMouseClicked(new Posn(20, 20), "LeftButton");
    this.smallMinesweeper.onMouseClicked(new Posn(20, 40), "RightButton");
    this.smallMinesweeper.onMouseClicked(new Posn(40, 40), "LeftButton");

    //check to make sure cells are clicked correctly
    t.checkExpect(this.smallMinesweeper.grid.get(0).get(0).isClicked, true);
    t.checkExpect(this.smallMinesweeper.grid.get(0).get(1).isFlagged, true);
    t.checkExpect(this.smallMinesweeper.grid.get(1).get(0).isFlagged, true);
    t.checkExpect(this.smallMinesweeper.grid.get(1).get(1).isClicked, true);

    //make sure game is won
    t.checkExpect(this.smallMinesweeper.gameOver, false);
    t.checkExpect(this.smallMinesweeper.gameWin, true);

    this.initMineSweeper();

    //lose game and make sure it's lost
    smallMinesweeper.onMouseClicked(new Posn(40, 20), "LeftButton");
    t.checkExpect(this.smallMinesweeper.gameOver, true);
    t.checkExpect(this.smallMinesweeper.gameWin, false);

  }

  //actually runs the world
  void testWorld(Tester t) {
    initMineSweeper();
    MineSweeper testM = new MineSweeper(4, 4, 2);
    testM.bigBang(testM.gridWidth * 25, testM.gridHeight * 25 + 100, 10);
  }

  //-----------------------------------------------------------------
  //Tests for Cell class methods

  void testCellConstructor(Tester t) {
    Cell testCellF = new Cell(false);
    Cell testCellT = new Cell(true);

    t.checkExpect(testCellF.bomb, false);
    t.checkExpect(testCellT.bomb, true);
    t.checkExpect(testCellF.neighbor, new ArrayList<Cell>());
    t.checkExpect(testCellT.cellSize, 25);
    t.checkExpect(testCellF.isClicked, false);
    t.checkExpect(testCellT.isFlagged, false);
    t.checkExpect(testCellF.adjacentBombs, 0);
  }

  void testCountBombs(Tester t) {
    this.initMineSweeper();
    this.cell.neighbor.add(this.cellB);
    this.cell.neighbor.add(this.cellB);
    this.cell.neighbor.add(this.cellB);
    this.cell.neighbor.add(this.cellB);
    this.cell.countBombs();
    t.checkExpect(this.cell.adjacentBombs, 4);    
  }

  void testClick(Tester t) {
    initMineSweeper();
    cell.click();
    t.checkExpect(cell.isClicked, true);
  }

  void testCellGameOver(Tester t) { 

    this.initMineSweeper();

    t.checkExpect(this.smallGrid.get(0).get(0).gameOver(), false);
    this.smallGrid.get(1).get(0).click();
    t.checkExpect(this.smallGrid.get(1).get(0).gameOver(), true);

  }

  void testFlood(Tester t) {

    initMineSweeper();

    //click cell at 0 0 to flood
    this.testFloodMinesweeper.grid.get(0).get(0).click();

    t.checkExpect(this.testFloodMinesweeper.grid.get(0).get(0).isClicked, true);
    t.checkExpect(this.testFloodMinesweeper.grid.get(0).get(1).isClicked, true);
    t.checkExpect(this.testFloodMinesweeper.grid.get(0).get(2).isClicked, true);
    t.checkExpect(this.testFloodMinesweeper.grid.get(0).get(3).isClicked, true);
    t.checkExpect(this.testFloodMinesweeper.grid.get(1).get(3).isClicked, true);

    //only cells that shouldn't be clicked
    t.checkExpect(this.testFloodMinesweeper.grid.get(2).get(3).isClicked, false);
    t.checkExpect(this.testFloodMinesweeper.grid.get(3).get(2).isClicked, false);
    t.checkExpect(this.testFloodMinesweeper.grid.get(3).get(3).isClicked, false);

    //TODO - add tests for the flood method
    //Do this on a small array to test the method -- could be a pain
  }

  void testFlag(Tester t) {
    Cell testCell = new Cell(true);
    testCell.flag();
    t.checkExpect(testCell.isFlagged, true);
  }

  void testDrawCell(Tester t) {
    Cell bombCell = new Cell(true);
    Cell flagCell = new Cell(true);
    flagCell.flag();
    Cell coveredCell = new Cell(false);
    Cell clickedCell = new Cell(false);
    clickedCell.click();
    bombCell.click();
    Cell fourCell = new Cell(false);
    fourCell.click();
    fourCell.adjacentBombs = 4;


    WorldImage bombImage = new OverlayImage(
        new CircleImage(8, OutlineMode.SOLID, Color.RED),
        new OverlayImage(
            new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
            new RectangleImage(25, 25, OutlineMode.SOLID, Color.DARK_GRAY)));

    WorldImage coveredImage = 
        new OverlayImage(
            new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
            new RectangleImage(25, 25, OutlineMode.SOLID, Color.LIGHT_GRAY));

    WorldImage cell0Image = new OverlayImage(
        new RectangleImage(25, 25, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(25, 25, OutlineMode.SOLID, Color.DARK_GRAY));

    WorldImage cellNImage = new OverlayImage(
        new TextImage(Integer.toString(4), 10, Color.RED), cell0Image);

    WorldImage flaggedImage = 
        new OverlayImage(
            new StarImage(8, OutlineMode.SOLID, Color.YELLOW),
            coveredImage);

    t.checkExpect(bombCell.drawCell(), bombImage);
    t.checkExpect(coveredCell.drawCell(), coveredImage);
    t.checkExpect(clickedCell.drawCell(), cell0Image);
    t.checkExpect(fourCell.drawCell(), cellNImage);
    t.checkExpect(flagCell.drawCell(), flaggedImage);
  }

  void testFillNeighbors(Tester t) {
    this.initMineSweeper();
    System.out.println("Fill Neighbors");
    this.cell.neighbor.add(this.cellB);
    this.printCellData(this.cell.neighbor);
    this.grid.get(0).get(0).fillNeighbors(this.grid, 0, 0, 4, 4);
    Cell cellA = new Cell(false);
    Cell cellC = new Cell(true);
    ArrayList<Cell> testNeighbor = new ArrayList<Cell>(Arrays.asList(cellA, cellB, cellC));

    t.checkExpect(grid.get(0).get(0).neighbor, testNeighbor);
  }

}