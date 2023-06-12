package libs.javalib.worldimages;

import libs.javalib.worldcanvas.WorldSceneBase;

public final class WorldEnd {
   public boolean worldEnds;
   public WorldSceneBase lastScene;

   public WorldEnd(boolean worldEnds, WorldSceneBase lastScene) {
      this.worldEnds = worldEnds;
      this.lastScene = lastScene;
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof WorldEnd)) {
         return false;
      } else {
         WorldEnd end = (WorldEnd)obj;
         return this.worldEnds == end.worldEnds && this.lastScene.equals(end.lastScene);
      }
   }

   @Override
   public int hashCode() {
      int hash = this.lastScene.hashCode();
      if (this.worldEnds) {
         hash *= 2;
      }

      return hash;
   }

   public StringBuilder toIndentedString(StringBuilder sb, String linePrefix, int indent) {
      sb.append("new WorldEnd(this.worldEnds = ").append(this.worldEnds).append(",\n");
      sb.append(linePrefix).append("  this.lastScene = ");
      if (this.worldEnds) {
         this.lastScene.toIndentedString(sb, linePrefix + "  ", indent);
      } else {
         sb.append("<irrelevant>");
      }

      sb.append(")");
      return sb;
   }
}
