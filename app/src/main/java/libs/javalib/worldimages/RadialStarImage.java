package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D.Double;
import java.util.Stack;

public final class RadialStarImage extends WorldImage {
   public int points;
   public double outerRadius;
   public double innerRadius;
   public OutlineMode fill;
   public Color color;
   private Path2D poly;

   public RadialStarImage(int numPoints, double innerRadius, double outerRadius, OutlineMode fill, Color color) {
      super(1);
      if (numPoints < 3) {
         throw new IllegalArgumentException("There must be at least 3 points in a polygon");
      } else if (outerRadius < innerRadius) {
         throw new IllegalArgumentException("The outer radius must be larger than the inner radius");
      } else {
         this.outerRadius = outerRadius;
         this.innerRadius = innerRadius;
         this.points = numPoints;
         this.color = color;
         this.fill = fill;
         this.generatePoly();
      }
   }

   private void generatePoly() {
      this.poly = new Double();
      double skipAngle = (Math.PI * 2) / (2.0 * (double)this.points);
      double curAngle = Math.PI / 2;
      this.poly.moveTo(Math.cos(curAngle) * this.outerRadius, -Math.sin(curAngle) * this.outerRadius);

      for(int i = 0; i < this.points; ++i) {
         if (i != 0) {
            this.poly.lineTo(Math.cos(curAngle) * this.outerRadius, -Math.sin(curAngle) * this.outerRadius);
         }

         curAngle += skipAngle;
         this.poly.lineTo(Math.cos(curAngle) * this.innerRadius, -Math.sin(curAngle) * this.innerRadius);
         curAngle += skipAngle;
      }

      this.poly.closePath();
   }

   @Override
   protected BoundingBox getBBHelp(AffineTransform t) {
      Rectangle2D ans = this.poly.getBounds2D();
      return new BoundingBox(ans.getMinX(), ans.getMinY(), ans.getMaxX(), ans.getMaxY());
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
   protected void drawStackUnsafe(Graphics2D g) {
      if (this.color == null) {
         this.color = new Color(0, 0, 0);
      }

      Paint oldPaint = g.getPaint();
      g.setPaint(this.color);
      if (this.fill == OutlineMode.OUTLINE) {
         g.draw(this.poly);
      } else if (this.fill == OutlineMode.SOLID) {
         g.fill(this.poly);
      }

      g.setPaint(oldPaint);
   }

   @Override
   protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
      this.drawStackUnsafe(g);
   }

   @Override
   public double getWidth() {
      return this.poly.getBounds2D().getWidth();
   }

   @Override
   public double getHeight() {
      return this.poly.getBounds2D().getHeight();
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ")
         .append(this.simpleName())
         .append("(")
         .append("this.outerRadius = ")
         .append(this.outerRadius)
         .append(",")
         .append("this.innerRadius = ")
         .append(this.innerRadius)
         .append(",");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("points", this.points), new ImageField("fill", this.fill), new ImageField("color", this.color)));
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (!this.getClass().equals(other.getClass())) {
         return false;
      } else {
         RadialStarImage that = (RadialStarImage)other;
         return this.outerRadius == that.outerRadius
            && this.innerRadius == that.innerRadius
            && this.points == that.points
            && this.fill == that.fill
            && this.color.equals(that.color)
            && this.pinhole.equals(that.pinhole);
      }
   }

   @Override
   public int hashCode() {
      return this.color.hashCode() + this.fill.hashCode() + (int)this.outerRadius + this.points;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new RadialStarImage(this.points, this.innerRadius, this.outerRadius, this.fill, this.color);
      i.pinhole = p;
      return i;
   }
}
