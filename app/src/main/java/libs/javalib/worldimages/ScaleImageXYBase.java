package libs.javalib.worldimages;

import java.awt.geom.AffineTransform;
import java.util.Stack;

abstract class ScaleImageXYBase extends TransformImageBase {
   public double scaleX;
   public double scaleY;

   public ScaleImageXYBase(WorldImage img, double scaleX, double scaleY) {
      super(img, AffineTransform.getScaleInstance(scaleX, scaleY));
      this.img = img;
      this.scaleX = scaleX;
      this.scaleY = scaleY;
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ")
         .append(this.simpleName())
         .append("(")
         .append("this.scaleX = ")
         .append(this.scaleX)
         .append(", ")
         .append("this.scaleY = ")
         .append(this.scaleY)
         .append(",");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("img", this.img)));
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (this.getClass().equals(other.getClass()) && this.pinhole.equals(other.pinhole)) {
         ScaleImageXYBase that = (ScaleImageXYBase)other;
         if (Math.abs(this.scaleX - that.scaleX) < 1.0E-5 && Math.abs(this.scaleY - that.scaleY) < 1.0E-5) {
            worklistThis.push(this.img);
            worklistThat.push(that.img);
            return true;
         }
      }

      return false;
   }

   @Override
   public int hashCode() {
      return (int)(this.scaleX * 42.0 + this.scaleY * -57.0);
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new ScaleImageXY(this.img, this.scaleX, this.scaleY);
      i.pinhole = p;
      return i;
   }
}
