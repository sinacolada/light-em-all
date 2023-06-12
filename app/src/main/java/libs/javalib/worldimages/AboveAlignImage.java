package libs.javalib.worldimages;

import java.util.Stack;

public final class AboveAlignImage extends OverlayOffsetAlignBase {
   private AboveAlignImage(AlignModeX mode, WorldImage im1, WorldImage im2) {
      super(mode, AlignModeY.PINHOLE, im1, 0.0, im1.getHeight() / 2.0 + im2.getHeight() / 2.0, im2);
   }

   public AboveAlignImage(AlignModeX mode, WorldImage im1, WorldImage... ims) {
      this(mode, im1, multipleImageHandling(mode, ims));
   }

   public AboveAlignImage(String mode, WorldImage im1, WorldImage... ims) {
      this(AlignModeX.fromString(mode), im1, ims);
   }

   private static WorldImage multipleImageHandling(AlignModeX mode, WorldImage[] ims) {
      if (ims.length == 0) {
         return new EmptyImage();
      } else if (ims.length <= 1) {
         return ims[0];
      } else {
         WorldImage[] images = new WorldImage[ims.length - 1];
         System.arraycopy(ims, 1, images, 0, images.length);
         return new AboveAlignImage(mode, ims[0], images);
      }
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ").append(this.simpleName()).append("(").append("this.mode = ").append(this.alignX).append(",");
      stack.push(new FieldsWLItem(this.pinhole, new ImageField("im1", this.top), new ImageField("im2", this.bot)));
      return sb;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new AboveAlignImage(this.alignX, this.top, this.bot);
      i.pinhole = p;
      return i;
   }
}
