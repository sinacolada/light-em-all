package libs.javalib.worldimages;

import java.awt.Color;

public final class HexagonImage extends RegularPolyImageBase {
   public HexagonImage(double sideLen, OutlineMode fill, Color color) {
      super(sideLen, LengthMode.SIDE, 6, fill, color);
   }

   public HexagonImage(double sideLen, String fill, Color color) {
      this(sideLen, OutlineMode.fromString(fill), color);
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new HexagonImage(this.sideLen, this.fill, this.color);
      i.pinhole = p;
      return i;
   }
}
