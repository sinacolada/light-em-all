package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Stack;

public final class FromFileImage extends WorldImage {
   public String fileName;
   protected volatile ImageMaker imread;

   public FromFileImage(String fileName) {
      super(1);
      this.imread = new ImageMaker(fileName);
      this.fileName = fileName;
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
      g.translate(-((double)this.imread.width / 2.0), -((double)this.imread.height / 2.0));
      g.drawRenderedImage(this.imread.image, new AffineTransform());
      g.translate((double)this.imread.width / 2.0, (double)this.imread.height / 2.0);
   }

   @Override
   protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
      this.drawStackUnsafe(g);
   }

   @Override
   public double getWidth() {
      return (double)this.imread.width;
   }

   @Override
   public double getHeight() {
      return (double)this.imread.height;
   }

   public Color getColorAt(int x, int y) throws IndexOutOfBoundsException {
      return this.imread.getColorPixel(x, y);
   }

   @Override
   protected BoundingBox getBBHelp(AffineTransform t) {
      double w = this.getWidth();
      double h = this.getHeight();
      Point2D tl = t.transform(new Double(-w / 2.0, -h / 2.0), null);
      Point2D br = t.transform(new Double(w / 2.0, h / 2.0), null);
      return new BoundingBox(tl.getX(), tl.getY(), br.getX(), br.getY());
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ")
         .append(this.simpleName())
         .append("(")
         .append("this.fileName = \"")
         .append(this.fileName.replace("\\", "\\\\").replace("\"", "\\\""))
         .append("\"");
      if (this.pinhole.x == 0 && this.pinhole.y == 0) {
         sb = sb.append(")");
      } else {
         stack.push(new FieldsWLItem(this.pinhole));
      }

      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (!(other instanceof FromFileImage)) {
         return false;
      } else {
         FromFileImage that = (FromFileImage)other;
         return this.fileName.equals(that.fileName) && this.pinhole.equals(that.pinhole);
      }
   }

   @Override
   public int hashCode() {
      return this.fileName.hashCode();
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new FromFileImage(this.fileName);
      i.pinhole = p;
      return i;
   }
}
