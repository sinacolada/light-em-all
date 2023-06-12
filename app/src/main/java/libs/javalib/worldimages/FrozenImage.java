package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Stack;

public final class FrozenImage extends WorldImage {
   BufferedImage img;

   public FrozenImage(WorldImage img) {
      super(1);
      this.img = new BufferedImage((int)img.getWidth(), (int)img.getHeight(), 2);
      Graphics2D g = this.img.createGraphics();
      g.translate(img.getWidth() / 2.0, img.getHeight() / 2.0);
      img.draw(g);
      g.dispose();
      this.pinhole = img.pinhole;
   }

   private FrozenImage(BufferedImage img, Posn pinhole) {
      super(pinhole, 1);
      this.img = img;
   }

   public Color getColorAt(int x, int y) throws IndexOutOfBoundsException {
      boundsCheck(x, y, this.img.getWidth(), this.img.getHeight());
      int[] ans = new int[4];
      this.img.getRaster().getPixel(x, y, ans);
      return new Color(ans[0], ans[1], ans[2], ans[3]);
   }

   @Override
   int numKids() {
      return 0;
   }

   @Override
   WorldImage getKid(int i) {
      throw new IllegalArgumentException("No such kid " + i);
   }

   @Override
   AffineTransform getTransform(int i) {
      throw new IllegalArgumentException("No such kid " + i);
   }

   @Override
   protected BoundingBox getBBHelp(AffineTransform t) {
      return new BoundingBox(WorldImage.transformPosn(t, 0.0, 0.0), WorldImage.transformPosn(t, this.getWidth(), this.getHeight()));
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      return new FrozenImage(this.img, p);
   }

   @Override
   protected void drawStackUnsafe(Graphics2D g) {
      g.drawImage(this.img, AffineTransform.getTranslateInstance(-this.getWidth() / 2.0, -this.getHeight() / 2.0), null);
   }

   @Override
   protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
      this.drawStackUnsafe(g);
   }

   @Override
   public double getWidth() {
      return (double)this.img.getWidth();
   }

   @Override
   public double getHeight() {
      return (double)this.img.getHeight();
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      if (this.pinhole.x == 0 && this.pinhole.y == 0) {
         sb = sb.append("new FrozenImage()");
      } else {
         sb = sb.append("new FrozenImage(pinhole = ").append(this.pinhole.toString()).append(")");
      }

      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (other instanceof FrozenImage && this.pinhole.equals(other.pinhole)) {
         FrozenImage that = (FrozenImage)other;
         BufferedImage imgA = this.img;
         BufferedImage imgB = that.img;
         if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {
            int width = imgA.getWidth();
            int height = imgA.getHeight();

            for(int y = 0; y < height; ++y) {
               for(int x = 0; x < width; ++x) {
                  if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                     return false;
                  }
               }
            }

            return true;
         }
      }

      return false;
   }
}
