package libs.javalib.funworld;

import java.awt.event.WindowListener;
import libs.javalib.worldcanvas.WorldCanvas;
import libs.javalib.worldimages.Posn;
import libs.javalib.worldimages.WorldEnd;

public abstract class World {
   public WorldCanvas theCanvas;
   private transient boolean worldExists = false;
   transient MyTimer mytime;
   transient boolean stopTimer = false;
   private transient MyKeyAdapter keyAdapter;
   private transient MyMouseAdapter mouseAdapter;
   private transient WindowListener windowClosing;
   private transient WorldScene blankScene = new WorldScene(0, 0);
   public WorldEnd lastWorld = new WorldEnd(false, this.blankScene);

   public boolean bigBang(int width, int height, double speed) {
      if (this.worldExists) {
         System.out.println("Only one world can run at a time");
         return true;
      } else {
         this.theCanvas = new WorldCanvas(width, height);
         this.blankScene = new WorldScene(width, height);
         this.lastWorld = new WorldEnd(false, this.blankScene);
         this.theCanvas.frame.setDefaultCloseOperation(2);
         this.windowClosing = new MyWindowClosingListener(this);
         this.theCanvas.frame.addWindowListener(this.windowClosing);
         long start = System.currentTimeMillis();
         long tmp = System.currentTimeMillis();

         while(tmp - start < 1000L) {
            tmp = System.currentTimeMillis();
         }

         this.keyAdapter = new MyKeyAdapter(this);
         this.theCanvas.frame.addKeyListener(this.keyAdapter);
         this.theCanvas.frame.setFocusTraversalKeysEnabled(false);
         this.mouseAdapter = new MyMouseAdapter(this);
         this.theCanvas.frame.addMouseListener(this.mouseAdapter);
         this.theCanvas.frame.addMouseMotionListener(this.mouseAdapter);
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

         System.out.println("funworld version 1.0 --- 26 June  2012\n-----------------------------------------\n");
         return this.drawWorld("");
      }
   }

   public WorldScene getEmptyScene() {
      return this.blankScene;
   }

   public boolean bigBang(int w, int h) {
      return this.bigBang(w, h, 0.0);
   }

   private void stopWorld() {
      if (this.worldExists) {
         this.mytime.timer.stop();
         this.worldExists = false;
         this.mytime.stopTimer();
         this.theCanvas.frame.removeKeyListener(this.keyAdapter);
         this.theCanvas.frame.removeMouseListener(this.mouseAdapter);
         System.out.println("The world stopped.");
         this.theCanvas.clear();
         this.theCanvas.drawScene(this.lastWorld.lastScene);
      }
   }

   public WorldEnd worldEnds() {
      return new WorldEnd(false, this.makeScene());
   }

   public World endOfWorld(String s) {
      this.lastWorld = new WorldEnd(true, this.lastScene(s));
      this.stopWorld();
      return this;
   }

   public World testOnTick() {
      this.lastWorld = this.worldEnds();
      if (this.lastWorld.worldEnds) {
         this.stopWorld();
      }

      return this.processTick();
   }

   synchronized World processTick() {
      try {
         if (!this.worldExists || this.stopTimer) {
            return this;
         }

         this.lastWorld = this.worldEnds();
         if (!this.lastWorld.worldEnds) {
            World bw = this.onTick();
            if (bw.lastWorld.worldEnds) {
               bw.stopWorld();
               return bw;
            }

            return this.resetWorld(bw);
         }

         this.stopWorld();
      } catch (RuntimeException var2) {
         var2.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
      }

      return this;
   }

   public World onTick() {
      return this;
   }

   synchronized World processKeyEvent(String ke) {
      try {
         if (this.worldExists) {
            World bw = this.onKeyEvent(ke);
            return !this.lastWorld.worldEnds ? this.resetWorld(bw) : this;
         } else {
            return this;
         }
      } catch (RuntimeException var3) {
         var3.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
         return this;
      }
   }

   public World onKeyEvent(String s) {
      return this;
   }

   synchronized World processKeyReleased(String key) {
      try {
         if (this.worldExists) {
            World bw = this.onKeyReleased(key);
            return !this.lastWorld.worldEnds ? this.resetWorld(bw) : this;
         } else {
            return this;
         }
      } catch (RuntimeException var3) {
         var3.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
         return this;
      }
   }

