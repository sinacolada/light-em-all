package libs.javalib.funworld;

import libs.javalib.utils.AbstractKeyAdapter;

final class MyKeyAdapter extends AbstractKeyAdapter {
   MyKeyAdapter(World currentWorld) {
      super(new MyKeyAdapter.OnKey(currentWorld), new MyKeyAdapter.OnReleased(currentWorld));
   }

   void resetWorld(World currentWorld) {
      this.onKey = new MyKeyAdapter.OnKey(currentWorld);
      this.onReleased = new MyKeyAdapter.OnReleased(currentWorld);
   }

   static class OnKey implements AbstractKeyAdapter.Consumer<String> {
      final World currentWorld;

      OnKey(World w) {
         this.currentWorld = w;
      }

      public void apply(String data) {
         this.currentWorld.processKeyEvent(data);
      }
   }

   static class OnReleased implements AbstractKeyAdapter.Consumer<String> {
      final World currentWorld;

      OnReleased(World w) {
         this.currentWorld = w;
      }

      public void apply(String data) {
         this.currentWorld.processKeyReleased(data);
      }
   }
}
