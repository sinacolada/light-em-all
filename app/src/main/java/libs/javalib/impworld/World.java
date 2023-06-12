package libs.javalib.impworld;

import java.awt.event.WindowListener;
import libs.javalib.worldcanvas.WorldCanvas;
import libs.javalib.worldimages.Posn;
import libs.javalib.worldimages.WorldEnd;

public abstract class World {
   WorldCanvas theCanvas;
   private transient boolean worldExists = false;
   transient MyTimer mytime;
   transient boolean stopTimer = false;
   private transient MyKeyAdapter ka;
   private transient MyMouseAdapter ma;
   private transient WindowListener windowClosing;
   private transient WorldScene blankScene = new WorldScene(0, 0);
   public WorldEnd lastWorld = new WorldEnd(false, this.blankScene);
   private int width;
   private int height;

   public void bigBang(int w, int h, double speed) {
      if (this.worldExists) {
         System.out.println("Only one world can run at a time");
      } else {
         this.width = w;
         this.height = h;
         this.theCanvas = new WorldCanvas(w, h);
         this.blankScene = new WorldScene(w, h);
         this.lastWorld = new WorldEnd(false, this.blankScene);
         this.theCanvas.frame.setDefaultCloseOperation(2);
         this.windowClosing = new MyWindowClosingListener(this);
         this.theCanvas.frame.addWindowListener(this.windowClosing);
         long start = System.currentTimeMillis();
         long tmp = System.currentTimeMillis();

         while(tmp - start < 1000L) {
            tmp = System.currentTimeMillis();
         }

         this.ka = new MyKeyAdapter(this);
         this.theCanvas.frame.addKeyListener(this.ka);
         this.theCanvas.frame.setFocusTraversalKeysEnabled(false);
         this.ma = new MyMouseAdapter(this);
         this.theCanvas.frame.addMouseListener(this.ma);
         this.theCanvas.frame.addMouseMotionListener(this.ma);
         this.theCanvas.frame.setFocusable(true);
         this.theCanvas.show();
         this.worldExists = true;
         this.mytime = new MyTimer(this, speed);
         this.drawWorld("");
         start = System.currentTimeMillis();
         tmp = System.currentTimeMillis();

         while(tmp - start < 1000L) {
            tmp = System.currentTimeMillis();
         }

         if (speed > 0.0) {
            this.mytime.timer.start();
         }

         this.drawWorld("");
         System.out.println("impworld version 1.0 --- 26 June  2012\n-----------------------------------------\n");
      }
   }

   public WorldScene getEmptyScene() {
      return new WorldScene(this.width, this.height);
   }

   public void bigBang(int w, int h) {
      this.bigBang(w, h, 0.0);
   }

   void stopWorld() {
      if (this.worldExists) {
         this.mytime.timer.stop();
         this.worldExists = false;
         this.mytime.stopTimer();
         this.theCanvas.frame.removeKeyListener(this.ka);
         this.theCanvas.frame.removeMouseListener(this.ma);
         System.out.println("The world stopped.");
         this.theCanvas.clear();
         this.theCanvas.drawScene(this.lastWorld.lastScene);
      }
   }

   public WorldEnd worldEnds() {
      return new WorldEnd(false, this.makeScene());
   }

   public void endOfWorld(String s) {
      this.lastWorld = new WorldEnd(true, this.lastScene(s));
      this.stopWorld();
   }

   public void testOnTick() {
      this.lastWorld = this.worldEnds();
      if (this.lastWorld.worldEnds) {
         this.stopWorld();
      }

      this.processTick();
   }

   synchronized void processTick() {
      try {
         if (this.worldExists && !this.stopTimer) {
            this.lastWorld = this.worldEnds();
            if (this.lastWorld.worldEnds) {
               this.stopWorld();
            } else {
               this.onTick();
               if (this.lastWorld.worldEnds) {
                  this.stopWorld();
               } else {
                  this.drawWorld("");
               }
            }
         }
      } catch (RuntimeException var2) {
         var2.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
      }
   }

   public void onTick() {
   }

