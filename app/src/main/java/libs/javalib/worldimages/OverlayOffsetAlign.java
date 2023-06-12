package libs.javalib.worldimages;

public final class OverlayOffsetAlign extends OverlayOffsetAlignBase {
   public OverlayOffsetAlign(AlignModeX alignX, AlignModeY alignY, WorldImage top, double dx, double dy, WorldImage bot) {
      super(alignX, alignY, top, dx, dy, bot);
   }

   public OverlayOffsetAlign(String alignX, String alignY, WorldImage top, double dx, double dy, WorldImage bot) {
      super(AlignModeX.fromString(alignX), AlignModeY.fromString(alignY), top, dx, dy, bot);
   }
}
