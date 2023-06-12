package lightemall;

import java.awt.Color;
import java.util.ArrayList;

import libs.javalib.worldimages.*;

// to represent a game piece
class GamePiece {
  // in logical coordinates, with the origin
  // at the top-left corner of the screen
  int row;
  int col;

  // whether this GamePiece is connected to the
  // adjacent left, right, top, or bottom pieces
  boolean left;
  boolean right;
  boolean top;
  boolean bottom;

  // whether the power station is on this piece
  boolean powerStation;

  // list of connected game pieces (from this node to other node)
  ArrayList<Edge> connections;

  // whether this node has a connection to the power station (lit or not)
  boolean lit;

  // distance from power station
  int distance;

  // to construct a GamePiece
  public GamePiece(int row, int col, boolean left, boolean right,
      boolean top, boolean bottom, boolean powerStation) {
    this.row = row;
    this.col = col;
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
    this.powerStation = powerStation;

    this.lit = false;
    this.distance = 0;
    this.connections = new ArrayList<Edge>();
  }

  // draws the game piece
  public WorldImage draw(int radius) {

    Color wireColor;

    if (this.lit && this.distance <= radius) {
      wireColor = new Color(
          (int)(255.0 - 100 * ((1000 * (double)this.distance) / (1000 * (double)radius))),
          (int)(255.0 - 200 * ((1000 * (double)this.distance) / (1000 * (double)radius))),
          0);
    }
    else {
      wireColor = new Color(128, 128, 128);
    }

    // draws piece
    WorldImage pieceImg = new OverlayImage(new RectangleImage(10, 10,
        OutlineMode.SOLID, wireColor), new RectangleImage(100, 100, 
            OutlineMode.SOLID,
            new Color(64, 64, 64)));

    // draws wires dependent on this piece
    WorldImage horizontalWire = new RectangleImage(50, 10, OutlineMode.SOLID, wireColor);
    WorldImage verticalWire = new RectangleImage(10, 50, OutlineMode.SOLID, wireColor);
    if (this.left) {
      pieceImg = new OverlayImage(horizontalWire, pieceImg.movePinhole(-25, 0))
          .movePinhole(25, 0);
    }
    if (this.right) {
      pieceImg = new OverlayImage(horizontalWire, pieceImg.movePinhole(25, 0))
          .movePinhole(-25, 0);
    }
    if (this.top) {
      pieceImg = new OverlayImage(verticalWire, pieceImg.movePinhole(0, -25))
          .movePinhole(0, 25);
    }
    if (this.bottom) {
      pieceImg = new OverlayImage(verticalWire, pieceImg.movePinhole(0, 25))
          .movePinhole(0, -25);
    }

    return new FrameImage(pieceImg);
  }

  // gets all extended pieces of this piece based on connections
  public ArrayList<GamePiece> extendedPieces() {

    ArrayList<GamePiece> extended = new ArrayList<GamePiece>();
    ArrayList<GamePiece> workList = new ArrayList<GamePiece>();

    workList.add(this);
    this.distance = 0;

    while (workList.size() > 0) {
      GamePiece g = workList.remove(0);
      extended.add(g);
      for (Edge e : g.connections) {
        if (!extended.contains(e.toNode)) {
          workList.add(e.toNode);
          e.toNode.updateDistance(g.distance);
        }
      }
    }
    return extended;
  }

  // rotates this piece left
  public void rotateLeft() {
    boolean temp = this.top;
    this.top = this.right;
    this.right = this.bottom;
    this.bottom = this.left;
    this.left = temp;
  }

  // rotates this piece right
  public void rotateRight() {
    boolean temp = this.top;
    this.top = this.left;
    this.left = this.bottom;
    this.bottom = this.right;
    this.right = temp;
  }

  // determines whether this game piece has g connected to its left
  public boolean leftConnection(GamePiece g) {
    if (this.left) {
      if (g.right) {
        return true;
      }
    }
    return false;
  }

  //determines whether this game piece has g connected to its right
  public boolean rightConnection(GamePiece g) {
    if (this.right) {
      if (g.left) {
        return true;
      }
    }
    return false;
  }

  //determines whether this game piece has g connected to its top
  public boolean topConnection(GamePiece g) {
    if (this.top) {
      if (g.bottom) {
        return true;
      }
    }
    return false;
  }

  //determines whether this game piece has g connected to its bottom
  public boolean bottomConnection(GamePiece g) {
    if (this.bottom) {
      if (g.top) {
        return true;
      }
    }
    return false;
  }

  // updates distance to power station by 1
  public void updateDistance(int d) {
    this.distance = d + 1;
  }
}