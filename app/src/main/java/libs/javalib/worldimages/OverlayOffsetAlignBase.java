package libs.javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Stack;

abstract class OverlayOffsetAlignBase extends WorldImage {
   public WorldImage bot;
   public WorldImage top;
   protected DPosn deltaTop;
   protected DPosn deltaBot;
   public double dx;
   public double dy;
   public AlignModeX alignX;
   public AlignModeY alignY;
   private int hashCode;

   public OverlayOffsetAlignBase(AlignModeX alignX, AlignModeY alignY, WorldImage top, double dx, double dy, WorldImage bot) {
      super(1 + Math.max(top.depth, bot.depth));
      this.bot = bot;
      this.top = top;
      this.alignX = alignX;
      this.alignY = alignY;
      this.dx = dx;
      this.dy = dy;
      double botWidth = this.bot.getWidth();
      double topWidth = this.top.getWidth();
      double botHeight = this.bot.getHeight();
      double topHeight = this.top.getHeight();
      double rightX = Math.max(botWidth / 2.0 + this.xBotMoveDist(), topWidth / 2.0 + this.xTopMoveDist());
      double leftX = Math.min(-botWidth / 2.0 + this.xBotMoveDist(), -topWidth / 2.0 + this.xTopMoveDist());
      double bottomY = Math.max(botHeight / 2.0 + this.yBotMoveDist(), topHeight / 2.0 + this.yTopMoveDist());
      double topY = Math.min(-botHeight / 2.0 + this.yBotMoveDist(), -topHeight / 2.0 + this.yTopMoveDist());
      double centerX = (rightX + leftX) / 2.0;
      double centerY = (bottomY + topY) / 2.0;
      double botDeltaY = -centerY + this.yBotMoveDist();
      double topDeltaY = -centerY + this.yTopMoveDist();
      double botDeltaX = -centerX + this.xBotMoveDist();
      double topDeltaX = -centerX + this.xTopMoveDist();
      this.deltaBot = new DPosn(botDeltaX, botDeltaY);
      this.deltaTop = new DPosn(topDeltaX, topDeltaY);
      if (alignY == AlignModeY.PINHOLE && alignX == AlignModeX.PINHOLE && dx == 0.0 && dy == 0.0) {
         this.pinhole = new Posn((int)(-Math.round(centerX)), (int)(-Math.round(centerY)));
      }

      this.hashCode = this.bot.hashCode() + this.top.hashCode() + this.alignX.hashCode() + this.alignY.hashCode() + (int)this.dx * 37 + (int)this.dy * 16;
   }

   @Override
   protected BoundingBox getBBHelp(AffineTransform t) {
      AffineTransform temp = new AffineTransform(t);
      temp.translate(this.deltaBot.x, this.deltaBot.y);
      BoundingBox botBox = this.bot.getBB(temp);
      temp.setTransform(t);
      temp.translate(this.deltaTop.x, this.deltaTop.y);
      BoundingBox topBox = this.top.getBB(temp);
      return botBox.combine(topBox);
   }

   @Override
   int numKids() {
      return 2;
   }

   @Override
   WorldImage getKid(int i) {
      if (i == 0) {
         return this.bot;
      } else if (i == 1) {
         return this.top;
      } else {
         throw new IllegalArgumentException("No such kid " + i);
      }
   }

   @Override
   AffineTransform getTransform(int i) {
      if (i == 0) {
         return AffineTransform.getTranslateInstance(this.deltaBot.x, this.deltaBot.y);
      } else if (i == 1) {
         return AffineTransform.getTranslateInstance(this.deltaTop.x, this.deltaTop.y);
      } else {
         throw new IllegalArgumentException("No such kid " + i);
      }
   }

   private double yBotMoveDist() {
      double moveDist = 0.0;
      if (this.alignY == AlignModeY.TOP || this.alignY == AlignModeY.BOTTOM) {
         double h1 = this.top.getHeight();
         double h2 = this.bot.getHeight();
         if (this.alignY == AlignModeY.TOP) {
            moveDist = (h2 - h1) / 2.0;
         } else if (this.alignY == AlignModeY.BOTTOM) {
            moveDist = (h1 - h2) / 2.0;
         }
      } else if (this.alignY == AlignModeY.PINHOLE) {
         moveDist = (double)(-this.bot.pinhole.y);
      }

      return moveDist + this.dy;
   }

   private double yTopMoveDist() {
      return this.alignY == AlignModeY.PINHOLE ? (double)(-this.top.pinhole.y) : 0.0;
   }

   private double xBotMoveDist() {
      double moveDist = 0.0;
      if (this.alignX == AlignModeX.LEFT || this.alignX == AlignModeX.RIGHT) {
         double w1 = this.top.getWidth();
         double w2 = this.bot.getWidth();
         if (this.alignX == AlignModeX.LEFT) {
            moveDist = (w2 - w1) / 2.0;
         } else if (this.alignX == AlignModeX.RIGHT) {
            moveDist = (w1 - w2) / 2.0;
         }
      } else if (this.alignX == AlignModeX.PINHOLE) {
         moveDist = (double)(-this.bot.pinhole.x);
      }

      return moveDist + this.dx;
   }

   private double xTopMoveDist() {
      return this.alignX == AlignModeX.PINHOLE ? (double)(-this.top.pinhole.x) : 0.0;
   }

   @Override
   protected void drawStackUnsafe(Graphics2D g) {
      AffineTransform old = g.getTransform();
      g.translate(this.deltaBot.x, this.deltaBot.y);
      this.bot.drawStackUnsafe(g);
      g.setTransform(old);
      g.translate(this.deltaTop.x, this.deltaTop.y);
      this.top.drawStackUnsafe(g);
      g.setTransform(old);
   }

   @Override
   protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
      AffineTransform cur = g.getTransform();
      cur.translate(this.deltaTop.x, this.deltaTop.y);
      txs.push(cur);
      images.push(this.top);
      cur = g.getTransform();
      cur.translate(this.deltaBot.x, this.deltaBot.y);
      txs.push(cur);
      images.push(this.bot);
   }

   @Override
   public double getWidth() {
      return this.getBB().getWidth();
   }

   @Override
   public double getHeight() {
      return this.getBB().getHeight();
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ")
         .append(this.simpleName())
         .append("(")
         .append("this.alignX = ")
         .append(this.alignX)
         .append(", ")
         .append("this.alignY = ")
         .append(this.alignY)
         .append(",");
      stack.push(
         new FieldsWLItem(
            this.pinhole, new ImageField("top", this.top), new ImageField("dx", this.dx), new ImageField("dy", this.dy, true), new ImageField("bot", this.bot)
         )
      );
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (this.getClass().equals(other.getClass())) {
         OverlayOffsetAlignBase that = (OverlayOffsetAlignBase)other;
         if (this.alignX == that.alignX && this.alignY == that.alignY && this.dx == that.dx && this.dy == that.dy && this.pinhole.equals(that.pinhole)) {
            worklistThis.push(this.bot);
            worklistThat.push(that.bot);
            worklistThis.push(this.top);
            worklistThat.push(that.top);
            return true;
         }
      }

      return false;
   }

   @Override
   public int hashCode() {
      return this.hashCode;
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new OverlayOffsetAlign(this.alignX, this.alignY, this.top, this.dx, this.dy, this.bot);
      i.pinhole = p;
      return i;
   }
}
