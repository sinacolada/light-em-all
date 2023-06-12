// package lightemall;

// import java.awt.Color;
// import java.util.*;

// import app.src.main.java.lightemall.BranchPiece;
// import app.src.main.java.lightemall.Clock;
// import app.src.main.java.lightemall.CompareEdgeWeight;
// import app.src.main.java.lightemall.Edge;
// import app.src.main.java.lightemall.Find;
// import app.src.main.java.lightemall.GamePiece;
// import app.src.main.java.lightemall.LightEmAll;
// import libs.javalib.impworld.*;
// import libs.javalib.worldimages.*;
// import libs.tester.*;

// // represents a class of examples
// class ExamplesLightEmAll {
//   Random rand0;
//   Random rand1;
//   LightEmAll fixedGame0;
//   LightEmAll fixedGame0Solved;
//   LightEmAll fixedGame1;
//   Color wireOff = new Color(128, 128, 128);
//   Color wireLit11 = new Color(215, 175, 0);
//   Color wireLitPS = new Color(255, 255, 0);
//   WorldImage offHWire;
//   WorldImage offVWire;
//   WorldImage litHWire11;
//   WorldImage litVWire11;
//   WorldImage litHWirePS;
//   WorldImage litVWirePS;
//   WorldImage offPiece;
//   WorldImage litPiece11;
//   WorldImage litPiecePS;
//   WorldImage g0cell00Img;         // cell at game0's board.get(1).get(1)
//   WorldImage g1cell11Img;         // cell at game1's board.get(1).get(1)
//   WorldImage g0cell00ImgRot;
//   WorldImage g1cellPSImg;         // game1's PowerStation cell
//   WorldImage powerStationImg;
//   WorldImage textBox;

//   // reset Data
//   void initData() {
//     this.rand0 = new Random(0);
//     this.rand1 = new Random(1);
//     this.fixedGame0 = new LightEmAll(4, 4, this.rand0);
//     this.fixedGame0Solved = new LightEmAll(4, 4, this.rand0);
//     this.fixedGame0Solved.onMouseClicked(new Posn(50, 50), "LeftButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(50, 250), "LeftButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(150, 50), "LeftButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(150, 50), "LeftButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(150, 350), "LeftButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(250, 50), "LeftButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(250, 350), "LeftButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(350, 50), "RightButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(350, 50), "RightButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(350, 150), "LeftButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(350, 150), "LeftButton");
//     // game now connected, use the following to solve
//     //   this.fixedGame0Solved.onKeyEvent("left");
//     this.fixedGame1 = new LightEmAll(4, 4, this.rand1);
//     this.offHWire = new RectangleImage(50, 10, OutlineMode.SOLID, this.wireOff);
//     this.offVWire = new RectangleImage(10, 50, OutlineMode.SOLID, this.wireOff);

//     // wire for piece at game1's board.get(1).get(1)
//     this.litHWire11 = new RectangleImage(50, 10, OutlineMode.SOLID, this.wireLit11);
//     this.litVWire11 = new RectangleImage(10, 50, OutlineMode.SOLID, this.wireLit11);

//     // wire for power station
//     this.litHWirePS = new RectangleImage(50, 10, OutlineMode.SOLID, this.wireLitPS);
//     this.litVWirePS = new RectangleImage(10, 50, OutlineMode.SOLID, this.wireLitPS);
//     this.offPiece = new OverlayImage(new RectangleImage(10, 10,
//         OutlineMode.SOLID, this.wireOff), new RectangleImage(100, 100, 
//             OutlineMode.SOLID,
//             new Color(64, 64, 64)));
//     this.litPiece11 = new OverlayImage(new RectangleImage(10, 10,
//         OutlineMode.SOLID, this.wireLit11), new RectangleImage(100, 100, 
//             OutlineMode.SOLID,
//             new Color(64, 64, 64)));
//     this.litPiecePS = new OverlayImage(new RectangleImage(10, 10,
//         OutlineMode.SOLID, this.wireLitPS), new RectangleImage(100, 100, 
//             OutlineMode.SOLID,
//             new Color(64, 64, 64)));

//     this.g0cell00Img = new FrameImage(
//         new OverlayImage(this.offVWire, 
//             this.offPiece
//             .movePinhole(0, 25)).movePinhole(0, -25));

//     this.g0cell00ImgRot = new FrameImage(
//         new OverlayImage(this.offHWire, 
//             this.offPiece
//             .movePinhole(25, 0)).movePinhole(-25, 0));

//     this.g1cell11Img = new FrameImage(
//         new OverlayImage(this.litVWire11, // bottom
//             new OverlayImage(this.litHWire11, // right
//                 new OverlayImage(this.litHWire11, // left
//                     this.litPiece11
//                     .movePinhole(-25, 0)).movePinhole(25, 0)
//                 .movePinhole(25, 0)).movePinhole(-25, 0)
//             .movePinhole(0, 25)).movePinhole(0, -25));

//     this.powerStationImg = new EmptyImage();
//     WorldImage powerTriangle = new RegularPolyImage(40, 3, OutlineMode.SOLID, 
//         new Color(0, 255, 127));
//     for (int i = 0; i < 3; i++) {
//       this.powerStationImg = 
//           new OverlayImage(new RotateImage(powerTriangle, i * 80), this.powerStationImg);
//     }

//     // this will not have a Power Station on top, because that is placed only in .makeScene()
//     this.g1cellPSImg = new FrameImage(
//         new OverlayImage(this.litVWirePS, // bottom
//             new OverlayImage(this.litHWirePS, // left
//                 this.litPiecePS
//                 .movePinhole(-25, 0)).movePinhole(25, 0)
//             .movePinhole(0, 25)).movePinhole(0, -25));
//     this.textBox = new OverlayImage(new TextImage("You Win!", 25, Color.WHITE),
//         new RectangleImage(180, 60, OutlineMode.SOLID, new Color(0, 0, 0, 120)));
//   }

//   // tests the 9x9 Game seen in the assignment listing
//   void testGame(Tester t) {
//     LightEmAll assignmentGame = new LightEmAll(16, 9);
//     assignmentGame.bigBang(assignmentGame.width * 100, (assignmentGame.height + 1) * 100, 1);
//   }

//   // tests LightEmAll fixedGame0
//   void testGame0(Tester t) {
//     initData();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).distance, 6);
//     this.fixedGame0.bigBang(
//         this.fixedGame0.width * 100, 
//         (this.fixedGame0.height + 1) * 100,
//         1);
//   }

//   // tests LightEmAll fixedGame1
//   void testGame1(Tester t) {
//     initData();
//     t.checkExpect(this.fixedGame1.board.get(0).get(0).distance, 6);
//     this.fixedGame1.bigBang(
//         this.fixedGame1.width * 100, 
//         (this.fixedGame1.height + 1) * 100,
//         1);
//   }

