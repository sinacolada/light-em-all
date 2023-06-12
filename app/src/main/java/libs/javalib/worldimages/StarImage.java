package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D.Double;
import java.util.Stack;

public final class StarImage extends WorldImage {
   public int points;
   public double radius;
   public int skipCount;
   public OutlineMode fill;
   public Color color;
   private Path2D poly;

   public StarImage(double radius, OutlineMode fill, Color color) {
      this(radius, 5, fill, color);
   }

   public StarImage(double radius, int numPoints, OutlineMode fill, Color color) {
      this(radius, numPoints, numPoints / 2, fill, color);
   }

   public StarImage(double radius, int numPoints, int skipCount, OutlineMode fill, Color color) {
      super(1);
      if (numPoints < 3) {
         throw new IllegalArgumentException("There must be at least 3 points in a polygon");
      } else if (skipCount >= 1 && skipCount < numPoints) {
         this.radius = radius;
         this.points = numPoints;
         this.skipCount = skipCount;
         this.color = color;
         this.fill = fill;
         this.generatePoly();
      } else {
         throw new IllegalArgumentException("The skip-count must be positive and less than the number of points");
      }
   }

   private void generatePoly() {
      this.poly = new Double();
      double skipAngle = (double)this.skipCount * (Math.PI * 2) / (double)this.points;
      int gcd = this.GCD(this.points, this.skipCount);
      int pointsPerComponent = this.points / gcd;
      int numComponents = gcd;

      for(int component = 0; component < numComponents; ++component) {
         double curAngle = (Math.PI * 2) * ((double)component / (double)this.points) + (Math.PI / 2);
         this.poly.moveTo(Math.cos(curAngle) * this.radius, -Math.sin(curAngle) * this.radius);

         for(int i = 0; i < pointsPerComponent; ++i) {
            curAngle += skipAngle;
            this.poly.lineTo(Math.cos(curAngle) * this.radius, -Math.sin(curAngle) * this.radius);
         }

         this.poly.closePath();
      }

      Rectangle2D bb = this.poly.getBounds2D();
      this.poly.transform(AffineTransform.getTranslateInstance(-bb.getCenterX(), -bb.getCenterY()));
      this.pinhole = new Posn((int)(-bb.getCenterX()), (int)(-bb.getCenterY()));
   }

   private int GCD(int a, int b) {
      while(b != 0) {
         int t = a;
         a = b;
         b = t % b;
      }

      return b == 0 ? a : this.GCD(b, a % b);
   }

   @Override
   protected BoundingBox getBBHelp(AffineTransform t) {
      Rectangle2D ans = this.poly.createTransformedShape(t).getBounds2D();
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
      sb = sb.append("new ").append(this.simpleName()).append("(").append("this.radius = ").append(this.radius).append(",");
      stack.push(
         new FieldsWLItem(
            this.pinhole,
            new ImageField("points", this.points),
            new ImageField("skipCount", this.skipCount),
            new ImageField("fill", this.fill),
            new ImageField("color", this.color)
         )
      );
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (!this.getClass().equals(other.getClass())) {
         return false;
      } else {
         StarImage that = (StarImage)other;
         return this.radius == that.radius
            && this.points == that.points
            && this.skipCount == that.skipCount
            && this.fill == that.fill
            && this.color.equals(that.color)
            && this.pinhole.equals(that.pinhole);
      }
   }

   @Override
   public int hashCode() {
      return this.color.hashCode() + this.fill.hashCode() + (int)this.radius + this.points;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new StarImage(this.radius, this.points, this.skipCount, this.fill, this.color);
      i.pinhole = p;
      return i;
   }
}
