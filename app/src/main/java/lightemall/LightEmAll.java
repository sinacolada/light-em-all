package lightemall;

import java.util.*;

import libs.javalib.impworld.*;
import libs.javalib.worldimages.*;

import java.awt.Color;

// to represent a LightEmAll game
class LightEmAll extends World {

  // a list of rows of GamePieces,
  // i.e., represents the board in row-major order
  ArrayList<ArrayList<GamePiece>> board;

  // a list of all nodes
  ArrayList<GamePiece> nodes;

  // a list of all edges in this board
  ArrayList<Edge> edges;

  // a list of edges of the minimum spanning tree
  ArrayList<Edge> mst;

  // the width and height of the board
  int width;
  int height;

  // the current location of the power station,
  // as well as its effective radius
  int powerRow;
  int powerCol;
  int radius;

  // tracks the number of moves made and time played
  int numMoves;
  Clock time;

  // the random seed for this LightEmAll
  Random rand;

  // to construct a LightEmAll game with a fixed rand seed
  public LightEmAll(int width, int height, Random rand) {
    this.width = width;
    this.height = height;
    this.powerCol = this.width / 2;
    this.powerRow = 0;
    this.rand = rand;

    // initialize lists
    this.board = new ArrayList<ArrayList<GamePiece>>();
    for (int i = 0; i < this.height; i++) {
      board.add(new ArrayList<GamePiece>());
    }
    this.nodes = new ArrayList<GamePiece>();
    this.edges = new ArrayList<Edge>();
    this.mst = new ArrayList<Edge>();

    this.numMoves = 0;
    this.time = new Clock(0, 0, 0);

    // generates random tree using kruskal's algorithm
    this.treeGeneration();

    // sets radius to height of the minimum spanning tree formed by the algorithm
    // for the power station to be able to reach all pieces
    this.setRadius();

    // randomize the pieces
    this.randomizeRotation();

    // update connections to take randomization into account;
    this.setConnections();

    // lights wires based on power station location
    this.lightWires();
  }

  // to construct a LightEmAll game with a random seed
  public LightEmAll(int width, int height) {
    this(width, height, new Random());
  }