//   // Tests on Function Objects:
//   // tests functionality of find function object's apply method
//   // on example hashmap and posn for example nodes
//   void testFind(Tester t) {
//     HashMap<Character, Character> charMap = new HashMap<Character, Character>();
//     charMap.put('a', 'e');
//     charMap.put('b', 'a');
//     charMap.put('c', 'e');
//     charMap.put('d', 'e');
//     charMap.put('e', 'e');
//     charMap.put('f', 'f');
//     HashMap<Integer, Integer> intMap = new HashMap<Integer, Integer>();
//     intMap.put(0, 0);
//     intMap.put(22, 1);
//     intMap.put(1, -5);
//     intMap.put(-5, 0);
//     intMap.put(-1, 1);
//     Find<Character> find1 = new Find<Character>();
//     Find<Integer> find2 = new Find<Integer>();
//     t.checkExpect(find1.apply(charMap, 'c'), 'e');
//     t.checkExpect(find1.apply(charMap, 'b'), 'e');
//     t.checkExpect(find1.apply(charMap, 'a'), 'e');
//     t.checkExpect(find1.apply(charMap, 'f'), 'f');
//     t.checkExpect(find1.apply(charMap, 'e'), 'e');
//     t.checkExpect(find1.apply(charMap, 'd'), 'e');
//     t.checkExpect(find2.apply(intMap, 22), 0);
//     t.checkExpect(find2.apply(intMap, -1), 0);
//     t.checkExpect(find2.apply(intMap, -5), 0);
//     t.checkExpect(find2.apply(intMap, 1), 0);
//     t.checkExpect(find2.apply(intMap, 0), 0);
//   }

//   // tests functionality of CompareEdgeWeight function object's compare method
//   // on example gamePieces
//   void testCompareEdgeWeight(Tester t) {
//     initData();
//     CompareEdgeWeight compEdge = new CompareEdgeWeight();
//     t.checkExpect(
//         this.fixedGame0.edges.get(0).weight - this.fixedGame0.edges.get(5).weight, 13);
//     t.checkExpect(
//         this.fixedGame0.edges.get(15).weight - this.fixedGame0.edges.get(6).weight, -57);
//     t.checkExpect(
//         this.fixedGame0.edges.get(9).weight - this.fixedGame0.edges.get(8).weight, 14);
//     t.checkExpect(
//         this.fixedGame0.edges.get(7).weight - this.fixedGame0.edges.get(1).weight, -30);
//     t.checkExpect(
//         this.fixedGame0.edges.get(2).weight - this.fixedGame0.edges.get(3).weight, 88);
//     t.checkExpect(
//         this.fixedGame0.edges.get(11).weight - this.fixedGame0.edges.get(13).weight, 32);
//     t.checkExpect(compEdge.compare(
//         this.fixedGame0.edges.get(0), this.fixedGame0.edges.get(5)), 13);
//     t.checkExpect(compEdge.compare(
//         this.fixedGame0.edges.get(15), this.fixedGame0.edges.get(6)), -57);
//     t.checkExpect(compEdge.compare(
//         this.fixedGame0.edges.get(9), this.fixedGame0.edges.get(8)), 14);
//     t.checkExpect(compEdge.compare(
//         this.fixedGame0.edges.get(7), this.fixedGame0.edges.get(1)), -30);
//     t.checkExpect(compEdge.compare(
//         this.fixedGame0.edges.get(2), this.fixedGame0.edges.get(3)), 88);
//     t.checkExpect(compEdge.compare(
//         this.fixedGame0.edges.get(11), this.fixedGame0.edges.get(13)), 32);
//   }

//   // tests functionality of BranchPiece function object's test method
//   // on example gamepieces
//   void testBranchPiece(Tester t) {
//     initData();
//     BranchPiece branchPiece = new BranchPiece();
//     t.checkExpect(branchPiece.test(this.fixedGame0.nodes.get(0)), false);
//     t.checkExpect(branchPiece.test(this.fixedGame0.nodes.get(15)), false);
//     t.checkExpect(branchPiece.test(this.fixedGame0.nodes.get(5)), true);
//     t.checkExpect(branchPiece.test(this.fixedGame0.nodes.get(7)), false);
//     t.checkExpect(branchPiece.test(this.fixedGame0.nodes.get(12)), true);
//     t.checkExpect(branchPiece.test(this.fixedGame0.nodes.get(6)), true);
//     t.checkExpect(branchPiece.test(this.fixedGame0.nodes.get(3)), false);
//     t.checkExpect(branchPiece.test(this.fixedGame0.nodes.get(1)), true);
//   }

//   // tests functionality of clock methods on example LightEmAll game
//   void testClock(Tester t) {
//     initData();
//     Clock clock0 = new Clock();
//     Clock clock1 = new Clock(71, 59, 59);
//     t.checkExpect(clock0.asString(), "00:00:00");
//     t.checkExpect(clock1.asString(), "11:59:59");
//     t.checkExpect(clock0.hr, 0);
//     t.checkExpect(clock0.min, 0);
//     t.checkExpect(clock0.sec, 0);
//     t.checkExpect(clock1.hr, 23);
//     t.checkExpect(clock1.min, 59);
//     t.checkExpect(clock1.sec, 59);
//     clock0.incrementSec();
//     clock1.incrementSec();
//     t.checkExpect(clock0.hr, 0);
//     t.checkExpect(clock0.min, 0);
//     t.checkExpect(clock0.sec, 1);
//     t.checkExpect(clock1.hr, 0);
//     t.checkExpect(clock1.min, 0);
//     t.checkExpect(clock1.sec, 0);
//     clock0 = new Clock();
//     clock1 = new Clock(71, 59, 59);
//     clock0.incrementMin();
//     clock1.incrementMin();
//     t.checkExpect(clock0.hr, 0);
//     t.checkExpect(clock0.min, 1);
//     t.checkExpect(clock0.sec, 0);
//     t.checkExpect(clock1.hr, 0);
//     t.checkExpect(clock1.min, 0);
//     t.checkExpect(clock1.sec, 59);
//     clock0 = new Clock();
//     clock1 = new Clock(71, 59, 59);
//     clock0.incrementHrs();
//     clock1.incrementHrs();
//     t.checkExpect(clock0.hr, 1);
//     t.checkExpect(clock0.min, 0);
//     t.checkExpect(clock0.sec, 0);
//     t.checkExpect(clock1.hr, 0);
//     t.checkExpect(clock1.min, 59);
//     t.checkExpect(clock1.sec, 59);

//   }

//   // tests the .draw(int radius) method on GamePiece
//   void testDrawGP(Tester t) {
//     initData();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).draw(this.fixedGame0.radius), 
//         this.g0cell00Img); // off vertical case
//     this.fixedGame0.board.get(0).get(0).rotateLeft();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).draw(this.fixedGame0.radius), 
//         this.g0cell00ImgRot); // off horizontal case
//     t.checkExpect(this.fixedGame1.board.get(1).get(1).draw(this.fixedGame1.radius), 
//         this.g1cell11Img); // on case w/ all wires except top and distance 2
//     t.checkExpect(this.fixedGame1.board.get(0).get(2).draw(this.fixedGame1.radius), 
//         this.g1cellPSImg); // on case w/ left and bottom wires and distance 0
//   }

