package libs.javalib.worldimages;

import java.util.Stack;

public final class AboveImage extends OverlayOffsetAlignBase {
   private AboveImage(WorldImage im1, WorldImage im2) {
      super(AlignModeX.PINHOLE, AlignModeY.PINHOLE, im1, 0.0, im1.getHeight() / 2.0 + im2.getHeight() / 2.0, im2);
   }

   public AboveImage(WorldImage im1, WorldImage... ims) {
      this(im1, multipleImageHandling(ims));
   }

   public AboveImage(String mode, WorldImage im1, WorldImage... ims) {
      this(im1, ims);
   }

   private static WorldImage multipleImageHandling(WorldImage[] ims) {
      if (ims.length == 0) {
         return new EmptyImage();
      } else if (ims.length <= 1) {
         return ims[0];
      } else {
         WorldImage[] images = new WorldImage[ims.length - 1];
         System.arraycopy(ims, 1, images, 0, images.length);
         return new AboveImage(ims[0], images);
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
      WorldImage i = new AboveImage(this.top, this.bot);
      i.pinhole = p;
      return i;
   }
}
