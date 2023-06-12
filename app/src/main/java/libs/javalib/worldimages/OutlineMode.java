package libs.javalib.worldimages;

public enum OutlineMode {
   SOLID,
   OUTLINE;

   @Override
   public String toString() {
      return this.name().toLowerCase();
   }

   public static OutlineMode fromString(String name) {
      return valueOf(name.toUpperCase());
   }
}