//   // tests the .rotateLeft() method on GamePiece
//   void testRotateLeftGP(Tester t) {
//     initData();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).left, false);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).right, false);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).top, false);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).bottom, true);
//     this.fixedGame0.board.get(0).get(0).rotateLeft();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).left, false);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).right, true);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).top, false);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).bottom, false);

//     initData();
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).left, true);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).right, false);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).top, false);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).bottom, false);
//     this.fixedGame0.board.get(1).get(0).rotateLeft();
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).left, false);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).right, false);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).top, false);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).bottom, true);

//     initData();
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).left, true);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).right, false);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).top, false);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).bottom, false);
//     this.fixedGame0.board.get(3).get(3).rotateLeft();
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).left, false);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).right, false);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).top, false);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).bottom, true);
//   }

//   // tests the .rotateRight() method on GamePiece
//   void testRotateRightGP(Tester t) {
//     initData();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).left, false);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).right, false);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).top, false);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).bottom, true);
//     this.fixedGame0.board.get(0).get(0).rotateRight();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).left, true);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).right, false);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).top, false);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).bottom, false);

//     initData();
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).left, true);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).right, false);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).top, false);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).bottom, false);
//     this.fixedGame0.board.get(1).get(0).rotateRight();
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).left, false);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).right, false);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).top, true);
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).bottom, false);

//     initData();
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).left, true);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).right, false);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).top, false);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).bottom, false);
//     this.fixedGame0.board.get(3).get(3).rotateRight();
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).left, false);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).right, false);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).top, true);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).bottom, false);
//   }

//   // tests the .leftConnection(GamePiece g) method on GamePiece
//   // determines whether this game piece has g connected to its left
//   void testLeftConnectionGP(Tester t) {
//     initData();
//     t.checkExpect(this.fixedGame0.board.get(0).get(1).leftConnection(
//         this.fixedGame0.board.get(0).get(0)), false);
//     this.fixedGame0.board.get(0).get(0).rotateLeft();
//     t.checkExpect(this.fixedGame0.board.get(0).get(1).leftConnection(
//         this.fixedGame0.board.get(0).get(0)), true);

//     initData();
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).leftConnection(
//         this.fixedGame0.board.get(3).get(2)), true);
//     t.checkExpect(this.fixedGame0.board.get(1).get(3).leftConnection(
//         this.fixedGame0.board.get(1).get(2)), false);
//   }

//   // tests the .rightConnection(GamePiece g) method on GamePiece
//   // determines whether this game piece has g connected to its right
//   void testRightConnectionGP(Tester t) {
//     initData();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).rightConnection(
//         this.fixedGame0.board.get(0).get(1)), false);
//     this.fixedGame0.board.get(0).get(0).rotateLeft();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).rightConnection(
//         this.fixedGame0.board.get(0).get(1)), true);

//     initData();
//     t.checkExpect(this.fixedGame0.board.get(3).get(2).rightConnection(
//         this.fixedGame0.board.get(3).get(3)), true);
//     t.checkExpect(this.fixedGame0.board.get(1).get(2).rightConnection(
//         this.fixedGame0.board.get(1).get(3)), false);
//   }

//   // tests the .topConnection(GamePiece g) method on GamePiece
//   // determines whether this game piece has g connected to its top
//   void testTopConnectionGP(Tester t) {
//     initData();
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).topConnection(
//         this.fixedGame0.board.get(0).get(0)), false);
//     this.fixedGame0.board.get(1).get(0).rotateRight();
//     t.checkExpect(this.fixedGame0.board.get(1).get(0).topConnection(
//         this.fixedGame0.board.get(0).get(0)), true);

//     initData();
//     t.checkExpect(this.fixedGame0.board.get(2).get(2).topConnection(
//         this.fixedGame0.board.get(1).get(2)), true);
//     this.fixedGame0.board.get(2).get(2).rotateRight();
//     t.checkExpect(this.fixedGame0.board.get(2).get(2).topConnection(
//         this.fixedGame0.board.get(1).get(2)), false);

//     initData();
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).topConnection(
//         this.fixedGame0.board.get(2).get(3)), false);
//     this.fixedGame0.board.get(2).get(3).rotateRight();
//     this.fixedGame0.board.get(3).get(3).rotateRight();
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).topConnection(
//         this.fixedGame0.board.get(2).get(3)), true);
//   }

//   // tests the .bottomConnection(GamePiece g) method on GamePiece
//   // determines whether this game piece has g connected to its bottom
//   void testBottomConnectionGP(Tester t) {
//     initData();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).bottomConnection(
//         this.fixedGame0.board.get(1).get(0)), false);
//     this.fixedGame0.board.get(1).get(0).rotateRight();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).bottomConnection(
//         this.fixedGame0.board.get(1).get(0)), true);

//     initData();
//     t.checkExpect(this.fixedGame0.board.get(1).get(2).bottomConnection(
//         this.fixedGame0.board.get(2).get(2)), true);
//     this.fixedGame0.board.get(2).get(2).rotateRight();
//     t.checkExpect(this.fixedGame0.board.get(1).get(2).bottomConnection(
//         this.fixedGame0.board.get(2).get(2)), false);

//     initData();
//     t.checkExpect(this.fixedGame0.board.get(2).get(3).bottomConnection(
//         this.fixedGame0.board.get(3).get(3)), false);
//     this.fixedGame0.board.get(2).get(3).rotateRight();
//     this.fixedGame0.board.get(3).get(3).rotateRight();
//     t.checkExpect(this.fixedGame0.board.get(2).get(3).bottomConnection(
//         this.fixedGame0.board.get(3).get(3)), true);
//   }

//   // tests the .updateDistance(int d) method on GamePiece
//   void testUpdateDistance(Tester t) {
//     // check initial distances
//     initData();
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).distance, 6);
//     t.checkExpect(this.fixedGame0.board.get(0).get(3).distance, 7);
//     t.checkExpect(this.fixedGame0.board.get(3).get(0).distance, 7);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).distance, 0);
//     t.checkExpect(this.fixedGame0.board.get(1).get(1).distance, 4);
//     t.checkExpect(this.fixedGame0.board.get(2).get(2).distance, 2);
//     t.checkExpect(this.fixedGame0.board.get(3).get(1).distance, 6);

//     // call updateDistance(int d) with the cells' initial distances and
//     // check the new distances are all one greater
//     for (GamePiece gp : this.fixedGame0.nodes) {
//       gp.updateDistance(gp.distance);
//     }

//     t.checkExpect(this.fixedGame0.board.get(0).get(0).distance, 7);
//     t.checkExpect(this.fixedGame0.board.get(0).get(3).distance, 8);
//     t.checkExpect(this.fixedGame0.board.get(3).get(0).distance, 8);
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).distance, 1);
//     t.checkExpect(this.fixedGame0.board.get(1).get(1).distance, 5);
//     t.checkExpect(this.fixedGame0.board.get(2).get(2).distance, 3);
//     t.checkExpect(this.fixedGame0.board.get(3).get(1).distance, 7);
//   }