   public World onKeyReleased(String key) {
      return this;
   }

   World processMouseClicked(Posn mouse, String button) {
      try {
         if (this.worldExists) {
            World bw = this.onMouseClicked(mouse, button);
            return !this.lastWorld.worldEnds ? this.resetWorld(bw) : this;
         } else {
            return this;
         }
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
         return this;
      }
   }

   public World onMouseClicked(Posn mouse) {
      return this;
   }

   public World onMouseClicked(Posn mouse, String buttonName) {
      return this.onMouseClicked(mouse);
   }

   World processMouseEntered(Posn mouse) {
      try {
         if (this.worldExists) {
            World bw = this.onMouseEntered(mouse);
            return !this.lastWorld.worldEnds ? this.resetWorld(bw) : this;
         } else {
            return this;
         }
      } catch (RuntimeException var3) {
         var3.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
         return this;
      }
   }

   public World onMouseEntered(Posn mouse) {
      return this;
   }

   World processMouseExited(Posn mouse) {
      try {
         if (this.worldExists) {
            World bw = this.onMouseExited(mouse);
            return !this.lastWorld.worldEnds ? this.resetWorld(bw) : this;
         } else {
            return this;
         }
      } catch (RuntimeException var3) {
         var3.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
         return this;
      }
   }

   public World onMouseExited(Posn mouse) {
      return this;
   }

   World processMousePressed(Posn mouse, String button) {
      try {
         if (this.worldExists) {
            World bw = this.onMousePressed(mouse, button);
            return !this.lastWorld.worldEnds ? this.resetWorld(bw) : this;
         } else {
            return this;
         }
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
         return this;
      }
   }

   public World onMousePressed(Posn mouse) {
      return this;
   }

   public World onMousePressed(Posn mouse, String buttonName) {
      return this.onMousePressed(mouse);
   }

   World processMouseReleased(Posn mouse, String button) {
      try {
         if (this.worldExists) {
            World bw = this.onMouseReleased(mouse, button);
            return !this.lastWorld.worldEnds ? this.resetWorld(bw) : this;
         } else {
            return this;
         }
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
         return this;
      }
   }

   public World onMouseReleased(Posn mouse) {
      return this;
   }

   public World onMouseReleased(Posn mouse, String buttonName) {
      return this.onMouseReleased(mouse);
   }

   World processMouseMoved(Posn mouse, String button) {
      try {
         if (this.worldExists) {
            World bw = this.onMouseMoved(mouse, button);
            return !this.lastWorld.worldEnds ? this.resetWorld(bw) : this;
         } else {
            return this;
         }
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         this.drawWorld("");
         Runtime.getRuntime().halt(1);
         return this;
      }
   }

   public World onMouseMoved(Posn mouse) {
      return this;
   }

   public World onMouseMoved(Posn mouse, String buttonName) {
      return this.onMouseMoved(mouse);
   }

   private synchronized World resetWorld(World bw) {
      if (this.worldExists) {
         bw.blankScene = this.blankScene;
         bw.theCanvas = this.theCanvas;
         bw.worldExists = true;
         bw.keyAdapter = this.keyAdapter;
         bw.mouseAdapter = this.mouseAdapter;
         bw.windowClosing = this.windowClosing;
         bw.keyAdapter.resetWorld(bw);
         bw.mouseAdapter.currentWorld = bw;
         bw.mytime = this.mytime;
         bw.mytime.setSpeed();
         bw.mytime.currentWorld = bw;
         bw.drawWorld("");
         return bw;
      } else {
         this.theCanvas.clear();
         this.drawWorld("");
         return this;
      }
   }

   synchronized boolean drawWorld(String s) {
      if (this.worldExists) {
         this.theCanvas.clear();
         this.theCanvas.drawScene(this.makeScene());
         return true;
      } else {
         this.theCanvas.clear();
         this.theCanvas.drawScene(this.lastScene(s));
         return true;
      }
   }

   public abstract WorldScene makeScene();

   public WorldScene lastScene(String s) {
      return this.makeScene();
   }
}
