package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Stack;

public final class ComputedPixelImage extends WorldImage {
   public final int width;
   public final int height;
   private final BufferedImage image;
   private final WritableRaster raster;

   public ComputedPixelImage(int width, int height) {
      super(1);
      this.width = width;
      this.height = height;
      this.image = new BufferedImage(width, height, 2);
      this.raster = this.image.getRaster();
   }

   public void setPixel(int x, int y, Color c) throws IndexOutOfBoundsException {
      boundsCheck(x, y, this.width, this.height);
      this.raster.setPixel(x, y, new int[]{c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()});
   }

   public Color getPixel(int x, int y) throws IndexOutOfBoundsException {
      boundsCheck(x, y, this.width, this.height);
      int[] ans = new int[4];
      this.raster.getPixel(x, y, ans);
      return new Color(ans[0], ans[1], ans[2], ans[3]);
   }

   public Color getColorAt(int x, int y) throws IndexOutOfBoundsException {
      return this.getPixel(x, y);
   }

   public void setColorAt(int x, int y, Color c) throws IndexOutOfBoundsException {
      this.setPixel(x, y, c);
   }

   public void setPixels(int x, int y, int width, int height, Color c) throws IndexOutOfBoundsException {
      boundsCheck(x, y, this.width, this.height);
      if (width < 0) {
         throw new IndexOutOfBoundsException("Width cannot be negative");
      } else if (x + width > this.width) {
         throw new IndexOutOfBoundsException(String.format("Right edge of rectangle (%d) is not in range [0, %d)", x + width, this.width));
      } else if (height < 0) {
         throw new IndexOutOfBoundsException("Height cannot be negative");
      } else if (y + height > this.height) {
         throw new IndexOutOfBoundsException(String.format("Bottom edge of rectangle (%d) is not in range [0, %d)", y + height, this.height));
      } else {
         int[] sample = new int[]{c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()};

         for(int w = 0; w < width; ++w) {
            for(int h = 0; h < height; ++h) {
               this.raster.setPixel(x + w, y + h, sample);
            }
         }
      }
   }

   @Override
   int numKids() {
      return 0;
   }

   @Override
   WorldImage getKid(int i) {
      throw new IllegalArgumentException("No such kid " + i);
   }

   @Override
   AffineTransform getTransform(int i) {
      throw new IllegalArgumentException("No such kid " + i);
   }

   @Override
   protected BoundingBox getBBHelp(AffineTransform t) {
      Point2D tl = WorldImage.transformPosn(t, (double)(-this.width) / 2.0, (double)(-this.height) / 2.0);
      Point2D tr = WorldImage.transformPosn(t, (double)this.width / 2.0, (double)(-this.height) / 2.0);
      Point2D bl = WorldImage.transformPosn(t, (double)(-this.width) / 2.0, (double)this.height / 2.0);
      Point2D br = WorldImage.transformPosn(t, (double)this.width / 2.0, (double)this.height / 2.0);
      return BoundingBox.containing(tl, tr, bl, br);
   }

   @Override
   protected void drawStackUnsafe(Graphics2D g) {
      g.translate(-((double)this.width / 2.0), -((double)this.height / 2.0));
      g.drawRenderedImage(this.image, new AffineTransform());
      g.translate((double)this.width / 2.0, (double)this.height / 2.0);
   }

   @Override
   protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
      this.drawStackUnsafe(g);
   }

   @Override
   public double getWidth() {
      return (double)this.width;
   }

   @Override
   public double getHeight() {
      return (double)this.height;
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ")
         .append(this.simpleName())
         .append("(")
         .append("this.width = ")
         .append(this.width)
         .append(", ")
         .append("this.height = ")
         .append(this.height)
         .append(",");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("pixels", "[...elided...]")));
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (this.getClass().equals(other.getClass())) {
         ComputedPixelImage that = (ComputedPixelImage)other;
         if (this.width == that.width && this.height == that.height && this.pinhole.equals(that.pinhole)) {
            for(int x = 0; x < this.width; ++x) {
               for(int y = 0; y < this.height; ++y) {
                  int[] thisPx = new int[4];
                  int[] thatPx = new int[4];
                  this.raster.getPixel(x, y, thisPx);
                  that.raster.getPixel(x, y, thatPx);

                  for(int c = 0; c < 4; ++c) {
                     if (thisPx[c] != thatPx[c]) {
                        return false;
                     }
                  }
               }
            }

            return true;
         }
      }

      return false;
   }

   @Override
   public int hashCode() {
      return 1000 * this.width + this.height;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      ComputedPixelImage i = new ComputedPixelImage(this.width, this.height);
      i.pinhole = p;
      i.raster.setDataElements(0, 0, this.raster);
      return i;
   }
}
