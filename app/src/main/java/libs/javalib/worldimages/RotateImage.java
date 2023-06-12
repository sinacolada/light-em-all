package libs.javalib.worldimages;

import java.awt.geom.AffineTransform;
import java.util.Stack;

public final class RotateImage extends TransformImageBase {
   private double rotationDegrees;

   public RotateImage(WorldImage img, double rotationDegrees) {
      super(img, AffineTransform.getRotateInstance(Math.toRadians(rotationDegrees)));
      this.rotationDegrees = rotationDegrees;
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(").append("this.rotationDegrees = ").append(this.rotationDegrees).append(",");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("img", this.img)));
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (other instanceof RotateImage && this.pinhole.equals(other.pinhole)) {
         RotateImage that = (RotateImage)other;
         if (Math.abs(this.rotationDegrees - that.rotationDegrees) < 1.0E-5) {
            worklistThis.push(this.img);
            worklistThat.push(that.img);
            return true;
         }
      }

      return false;
   }

   @Override
   public int hashCode() {
      return (int)(this.rotationDegrees * 1000.0);
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new RotateImage(this.img, this.rotationDegrees);
      i.pinhole = p;
      return i;
   }
}
