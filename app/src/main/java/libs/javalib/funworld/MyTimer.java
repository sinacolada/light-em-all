package libs.javalib.funworld;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

final class MyTimer {
   World currentWorld;
   Timer timer;
   public boolean running = true;
   final int speed;
   ActionListener timerTasks = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
         if (MyTimer.this.running) {
            MyTimer.this.currentWorld.processTick();
         }
      }
   };

   MyTimer(World currentWorld, double speed) {
      this.currentWorld = currentWorld;
      this.timer = new Timer((int)(speed * 1000.0), this.timerTasks);
      this.speed = (int)(speed * 1000.0);
   }

   void setSpeed() {
      this.timer.setDelay(this.speed);
   }

   void stopTimer() {
      this.running = false;
   }
}
