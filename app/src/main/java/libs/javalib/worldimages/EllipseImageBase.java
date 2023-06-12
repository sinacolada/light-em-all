package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D.Double;
import java.util.Stack;

abstract class EllipseImageBase extends WorldImage {
   public int width;
   public int height;
   public OutlineMode fill;
   public Color color;

   public EllipseImageBase(int width, int height, OutlineMode mode, Color color) {
      super(1);
      this.width = width;
      this.height = height;
      this.fill = mode;
      this.color = color;
   }

   public EllipseImageBase(int width, int height, String outlineMode, Color color) {
      this(width, height, OutlineMode.fromString(outlineMode), color);
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
      double rx = (double)this.width / 2.0;
      double ry = (double)this.height / 2.0;
      double m11 = t.getScaleX();
      double m21 = t.getShearX();
      double m31 = t.getTranslateX();
      double m12 = t.getShearY();
      double m22 = t.getScaleY();
      double m32 = t.getTranslateY();
      double xOffset = Math.sqrt(Math.pow(m11, 2.0) * Math.pow(rx, 2.0) + Math.pow(m21, 2.0) * Math.pow(ry, 2.0));
      double yOffset = Math.sqrt(Math.pow(m12, 2.0) * Math.pow(rx, 2.0) + Math.pow(m22, 2.0) * Math.pow(ry, 2.0));
      double xMin = m31 - xOffset;
      double xMax = m31 + xOffset;
      double yMin = m32 - yOffset;
      double yMax = m32 + yOffset;
      return new BoundingBox(xMin, yMin, xMax, yMax);
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
            if (this.fill == OutlineMode.SOLID) {
               g.fill(new Double((double)(-this.width) / 2.0, (double)(-this.height) / 2.0, (double)this.width, (double)this.height));
            } else if (this.fill == OutlineMode.OUTLINE) {
               g.draw(new Double((double)(-this.width) / 2.0, (double)(-this.height) / 2.0, (double)this.width, (double)this.height));
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
         EllipseImageBase that = (EllipseImageBase)other;
         return this.width == that.width
            && this.height == that.height
            && this.fill == that.fill
            && this.color.equals(that.color)
            && this.pinhole.equals(that.pinhole);
      }
   }

   @Override
   public int hashCode() {
      return this.color.hashCode() + this.width + this.height;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new EllipseImage(this.width, this.height, this.fill, this.color);
      i.pinhole = p;
      return i;
   }
}
