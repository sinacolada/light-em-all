package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.Stack;
import libs.javalib.worldcanvas.CanvasPanel;

public final class TextImage extends WorldImage {
   public String text;
   public double size;
   public Color color;
   public FontStyle style = FontStyle.REGULAR;
   public double width = 0.0;
   public double height = 0.0;
   private double baselineDy = 0.0;
   public static CanvasPanel c = new CanvasPanel(600, 600);
   protected static Graphics2D g = c.getBufferGraphics();
   protected static Font font = g.getFont();

   public TextImage(String text, double size, FontStyle style, Color color) {
      super(1);
      if (text == null || text.equals("")) {
         text = " ";
      }

      this.text = text;
      this.size = size;
      this.style = style;
      this.color = color;
      this.setWidthHeight();
   }

   public TextImage(String text, double size, Color color) {
      this(text, size, FontStyle.REGULAR, color);
   }

   public TextImage(String text, int size, Color color) {
      this(text, (double)size, FontStyle.REGULAR, color);
   }

   public TextImage(String text, Color color) {
      this(text, 13.0, FontStyle.REGULAR, color);
   }

   @Override
   int numKids() {
      return 0;
   }

   @Override
   WorldImage getKid(int i) {
      throw new IllegalArgumentException("No such kid " + i);
   }

   @Override
   AffineTransform getTransform(int i) {
      throw new IllegalArgumentException("No such kid " + i);
   }

   @Override
   protected void drawStackUnsafe(Graphics2D g) {
      if (this.text == null) {
         this.text = "";
      }

      if (this.color == null) {
         this.color = new Color(0, 0, 0);
      }

      Paint oldPaint = g.getPaint();
      Font oldFont = g.getFont();
      g.setFont(oldFont.deriveFont(this.style.ordinal(), (float)this.size));
      g.setPaint(this.color);
      g.drawString(this.text, (int)(-Math.round(this.width / 2.0)), (int)(-this.baselineDy));
      g.setPaint(oldPaint);
      g.setFont(oldFont);
   }

   @Override
   protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
      this.drawStackUnsafe(g);
   }

   protected void setWidthHeight() {
      Rectangle2D bounds = this.getBoundingBox();
      this.width = (double)((int)bounds.getWidth());
      this.height = (double)((int)bounds.getHeight());
   }

   @Override
   protected BoundingBox getBBHelp(AffineTransform t) {
      AffineTransform old = g.getTransform();
      g.setTransform(t);
      Rectangle2D bounds = this.getBoundingBox();
      g.setTransform(old);
      return new BoundingBox(bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY());
   }

   private Rectangle2D getBoundingBox() {
      Font newFont = font.deriveFont(this.style.ordinal(), (float)this.size);
      FontRenderContext frc = new FontRenderContext(null, true, true);
      TextLayout layout = new TextLayout(this.text, newFont, frc);
      double width = layout.getBounds().getWidth();
      double height = (double)(layout.getAscent() + layout.getDescent());
      this.baselineDy = height / 2.0 - (double)layout.getAscent();
      Rectangle2D ans = new Double(-width / 2.0, -height / 2.0, width, height);
      return ans;
   }

   @Override
   public double getWidth() {
      return this.width;
   }

   @Override
   public double getHeight() {
      return this.height;
   }

   @Override
   protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
      sb = sb.append("new ")
         .append(this.simpleName())
         .append("(")
         .append("this.text = \"")
         .append(this.text.replace("\\", "\\\\").replace("\"", "\\\""))
         .append("\",");
      stack.push(
         new FieldsWLItem(this.pinhole, new ImageField("size", this.size), new ImageField("style", this.style, true), new ImageField("color", this.color))
      );
      return sb;
   }

   @Override
   protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
      if (!(other instanceof TextImage)) {
         return false;
      } else {
         TextImage that = (TextImage)other;
         return this.size == that.size
            && this.style == that.style
            && this.text.equals(that.text)
            && this.color.equals(that.color)
            && this.pinhole.equals(that.pinhole);
      }
   }

   @Override
   public int hashCode() {
      return this.color.hashCode() + (int)this.size + this.style.hashCode() + this.text.hashCode();
   }

   @Override
   public WorldImage movePinholeTo(Posn p) {
      WorldImage i = new TextImage(this.text, this.size, this.style, this.color);
      i.pinhole = p;
      return i;
   }
}
