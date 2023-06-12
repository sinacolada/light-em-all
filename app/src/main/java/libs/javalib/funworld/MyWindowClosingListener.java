package libs.javalib.funworld;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MyWindowClosingListener extends WindowAdapter {
   World w;

   MyWindowClosingListener(World w) {
      this.w = w;
   }

   @Override
   public void windowClosing(WindowEvent we) {
      if (this.w != null && this.w.mytime != null) {
         this.w.mytime.stopTimer();
      }
   }
}
