package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D.Double;
import java.util.Stack;

public final class LineImage extends WorldImage {
   public Posn endPoint;
   public Color color;

   public LineImage(Posn endPoint, Color color) {
      super(1);
      this.endPoint = endPoint;
      this.color = color;
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
      Point2D end1 = WorldImage.transformPosn(t, (double)(-this.endPoint.x) / 2.0, (double)(-this.endPoint.y) / 2.0);
      Point2D end2 = WorldImage.transformPosn(t, (double)this.endPoint.x / 2.0, (double)this.endPoint.y / 2.0);
      return new BoundingBox(end1, end2);
   }

   @Override
   protected void drawStackUnsafe(Graphics2D g) {
      DPosn midpoint = new DPosn((double)this.endPoint.x / 2.0, (double)this.endPoint.y / 2.0);
      Paint oldPaint = g.getPaint();
      if (this.color != null) {
         g.setPaint(this.color);
      } else {
         g.setPaint(Color.BLACK);
      }

      g.draw(new Double(-midpoint.x, -midpoint.y, midpoint.x, midpoint.y));
      g.setPaint(oldPaint);
   }

   @Override
   protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
      this.drawStackUnsafe(g);
   }

   @Override
   public double getWidth() {
      return (double)Math.abs(this.endPoint.x);
   }

   @Override
   public double getHeight() {
      return (double)Math.abs(this.endPoint.y);
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(").append("this.endPoint = ").append(this.endPoint.coords()).append(",");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("color", this.color)));
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (!(other instanceof LineImage)) {
         return false;
      } else {
         LineImage that = (LineImage)other;
         return this.endPoint.x == that.endPoint.x && this.endPoint.y == that.endPoint.y && this.color.equals(that.color) && this.pinhole.equals(that.pinhole);
      }
   }

   @Override
   public int hashCode() {
      return this.color.hashCode() + this.endPoint.x + this.endPoint.y;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new LineImage(this.endPoint, this.color);
      i.pinhole = p;
      return i;
   }
}
