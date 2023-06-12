package libs.javalib.worldimages;

import java.awt.Color;
import java.util.Stack;

public class EmptyImage extends RectangleImageBase {
   public EmptyImage() {
      super(0, 0, "solid", new Color(0, 0, 0, 0));
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      if (this.pinhole.x == 0 && this.pinhole.y == 0) {
         sb = sb.append("new EmptyImage()");
      } else {
         sb = sb.append("new EmptyImage(pinhole = ").append(this.pinhole.toString()).append(")");
      }

      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      return other instanceof EmptyImage && this.pinhole.equals(other.pinhole);
   }
}
