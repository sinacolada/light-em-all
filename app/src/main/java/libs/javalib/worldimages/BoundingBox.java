package libs.javalib.worldimages;

import java.awt.geom.Point2D;

public final class BoundingBox {
   private double tlx;
   private double tly;
   private double brx;
   private double bry;

   static BoundingBox containing(Point2D... p) {
      if (p.length == 0) {
         throw new IllegalArgumentException("Can't create a bounding box without at least one point");
      } else {
         BoundingBox b = new BoundingBox(p[0], p[0]);

         for(int i = 1; i < p.length; ++i) {
            b.combineWith(p[i]);
         }

         return b;
      }
   }

   BoundingBox(BoundingBox bb) {
      this(bb.tlx, bb.tly, bb.brx, bb.bry);
   }

   BoundingBox(Posn tl, Posn br) {
      this((double)tl.x, (double)tl.y, (double)br.x, (double)br.y);
   }

   BoundingBox(Point2D tl, Point2D br) {
      this(tl.getX(), tl.getY(), br.getX(), br.getY());
   }

   BoundingBox(double tlx, double tly, double brx, double bry) {
      this.tlx = Math.min(tlx, brx);
      this.tly = Math.min(tly, bry);
      this.brx = Math.max(tlx, brx);
      this.bry = Math.max(tly, bry);
   }

   boolean contains(Posn p) {
      return this.contains((double)p.x, (double)p.y);
   }

   boolean contains(Point2D p) {
      return this.contains(p.getX(), p.getY());
   }

   boolean contains(double px, double py) {
      return this.tlx <= px && px <= this.brx && this.tly <= py && py <= this.bry;
   }

   BoundingBox combine(BoundingBox other) {
      if (this.tlx <= other.tlx && other.brx <= this.brx && this.tly <= other.tly && other.bry <= this.bry) {
         return this;
      } else {
         return other.tlx <= this.tlx && this.brx <= other.brx && other.tly <= this.tly && this.bry <= other.bry
            ? other
            : new BoundingBox(Math.min(this.tlx, other.tlx), Math.min(this.tly, other.tly), Math.max(this.brx, other.brx), Math.max(this.bry, other.bry));
      }
   }

   void combineWith(BoundingBox other) {
      this.tlx = Math.min(this.tlx, other.tlx);
      this.tly = Math.min(this.tly, other.tly);
      this.brx = Math.max(this.brx, other.brx);
      this.bry = Math.max(this.bry, other.bry);
   }

   void combineWith(Posn p) {
      this.combineWith((double)p.x, (double)p.y);
   }

   void combineWith(Point2D p) {
      this.combineWith(p.getX(), p.getY());
   }

   void combineWith(double px, double py) {
      this.tlx = Math.min(this.tlx, px);
      this.tly = Math.min(this.tly, py);
      this.brx = Math.max(this.brx, px);
      this.bry = Math.max(this.bry, py);
   }

   BoundingBox translated(double dx, double dy) {
      return new BoundingBox(this.tlx + dx, this.tly + dy, this.brx + dx, this.bry + dy);
   }

   BoundingBox add(Posn p) {
      return this.add((double)p.x, (double)p.y);
   }

   BoundingBox add(Point2D p) {
      return this.add(p.getX(), p.getY());
   }

   BoundingBox add(double px, double py) {
      if (this.contains(px, py)) {
         return this;
      } else {
         BoundingBox ans = new BoundingBox(this);
         ans.combineWith(px, py);
         return ans;
      }
   }

   double getWidth() {
      return this.brx - this.tlx;
   }

   double getHeight() {
      return this.bry - this.tly;
   }

   double getTlx() {
      return this.tlx;
   }

   double getTly() {
      return this.tly;
   }

   double getBrx() {
      return this.brx;
   }

   double getBry() {
      return this.bry;
   }

   @Override
   public String toString() {
      return String.format("BB((%f,%f)-(%f,%f))", this.tlx, this.tly, this.brx, this.bry);
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof BoundingBox)) {
         return false;
      } else {
         BoundingBox other = (BoundingBox)obj;
         return this.tlx == other.tlx && this.tly == other.tly && this.brx == other.brx && this.bry == other.bry;
      }
   }

   @Override
   public int hashCode() {
      return (int)(this.tlx * 37.0 + this.tly * 43.0 + this.brx * 91.0 + this.bry * 103.0);
   }
}
