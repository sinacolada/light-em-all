package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D.Double;
import java.util.Stack;

public final class FrameImage extends RectangleImageBase {
   public WorldImage img;

   public FrameImage(WorldImage img, Color color) {
      super((int)Math.ceil(img.getBB().getWidth()), (int)Math.ceil(img.getBB().getHeight()), OutlineMode.OUTLINE, color);
      this.img = img;
      this.pinhole = this.img.pinhole;
   }

   public FrameImage(WorldImage img) {
      this(img, Color.black);
   }

   @Override
   protected void drawStackUnsafe(Graphics2D g) {
      if (this.width > 0) {
         if (this.height > 0) {
            if (this.color == null) {
               this.color = new Color(0, 0, 0);
            }

            Paint oldPaint = g.getPaint();
            Stroke oldStroke = g.getStroke();
            this.img.drawStackUnsafe(g);
            g.setPaint(this.color);
            BoundingBox bb = this.img.getBB();
            g.draw(new Double(bb.getTlx(), bb.getTly(), bb.getWidth(), bb.getHeight()));
            g.setPaint(oldPaint);
            g.setStroke(oldStroke);
         }
      }
   }

   @Override
   protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
      if (this.width > 0) {
         if (this.height > 0) {
            if (this.color == null) {
               this.color = new Color(0, 0, 0);
            }

            BoundingBox bb = this.img.getBB();
            images.push(new RectangleImage((int)Math.ceil(bb.getWidth()), (int)Math.ceil(bb.getHeight()), OutlineMode.OUTLINE, this.color));
            txs.push(g.getTransform());
            this.img.drawStacksafe(g, images, txs);
         }
      }
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("color", this.color), new ImageField("img", this.img)));
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (other instanceof FrameImage) {
         FrameImage that = (FrameImage)other;
         if (this.color.equals(that.color) && this.pinhole.equals(that.pinhole)) {
            worklistThis.push(this.img);
            worklistThat.push(that.img);
            return true;
         }
      }

      return false;
   }

   @Override
   public int hashCode() {
      return super.hashCode() + this.color.hashCode();
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      return new FrameImage(this.img.movePinholeTo(p), this.color);
   }
}