//   // tests the .extendedPieces() method on GamePiece
//   void testExtendedPiecesGP(Tester t) {
//     // single disconnected
//     initData();
//     this.fixedGame0Solved.onMouseClicked(new Posn(50, 50), "LeftButton");
//     this.fixedGame0Solved.setConnections();
//     ArrayList<GamePiece> singleDisconnected = 
//         this.fixedGame0Solved.board.get(0).get(0).extendedPieces();
//     t.checkExpect(singleDisconnected.contains(
//         this.fixedGame0Solved.board.get(0).get(0)), true);
//     t.checkExpect(singleDisconnected.contains(
//         this.fixedGame0Solved.board.get(1).get(0)), false);

//     // two connected
//     initData();
//     this.fixedGame0Solved.onMouseClicked(new Posn(50, 50), "RightButton");
//     this.fixedGame0Solved.onMouseClicked(new Posn(50, 150), "LeftButton");
//     this.fixedGame0Solved.setConnections();
//     ArrayList<GamePiece> twoConnected = 
//         this.fixedGame0Solved.board.get(0).get(0).extendedPieces();
//     t.checkExpect(twoConnected.contains(this.fixedGame0Solved.board.get(0).get(0)), true);
//     t.checkExpect(twoConnected.contains(this.fixedGame0Solved.board.get(0).get(1)), false);
//     t.checkExpect(twoConnected.contains(this.fixedGame0Solved.board.get(0).get(2)), false);
//     t.checkExpect(twoConnected.contains(this.fixedGame0Solved.board.get(1).get(0)), false);
//     t.checkExpect(twoConnected.contains(this.fixedGame0Solved.board.get(1).get(1)), false);
//     t.checkExpect(twoConnected.contains(this.fixedGame0Solved.board.get(1).get(2)), false);
//   }

//   // tests the .treeGeneration() method on LightEmAll
//   void testTreeGeneration(Tester t) {
//     initData();

//     // create a temp game and manually set all data to equal this.fixedGame0
//     // then check that the two scenes match
//     LightEmAll tempGame = new LightEmAll(4, 4, new Random(0));
//     tempGame.width = 4;
//     tempGame.height = 4;
//     tempGame.powerCol = 2;
//     tempGame.powerRow = 0;
//     tempGame.rand = new Random(0);

//     // initialize lists
//     tempGame.board = new ArrayList<ArrayList<GamePiece>>();
//     for (int i = 0; i < 4; i++) {
//       tempGame.board.add(new ArrayList<GamePiece>());
//     }
//     tempGame.nodes = new ArrayList<GamePiece>();
//     tempGame.edges = new ArrayList<Edge>();
//     tempGame.mst = new ArrayList<Edge>();
//     tempGame.numMoves = 0;
//     tempGame.time = new Clock(0, 0, 0);

//     for (int i = 0; i < 4; i++) {
//       for (int j = 0; j < 4; j++) {
//         GamePiece toAdd;
//         boolean powerStation = false;
//         if (i == 0 && j == 0) {
//           powerStation = true;
//         }
//         toAdd = new GamePiece(i, j, false, false, false, false, powerStation);
//         tempGame.board.get(i).add(toAdd);
//         tempGame.nodes.add(toAdd);
//       }
//     }

//     tempGame.connectAll();
//     tempGame.initializeConnections();
//     tempGame.createMST();
//     tempGame.setMSTConnections();
//     tempGame.setRadius();
//     tempGame.randomizeRotation();
//     tempGame.setConnections();
//     tempGame.lightWires();

//     initData();
//     t.checkExpect(this.fixedGame0Solved.width, tempGame.width);
//     t.checkExpect(this.fixedGame0Solved.height, tempGame.height);
//     t.checkExpect(this.fixedGame0Solved.powerRow, tempGame.powerRow);
//     t.checkExpect(this.fixedGame0Solved.powerCol, tempGame.powerCol);
//     t.checkExpect(this.fixedGame0Solved.radius, tempGame.radius);
//     t.checkExpect(this.fixedGame0Solved.makeScene(), tempGame.makeScene());
//     t.checkExpect(tempGame.board.get(0).get(0).left, 
//         this.fixedGame0Solved.board.get(0).get(0).left);
//     t.checkExpect(tempGame.board.get(0).get(0).right, 
//         this.fixedGame0Solved.board.get(0).get(0).right);
//     t.checkExpect(tempGame.board.get(0).get(0).top, 
//         this.fixedGame0Solved.board.get(0).get(0).top);
//     t.checkExpect(tempGame.board.get(0).get(0).bottom, 
//         this.fixedGame0Solved.board.get(0).get(0).bottom);
//     t.checkExpect(tempGame.board.get(0).get(3).top, 
//         this.fixedGame0Solved.board.get(0).get(3).top);
//     t.checkExpect(tempGame.board.get(0).get(3).bottom, 
//         this.fixedGame0Solved.board.get(0).get(3).bottom);
//     t.checkExpect(tempGame.board.get(3).get(0).top, 
//         this.fixedGame0Solved.board.get(3).get(0).top);
//     t.checkExpect(tempGame.board.get(3).get(3).top, 
//         this.fixedGame0Solved.board.get(3).get(3).top);
//     t.checkExpect(tempGame.board.get(3).get(3).bottom, 
//         this.fixedGame0Solved.board.get(3).get(3).bottom);
//     t.checkExpect(tempGame.board.get(1).get(1).left, 
//         this.fixedGame0Solved.board.get(1).get(1).left);
//     t.checkExpect(tempGame.board.get(1).get(1).right, 
//         this.fixedGame0Solved.board.get(1).get(1).right);
//     t.checkExpect(tempGame.board.get(1).get(1).top, 
//         this.fixedGame0Solved.board.get(1).get(1).top);
//     t.checkExpect(tempGame.board.get(1).get(1).bottom, 
//         this.fixedGame0Solved.board.get(1).get(1).bottom);
//   }

//   // tests the .connectAll() method on LightEmAll
//   void testConnectAll(Tester t) {
//     initData();
//     // manually connect all and compare to the function
//     LightEmAll manualGame = new LightEmAll(4, 4, new Random(0));
//     for (GamePiece g : manualGame.nodes) {
//       // add each connection if not off of the board (edge cases)
//       if (g.row != 0) {
//         g.top = true;
//       }
//       if (g.row != 3) {
//         g.bottom = true;
//       }
//       if (g.col != 0) {
//         g.left = true;
//       }
//       if (g.col != 3) {
//         g.right = true;
//       }
//     }

//     LightEmAll autoGame = new LightEmAll(4, 4, new Random(0));
//     autoGame.connectAll();

//     // check edge case nodes for equality
//     t.checkExpect(manualGame.board.get(0).get(0).left, 
//         autoGame.board.get(0).get(0).left);
//     t.checkExpect(manualGame.board.get(0).get(0).right, 
//         autoGame.board.get(0).get(0).right);
//     t.checkExpect(manualGame.board.get(0).get(0).top, 
//         autoGame.board.get(0).get(0).top);
//     t.checkExpect(manualGame.board.get(0).get(0).bottom, 
//         autoGame.board.get(0).get(0).bottom);

