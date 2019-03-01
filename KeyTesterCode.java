import java.util.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;


class MineSweeper extends World {
  
  int gridHeight;
  int gridWidth;
  ArrayList<ArrayList<Cell>> grid; 
  int flags;
  int bombs;
  ArrayList<Integer> bombList;
  boolean gameOver;
  boolean gameWin;
  boolean inputWidth;
  boolean inputHeight;
  boolean inputBomb;
  String inputState;
  

  MineSweeper(int gridWidth, int gridHeight, int bombs) {
    this.gridWidth = gridWidth;
    this.gridHeight = gridHeight;
    if(bombs > gridWidth * gridHeight - 1) {
    	this.bombs = gridWidth * gridHeight - 1;
    }
    this.bombs = bombs;
    this.flags = bombs;
    this.makeBombList(new Random());
    this.grid = this.initializeCells(this.fillCells());
  }
 
  MineSweeper() {
	  this.inputWidth = true;
	  this.inputHeight = false;
	  this.inputBomb = false;
	  this.inputState = "";
  }
   
  //add all of the cells to the 2d grid
  ArrayList<ArrayList<Cell>> fillCells() {
    ArrayList<ArrayList<Cell>> grid = new ArrayList<ArrayList<Cell>>();

    for (int column = 0; column < this.gridWidth; column++) {
      ArrayList<Cell> tempArray = new ArrayList<Cell>();

      for (int row = 0; row < this.gridHeight; row++) {

        if (this.bombList.contains(this.gridWidth * row + column)) {
          tempArray.add(new Cell(true));
        }

        else {
          tempArray.add(new Cell(false));
        }
      }
      grid.add(tempArray);
    }
    return grid;
  }

  //add the neighbors and number of bombs to each cell in the 2d grid
  ArrayList<ArrayList<Cell>> initializeCells(ArrayList<ArrayList<Cell>> grid) {

    for (int column = 0; column < this.gridWidth; column++) {

      for (int row = 0; row < this.gridHeight; row++) {
        grid.get(column).get(row).fillNeighbors(grid, column, row, this.gridWidth, this.gridHeight);
        grid.get(column).get(row).countBombs();
      }
    }
    return grid;
  }


  //flatten the 2D array into a 1D array 
  ArrayList<Cell> flatten(ArrayList<ArrayList<Cell>> grid) {
    ArrayList<Cell> flatArray = new ArrayList<Cell>();

    for (int row = 0; row < this.gridHeight; row++) {

      for (int column = 0; column < this.gridWidth; column++) {
        flatArray.add(grid.get(column).get(row));
      }
    }
    return flatArray;
  }

  //make a list of the number of all of the cells 
  ArrayList<Integer> makeCellList() {
    int numCells = this.gridHeight * this.gridWidth;
    ArrayList<Integer> cellList = new ArrayList<Integer>();

    for (int i = 0; i < numCells; i++) {
      cellList.add(i);
    }

    return cellList;
  } 

  //EFFECT: Add to this bombList all the indices that have bombs
  void makeBombList(Random rand) {
    ArrayList<Integer> cellList = this.makeCellList();
    this.bombList = new ArrayList<Integer>(); 

    for (int j = 0; j < this.bombs; j++) {
      this.bombList.add(cellList.remove(rand.nextInt(cellList.size())));
    }
  }

  //draw all of the cells in the given grid onto the world canvas
  public WorldImage drawCells() {

    WorldImage grid = new EmptyImage();

    for (int row = 0; row < this.gridHeight; row++) {
      WorldImage rowImage = new EmptyImage();

      for (int column = 0; column < this.gridWidth; column++) {
        rowImage = new BesideImage(rowImage, this.grid.get(column).get(row).drawCell());
      }
      grid = new AboveImage(grid, rowImage);
    }

    return grid;
  }

  boolean gameWin() {
    int numFlagged = 0;
    for (int row = 0; row < this.gridHeight; row++) {
      for (int column = 0; column < this.gridWidth; column++) {
        if (grid.get(column).get(row).isFlagged && grid.get(column).get(row).bomb) {
          numFlagged += 1;
        }
      }
    }
    int numClicked = 0;
    for (int row = 0; row < this.gridHeight; row++) {
      for (int column = 0; column < this.gridWidth; column ++) {
        if (grid.get(column).get(row).isClicked && !grid.get(column).get(row).isFlagged) {
          numClicked += 1;
        }
      }
    }

    return numFlagged == bombs && (numFlagged + numClicked) == (gridWidth * gridHeight);
  }

