package libs.javalib.worldimages;

import java.awt.Color;

public final class RectangleImage extends RectangleImageBase {
   public RectangleImage(int width, int height, OutlineMode fill, Color color) {
      super(width, height, fill, color);
   }

   public RectangleImage(int width, int height, String fill, Color color) {
      super(width, height, fill, color);
   }
}