//     t.checkExpect(manualGame.board.get(0).get(3).left, 
//         autoGame.board.get(0).get(3).left);
//     t.checkExpect(manualGame.board.get(0).get(3).right, 
//         autoGame.board.get(0).get(3).right);
//     t.checkExpect(manualGame.board.get(0).get(3).top, 
//         autoGame.board.get(0).get(3).top);
//     t.checkExpect(manualGame.board.get(0).get(3).bottom, 
//         autoGame.board.get(0).get(3).bottom);

//     t.checkExpect(manualGame.board.get(3).get(0).left, 
//         autoGame.board.get(3).get(0).left);
//     t.checkExpect(manualGame.board.get(3).get(0).right, 
//         autoGame.board.get(3).get(0).right);
//     t.checkExpect(manualGame.board.get(3).get(0).top, 
//         autoGame.board.get(3).get(0).top);
//     t.checkExpect(manualGame.board.get(3).get(0).bottom, 
//         autoGame.board.get(3).get(0).bottom);

//     t.checkExpect(manualGame.board.get(3).get(3).left, 
//         autoGame.board.get(3).get(3).left);
//     t.checkExpect(manualGame.board.get(3).get(3).right, 
//         autoGame.board.get(3).get(3).right);
//     t.checkExpect(manualGame.board.get(3).get(3).top, 
//         autoGame.board.get(3).get(3).top);
//     t.checkExpect(manualGame.board.get(3).get(3).bottom, 
//         autoGame.board.get(3).get(3).bottom);

//     t.checkExpect(manualGame.board.get(2).get(2).left, 
//         autoGame.board.get(2).get(2).left);
//     t.checkExpect(manualGame.board.get(2).get(2).right, 
//         autoGame.board.get(2).get(2).right);
//     t.checkExpect(manualGame.board.get(2).get(2).top, 
//         autoGame.board.get(2).get(2).top);
//     t.checkExpect(manualGame.board.get(2).get(2).bottom, 
//         autoGame.board.get(2).get(2).bottom);

//     t.checkExpect(manualGame.board.get(1).get(1).left, 
//         autoGame.board.get(1).get(1).left);
//     t.checkExpect(manualGame.board.get(1).get(1).right, 
//         autoGame.board.get(1).get(1).right);
//     t.checkExpect(manualGame.board.get(1).get(1).top, 
//         autoGame.board.get(1).get(1).top);
//     t.checkExpect(manualGame.board.get(1).get(1).bottom, 
//         autoGame.board.get(1).get(1).bottom);
//   }

//   // tests the .updateInitialConnections(GamePiece g) method on LighEmAll
//   void testUpdateInitialConnections(Tester t) {
//     initData();

//     // reset the connections then
//     // run the method and ensure the connections exist as they are supposed to
//     this.fixedGame0.board.get(0).get(0).connections = new ArrayList<Edge>();
//     this.fixedGame0.board.get(3).get(3).connections = new ArrayList<Edge>();
//     this.fixedGame0.board.get(1).get(1).connections = new ArrayList<Edge>();
//     this.fixedGame0.board.get(2).get(2).connections = new ArrayList<Edge>();

//     this.fixedGame0.connectAll();
//     this.fixedGame0.setConnections();
//     this.fixedGame0.updateInitialConnections(this.fixedGame0.board.get(0).get(0));
//     this.fixedGame0.updateInitialConnections(this.fixedGame0.board.get(3).get(3));
//     this.fixedGame0.updateInitialConnections(this.fixedGame0.board.get(1).get(1));
//     this.fixedGame0.updateInitialConnections(this.fixedGame0.board.get(2).get(2));

//     for (Edge e : this.fixedGame0.edges) {
//       e.weight = 100;
//     }

//     t.checkExpect(this.fixedGame0.board.get(0).get(0).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(0).get(0), 
//                 this.fixedGame0.board.get(0).get(1), 100),
//             new Edge(this.fixedGame0.board.get(0).get(0), 
//                 this.fixedGame0.board.get(1).get(0), 100))));
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(3).get(3), 
//                 this.fixedGame0.board.get(3).get(2), 100),
//             new Edge(this.fixedGame0.board.get(3).get(3), 
//                 this.fixedGame0.board.get(2).get(3), 100))));
//     t.checkExpect(this.fixedGame0.board.get(1).get(1).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(1).get(1), 
//                 this.fixedGame0.board.get(1).get(0), 100),
//             new Edge(this.fixedGame0.board.get(1).get(1), 
//                 this.fixedGame0.board.get(1).get(2), 100),
//             new Edge(this.fixedGame0.board.get(1).get(1), 
//                 this.fixedGame0.board.get(0).get(1), 100),
//             new Edge(this.fixedGame0.board.get(1).get(1), 
//                 this.fixedGame0.board.get(2).get(1), 100))));
//     t.checkExpect(this.fixedGame0.board.get(2).get(2).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(2).get(2), 
//                 this.fixedGame0.board.get(2).get(1), 100),
//             new Edge(this.fixedGame0.board.get(2).get(2), 
//                 this.fixedGame0.board.get(2).get(3), 100),
//             new Edge(this.fixedGame0.board.get(2).get(2), 
//                 this.fixedGame0.board.get(1).get(2), 100),
//             new Edge(this.fixedGame0.board.get(2).get(2), 
//                 this.fixedGame0.board.get(3).get(2), 100))));
//   }

//   // tests the .initializeConnections() method on LightEmAll
//   void testInitializeConnections(Tester t) {
//     initData();

//     // reset the connections then
//     // run the method and ensure the connections exist as they are supposed to
//     this.fixedGame0.board.get(0).get(0).connections = new ArrayList<Edge>();
//     this.fixedGame0.board.get(3).get(3).connections = new ArrayList<Edge>();
//     this.fixedGame0.board.get(1).get(1).connections = new ArrayList<Edge>();
//     this.fixedGame0.board.get(2).get(2).connections = new ArrayList<Edge>();

//     this.fixedGame0.connectAll();
//     this.fixedGame0.setConnections();

//     // manually run initializeConnections() with this.fixedGame0.updateConnections(above cells)
//     this.fixedGame0.updateInitialConnections(this.fixedGame0.board.get(0).get(0));
//     this.fixedGame0.updateInitialConnections(this.fixedGame0.board.get(3).get(3));
//     this.fixedGame0.updateInitialConnections(this.fixedGame0.board.get(1).get(1));
//     this.fixedGame0.updateInitialConnections(this.fixedGame0.board.get(2).get(2));

//     for (Edge e : this.fixedGame0.edges) {
//       e.weight = 100;
//     }

