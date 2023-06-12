package libs.javalib.funworld;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import libs.javalib.worldimages.Posn;
import javax.swing.SwingUtilities;

final class MyMouseAdapter extends MouseAdapter {
   World currentWorld;
   Posn mousePosn;

   private String buttonNameFor(MouseEvent evt) {
      if (SwingUtilities.isLeftMouseButton(evt)) {
         return "LeftButton";
      } else if (SwingUtilities.isMiddleMouseButton(evt)) {
         return "MiddleButton";
      } else {
         return SwingUtilities.isRightMouseButton(evt) ? "RightButton" : "UnknownButton";
      }
   }

   MyMouseAdapter(World currentWorld) {
      this.currentWorld = currentWorld;
   }

   Posn adjustMousePosn(Posn mousePosn) {
      Insets ins = this.currentWorld.theCanvas.frame.getInsets();
      mousePosn.y -= ins.top;
      mousePosn.x -= ins.left;
      return mousePosn;
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      this.currentWorld.stopTimer = true;
      this.mousePosn = new Posn(e.getX(), e.getY());
      this.currentWorld = this.currentWorld.processMouseClicked(this.adjustMousePosn(this.mousePosn), this.buttonNameFor(e));
      this.currentWorld.stopTimer = false;
   }

   @Override
   public void mouseEntered(MouseEvent e) {
      this.currentWorld.stopTimer = true;
      this.mousePosn = new Posn(e.getX(), e.getY());
      this.currentWorld = this.currentWorld.processMouseEntered(this.adjustMousePosn(this.mousePosn));
      this.currentWorld.stopTimer = false;
   }

   @Override
   public void mouseExited(MouseEvent e) {
      this.currentWorld.stopTimer = true;
      this.mousePosn = new Posn(e.getX(), e.getY());
      this.currentWorld = this.currentWorld.processMouseExited(this.adjustMousePosn(this.mousePosn));
      this.currentWorld.stopTimer = false;
   }

   @Override
   public void mousePressed(MouseEvent e) {
      this.currentWorld.stopTimer = true;
      this.mousePosn = new Posn(e.getX(), e.getY());
      this.currentWorld = this.currentWorld.processMousePressed(this.adjustMousePosn(this.mousePosn), this.buttonNameFor(e));
      this.currentWorld.stopTimer = false;
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      this.currentWorld.stopTimer = true;
      this.mousePosn = new Posn(e.getX(), e.getY());
      this.currentWorld = this.currentWorld.processMouseReleased(this.adjustMousePosn(this.mousePosn), this.buttonNameFor(e));
      this.currentWorld.stopTimer = false;
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      this.currentWorld.stopTimer = true;
      this.mousePosn = new Posn(e.getX(), e.getY());
      this.currentWorld.processMouseMoved(this.adjustMousePosn(this.mousePosn), this.buttonNameFor(e));
      this.currentWorld.stopTimer = false;
   }
}