  //inherits from World, makes the WorldScene for MineSweeper using the drawCells function
  @Override
  public WorldScene makeScene() {
	System.out.println(this.inputWidth || this.inputHeight || this.inputBomb);
    WorldScene scene = new WorldScene(this.gridWidth * 25, this.gridHeight * 25);
    int boardHeight = (this.gridHeight * 25) / 2;
    int boardWidth = (this.gridWidth * 25) / 2;	
    WorldImage InputWidth = 
    				new TextImage("Choose Width: " + this.inputState , 15, FontStyle.REGULAR,Color.BLACK);
    WorldImage InputHeight = 
			new TextImage("Choose Height: " + this.inputState , 15, FontStyle.REGULAR,Color.BLACK);
    WorldImage InputBomb = 
			new TextImage("Choose Number of Bombs: " + this.inputState , 15, FontStyle.REGULAR,Color.BLACK);
    				
    WorldImage gameFinished = new EmptyImage();
    WorldImage infoBox = new OverlayImage(
        new TextImage("Number of Flags: " + Integer.toString(this.flags), 15,
            FontStyle.REGULAR,Color.BLACK),
        new RectangleImage(this.gridWidth * 25, 100, OutlineMode.OUTLINE, Color.BLACK));
    if(this.inputWidth) {
    	scene.placeImageXY(InputWidth, 100, 100);
    	return scene;
    }
    else if(this.inputHeight) {
    	scene.placeImageXY(InputHeight, 100, 100);
    	return scene;
    }
    else if(this.inputBomb) {
    	scene.placeImageXY(InputBomb, 100, 100);
    	return scene;
    }
    else if(this.gameOver || this.gameWin) {
      if(this.gameOver) {
        gameFinished =  new TextImage("Game Over", boardHeight * 0.375, 
            FontStyle.BOLD, Color.RED);
      }
      if(this.gameWin) {      
        gameFinished = new TextImage("You Won", boardHeight * 0.375, 
            FontStyle.BOLD, Color.GREEN);
      }
      WorldImage restart = 
          new OverlayImage(
              new TextImage("Click Anywhere To Restart", boardHeight / 10, 
                  FontStyle.BOLD, Color.BLACK),
              new RectangleImage(boardHeight * (3 / 4), 
                  boardWidth, OutlineMode.SOLID, Color.WHITE));

      WorldImage header = 
          new OverlayImage(
              gameFinished,
              new RectangleImage(boardHeight * (1 / 4), 
                  boardWidth, OutlineMode.OUTLINE, Color.BLACK));
      WorldImage endGameScreen = new AboveImage(header, restart);
      scene.placeImageXY(endGameScreen, boardWidth, boardHeight);
      return scene;

    }
    else {
      WorldImage gameBoard = 
          new AboveImage(
              this.drawCells(),
              infoBox);
      scene.placeImageXY(gameBoard, boardWidth, boardHeight + 50);
      return scene;
    }
  }

  //inherits from World, detects mouse clicks from the user and does the appropriate action,
  //flags/unflags if the button is a right click, and clicks open the cell if it's a left click
  @Override
  public void onMouseClicked(Posn pos, String button) {
    if(this.gameOver || this.gameWin) {
      this.gameOver = false;
      this.gameWin = false;
      this.makeBombList(new Random());
      this.grid = this.initializeCells(this.fillCells());
      this.flags = this.bombs;

    }
    else {
      for (int row = 0; row < this.gridWidth; row++) {

        for (int column = 0; column < this.gridHeight; column++) {

          if ((column * 25) < pos.x && pos.x < (column + 1) * 25
              && (row * 25) < pos.y && pos.y < (row + 1) * 25) {

            if (button.equals("LeftButton")) {
              Cell cell = this.grid.get(column).get(row);
              cell.click();
              this.gameOver = cell.gameOver();
              this.gameWin = this.gameWin() && this.flags == 0;
            }

            else if (button.equals("RightButton")) { 
              this.flags += this.grid.get(column).get(row).flag();
              this.gameWin = this.gameWin();
              System.out.print(this.gameWin);
              System.out.println(this.flags);
              
            }
          }
        }
      }
    }
  }

  public void onKeyEvent(String key) {
	  if(key.length() == 1) { 
		  this.inputState += key;
	  }
	  if(this.inputWidth && key.equals("enter")) {
		  this.gridWidth = Integer.parseInt(this.inputState);
		  System.out.println("input width");
		  this.inputState = "";
		  this.inputWidth = false;
		  this.inputHeight = true;  
	  }
	  else if(this.inputHeight && key.equals("enter")) {
		  this.gridHeight = Integer.parseInt(this.inputState);
		  System.out.println("input height");
		  this.inputState = "";
		  this.inputHeight = false;
		  this.inputBomb = true;
	  }
	  else if(this.inputBomb && key.equals("enter")) {
		  this.bombs = Integer.parseInt(this.inputState);
		  if(this.bombs >= this.gridHeight * this.gridWidth) {
			  this.bombs = this.gridHeight + this.gridWidth - 1;
		  }
		  this.inputState = "";
		  this.inputBomb = false;
		  this.flags = this.bombs;
		  this.makeBombList(new Random());
		  this.grid = this.initializeCells(this.fillCells());
	  }
  }

}