//     t.checkExpect(this.fixedGame0.board.get(0).get(0).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(0).get(0), 
//                 this.fixedGame0.board.get(0).get(1), 100),
//             new Edge(this.fixedGame0.board.get(0).get(0), 
//                 this.fixedGame0.board.get(1).get(0), 100))));
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(3).get(3), 
//                 this.fixedGame0.board.get(3).get(2), 100),
//             new Edge(this.fixedGame0.board.get(3).get(3), 
//                 this.fixedGame0.board.get(2).get(3), 100))));
//     t.checkExpect(this.fixedGame0.board.get(1).get(1).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(1).get(1), 
//                 this.fixedGame0.board.get(1).get(0), 100),
//             new Edge(this.fixedGame0.board.get(1).get(1), 
//                 this.fixedGame0.board.get(1).get(2), 100),
//             new Edge(this.fixedGame0.board.get(1).get(1), 
//                 this.fixedGame0.board.get(0).get(1), 100),
//             new Edge(this.fixedGame0.board.get(1).get(1), 
//                 this.fixedGame0.board.get(2).get(1), 100))));
//     t.checkExpect(this.fixedGame0.board.get(2).get(2).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(2).get(2), 
//                 this.fixedGame0.board.get(2).get(1), 100),
//             new Edge(this.fixedGame0.board.get(2).get(2), 
//                 this.fixedGame0.board.get(2).get(3), 100),
//             new Edge(this.fixedGame0.board.get(2).get(2), 
//                 this.fixedGame0.board.get(1).get(2), 100),
//             new Edge(this.fixedGame0.board.get(2).get(2), 
//                 this.fixedGame0.board.get(3).get(2), 100))));
//   }

//   // tests the .createMST() method on LightEmAll
//   void testCreateMST(Tester t) {
//     initData();

//     // check that for different LighEmAlls with the same random seed, the MST is identical
//     // and that edge weights are identical
//     LightEmAll tempGame0 = new LightEmAll(4, 4, new Random(0));
//     LightEmAll tempGame1 = new LightEmAll(4, 4, new Random(1));

//     t.checkExpect(this.fixedGame0.mst, tempGame0.mst);
//     t.checkExpect(this.fixedGame0.mst.get(0).weight, tempGame0.mst.get(0).weight);
//     t.checkExpect(this.fixedGame0.mst.get(1).weight, tempGame0.mst.get(1).weight);
//     t.checkExpect(this.fixedGame0.mst.get(2).weight, tempGame0.mst.get(2).weight);
//     t.checkExpect(this.fixedGame0.mst.get(3).weight, tempGame0.mst.get(3).weight);
//     t.checkExpect(this.fixedGame0.mst.get(4).weight, tempGame0.mst.get(4).weight);
//     t.checkExpect(this.fixedGame0.mst.get(5).weight, tempGame0.mst.get(5).weight);
//     t.checkExpect(this.fixedGame1.mst, tempGame1.mst);
//     t.checkExpect(this.fixedGame1.mst.get(0).weight, tempGame1.mst.get(0).weight);
//     t.checkExpect(this.fixedGame1.mst.get(1).weight, tempGame1.mst.get(1).weight);
//     t.checkExpect(this.fixedGame1.mst.get(2).weight, tempGame1.mst.get(2).weight);
//     t.checkExpect(this.fixedGame1.mst.get(3).weight, tempGame1.mst.get(3).weight);
//     t.checkExpect(this.fixedGame1.mst.get(4).weight, tempGame1.mst.get(4).weight);
//     t.checkExpect(this.fixedGame1.mst.get(5).weight, tempGame1.mst.get(5).weight);
//   }

//   // tests the setMSTConnections() method on LightEmAll
//   void testSetMSTConnections(Tester t) {
//     initData();

//     // check that for different LighEmAlls with the same random seed, the MST is identical
//     // and that edge weights are identical, and that connections are identical
//     LightEmAll tempGame0 = new LightEmAll(4, 4, new Random(0));
//     LightEmAll tempGame1 = new LightEmAll(4, 4, new Random(1));
//     tempGame0.setMSTConnections();
//     this.fixedGame0.setMSTConnections();
//     tempGame1.setMSTConnections();
//     this.fixedGame1.setMSTConnections();

//     t.checkExpect(this.fixedGame0.mst, tempGame0.mst);
//     t.checkExpect(this.fixedGame0.mst.get(0).weight, tempGame0.mst.get(0).weight);
//     t.checkExpect(this.fixedGame0.mst.get(1).weight, tempGame0.mst.get(1).weight);
//     t.checkExpect(this.fixedGame0.mst.get(2).weight, tempGame0.mst.get(2).weight);
//     t.checkExpect(this.fixedGame0.mst.get(3).weight, tempGame0.mst.get(3).weight);
//     t.checkExpect(this.fixedGame0.mst.get(4).weight, tempGame0.mst.get(4).weight);
//     t.checkExpect(this.fixedGame0.mst.get(5).weight, tempGame0.mst.get(5).weight);
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).connections.get(0),
//         tempGame0.board.get(0).get(0).connections.get(0));
//     t.checkExpect(this.fixedGame0.board.get(0).get(3).connections.get(0),
//         tempGame0.board.get(0).get(3).connections.get(0));
//     t.checkExpect(this.fixedGame0.board.get(3).get(0).connections.get(0),
//         tempGame0.board.get(3).get(0).connections.get(0));
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).connections.get(0),
//         tempGame0.board.get(3).get(3).connections.get(0));
//     t.checkExpect(this.fixedGame0.board.get(1).get(1).connections.get(0),
//         tempGame0.board.get(1).get(1).connections.get(0));
//     t.checkExpect(this.fixedGame0.board.get(2).get(2).connections.get(0),
//         tempGame0.board.get(2).get(2).connections.get(0));
//     t.checkExpect(this.fixedGame1.mst, tempGame1.mst);
//     t.checkExpect(this.fixedGame1.mst.get(0).weight, tempGame1.mst.get(0).weight);
//     t.checkExpect(this.fixedGame1.mst.get(1).weight, tempGame1.mst.get(1).weight);
//     t.checkExpect(this.fixedGame1.mst.get(2).weight, tempGame1.mst.get(2).weight);
//     t.checkExpect(this.fixedGame1.mst.get(3).weight, tempGame1.mst.get(3).weight);
//     t.checkExpect(this.fixedGame1.mst.get(4).weight, tempGame1.mst.get(4).weight);
//     t.checkExpect(this.fixedGame1.mst.get(5).weight, tempGame1.mst.get(5).weight);
//     t.checkExpect(this.fixedGame1.board.get(0).get(0).connections.get(0),
//         tempGame1.board.get(0).get(0).connections.get(0));
//     t.checkExpect(this.fixedGame1.board.get(0).get(3).connections.get(0),
//         tempGame1.board.get(0).get(3).connections.get(0));
//     t.checkExpect(this.fixedGame1.board.get(3).get(0).connections.get(0),
//         tempGame1.board.get(3).get(0).connections.get(0));
//     t.checkExpect(this.fixedGame1.board.get(3).get(3).connections.get(0),
//         tempGame1.board.get(3).get(3).connections.get(0));
//     t.checkExpect(this.fixedGame1.board.get(1).get(1).connections.get(0),
//         tempGame1.board.get(1).get(1).connections.get(0));
//     t.checkExpect(this.fixedGame1.board.get(2).get(2).connections.get(0),
//         tempGame1.board.get(2).get(2).connections.get(0));
//   }

//   // tests the setRadius() method on LightEmAll
//   void testSetRadius(Tester t) {
//     initData();

