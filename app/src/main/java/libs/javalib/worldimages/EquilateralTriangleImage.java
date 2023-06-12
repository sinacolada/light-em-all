package libs.javalib.worldimages;

import java.awt.Color;

public final class EquilateralTriangleImage extends RegularPolyImageBase {
   public EquilateralTriangleImage(double sideLen, OutlineMode fill, Color color) {
      super(sideLen, LengthMode.SIDE, 3, fill, color);
   }

   public EquilateralTriangleImage(double sideLen, String fill, Color color) {
      this(sideLen, OutlineMode.fromString(fill), color);
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new EquilateralTriangleImage(this.sideLen, this.fill, this.color);
      i.pinhole = p;
      return i;
   }
}
