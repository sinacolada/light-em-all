package libs.javalib.worldcanvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import libs.javalib.impworld.WorldScene;
import libs.javalib.worldimages.CircleImage;
import javax.swing.JFrame;

public class WorldCanvas {
   protected static int WINDOWS_OPEN = 0;
   public transient JFrame frame;
   public transient CanvasPanel panel;
   protected int width;
   protected int height;
   protected transient WindowAdapter winapt = new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
         --WorldCanvas.WINDOWS_OPEN;
         WorldCanvas.this.panel.clearPanel();
         if (WorldCanvas.WINDOWS_OPEN == 0) {
            System.exit(0);
         }
      }
   };

   public WorldCanvas(int width, int height, String title) {
      this.width = width;
      this.height = height;
      this.frame = new JFrame(title);
      this.frame.setLayout(new BorderLayout());
      this.frame.setResizable(false);
      this.frame.addWindowListener(this.winapt);
      this.frame.setDefaultCloseOperation(1);
      this.panel = new CanvasPanel(width, height);
      this.panel.addNotify();
      this.frame.getContentPane().add(this.panel, "Center");
      this.frame.getContentPane().setMinimumSize(new Dimension(width, height));
      this.frame.pack();
      Graphics g = this.panel.getGraphics();
      this.frame.update(g);
      this.frame.setVisible(false);
   }

   public WorldCanvas(int width, int height) {
      this(width, height, "Canvas");
   }

   public final Graphics2D getBufferGraphics() {
      return this.panel.getBufferGraphics();
   }

   public Color getColorAt(int x, int y) throws IndexOutOfBoundsException {
      if (x < 0 || x >= this.width) {
         throw new IndexOutOfBoundsException(String.format("Specified x (%d) is not in range [0, %d)", x, this.width));
      } else if (y >= 0 && y < this.height) {
         int[] ans = new int[4];
         this.panel.getBuffer().getRaster().getPixel(x, y, ans);
         return new Color(ans[0], ans[1], ans[2], ans[3]);
      } else {
         throw new IndexOutOfBoundsException(String.format("Specified y (%d) is not in range [0, %d)", y, this.height));
      }
   }

   public boolean drawScene(WorldSceneBase scene) {
      if (this.frame.getWidth() != scene.width || this.frame.getHeight() != scene.height) {
         this.frame.getContentPane().setMinimumSize(new Dimension(scene.width, scene.height));
      }

      this.panel.drawScene(scene);
      return true;
   }

   public void printCurrentFont() {
      this.panel.getFont();
   }

   public boolean show() {
      if (!this.frame.isVisible()) {
         ++WINDOWS_OPEN;
         this.frame.setVisible(true);
         return true;
      } else {
         return true;
      }
   }

   public boolean close() {
      if (this.frame.isVisible()) {
         --WINDOWS_OPEN;
         this.frame.setVisible(false);
         this.panel.clearPanel();
      }

      return true;
   }

   public void clear() {
      this.panel.clearPanel();
   }

   private static void nextStep(String message) {
      try {
         System.out.println(message);
         System.out.println("Press RETURN");
         System.in.read();
      } catch (IOException var2) {
         System.out.println("Next step");
      }
   }

   @Override
   public String toString() {
      return "new Canvas(" + this.width + ", " + this.height + ")";
   }

   public String toIndentedString(String indent) {
      return "new Canvas(" + this.width + ", " + this.height + ")";
   }

   public static void main(String[] argv) {
      nextStep("Canvas with default name is constructed");
      WorldCanvas sm1 = new WorldCanvas(200, 200);
      nextStep("To show the canvas ... ");
      sm1.show();
      WorldScene scene1 = new WorldScene(200, 200);
      nextStep("Canvas shown - should be blank - add red and blue disk");
      scene1.placeImageXY(new CircleImage(20, "outline", Color.red), 50, 50);
      scene1.placeImageXY(new CircleImage(20, "outline", Color.blue), 150, 50);
      sm1.drawScene(scene1);
      nextStep("Show the canvas again - it should not do anything");
      sm1.show();
      nextStep("Draw a green disk");
      scene1.placeImageXY(new CircleImage(50, "outline", Color.green), 50, 150);
      sm1.drawScene(scene1);
      nextStep("Close the Canvas");
      sm1.close();
      nextStep("Show the canvas again - it should be cleared");
      sm1.show();
      nextStep("Paint one disks on the canvas");
      WorldScene scene2 = new WorldScene(200, 200);
      scene2.placeImageXY(new CircleImage(25, "outline", Color.black), 50, 150);
      sm1.drawScene(scene2);
      nextStep("Construct a second canvas with the name Smiley");
      WorldCanvas sm2 = new WorldCanvas(200, 200, "Smiley");
      nextStep("Show the second canvas");
      sm2.show();
      nextStep("Paint two disks on the Smiley canvas");
      WorldScene scene3 = new WorldScene(200, 200);
      scene3.placeImageXY(new CircleImage(20, "outline", Color.red), 50, 50);
      scene3.placeImageXY(new CircleImage(50, "outline", Color.blue), 150, 50);
      sm2.drawScene(scene3);
      nextStep("Manually close the 'Canvas' windowand see if we can bring it back to life");
      sm1.show();
      nextStep("The first canvas should be shown - cleared");
      WorldScene scene4 = new WorldScene(200, 200);
      scene4.placeImageXY(new CircleImage(30, "outline", Color.red), 50, 50);
      scene4.placeImageXY(new CircleImage(30, "outline", Color.blue), 150, 50);
      scene4.placeImageXY(new CircleImage(30, "outline", Color.green), 50, 150);
      sm1.drawScene(scene4);
      nextStep("The first canvas has three disks drawn");
      System.out.println("Close both canvas windows to end the program");
   }
}
