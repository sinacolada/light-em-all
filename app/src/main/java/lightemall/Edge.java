package lightemall;

// to represent an edge
class Edge {
  GamePiece fromNode;
  GamePiece toNode;
  int weight;

  // to construct an edge 
  public Edge(GamePiece fromNode, GamePiece toNode, int weight) {
    this.fromNode = fromNode;
    this.toNode = toNode;
    this.weight = weight;
  }  
}