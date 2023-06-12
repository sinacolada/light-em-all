package libs.javalib.worldimages;

public enum LengthMode {
   RADIUS,
   SIDE;

   @Override
   public String toString() {
      return this.name().toLowerCase();
   }

   public static LengthMode fromString(String name) {
      return valueOf(name.toUpperCase());
   }
}