//class that contains data and methods involving a cell's state (clicked, unclicked), flag status,
//bomb status, size, adjacent bombs, and neighboring cells
class Cell {
  ArrayList<Cell> neighbor;
  boolean bomb;
  int adjacentBombs;
  boolean isFlagged;
  boolean isClicked;
  int cellSize;

  //standard constructor for Cell, sets the number of adjacent bombs to 0, the size to 25,
  //flag to false, neighbors to empty, and bomb to the given bomb value
  Cell(boolean bomb) {
    this.neighbor = new ArrayList<Cell>();
    this.bomb = bomb;
    this.adjacentBombs = 0;
    this.isFlagged = false;
    this.cellSize = 25;
  }

  //EFFECT: counts the number of bombs in a list of cells
  //and updates the adjacentBombs field accordingly
  void countBombs() {

    for (Cell cell : neighbor) {

      if (cell.bomb) {
        this.adjacentBombs ++;
      }
    }
  }

  //EFFECT: sets this cell's isClicked to true when user clicks it,
  //if the cell is not already flagged
  //and floods the area around this cell
  void click() {
    if (!this.isFlagged) {
      this.isClicked = true;
      this.flood(new ArrayList<Cell>(Arrays.asList(this))); 
    }

  }

  boolean gameOver() {
    return this.bomb && this.isClicked && !this.isFlagged;
  }

  //EFFECT: implements a breadth-first search to fill this cell's neighbors
  //and each of those cells' neighbors
  void flood(ArrayList<Cell> seen) {
    for (Cell cell : this.neighbor) {
      if(!seen.contains(cell) && this.adjacentBombs == 0 && !this.isFlagged) { 
        seen.add(cell);
        if(!cell.isFlagged) {
          cell.isClicked = true;
        }
        cell.flood(seen);
      }
    }
  }

  //EFFECT: makes the isFlagged field of this object true
  //also returns whether to increment or decrement the flag value
  int flag() {

    if (!this.isClicked) {
      this.isFlagged = !this.isFlagged;
      if (isFlagged) {
        return -1;
      }
      else {
        return 1;

      }
    }
    else {
      return 0;
    }
  }


  //draws the given cell, depending on the cell's state as a bomb, flag, covered, or uncovered
  WorldImage drawCell() {

    WorldImage bombCell = new OverlayImage(
        new CircleImage(8, OutlineMode.SOLID, Color.RED),
        new OverlayImage(
            new RectangleImage(this.cellSize, this.cellSize, OutlineMode.OUTLINE, Color.BLACK),
            new RectangleImage(this.cellSize, this.cellSize, OutlineMode.SOLID, Color.DARK_GRAY)));

    WorldImage covered = 
        new OverlayImage(
            new RectangleImage(this.cellSize, this.cellSize, OutlineMode.OUTLINE, Color.BLACK),
            new RectangleImage(this.cellSize, this.cellSize, OutlineMode.SOLID, Color.LIGHT_GRAY));

    WorldImage cell0 = 
        new OverlayImage(
            new RectangleImage(this.cellSize, this.cellSize, OutlineMode.OUTLINE, Color.BLACK),
            new RectangleImage(this.cellSize, this.cellSize, OutlineMode.SOLID, Color.DARK_GRAY));

    WorldImage cellN = 
        new OverlayImage(
            new TextImage(Integer.toString(this.adjacentBombs), 10, Color.RED), cell0);

    WorldImage flagged = 
        new OverlayImage(
            new StarImage(8, OutlineMode.SOLID, Color.YELLOW),
            covered);

    if (this.isClicked) {

      if (this.bomb) {
        return bombCell;
      }

      else if (this.adjacentBombs > 0) {
        return cellN;
      }

      else {
        return cell0;
      }
    }

    else if (this.isFlagged) {
      return flagged;
    }

    else {
      return covered;
    }
  }

  //EFFECT: adds the neighboring cells of this cell to this cell's neighbor field (as an ArrayList)
  void fillNeighbors(ArrayList<ArrayList<Cell>> grid, int column, int row, int width, int height) {

    for (int i = -1; i < 2; i ++) {

      for (int j = -1; j < 2; j++) {

        if (!(i == 0 && j == 0) 
            && column + i  >= 0 
            && column + i < width 
            && row + j >= 0 
            && row + j < height) {
          this.neighbor.add(grid.get(column + i).get(row + j));
        }
      }
    }
  }
}