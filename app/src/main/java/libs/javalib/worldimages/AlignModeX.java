package libs.javalib.worldimages;

public enum AlignModeX {
   LEFT,
   RIGHT,
   CENTER,
   PINHOLE;

   @Override
   public String toString() {
      return this.name().toLowerCase();
   }

   public static AlignModeX fromString(String name) {
      return valueOf(name.toUpperCase());
   }
}
