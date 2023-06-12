package libs.javalib.worldimages;

class ImageField {
   String name;
   Object value;
   boolean noNewlineBefore;

   ImageField(String name, Object value) {
      this(name, value, false);
   }

   ImageField(String name, Object value, boolean noNewlineBefore) {
      this.name = name;
      this.value = value;
      this.noNewlineBefore = noNewlineBefore;
   }
}
