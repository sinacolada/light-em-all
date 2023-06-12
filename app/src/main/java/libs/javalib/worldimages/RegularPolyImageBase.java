package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Stack;

abstract class RegularPolyImageBase extends WorldImage {
   public int sides;
   public double sideLen;
   public OutlineMode fill;
   public Color color;
   private Polygon poly;

   public RegularPolyImageBase(double length, LengthMode lengthMode, int numSides, OutlineMode fill, Color color) {
      super(1);
      if (numSides < 3) {
         throw new IllegalArgumentException("There must be at least 3 sides in a polygon");
      } else {
         this.sides = numSides;
         this.color = color;
         this.fill = fill;
         this.generatePoly(length, lengthMode);
      }
   }

   private void generatePoly(double sideLen, LengthMode lengthMode) {
      int[] xCoord = new int[this.sides];
      int[] yCoord = new int[this.sides];
      double internalAngle = (Math.PI * 2) / (double)this.sides;
      double rotation = (double)(this.sides - 2) * (Math.PI / (double)this.sides) / 2.0;
      double radius = 0.0;
      switch(lengthMode) {
         case SIDE:
            this.sideLen = sideLen;
            radius = sideLen / (2.0 * Math.sin(internalAngle / 2.0));
            break;
         case RADIUS:
            radius = sideLen;
            this.sideLen = 2.0 * sideLen * Math.sin(internalAngle / 2.0);
      }

      double xMin = radius;
      double xMax = -radius;
      double yMin = radius;
      double yMax = -radius;

      for(int i = 0; i < this.sides; ++i) {
         double x = (double)Math.round(Math.cos((double)i * internalAngle + rotation) * radius);
         xMin = Math.min(xMin, x);
         xMax = Math.max(xMax, x);
         xCoord[i] = (int)x;
         double y = (double)Math.round(Math.sin((double)i * internalAngle + rotation) * radius);
         yCoord[i] = (int)y;
         yMin = Math.min(yMin, y);
         yMax = Math.max(yMax, y);
      }

      double xAvg = (xMin + xMax) / 2.0;
      double yAvg = (yMin + yMax) / 2.0;

      for(int i = 0; i < this.sides; ++i) {
         xCoord[i] = (int)((double)xCoord[i] - xAvg);
         yCoord[i] = (int)((double)yCoord[i] - yAvg);
      }

      this.pinhole = new Posn((int)(-xAvg), (int)(-yAvg));
      this.poly = new Polygon(xCoord, yCoord, this.sides);
   }

   @Override
   protected BoundingBox getBBHelp(AffineTransform t) {
      Point2D p1 = WorldImage.transformPosn(t, (double)this.poly.xpoints[0], (double)this.poly.ypoints[0]);
      Point2D p2 = WorldImage.transformPosn(t, (double)this.poly.xpoints[1], (double)this.poly.ypoints[1]);
      BoundingBox ans = new BoundingBox(p1, p2);

      for(int i = 2; i < this.sides; ++i) {
         Point2D p = WorldImage.transformPosn(t, (double)this.poly.xpoints[i], (double)this.poly.ypoints[i]);
         ans.combineWith(p);
      }

      return ans;
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
      int minX = this.poly.xpoints[0];
      int maxX = this.poly.xpoints[0];

      for(int i = 0; i < this.sides; ++i) {
         minX = Math.min(minX, this.poly.xpoints[i]);
         maxX = Math.max(maxX, this.poly.xpoints[i]);
      }

      return (double)(maxX - minX);
   }

   @Override
   public double getHeight() {
      int minY = this.poly.ypoints[0];
      int maxY = this.poly.ypoints[0];

      for(int i = 0; i < this.sides; ++i) {
         minY = Math.min(minY, this.poly.ypoints[i]);
         maxY = Math.max(maxY, this.poly.ypoints[i]);
      }

      return (double)(maxY - minY);
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(").append("this.sidelen = ").append(this.sideLen).append(",");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("sides", this.sides), new ImageField("fill", this.fill), new ImageField("color", this.color)));
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (!this.getClass().equals(other.getClass())) {
         return false;
      } else {
         RegularPolyImageBase that = (RegularPolyImageBase)other;
         return this.sideLen == that.sideLen
            && this.sides == that.sides
            && this.fill == that.fill
            && this.color.equals(that.color)
            && this.pinhole.equals(that.pinhole);
      }
   }

   @Override
   public int hashCode() {
      return this.color.hashCode() + this.fill.hashCode() + (int)this.sideLen + this.sides;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new RegularPolyImage(this.sideLen, this.sides, this.fill, this.color);
      i.pinhole = p;
      return i;
   }
}