//     LightEmAll tempGame0 = new LightEmAll(4, 4, new Random(0));
//     LightEmAll tempGame1 = new LightEmAll(4, 4, new Random(1));
//     LightEmAll tempGame2 = new LightEmAll(5, 5, new Random(2));
//     LightEmAll tempGame2v2 = new LightEmAll(5, 5, new Random(2));

//     t.checkExpect(this.fixedGame0.radius, 5);
//     t.checkExpect(this.fixedGame1.radius, 5);
//     t.checkExpect(tempGame0.radius, 5);
//     t.checkExpect(tempGame1.radius, 5);
//     t.checkExpect(tempGame2.radius, 8);
//     t.checkExpect(tempGame2v2.radius, 8);

//   }

//   // tests the .updateConnections(GamePiece g) method on LightEmAll
//   void testUpdateConnections(Tester t) {
//     initData();

//     this.fixedGame0.board.get(0).get(0).connections = new ArrayList<Edge>();
//     this.fixedGame0.board.get(3).get(3).connections = new ArrayList<Edge>();

//     this.fixedGame0.updateConnections(this.fixedGame0.board.get(0).get(0));
//     this.fixedGame0.updateConnections(this.fixedGame0.board.get(3).get(3));

//     t.checkExpect(this.fixedGame0.board.get(0).get(0).connections, 
//         new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(3).get(3), 
//                 this.fixedGame0.board.get(3).get(2), 100))));
//   }

//   // tests the .setConnections() method on LightEmAll
//   void testSetConnections(Tester t) {
//     // reset data check initial edges for corners and center tile
//     initData();

//     t.checkExpect(this.fixedGame0.board.get(0).get(0).connections, 
//         new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(0).get(3).connections, 
//         new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(3).get(0).connections, 
//         new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(3).get(3), 
//                 this.fixedGame0.board.get(3).get(2), 100))));
//     t.checkExpect(this.fixedGame0.board.get(2).get(2).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(2).get(2), 
//                 this.fixedGame0.board.get(1).get(2), 100))));

//     // reset data then delete all edges
//     initData();
//     for (ArrayList<GamePiece> arr : this.fixedGame0.board) {
//       for (GamePiece gp : arr) {
//         gp.connections = new ArrayList<Edge>();
//       }
//     }
//     t.checkExpect(this.fixedGame0.board.get(0).get(0).connections, new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(0).get(3).connections, new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(3).get(0).connections, new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).connections, new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(2).get(2).connections, new ArrayList<Edge>());

//     // set connections then check that edges are the same as initial data
//     this.fixedGame0.setConnections();

//     t.checkExpect(this.fixedGame0.board.get(0).get(0).connections, 
//         new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(0).get(3).connections, 
//         new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(3).get(0).connections, 
//         new ArrayList<Edge>());
//     t.checkExpect(this.fixedGame0.board.get(3).get(3).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(3).get(3), 
//                 this.fixedGame0.board.get(3).get(2), 100))));
//     t.checkExpect(this.fixedGame0.board.get(2).get(2).connections, 
//         new ArrayList<Edge>(Arrays.asList(
//             new Edge(this.fixedGame0.board.get(2).get(2), 
//                 this.fixedGame0.board.get(1).get(2), 100))));
//   }

//   // tests the .lightWires() method on LightEmAll
//   void testLightWires(Tester t) {
//     // turn all cells "lit" boolean to false then manually light cells
//     initData();
//     for (GamePiece gp : this.fixedGame1.nodes) {
//       gp.lit = false;
//     }
//     t.checkExpect(this.fixedGame1.board.get(0).get(0).lit, false);
//     t.checkExpect(this.fixedGame1.board.get(0).get(3).lit, false);
//     t.checkExpect(this.fixedGame1.board.get(1).get(1).lit, false);
//     t.checkExpect(this.fixedGame1.board.get(1).get(2).lit, false);

//     for (GamePiece gp : this.fixedGame1.nodes) {
//       if (gp.distance <= this.fixedGame1.radius) {
//         gp.lit = true;
//       }
//     }

//     // check that the manual (correct) illumination matches the method
//     // for corners, and center cells
//     // MANUAL:
//     t.checkExpect(this.fixedGame1.board.get(0).get(0).lit, false);
//     t.checkExpect(this.fixedGame1.board.get(0).get(3).lit, false);
//     t.checkExpect(this.fixedGame1.board.get(1).get(1).lit, true);
//     t.checkExpect(this.fixedGame1.board.get(1).get(2).lit, true);
//     // METHOD:
//     initData();
//     t.checkExpect(this.fixedGame1.board.get(0).get(0).lit, false);
//     t.checkExpect(this.fixedGame1.board.get(0).get(3).lit, false);
//     t.checkExpect(this.fixedGame1.board.get(1).get(1).lit, true);
//     t.checkExpect(this.fixedGame1.board.get(1).get(2).lit, true);
//   }

//   // tests the .makeScene() method on LightEmAll
//   void testMakeScene(Tester t) {
//     initData();

//     // manually build a board
//     WorldScene s = this.fixedGame0.getEmptyScene();
//     for (ArrayList<GamePiece> arr : this.fixedGame0.board) {
//       for (GamePiece gp : arr) {
//         s.placeImageXY(gp.draw(this.fixedGame0.radius), gp.col * 100, gp.row * 100);
//       }
//     }
//     s.placeImageXY(this.powerStationImg, this.fixedGame0.powerCol, this.fixedGame0.powerRow);

//     t.checkExpect(this.fixedGame0.makeScene(), s);
//   }

//   // tests the .findPiece(Posn pos) method on LightEmAll
//   void testFindPiece(Tester t) {
//     initData();
//     LightEmAll tempGame = new LightEmAll(8, 8, new Random(3));

//     t.checkExpect(tempGame.findPiece(new Posn(1, 1)), 
//         tempGame.board.get(0).get(0));
//     t.checkExpect(tempGame.findPiece(new Posn(600, 143)), 
//         tempGame.board.get(1).get(6));
//     t.checkExpect(tempGame.findPiece(new Posn(472, 333)), 
//         tempGame.board.get(3).get(4));
//     t.checkExpect(tempGame.findPiece(new Posn(795, 779)), 
//         tempGame.board.get(7).get(7));
//     t.checkExpect(tempGame.findPiece(new Posn(9, 799)), 
//         tempGame.board.get(7).get(0));
//     t.checkExpect(tempGame.findPiece(new Posn(11, 131)), 
//         tempGame.board.get(1).get(0));
//   }

//   // tests the .onMouseClicked(Posn pos, String button) method on LightEmAll
//   void testOnMouseClicked(Tester t) {
//     initData();
//     LightEmAll tempGame = new LightEmAll(8, 8, new Random(3));

