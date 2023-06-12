package libs.javalib.worldimages;

import java.util.Iterator;

class FieldsWLItem implements Iterator<ImageField> {
   final Posn pinhole;
   final ImageField[] fields;
   int cur;

   FieldsWLItem(Posn pinhole, ImageField... fields) {
      this.pinhole = pinhole;
      this.fields = fields;
   }

   boolean first() {
      return this.cur == 0;
   }

   @Override
   public boolean hasNext() {
      return this.cur <= this.fields.length;
   }

   public ImageField next() {
      if (this.cur == 0) {
         ++this.cur;
         if (this.pinhole != null && (this.pinhole.x != 0 || this.pinhole.y != 0)) {
            return new ImageField("pinhole", this.pinhole);
         }
      }

      return this.fields[this.cur++ - 1];
   }

   @Override
   public void remove() {
   }

   void skipToEnd() {
      this.cur = this.fields.length + 1;
   }
}
