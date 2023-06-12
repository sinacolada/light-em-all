package libs.javalib.worldimages;

import java.util.Stack;

public final class OverlayImage extends OverlayOffsetAlignBase {
   public OverlayImage(WorldImage top, WorldImage bot) {
      super(AlignModeX.PINHOLE, AlignModeY.PINHOLE, top, 0.0, 0.0, bot);
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("top", this.top), new ImageField("bot", this.bot)));
      return sb;
   }

   @Override
   public int hashCode() {
      return this.bot.hashCode() + this.top.hashCode();
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new OverlayImage(this.top, this.bot);
      i.pinhole = p;
      return i;
   }
}