//     t.checkExpect(tempGame.board.get(1).get(6).left, false);
//     t.checkExpect(tempGame.board.get(1).get(6).right, true);
//     t.checkExpect(tempGame.board.get(1).get(6).top, true);
//     t.checkExpect(tempGame.board.get(1).get(6).bottom, false);
//     t.checkExpect(tempGame.board.get(7).get(7).left, false);
//     t.checkExpect(tempGame.board.get(7).get(7).right, true);
//     t.checkExpect(tempGame.board.get(7).get(7).top, true);
//     t.checkExpect(tempGame.board.get(7).get(7).bottom, false);
//     tempGame.onMouseClicked(new Posn(600, 143), "LeftButton");
//     tempGame.onMouseClicked(new Posn(795,779), "LeftButton");
//     t.checkExpect(tempGame.board.get(1).get(6).left, true);
//     t.checkExpect(tempGame.board.get(1).get(6).right, false);
//     t.checkExpect(tempGame.board.get(1).get(6).top, true);
//     t.checkExpect(tempGame.board.get(1).get(6).bottom, false);
//     t.checkExpect(tempGame.board.get(7).get(7).left, true);
//     t.checkExpect(tempGame.board.get(7).get(7).right, false);
//     t.checkExpect(tempGame.board.get(7).get(7).top, true);
//     t.checkExpect(tempGame.board.get(7).get(7).bottom, false);

//     initData();
//     tempGame = new LightEmAll(8, 8, new Random(3));

//     t.checkExpect(tempGame.board.get(1).get(6).left, false);
//     t.checkExpect(tempGame.board.get(1).get(6).right, true);
//     t.checkExpect(tempGame.board.get(1).get(6).top, true);
//     t.checkExpect(tempGame.board.get(1).get(6).bottom, false);
//     t.checkExpect(tempGame.board.get(7).get(7).left, false);
//     t.checkExpect(tempGame.board.get(7).get(7).right, true);
//     t.checkExpect(tempGame.board.get(7).get(7).top, true);
//     t.checkExpect(tempGame.board.get(7).get(7).bottom, false);
//     tempGame.onMouseClicked(new Posn(600, 143), "RightButton");
//     tempGame.onMouseClicked(new Posn(795, 779), "RightButton");
//     t.checkExpect(tempGame.board.get(1).get(6).left, false);
//     t.checkExpect(tempGame.board.get(1).get(6).right, true);
//     t.checkExpect(tempGame.board.get(1).get(6).top, false);
//     t.checkExpect(tempGame.board.get(1).get(6).bottom, true);
//     t.checkExpect(tempGame.board.get(7).get(7).left, false);
//     t.checkExpect(tempGame.board.get(7).get(7).right, true);
//     t.checkExpect(tempGame.board.get(7).get(7).top, false);
//     t.checkExpect(tempGame.board.get(7).get(7).bottom, true);
//   }

//   // tests the .onKeyEvent(String key) method on LightEmAll
//   void testOnKeyEvent(Tester t) {
//     initData();
//     // tests a sequence of keys, and ensures when an invalid
//     // input is given, the PowerStation does not move
//     t.checkExpect(this.fixedGame1.powerCol, 2);
//     t.checkExpect(this.fixedGame1.powerRow, 0);
//     this.fixedGame1.onKeyEvent("left");  // valid key
//     this.fixedGame1.lightWires();
//     t.checkExpect(this.fixedGame1.powerCol, 1);
//     t.checkExpect(this.fixedGame1.powerRow, 0);

//     this.fixedGame1.onKeyEvent("left");  // invalid key
//     this.fixedGame1.lightWires();
//     t.checkExpect(this.fixedGame1.powerCol, 1);
//     t.checkExpect(this.fixedGame1.powerRow, 0);
//     this.fixedGame1.onKeyEvent("right"); // valid key
//     this.fixedGame1.lightWires();
//     t.checkExpect(this.fixedGame1.powerCol, 2);
//     t.checkExpect(this.fixedGame1.powerRow, 0);

//     this.fixedGame1.onKeyEvent("up");    // invalid key
//     this.fixedGame1.lightWires();
//     t.checkExpect(this.fixedGame1.powerCol, 2);
//     t.checkExpect(this.fixedGame1.powerRow, 0);
//     this.fixedGame1.onKeyEvent("down");  // valid key
//     this.fixedGame1.lightWires();
//     t.checkExpect(this.fixedGame1.powerCol, 2);
//     t.checkExpect(this.fixedGame1.powerRow, 1);

//     this.fixedGame1.onKeyEvent("left");  // valid key
//     this.fixedGame1.lightWires();
//     t.checkExpect(this.fixedGame1.powerCol, 1);
//     t.checkExpect(this.fixedGame1.powerRow, 1);

//     this.fixedGame1.onKeyEvent("left"); // invalid key
//     this.fixedGame1.lightWires();
//     t.checkExpect(this.fixedGame1.powerCol, 1);
//     t.checkExpect(this.fixedGame1.powerRow, 1);
//     this.fixedGame1.onKeyEvent("up");    // invalid key
//     this.fixedGame1.lightWires();
//     t.checkExpect(this.fixedGame1.powerCol, 1);
//     t.checkExpect(this.fixedGame1.powerRow, 1);
//     this.fixedGame1.onKeyEvent("down");  // invalid key
//     this.fixedGame1.lightWires();
//     t.checkExpect(this.fixedGame1.powerCol, 1);
//     t.checkExpect(this.fixedGame1.powerRow, 1);
//   }

//   // tests the .won() method on LighEmAll
//   void testWon(Tester t) {
//     initData();
//     t.checkExpect(this.fixedGame0.won(), false);
//     t.checkExpect(this.fixedGame0Solved.won(), false);

//     // mutate to win the game
//     this.fixedGame0Solved.onKeyEvent("left");
//     for (GamePiece g : this.fixedGame0Solved.nodes) {
//       g.lit = true;
//     }
//     t.checkExpect(fixedGame0Solved.won(), true);
//   }


//   // tests the .lastScene(String msg) method on LightEmAll
//   void testlastScene(Tester t) {
//     initData();
//     LightEmAll game2 = new LightEmAll(5, 3);

//     WorldScene scene1Win = this.fixedGame0.makeScene();
//     scene1Win.placeImageXY(this.textBox, 400, 400);
//     t.checkExpect(this.fixedGame0.lastScene("You Win!"), scene1Win);

//     WorldScene scene2Win = game2.makeScene();
//     scene2Win.placeImageXY(this.textBox, 250, 150);
//     t.checkExpect(game2.lastScene("You Win!"), scene2Win);

//     WorldScene scene1Reset = this.fixedGame0.makeScene();
//     scene1Reset.placeImageXY(this.textBox, 400, 400);
//     t.checkExpect(this.fixedGame0.lastScene("Restarted"), scene1Reset);

//     WorldScene scene2Reset = game2.makeScene();
//     scene2Reset.placeImageXY(this.textBox, 250, 150);
//     t.checkExpect(game2.lastScene("Restarted"), scene2Reset);

//     WorldScene scene1Exit = this.fixedGame0.makeScene();
//     scene1Exit.placeImageXY(this.textBox, 400, 400);
//     t.checkExpect(this.fixedGame0.lastScene("Exit Game"), scene1Exit);

//     WorldScene scene2Exit = game2.makeScene();
//     scene2Exit.placeImageXY(this.textBox, 250, 150);
//     t.checkExpect(game2.lastScene("Exit Game"), scene2Exit);
//   }
// }