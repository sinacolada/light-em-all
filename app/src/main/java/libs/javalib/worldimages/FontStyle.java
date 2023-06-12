package libs.javalib.worldimages;

public enum FontStyle {
   REGULAR,
   BOLD,
   ITALIC,
   BOLD_ITALIC;

   @Override
   public String toString() {
      return this.name().toLowerCase();
   }

   public static FontStyle fromString(String name) {
      return valueOf(name.toUpperCase());
   }
}
