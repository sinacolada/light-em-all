package libs.javalib.funworld;

import libs.javalib.worldcanvas.WorldSceneBase;
import libs.javalib.worldimages.WorldImage;

public class WorldScene extends WorldSceneBase {
   public WorldScene(int width, int height) {
      super(width, height);
   }

   private WorldScene(int width, int height, WorldSceneBase.IList<WorldSceneBase.PlaceImage> imgs) {
      super(width, height, imgs);
   }

   public WorldScene placeImageXY(WorldImage image, int x, int y) {
      return new WorldScene(this.width, this.height, this.imgs.add(new WorldSceneBase.PlaceImage(image, x, y)));
   }
}
