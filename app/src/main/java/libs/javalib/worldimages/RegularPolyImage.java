package libs.javalib.worldimages;

import java.awt.Color;

public final class RegularPolyImage extends RegularPolyImageBase {
   public RegularPolyImage(double sideLen, int numSides, OutlineMode fill, Color color) {
      this(sideLen, LengthMode.SIDE, numSides, fill, color);
   }

   public RegularPolyImage(double sideLen, int numSides, String fill, Color color) {
      this(sideLen, LengthMode.SIDE, numSides, OutlineMode.fromString(fill), color);
   }

   public RegularPolyImage(double length, String lengthMode, int numSides, OutlineMode fill, Color color) {
      this(length, LengthMode.fromString(lengthMode), numSides, fill, color);
   }

   public RegularPolyImage(double length, LengthMode lengthMode, int numSides, OutlineMode fill, Color color) {
      super(length, lengthMode, numSides, fill, color);
   }

   public RegularPolyImage(double length, String lengthMode, int numSides, String fill, Color color) {
      super(length, LengthMode.fromString(lengthMode), numSides, OutlineMode.fromString(fill), color);
   }

   public RegularPolyImage(double length, LengthMode lengthMode, int numSides, String fill, Color color) {
      super(length, lengthMode, numSides, OutlineMode.fromString(fill), color);
   }
}
