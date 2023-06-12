package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Stack;

public final class TriangleImage extends WorldImage {
   public Posn p1;
   public Posn p2;
   public Posn p3;
   public OutlineMode fill;
   public Color color;
   private Polygon poly;

   public TriangleImage(Posn p1, Posn p2, Posn p3, OutlineMode fill, Color color) {
      super(1);
      this.p1 = p1;
      this.p2 = p2;
      this.p3 = p3;
      this.color = color;
      this.fill = fill;
      int centerX = (int)Math.round((double)Math.min(this.p1.x, Math.min(this.p2.x, this.p3.x)) + this.getWidth() / 2.0);
      int centerY = (int)Math.round((double)Math.min(this.p1.y, Math.min(this.p2.y, this.p3.y)) + this.getHeight() / 2.0);
      int[] xCoord = new int[]{p1.x - centerX, p2.x - centerX, p3.x - centerX};
      int[] yCoord = new int[]{p1.y - centerY, p2.y - centerY, p3.y - centerY};
      this.poly = new Polygon(xCoord, yCoord, 3);
   }

   public TriangleImage(Posn p1, Posn p2, Posn p3, String fill, Color color) {
      this(p1, p2, p3, OutlineMode.fromString(fill), color);
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
      Point2D p1 = WorldImage.transformPosn(t, (double)this.poly.xpoints[0], (double)this.poly.ypoints[0]);
      Point2D p2 = WorldImage.transformPosn(t, (double)this.poly.xpoints[1], (double)this.poly.ypoints[1]);
      Point2D p3 = WorldImage.transformPosn(t, (double)this.poly.xpoints[2], (double)this.poly.ypoints[2]);
      return BoundingBox.containing(p1, p2, p3);
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
      return (double)(Math.max(this.p1.x, Math.max(this.p2.x, this.p3.x)) - Math.min(this.p1.x, Math.min(this.p2.x, this.p3.x)));
   }

   @Override
   public double getHeight() {
      return (double)(Math.max(this.p1.y, Math.max(this.p2.y, this.p3.y)) - Math.min(this.p1.y, Math.min(this.p2.y, this.p3.y)));
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(");
      stack.push(
         new FieldsWLItem(
            this.pinhole,
            new ImageField("p1", this.p1),
            new ImageField("p2", this.p2),
            new ImageField("p3", this.p3),
            new ImageField("fill", this.fill),
            new ImageField("color", this.color)
         )
      );
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (!(other instanceof TriangleImage)) {
         return false;
      } else {
         TriangleImage that = (TriangleImage)other;
         return this.fill == that.fill
            && this.p1.x == that.p1.x
            && this.p1.y == that.p1.y
            && this.p2.x == that.p2.x
            && this.p2.y == that.p2.y
            && this.p3.x == that.p3.x
            && this.p3.y == that.p3.y
            && this.color.equals(that.color)
            && this.pinhole.equals(that.pinhole);
      }
   }

   @Override
   public int hashCode() {
      return this.color.hashCode() + this.p1.x * this.p1.y + this.p2.x * this.p2.y + this.p3.x * this.p3.y;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new TriangleImage(this.p1, this.p2, this.p3, this.fill, this.color);
      i.pinhole = p;
      return i;
   }
}
