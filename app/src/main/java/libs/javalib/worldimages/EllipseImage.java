package libs.javalib.worldimages;

import java.awt.Color;

public final class EllipseImage extends EllipseImageBase {
   public EllipseImage(int width, int height, OutlineMode outlineMode, Color color) {
      super(width, height, outlineMode, color);
   }

   public EllipseImage(int width, int height, String outlineMode, Color color) {
      super(width, height, outlineMode, color);
   }
}
