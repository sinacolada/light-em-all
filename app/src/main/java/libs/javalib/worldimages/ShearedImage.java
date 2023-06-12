package libs.javalib.worldimages;

import java.awt.geom.AffineTransform;
import java.util.Stack;

public final class ShearedImage extends TransformImageBase {
   public double sx;
   public double sy;

   public ShearedImage(WorldImage img, double sx, double sy) {
      super(img, AffineTransform.getShearInstance(sx, sy));
      this.sx = sx;
      this.sy = sy;
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ")
         .append(this.simpleName())
         .append("(")
         .append("this.sx = ")
         .append(this.sx)
         .append(", ")
         .append("this.sy = ")
         .append(this.sy)
         .append(",");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("img", this.img)));
      return sb;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new ShearedImage(this.img, this.sx, this.sy);
      i.pinhole = p;
      return i;
   }
}
