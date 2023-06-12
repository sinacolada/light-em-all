package libs.javalib.worldimages;

public enum AlignModeY {
   BOTTOM,
   TOP,
   MIDDLE,
   PINHOLE;

   @Override
   public String toString() {
      return this.name().toLowerCase();
   }

   public static AlignModeY fromString(String name) {
      return valueOf(name.toUpperCase());
   }
}