  // EFFECT: generates this board and its wire arrangement
  public void treeGeneration() {

    // initialize game pieces and add to board
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        GamePiece toAdd;
        boolean powerStation = false;

        // set initial powerStation location
        if (i == 0 && j == 0) {
          powerStation = true;
        }

        toAdd = new GamePiece(i, j, false, false, false, false, powerStation);

        this.board.get(i).add(toAdd);
        this.nodes.add(toAdd);
      }
    }

    // set all possible connections
    this.connectAll();

    // randomly assign weights to all connections
    this.initializeConnections();

    // add edges by weight w/o cycles until mst is formed
    this.createMST();

    // set connections created in the mst
    this.setMSTConnections();
  }

  // EFFECT: connects all GamePieces in this LighEmAll to their adjacent pieces
  public void connectAll() {
    for (ArrayList<GamePiece> row : this.board) {
      for (GamePiece g : row) {

        // add each connection if not off of the board (edge cases)
        // not on very top of board
        if (g.row != 0) {
          g.top = true;
        }

        // not on very bottom of board
        if (g.row != this.height - 1) {
          g.bottom = true;
        }

        // not on very left of board
        if (g.col != 0) {
          g.left = true;
        }

        // not on very right of board
        if (g.col != this.width - 1) {
          g.right = true;
        }
      }
    }
  }

  // EFFECT: updates connections of a game piece and this LightEmAll's list of Edges
  // with random edge weights in the range [0, 100]
  public void updateInitialConnections(GamePiece g) {

    // create empty list of connections
    ArrayList<Edge> connections = new ArrayList<Edge>();

    // check all four sides of the GamePiece, g, for connections
    // and add if they exist

    // left connection
    if (g.left) {
      GamePiece leftPiece = this.board.get(g.row).get(g.col - 1);
      Edge leftEdge = new Edge(g, leftPiece, this.rand.nextInt(101));
      connections.add(leftEdge);
      this.edges.add(leftEdge);
    }

    // right connection
    if (g.right) {
      GamePiece rightPiece = this.board.get(g.row).get(g.col + 1);
      Edge rightEdge = new Edge(g, rightPiece, this.rand.nextInt(101));
      connections.add(rightEdge);
      this.edges.add(rightEdge);
    }

    // top connection
    if (g.top) {
      GamePiece topPiece = this.board.get(g.row - 1).get(g.col);
      Edge topEdge = new Edge(g, topPiece, this.rand.nextInt(101));
      connections.add(topEdge);
      this.edges.add(topEdge);
    }

    // bottom connection
    if (g.bottom) {
      GamePiece bottomPiece = this.board.get(g.row + 1).get(g.col);
      Edge bottomEdge = new Edge(g, bottomPiece, this.rand.nextInt(101));
      connections.add(bottomEdge);
      this.edges.add(bottomEdge);
    }

    // set g's connections to the final list
    g.connections = connections;
  }

  // EFFECT: sets connections and adjacent cells for each game piece
  public void initializeConnections() {
    for (ArrayList<GamePiece> row : this.board) {
      for (GamePiece g : row) {
        this.updateInitialConnections(g);
      }
    }
  }

  // EFFECT: creates the minimum spanning tree
  public void createMST() {

    // shallow copy edges
    ArrayList<Edge> sortedEdges = new ArrayList<Edge>(this.edges);

    // sort edges by least weight
    sortedEdges.sort(new CompareEdgeWeight());

    // position is a unique identifying feature of each cell
    // a map of positions representing this game's GamePieces connections
    HashMap<Posn, Posn> reps = new HashMap<Posn, Posn>();

    // initialize a find class for finding topmost representative
    Find<Posn> find = new Find<Posn>();

    // initialize every node's representative to itself
    for (GamePiece g : this.nodes) {
      reps.put(new Posn(g.col, g.row), new Posn(g.col, g.row));
    }

    // makes sure that full mst is not done generating yet
    while (this.mst.size() != this.nodes.size() - 1) {

      // get cheapest edge
      Edge current = sortedEdges.get(0);

      // gets pieces cheapest edge connects
      GamePiece from = current.fromNode;
      GamePiece to = current.toNode;

      // get piece positions
      Posn fromPos = new Posn(from.col, from.row);
      Posn toPos = new Posn(to.col, to.row);

      // get piece representatives
      Posn fromRep = find.apply(reps, fromPos);
      Posn toRep = find.apply(reps, toPos);

      // discard piece if same representative
      if (fromRep.x == toRep.x && fromRep.y == toRep.y) {
        sortedEdges.remove(0);
      }

      // else set new representative and add to minimum spanning tree
      else {
        mst.add(current);
        reps.put(toRep, fromRep);
      }
    }
  }

  // EFFECT: sets the correct connections for all GamePieces
  public void setMSTConnections() {

    // reset all connections of GamePieces in this LightEmAll
    for (GamePiece g : this.nodes) {
      g.left = false;
      g.right = false;
      g.top = false;
      g.bottom = false;
      g.connections = new ArrayList<Edge>();
    }

    // set the new left, right, top, bottom and connections of
    // each GamePiece according to the MST
    for (Edge e : this.mst) {

      // get mst edge connections
      GamePiece from = e.fromNode;
      GamePiece to = e.toNode;

      // its an undirected graph - add edges to both pieces
      from.connections.add(e);
      to.connections.add(
          new Edge(to, from, e.weight));

      // constants for the rows/columns of GamePieces in e
      int fromRow = from.row;
      int toRow = to.row;
      int fromCol = from.col;
      int toCol = to.col;

      // set the fromNode and toNode's cardinal directions appropriately
      if (fromRow == toRow && fromCol == (toCol - 1)) {
        from.right = true;
        to.left = true;
      }

      if (fromRow == toRow && fromCol == (toCol + 1)) {
        from.left = true;
        to.right = true;
      }

      if (fromRow == (toRow - 1) && fromCol == toCol) {
        from.bottom = true;
        to.top = true;
      }

      if (fromRow == (toRow + 1) && fromCol == toCol) {
        from.top = true;
        to.bottom = true;
      }
    }
  }

  // EFFECT: sets radius to height of the minimum spanning tree
  public void setRadius() {

    int radius = 0;

    // get the filtered list of terminal nodes in the tree
    ArrayList<GamePiece> terminalNodes = new ArrayList<GamePiece>(this.nodes);
    terminalNodes.removeIf(new BranchPiece());

    // for each node in the list of terminal nodes 
    for (GamePiece root : terminalNodes) {

      // get the extended pieces of the root, which has the effect of setting
      // their distances to the distances away from this root node
      ArrayList<GamePiece> rootConnections = root.extendedPieces();

      // filter all elements in rootConnections and keep only terminal nodes
      rootConnections.removeIf(new BranchPiece());

      // check maximum distance from root node, then set it as the radius
      for (GamePiece g : rootConnections) {
        radius = Math.max(g.distance, radius);
      }
      // repeat loop to get maximum distance
    }

    // set the radius as the half the max distance + 1
    this.radius = Math.floorDiv(radius, 2) + 1;
  }

  // EFFECT: randomizes each piece by rotating it
  public void randomizeRotation() {

    for (GamePiece g : this.nodes) {
      // random number between 0 and 3
      int numRotations = this.rand.nextInt(3);

      // rotates by random number
      for (int i = 0; i < numRotations; i++) {
        g.rotateRight();
      }
    }
  }

  // EFFECT: updates connections of a game piece dependent on board
  public void updateConnections(GamePiece g) {

    ArrayList<Edge> connections = new ArrayList<Edge>();
    int row = g.row;
    int col = g.col;

    // edge weights dont matter anymore - set a constant
    int edgeWeight = 100;

    // not on topmost row
    if (row != 0) {
      GamePiece comp = this.board.get(row - 1).get(col);

      if (g.topConnection(comp)) {
        connections.add(new Edge(g, comp, edgeWeight));
      }
    }

    // not on bottommost row
    if (row != this.height - 1) {
      GamePiece comp = this.board.get(row + 1).get(col);

      if (g.bottomConnection(comp)) {
        connections.add(new Edge(g, comp, edgeWeight));
      }
    }

    // not on leftmost col
    if (col != 0) {
      GamePiece comp = this.board.get(row).get(col - 1);

      if (g.leftConnection(comp)) {
        connections.add(new Edge(g, comp, edgeWeight));
      }
    }

    // not on rightmost col
    if (col != this.width - 1) {
      GamePiece comp = this.board.get(row).get(col + 1);

      if (g.rightConnection(comp)) {
        connections.add(new Edge(g, comp, edgeWeight));
      }
    }
    g.connections = connections;
  }

  // EFFECT: sets connections and adjacent cells for each game piece
  public void setConnections() {
    for (ArrayList<GamePiece> row : this.board) {
      for (GamePiece g : row) {
        this.updateConnections(g);
      }
    }
  }

  // lights wires if there exists a path from game piece to power station
  public void lightWires() {

    // get extended pieces from power station and update their distances appropriately
    ArrayList<GamePiece> powerPieceExtended = 
        this.board.get(this.powerRow).get(this.powerCol).extendedPieces();

    // reset lighting
    for (ArrayList<GamePiece> row : this.board) {
      for (GamePiece g : row) {
        g.lit = false;
      }
    }

    // set new lighting if within effective radiius and connected to power station
    for (GamePiece g : powerPieceExtended) {
      if (g.distance <= this.radius) {
        g.lit = true;
      }
    }
  }

  // draws the game scene
  public WorldScene makeScene() {

    // scene
    WorldScene scene = this.getEmptyScene();

    // draws each piece on the scene
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        scene.placeImageXY(this.board.get(i).get(j).draw(this.radius), 100 * j + 50, 100 * i + 50);
      }
    }

    // draws the power station on the board scene
    WorldImage powerStation = new EmptyImage();
    WorldImage powerTriangle = new RegularPolyImage(40, 3, OutlineMode.SOLID, 
        new Color(0, 255, 127));
    for (int i = 0; i < 3; i++) {
      powerStation = new OverlayImage(new RotateImage(powerTriangle, i * 80), powerStation);
    }
    scene.placeImageXY(powerStation, 100 * this.powerCol + 50, 100 * this.powerRow + 50);

    // draws the current moves and current time played on the bottom of the window
    WorldImage movesImg = new OverlayImage(
        new TextImage("Moves: [" + this.numMoves + "]", 20, Color.WHITE),
        new RectangleImage((this.width - 1) * 50, 100, OutlineMode.SOLID, new Color(0, 0, 0, 120)));
    WorldImage timeImg = new OverlayImage(
        new TextImage("Time: [" + this.time.asString() + "]", 20, Color.WHITE),
        new RectangleImage((this.width - 1) * 50, 100, OutlineMode.SOLID, new Color(0, 0, 0, 120)));
    WorldImage scoreImg = new FrameImage(new BesideImage(movesImg, timeImg));

    // draws the buttons for restarting, and exiting the game
    WorldImage restartButton = new FrameImage(new OverlayImage(
        new TextImage("New Game", 18, Color.RED),
        new RectangleImage(100, 50, OutlineMode.SOLID, new Color(0, 0, 0, 120))));
    WorldImage exitButton = new FrameImage(new OverlayImage(
        new TextImage("Exit", 18, Color.RED),
        new RectangleImage(100, 50, OutlineMode.SOLID, new Color(0, 0, 0, 120))));
    WorldImage buttons = new AboveImage(restartButton, exitButton);

    // places the display for moves, time, and buttons at the bottom of the screen
    WorldImage bottomDisplay = new BesideImage(scoreImg, buttons);
    scene.placeImageXY(bottomDisplay, this.width * 50, this.height * 100 + 50);

    // sets action tab: allows restarting game, exiting game, and displays current game time
    return scene;
  }

  // finds corresponding piece given its position
  public GamePiece findPiece(Posn pos) {
    int row = pos.y / 100;
    int col = pos.x / 100;

    return this.board.get(row).get(col);
  }

  // handles rotation on mouse click
  public void onMouseClicked(Posn pos, String button) {

    // get row and column
    int row = pos.y / 100;
    int col = pos.x / 100;

    // only process a click if it was on a cell
    if (row < this.height && col < this.width) {
      GamePiece pieceClicked = this.findPiece(pos);

      // left click rotates game piece clicked left
      if (button.equals("LeftButton")) {
        pieceClicked.rotateLeft();
      }
      // right click rotates game piece clicked right
      else if (button.equals("RightButton")) {
        pieceClicked.rotateRight();
      }

      // update moves, connections, and lighting after click
      this.numMoves++;
      this.setConnections();
      this.lightWires();

      // check whether win condition is satisfied
      if (this.won()) {
        this.endOfWorld("You Win!");
      }
    }

    // process clicks on buttons

    // new game button
    else if (pos.y > (this.height * 100) 
        && pos.y < ((this.height * 100) + 50)
        && pos.x > ((this.width - 1) * 100)) {
      this.endOfWorld("Restarted");
      new LightEmAll(this.width, this.height, this.rand).bigBang(
          this.width * 100, 
          this.height * 100 + 100,
          1);
    }

    // exit button
    else if (pos.y > ((this.height * 100) + 50) 
        && pos.y < ((this.height + 1) * 100)
        && pos.x > ((this.width - 1) * 100)) {
      this.endOfWorld("Exit Game");
    }
  }

  // handles the on-tick updating of the time played
  public void onTick() {
    this.time.incrementSec();
  }

  // handles PowerStation movement
  public void onKeyEvent(String key) {

    // move PowerStation left if not in leftmost column and a left connection exists
    if (key.equals("left") && this.powerCol != 0) {
      if (this.board.get(this.powerRow).get(this.powerCol).leftConnection(
          this.board.get(this.powerRow).get(this.powerCol - 1))) {
        this.powerCol -= 1;
        this.lightWires();
        this.numMoves++;
      }
    }
    // move PowerStation right if not in rightmost column and a right connection exists
    else if (key.equals("right") && this.powerCol != this.width - 1) {
      if (this.board.get(this.powerRow).get(this.powerCol).rightConnection(
          this.board.get(this.powerRow).get(this.powerCol + 1))) {
        this.powerCol += 1;
        this.lightWires();
        this.numMoves++;
      }
    }
    // move PowerStation up if not in top row and a top connection exists
    else if (key.equals("up") && this.powerRow != 0) {
      if (this.board.get(this.powerRow).get(this.powerCol).topConnection(
          this.board.get(this.powerRow - 1).get(this.powerCol))) {
        this.powerRow -= 1;
        this.lightWires();
        this.numMoves++;
      }
    }
    // move PowerStation down if not in bottom row and a bottom connection exists
    else if (key.equals("down") && this.powerRow != this.height - 1) {
      if (this.board.get(this.powerRow).get(this.powerCol).bottomConnection(
          this.board.get(this.powerRow + 1).get(this.powerCol))) {
        this.powerRow += 1;
        this.lightWires();
        this.numMoves++;
      }
    }

    // check whether win condition is satisfied
    if (this.won()) {
      this.endOfWorld("You Win!");
    }
  }

  // returns whether all cells are lit
  public boolean won() {
    boolean won = true;
    for (GamePiece gp : this.nodes) {
      won = gp.lit && won;
    }
    return won;
  }

  // scene shown when the world ends
  public WorldScene lastScene(String msg) {

    // get the scene to be drawn
    WorldScene finalReveal = this.makeScene();

    // overlay the text
    WorldImage winImg = new OverlayImage(new TextImage(msg, 30, Color.WHITE),
        new RectangleImage(180, 60, OutlineMode.SOLID, new Color(0, 0, 0, 120)));
    WorldImage scoreImg = new OverlayImage(
        new TextImage("Total Moves: [" + this.numMoves + "]", 15, Color.WHITE),
        new RectangleImage(180, 30, OutlineMode.SOLID, new Color(0, 0, 0, 120)));
    WorldImage timeImg = new OverlayImage(
        new TextImage("Total Time: [" + this.time.asString() + "]", 15, Color.WHITE),
        new RectangleImage(180, 30, OutlineMode.SOLID, new Color(0, 0, 0, 120)));
    WorldImage txtImg = new AboveImage(winImg, scoreImg, timeImg);
    finalReveal.placeImageXY(txtImg, this.width * 50, this.height * 50);

    return finalReveal;
  }
}
