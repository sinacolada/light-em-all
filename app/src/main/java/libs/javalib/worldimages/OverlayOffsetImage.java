package libs.javalib.worldimages;

import java.util.Stack;

public final class OverlayOffsetImage extends OverlayOffsetAlignBase {
   public OverlayOffsetImage(WorldImage top, double dx, double dy, WorldImage bot) {
      super(AlignModeX.PINHOLE, AlignModeY.PINHOLE, top, dx, dy, bot);
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(");
      stack.push(
         new FieldsWLItem(
            this.pinhole, new ImageField("top", this.top), new ImageField("dx", this.dx), new ImageField("dy", this.dy, true), new ImageField("bot", this.bot)
         )
      );
      return sb;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new OverlayOffsetImage(this.top, this.dx, this.dy, this.bot);
      i.pinhole = p;
      return i;
   }
}