   synchronized void processKeyReleased(String key) {
      try {
         if (this.worldExists) {
            this.onKeyReleased(key);
            if (!this.lastWorld.worldEnds) {
               this.drawWorld("");
            } else {
               this.theCanvas.drawScene(this.lastWorld.lastScene);
            }
         }
      } catch (RuntimeException var3) {
         var3.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
      }
   }

   public void onKeyReleased(String key) {
   }

   synchronized void processKeyEvent(String ke) {
      try {
         if (this.worldExists) {
            this.onKeyEvent(ke);
            if (!this.lastWorld.worldEnds) {
               this.drawWorld("");
            } else {
               this.theCanvas.drawScene(this.lastWorld.lastScene);
            }
         }
      } catch (RuntimeException var3) {
         var3.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
      }
   }

   public void onKeyEvent(String s) {
   }

   private String buttonNameFor(int button) {
      switch(button) {
         case 1:
            return "LeftButton";
         case 2:
            return "MiddleButton";
         case 3:
            return "RightButton";
         default:
            return "UnknownButton";
      }
   }

   void processMouseClicked(Posn mouse, String button) {
      try {
         if (this.worldExists) {
            this.onMouseClicked(mouse, button);
            if (!this.lastWorld.worldEnds) {
               this.drawWorld("");
            } else {
               this.theCanvas.drawScene(this.lastWorld.lastScene);
            }
         }
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
      }
   }

   public void onMouseClicked(Posn mouse) {
   }

   public void onMouseClicked(Posn mouse, String buttonName) {
      this.onMouseClicked(mouse);
   }

   void processMouseEntered(Posn mouse) {
      try {
         if (this.worldExists) {
            this.onMouseEntered(mouse);
            if (!this.lastWorld.worldEnds) {
               this.drawWorld("");
            } else {
               this.theCanvas.drawScene(this.lastWorld.lastScene);
            }
         }
      } catch (RuntimeException var3) {
         var3.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
      }
   }

   public void onMouseEntered(Posn mouse) {
   }

   void processMouseExited(Posn mouse) {
      try {
         if (this.worldExists) {
            this.onMouseExited(mouse);
            if (!this.lastWorld.worldEnds) {
               this.drawWorld("");
            } else {
               this.theCanvas.drawScene(this.lastWorld.lastScene);
            }
         }
      } catch (RuntimeException var3) {
         var3.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
      }
   }

   public void onMouseExited(Posn mouse) {
   }

   void processMousePressed(Posn mouse, String button) {
      try {
         if (this.worldExists) {
            this.onMousePressed(mouse, button);
            if (!this.lastWorld.worldEnds) {
               this.drawWorld("");
            } else {
               this.theCanvas.drawScene(this.lastWorld.lastScene);
            }
         }
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
      }
   }

   public void onMousePressed(Posn mouse) {
   }

   public void onMousePressed(Posn mouse, String buttonName) {
      this.onMousePressed(mouse);
   }

   void processMouseReleased(Posn mouse, String button) {
      try {
         if (this.worldExists) {
            this.onMouseReleased(mouse, button);
            if (!this.lastWorld.worldEnds) {
               this.drawWorld("");
            } else {
               this.theCanvas.drawScene(this.lastWorld.lastScene);
            }
         }
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
      }
   }

   public void onMouseReleased(Posn mouse) {
   }

   public void onMouseReleased(Posn mouse, String buttonName) {
      this.onMouseReleased(mouse);
   }

   void processMouseMoved(Posn mouse, String button) {
      try {
         if (this.worldExists) {
            this.onMouseMoved(mouse, button);
            if (!this.lastWorld.worldEnds) {
               this.drawWorld("");
            } else {
               this.theCanvas.drawScene(this.lastWorld.lastScene);
            }
         }
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
      }
   }

   public void onMouseMoved(Posn mouse) {
   }

   public void onMouseMoved(Posn mouse, String buttonName) {
      this.onMouseMoved(mouse);
   }

   synchronized void drawWorld(String s) {
      if (this.worldExists) {
         this.theCanvas.clear();
         this.theCanvas.drawScene(this.makeScene());
      } else {
         this.theCanvas.clear();
         this.theCanvas.drawScene(this.lastScene(s));
      }
   }

   public abstract WorldScene makeScene();

   public WorldScene lastScene(String s) {
      return this.makeScene();
   }
}
