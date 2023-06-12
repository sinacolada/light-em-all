package libs.javalib.worldcanvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import libs.javalib.worldimages.OutlineMode;
import libs.javalib.worldimages.RectangleImage;
import libs.javalib.worldimages.WorldImage;
import javax.imageio.ImageIO;

public abstract class WorldSceneBase {
   public int width;
   public int height;
   protected WorldSceneBase.IList<WorldSceneBase.PlaceImage> imgs;
   protected Deque<WorldSceneBase.PlaceImage> revImgs;

   protected WorldSceneBase(int width, int height) {
      this.width = width;
      this.height = height;
      this.imgs = new WorldSceneBase.Empty<>();
      this.imgs = this.imgs.add(new WorldSceneBase.PlaceImage(new RectangleImage(width, height, OutlineMode.OUTLINE, Color.black), width / 2, height / 2));
      this.revImgs = null;
   }

   protected WorldSceneBase(int width, int height, WorldSceneBase.IList<WorldSceneBase.PlaceImage> imgs) {
      this.width = width;
      this.height = height;
      this.imgs = imgs;
      this.revImgs = null;
   }

   protected void draw(Graphics2D g) {
      this.revImagesIfNeeded();

      for(WorldSceneBase.PlaceImage i : this.revImgs) {
         g.translate(i.x - i.img.pinhole.x, i.y - i.img.pinhole.y);
         i.img.draw(g);
         g.translate(-i.x + i.img.pinhole.x, -i.y + i.img.pinhole.y);
      }
   }

   public final String saveImage(String filename) {
      try {
         BufferedImage img = new BufferedImage(this.width, this.height, 2);
         this.draw(img.createGraphics());
         return ImageIO.write(img, "png", new File(filename)) ? filename : "Could not save file";
      } catch (Exception var3) {
         return "Error saving file: " + var3.getMessage();
      }
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof WorldSceneBase)) {
         return false;
      } else {
         WorldSceneBase other = (WorldSceneBase)obj;
         if (this.width != other.width || this.height != other.height) {
            return false;
         } else if (this.width != 0 && this.height != 0) {
            BufferedImage image1 = new BufferedImage(this.width, this.height, 2);
            Graphics2D graphics2D1 = image1.createGraphics();
            this.draw(graphics2D1);
            BufferedImage image2 = new BufferedImage(this.width, this.height, 2);
            Graphics2D graphics2D2 = image2.createGraphics();
            other.draw(graphics2D2);
            int[] pix1 = new int[this.width * this.height];
            int[] pix2 = new int[this.width * this.height];
            PixelGrabber pg1 = new PixelGrabber(image1, 0, 0, this.width, this.height, pix1, 0, this.width);
            PixelGrabber pg2 = new PixelGrabber(image2, 0, 0, this.width, this.height, pix2, 0, this.width);

            try {
               pg1.grabPixels();
               pg2.grabPixels();
               return Arrays.equals(pix1, pix2);
            } catch (InterruptedException var12) {
               return false;
            }
         } else {
            return true;
         }
      }
   }

   private void revImagesIfNeeded() {
      if (this.revImgs == null) {
         this.revImgs = new ArrayDeque<>();

         for(WorldSceneBase.PlaceImage i : this.imgs) {
            this.revImgs.push(i);
         }
      }
   }

   public StringBuilder toIndentedString(StringBuilder sb, String linePrefix, int indent) {
      this.revImagesIfNeeded();
      sb.append("new ")
         .append(this.getClass().getSimpleName())
         .append("(){\n")
         .append(linePrefix + "  ")
         .append("this.width = ")
         .append(this.width)
         .append(", ")
         .append("this.height = ")
         .append(this.height);
      int count = 0;

      for(WorldSceneBase.PlaceImage i : this.revImgs) {
         sb.append(",\n").append(linePrefix + "  ").append("[").append(count).append("] = PlaceImage(");
         i.toIndentedString(sb, linePrefix + "    ", indent);
         sb.append(")");
         ++count;
      }

      sb.append("\n").append(linePrefix).append("}");
      return sb;
   }

   protected class Cons<T> implements WorldSceneBase.IList<T> {
      T first;
      WorldSceneBase.IList<T> rest;

      Cons(T first, WorldSceneBase.IList<T> rest) {
         this.first = first;
         this.rest = rest;
      }

      @Override
      public WorldSceneBase.Cons<T> add(T val) {
         return WorldSceneBase.this.new Cons<>(val, this);
      }

      @Override
      public Iterator<T> iterator() {
         return WorldSceneBase.this.new IListIterator<>(this);
      }
   }

   protected class Empty<T> implements WorldSceneBase.IList<T> {
      @Override
      public WorldSceneBase.Cons<T> add(T val) {
         return WorldSceneBase.this.new Cons<>(val, this);
      }

      @Override
      public Iterator<T> iterator() {
         return WorldSceneBase.this.new IListIterator<>(this);
      }
   }

   protected interface IList<T> extends Iterable<T> {
      WorldSceneBase.Cons<T> add(T var1);
   }

   protected class IListIterator<T> implements Iterator<T> {
      WorldSceneBase.IList<T> source;

      IListIterator(WorldSceneBase.IList<T> source) {
         this.source = source;
      }

      @Override
      public boolean hasNext() {
         return this.source instanceof WorldSceneBase.Cons;
      }

      @Override
      public T next() {
         WorldSceneBase.Cons<T> s = (WorldSceneBase.Cons)this.source;
         this.source = s.rest;
         return s.first;
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException("Remove is not supported");
      }
   }

   protected class PlaceImage {
      WorldImage img;
      int x;
      int y;

      public PlaceImage(WorldImage i, int x, int y) {
         this.img = i;
         this.x = x;
         this.y = y;
      }

      StringBuilder toIndentedString(StringBuilder sb, String linePrefix, int indent) {
         sb.append("this.x = ").append(this.x).append(", ");
         sb.append("this.y = ").append(this.y).append(",\n");
         sb.append(linePrefix);
         return this.img.toIndentedString(sb, linePrefix + "  ", indent);
      }
   }
}
