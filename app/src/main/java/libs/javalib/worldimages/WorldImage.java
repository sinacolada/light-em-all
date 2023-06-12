package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Stack;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;

public abstract class WorldImage {
   public Posn pinhole;
   static WeakHashMap<WorldImage, BoundingBox> bbCache;
   int depth;

   public int getImageNestingDepth() {
      return this.depth;
   }

   protected WorldImage(int depth) {
      this(new Posn(0, 0), depth);
   }

   protected WorldImage(Posn pinhole, int depth) {
      this.pinhole = pinhole;
      this.depth = depth;
      if (bbCache == null) {
         bbCache = new WeakHashMap<>();
      }
   }

   abstract int numKids();

   abstract WorldImage getKid(int var1);

   abstract AffineTransform getTransform(int var1);

   public BoundingBox getBB() {
      BoundingBox ret = bbCache.get(this);
      if (ret == null) {
         ret = this.getBB(new AffineTransform());
         bbCache.put(this, ret);
      }

      return ret;
   }

   protected BoundingBox getBB(final AffineTransform tx) {
      try {
         return tx.isIdentity() && bbCache.containsKey(this) ? this.getBB() : this.getBBHelp(tx);
      } catch (StackOverflowError var4) {
         final WorldImage img = this;
         return (new Callable<BoundingBox>() {
            public BoundingBox call() {
               return img.getBB(tx);
            }
         }).call();
      }
   }

   protected abstract BoundingBox getBBHelp(AffineTransform var1);

   protected static Point2D transformPosn(AffineTransform t, Posn p) {
      return transformPosn(t, (double)p.x, (double)p.y);
   }

   protected static Point2D transformPosn(AffineTransform t, DPosn p) {
      return transformPosn(t, p.x, p.y);
   }

   protected static Point2D transformPosn(AffineTransform t, double x, double y) {
      Point2D point = new Double(x, y);
      return t.transform(point, null);
   }

   public abstract WorldImage movePinholeTo(Posn var1);

   public WorldImage movePinhole(double dx, double dy) {
      return this.movePinholeTo(new Posn((int)Math.round((double)this.pinhole.x + dx), (int)Math.round((double)this.pinhole.y + dy)));
   }

   protected abstract void drawStackUnsafe(Graphics2D var1);

   protected abstract void drawStacksafe(Graphics2D var1, Stack<WorldImage> var2, Stack<AffineTransform> var3);

   public final void draw(Graphics2D g) {
      if (this.depth < 1000) {
         this.drawStackUnsafe(g);
      } else {
         Stack<WorldImage> images = new Stack<>();
         Stack<AffineTransform> txs = new Stack<>();
         AffineTransform initTx = g.getTransform();
         images.push(this);
         txs.push(initTx);

         while(!images.isEmpty()) {
            WorldImage nextI = images.pop();
            AffineTransform nextT = txs.pop();
            g.setTransform(nextT);
            nextI.drawStacksafe(g, images, txs);
         }

         g.setTransform(initTx);
      }
   }

   public final String saveImage(String filename) {
      try {
         BufferedImage img = new BufferedImage((int)this.getWidth(), (int)this.getHeight(), 2);
         Graphics2D g = img.createGraphics();
         g.setTransform(AffineTransform.getTranslateInstance(this.getWidth() / 2.0, this.getHeight() / 2.0));
         this.draw(g);
         return ImageIO.write(img, "png", new File(filename)) ? filename : "Could not save file";
      } catch (Exception var4) {
         return "Error saving file: " + var4.getMessage();
      }
   }

   protected final boolean equalsStacksafe(WorldImage that) {
      Stack<WorldImage> worklistThis = new Stack<>();
      Stack<WorldImage> worklistThat = new Stack<>();
      worklistThis.push(this);
      worklistThat.push(that);

      while(!worklistThis.empty()) {
         WorldImage one = worklistThis.pop();
         WorldImage two = worklistThat.pop();
         if (one != two && !one.equalsStacksafe(two, worklistThis, worklistThat)) {
            return false;
         }
      }

      return true;
   }

   protected abstract boolean equalsStacksafe(WorldImage var1, Stack<WorldImage> var2, Stack<WorldImage> var3);

   @Override
   public boolean equals(Object obj) {
      return obj instanceof WorldImage && this.equalsStacksafe((WorldImage)obj);
   }

   public WorldImage overlayImages(WorldImage... args) {
      WorldImage image = this;
      int length = args != null ? args.length : 0;

      for(int i = 0; i < length; ++i) {
         image = new OverlayImage(image, args[i]);
      }

      return image;
   }

   public abstract double getWidth();

   public abstract double getHeight();

   @Override
   public String toString() {
      return this.toIndentedString(new StringBuilder(), "", 0).toString();
   }

   public StringBuilder toIndentedString(StringBuilder sb, String linePrefix, int indent) {
      return ImagePrinter.makeString(this, sb, linePrefix, indent);
   }

   protected abstract StringBuilder toIndentedStringHelp(StringBuilder var1, Stack<Object> var2);

   protected String simpleName() {
      return this.getClass().getSimpleName();
   }

   protected static String colorString(String indent, Color color) {
      return "\n" + indent + colorString(color);
   }

   protected static String colorString(Color color) {
      String result = color.toString();
      int start = result.indexOf(91);
      result = result.substring(start, result.length());
      return "this.color = " + result;
   }

   protected static void boundsCheck(int x, int y, int width, int height) throws IndexOutOfBoundsException {
      if (x < 0 || x >= width) {
         throw new IndexOutOfBoundsException(String.format("Specified x (%d) is not in range [0, %d)", x, width));
      } else if (y < 0 || y >= height) {
         throw new IndexOutOfBoundsException(String.format("Specified y (%d) is not in range [0, %d)", y, height));
      }
   }
}
