package libs.javalib.worldimages;

import java.util.Stack;

public final class BesideAlignImage extends OverlayOffsetAlignBase {
   private BesideAlignImage(AlignModeY mode, WorldImage im1, WorldImage im2) {
      super(AlignModeX.PINHOLE, mode, im1, im1.getWidth() / 2.0 + im2.getWidth() / 2.0, 0.0, im2);
   }

   public BesideAlignImage(AlignModeY mode, WorldImage im1, WorldImage... ims) {
      this(mode, im1, multipleImageHandling(mode, ims));
   }

   public BesideAlignImage(String mode, WorldImage im1, WorldImage... ims) {
      this(AlignModeY.fromString(mode), im1, ims);
   }

   private static WorldImage multipleImageHandling(AlignModeY mode, WorldImage[] ims) {
      if (ims.length == 0) {
         return new EmptyImage();
      } else if (ims.length <= 1) {
         return ims[0];
      } else {
         WorldImage[] images = new WorldImage[ims.length - 1];
         System.arraycopy(ims, 1, images, 0, images.length);
         return new BesideAlignImage(mode, ims[0], images);
      }
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(").append("this.mode = ").append(this.alignY).append(",");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("im1", this.top), new ImageField("im2", this.bot)));
      return sb;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new BesideAlignImage(this.alignY, this.top, this.bot);
      i.pinhole = p;
      return i;
   }
}
