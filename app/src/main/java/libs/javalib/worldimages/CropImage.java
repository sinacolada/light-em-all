package libs.javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

public final class CropImage extends WorldImage {
   int x;
   int y;
   int width;
   int height;
   WorldImage img;

   public CropImage(int x, int y, int width, int height, WorldImage img) {
      super(1 + img.depth);
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.img = img;
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
   int numKids() {
      return 1;
   }

   @Override
   WorldImage getKid(int i) {
      if (i == 0) {
         return this.img;
      } else {
         throw new IllegalArgumentException("No such kid " + i);
      }
   }

   @Override
   AffineTransform getTransform(int i) {
      if (i == 0) {
         return new AffineTransform();
      } else {
         throw new IllegalArgumentException("No such kid " + i);
      }
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      CropImage c = new CropImage(this.x, this.y, this.width, this.height, this.img);
      c.pinhole = p;
      return c;
   }

   @Override
   protected void drawStackUnsafe(Graphics2D g) {
      if (this.width > 0) {
         if (this.height > 0) {
            AffineTransform oldTransform = g.getTransform();
            BufferedImage cropped = new BufferedImage(this.width, this.height, 2);
            Graphics2D g2d = cropped.createGraphics();
            g2d.translate(this.img.getWidth() / 2.0 - (double)this.x, this.img.getHeight() / 2.0 - (double)this.y);
            this.img.draw(g2d);
            g2d.dispose();
            g.translate((double)(-this.width) / 2.0, (double)(-this.height) / 2.0);
            g.drawImage(cropped, new AffineTransform(), null);
            g.setTransform(oldTransform);
         }
      }
   }

   @Override
   protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
      this.drawStackUnsafe(g);
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (other instanceof CropImage) {
         CropImage that = (CropImage)other;
         if (this.x == that.x && this.y == that.y && this.width == that.width && this.height == that.height && this.pinhole.equals(that.pinhole)) {
            worklistThis.push(this.img);
            worklistThat.push(that.img);
            return true;
         }
      }

      return false;
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
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("img", this.img)));
      return sb;
   }
}
