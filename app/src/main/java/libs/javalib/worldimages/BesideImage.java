package libs.javalib.worldimages;

import java.util.Stack;

public final class BesideImage extends OverlayOffsetAlignBase {
   private BesideImage(WorldImage im1, WorldImage im2) {
      super(AlignModeX.PINHOLE, AlignModeY.PINHOLE, im1, im1.getWidth() / 2.0 + im2.getWidth() / 2.0, 0.0, im2);
   }

   public BesideImage(WorldImage im1, WorldImage... ims) {
      this(im1, multipleImageHandling(ims));
   }

   private static WorldImage multipleImageHandling(WorldImage[] ims) {
      if (ims.length == 0) {
         return new EmptyImage();
      } else if (ims.length <= 1) {
         return ims[0];
      } else {
         WorldImage[] images = new WorldImage[ims.length - 1];
         System.arraycopy(ims, 1, images, 0, images.length);
         return new BesideImage(ims[0], images);
      }
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("im1", this.top), new ImageField("im2", this.bot)));
      return sb;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new BesideImage(this.top, this.bot);
      i.pinhole = p;
      return i;
   }
}
