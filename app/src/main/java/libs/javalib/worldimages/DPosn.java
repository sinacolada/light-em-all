package libs.javalib.worldimages;

class DPosn {
   private static final double EPSILON = 1.0E-6;
   public double x;
   public double y;

   public DPosn(double x, double y) {
      this.x = x;
      this.y = y;
   }

   @Override
   public boolean equals(Object other) {
      if (!(other instanceof DPosn)) {
         return false;
      } else {
         DPosn that = (DPosn)other;
         return Math.abs(this.x - that.x) < 1.0E-6 && Math.abs(this.y - that.y) < 1.0E-6;
      }
   }

   @Override
   public int hashCode() {
      return (int)(this.x * 2939.0 + this.y);
   }

   public Posn asPosn() {
      return new Posn((int)Math.round(this.x), (int)Math.round(this.y));
   }

   @Override
   public String toString() {
      return String.format("new DPosn(x = %f, y = %f)", this.x, this.y);
   }
}
