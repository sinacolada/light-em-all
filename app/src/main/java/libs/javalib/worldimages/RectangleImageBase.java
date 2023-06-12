package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.Stack;

abstract class RectangleImageBase extends WorldImage {
   public int width;
   public int height;
   public Color color;
   public OutlineMode fill;

   public RectangleImageBase(int width, int height, OutlineMode fill, Color color) {
      super(1);
      this.width = width;
      this.height = height;
      this.fill = fill;
      this.color = color;
   }

   public RectangleImageBase(int width, int height, String fill, Color color) {
      this(width, height, OutlineMode.fromString(fill), color);
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
      if (this.width > 0) {
         if (this.height > 0) {
            if (this.color == null) {
               this.color = new Color(0, 0, 0);
            }

            Paint oldPaint = g.getPaint();
            g.setPaint(this.color);
            if (this.fill == OutlineMode.OUTLINE) {
               g.draw(new Double((double)(-this.width) / 2.0, (double)(-this.height) / 2.0, (double)this.width, (double)this.height));
            } else if (this.fill == OutlineMode.SOLID) {
               g.fill(new Double((double)(-this.width) / 2.0, (double)(-this.height) / 2.0, (double)this.width, (double)this.height));
            }

            g.setPaint(oldPaint);
         }
      }
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
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("fill", this.fill), new ImageField("color", this.color)));
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (!this.getClass().equals(other.getClass())) {
         return false;
      } else {
         RectangleImageBase that = (RectangleImageBase)other;
         return this.width == that.width
            && this.height == that.height
            && this.color.equals(that.color)
            && this.fill == that.fill
            && this.pinhole.equals(that.pinhole);
      }
   }

   @Override
   public int hashCode() {
      return this.color.hashCode() + this.width + this.height;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new RectangleImage(this.width, this.height, this.fill, this.color);
      i.pinhole = p;
      return i;
   }
}
