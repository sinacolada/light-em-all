package libs.javalib.worldimages;

import java.awt.Color;
import java.util.Stack;

public final class CircleImage extends EllipseImageBase {
   public int radius;

   public CircleImage(int radius, OutlineMode fill, Color color) {
      super(radius * 2, radius * 2, fill, color);
      this.radius = radius;
   }

   public CircleImage(int radius, String fill, Color color) {
      this(radius, OutlineMode.fromString(fill), color);
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(").append("this.radius = ").append(this.radius).append(",");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("fill", this.fill), new ImageField("color", this.color)));
      return sb;
   }

   @Override
   public int hashCode() {
      return this.color.hashCode() + this.radius + this.fill.hashCode();
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new CircleImage(this.radius, this.fill, this.color);
      i.pinhole = p;
      return i;
   }
}
