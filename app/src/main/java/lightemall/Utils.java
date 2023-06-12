package lightemall;

import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;

// finds topmost representative of a node
class Find<T> {

  // returns the end-most representative value in the HashMap<T>
  public T apply(HashMap<T, T> map, T t) {

    // own rep, return (its a root rep.)
    if (map.get(t).equals(t)) {
      return t;
    }
    // find rep 1 level up on tree
    else {
      return this.apply(map, map.get(t));
    }
  }
}

// to represent a comparator of Edges by their weights
class CompareEdgeWeight implements Comparator<Edge> {

  // applies this functional object to the given Edges
  // Returns positive integer : When edge0 > edge1
  // Returns 0                : When edge0 = edge1
  // Returns negative integer : When edge0 < edge1
  public int compare(Edge edge0, Edge edge1) {
    return edge0.weight - edge1.weight;
  }
}

// to represent a predicate of GamePieces that determines
// if a GamePiece is a branch (NOT a terminal node) in the tree
class BranchPiece implements Predicate<GamePiece> {

  // returns whether the given GamePiece is a branch
  public boolean test(GamePiece gp) {

    // initialize the number of connections to 0
    int numConnections = 0;

    // add 1 for each connection the GamePiece has
    numConnections = gp.left ? numConnections + 1 : numConnections;
    numConnections = gp.right ? numConnections + 1 : numConnections;
    numConnections = gp.top ? numConnections + 1 : numConnections;
    numConnections = gp.bottom ? numConnections + 1 : numConnections;

    // terminal nodes have only one connection
    // return whether the GamePiece has more than one connection
    return numConnections > 1;
  }
}

// to represent time as a clock value
class Clock {
  int hr;
  int min;
  int sec;

  // to construct a Clock at a given time
  public Clock(int hours, int minutes, int seconds) {
    // set this.hr to the given hours modded by 24, or 0 if negative
    if (0 <= hours) {
      this.hr = Math.floorMod(hours, 24);
    }
    else {
      this.hr = 0;
    }
    
    // set this.min to the given minutes modded by 60, or 0 if negative
    if (0 <= minutes) {
      this.min = Math.floorMod(minutes, 60);
    }
    else {
      this.min = 0;
    }
    
    // set this.sec to the given seconds modded by 60, or 0 if negative
    if (0 <= seconds) {
      this.sec = Math.floorMod(seconds, 60);
    }
    else {
      this.sec = 0;
    }
  }
  
  // to construct a Clock at time 00:00:00
  public Clock() {
    this(0, 0, 0);
  }

  // a method to increment this Clock by one second
  public void incrementSec() {
    // this Clock is incremented by one second
    this.sec++;
    // if the before-increment time is hh:mm:59, the time is reset to hh:mm(increment):00
    if (this.sec > 59) {
      this.sec = 0;
      incrementMin(); // increment minutes accordingly
    }
  }

  // a method to increment this Clock by one minute
  public void incrementMin() {
    // this Clock is incremented by one minute
    this.min++;
    // if the before-increment time is hh:59:ss, the time is reset to hh(increment):00:ss
    if (this.min > 59) {
      this.min = 0;
      incrementHrs(); // increment hours accordingly
    }
  }

  // a method to increment this Clock by one hour
  public void incrementHrs() {
    // this Clock is incremented by one hour
    this.hr++;
    // if the before-increment time is 23:mm:ss, the time is reset to 00:mm:ss
    if (this.hr > 23) {
      this.hr = 0;
    }
  }

  // a method to represent this Clock as a String
  public String asString() {

    // initialize the string to be returned and mod the hours by 12 for conventional time
    String clockStr = "";
    int modHr = Math.floorMod(this.hr, 12);

    // if modHr is less than 10, add a placeholder 0, else display modHr
    String hrStr = "";
    if (modHr < 10) {
      hrStr = "0" + modHr + ":";
    }
    else {
      hrStr = modHr + ":";
    }

    // if this.min is less than 10, add a placeholder 0, else display this.min
    String minStr = "";
    if (this.min < 10) {
      minStr = "0" + this.min + ":";
    }
    else {
      minStr = this.min + ":";
    }
    
    // if this.sec is less than 10, add a placeholder 0, else display this.sec
    String secStr = "";
    if (this.sec < 10) {
      secStr = "0" + this.sec;
    }
    else {
      secStr = "" + this.sec;
    }

    // create and return the time string
    clockStr = hrStr + minStr + secStr;
    return clockStr;
  }
}
