package libs.javalib.impworld;

import libs.javalib.worldcanvas.WorldSceneBase;
import libs.javalib.worldimages.WorldImage;

public class WorldScene extends WorldSceneBase {
   public WorldScene(int width, int height) {
      super(width, height);
   }

   public void placeImageXY(WorldImage image, int x, int y) {
      this.imgs = this.imgs.add(new WorldSceneBase.PlaceImage(image, x, y));
      this.revImgs = null;
   }
}
