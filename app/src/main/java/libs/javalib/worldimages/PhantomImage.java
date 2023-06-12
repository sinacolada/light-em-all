package libs.javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Stack;

public final class PhantomImage extends WorldImage {
   int width;
   int height;
   WorldImage img;

   public PhantomImage(WorldImage img, int width, int height) {
      super(img.pinhole, 1 + img.depth);
      this.img = img;
      this.width = width;
      this.height = height;
   }

   public PhantomImage(WorldImage img) {
      this(img, 0, 0);
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
   protected BoundingBox getBBHelp(AffineTransform t) {
      Point2D tl = WorldImage.transformPosn(t, (double)(-this.width) / 2.0, (double)(-this.height) / 2.0);
      Point2D tr = WorldImage.transformPosn(t, (double)this.width / 2.0, (double)(-this.height) / 2.0);
      Point2D bl = WorldImage.transformPosn(t, (double)(-this.width) / 2.0, (double)this.height / 2.0);
      Point2D br = WorldImage.transformPosn(t, (double)this.width / 2.0, (double)this.height / 2.0);
      return BoundingBox.containing(tl, tr, bl, br);
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      return new PhantomImage(this.img.movePinholeTo(p), this.width, this.height);
   }

   @Override
   protected void drawStackUnsafe(Graphics2D g) {
      this.img.drawStackUnsafe(g);
   }

   @Override
   protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
      images.push(this.img);
      txs.push(g.getTransform());
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

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (other instanceof PhantomImage) {
         PhantomImage that = (PhantomImage)other;
         if (this.width == that.width && this.height == that.height && this.pinhole.equals(that.pinhole)) {
            worklistThis.push(this.img);
            worklistThat.push(that.img);
            return true;
         }
      }

      return false;
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(new Object[]{this.width, this.height, this.img});
   }
}
