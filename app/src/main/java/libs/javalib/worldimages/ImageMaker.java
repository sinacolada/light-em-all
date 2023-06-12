package libs.javalib.worldimages;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public final class ImageMaker {
   public int width;
   public int height;
   File inputfile;
   public static HashMap<String, BufferedImage> loadedImages = new HashMap<>();
   public BufferedImage imageSource;
   public BufferedImage image;
   public ColorModel cmodel;
   public static ColorModel canvasColorModel;
   public ColorConvertOp colorOp;

   protected ImageMaker() {
   }

   public ImageMaker(String filename) {
      try {
         this.inputfile = new File(filename);
         String abs = this.inputfile.getCanonicalPath();
         if (loadedImages.containsKey(abs)) {
            this.image = loadedImages.get(abs);
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();
         } else {
            this.imageSource = ImageIO.read(this.inputfile);
            this.width = this.imageSource.getWidth();
            this.height = this.imageSource.getHeight();
            this.cmodel = this.imageSource.getColorModel();
            this.image = new BufferedImage(this.width, this.height, 2);
            this.colorOp = new ColorConvertOp(this.cmodel.getColorSpace(), this.image.getColorModel().getColorSpace(), null);
            this.colorOp.filter(this.imageSource, this.image);
            loadedImages.put(abs, this.image);
         }
      } catch (IOException var3) {
         System.out.println("Could not open the image file " + filename);
      }
   }

   public Color getColorPixel(int x, int y) throws IndexOutOfBoundsException {
      WorldImage.boundsCheck(x, y, this.width, this.height);
      int[] ans = new int[4];
      this.image.getRaster().getPixel(x, y, ans);
      return new Color(ans[0], ans[1], ans[2], ans[3]);
   }
}
