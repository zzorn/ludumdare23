package ludumdare23;

import net.zzorn.gameflow.picture.PartPicture;
import net.zzorn.gameflow.picture.Picture;
import net.zzorn.utils.ImageUtils$;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 */
public class HealthBar {
    private final Image fullPic;
    private final Image emptyPic;

    public HealthBar(BufferedImage pic, double scale) {
        BufferedImage sourcePic;
        if (scale != 1.0) {
            int scaledW = (int)(pic.getWidth()  * scale);
            int scaledH = (int)(pic.getHeight() * scale);

            sourcePic = ImageUtils$.MODULE$.createScreenCompatibleImage(scaledW, scaledH, Transparency.BITMASK);
            sourcePic.getGraphics().drawImage(pic, 0,0, scaledW-1, scaledH-1, 0,0,pic.getWidth(), pic.getHeight(), null);
        }
        else {
            sourcePic = pic;
        }

        int halfW = sourcePic.getWidth() / 2;
        int halfH = sourcePic.getHeight() / 2;
        fullPic  = sourcePic.getSubimage(0,     0, halfW, halfH);
        emptyPic = sourcePic.getSubimage(halfW, 0, halfW, halfH);
    }

    public int getWidth() {
        return fullPic.getWidth(null);
    }

    public int getHeight() {
        return fullPic.getHeight(null);
    }

    public void draw(Graphics2D g, int x, int y, double percent) {
        int fullW = (int)(getWidth() * percent);
        g.drawImage(fullPic,  x, y, x+fullW, y+getHeight(), 0,0,fullW, getHeight(), null);
        g.drawImage(emptyPic, x+fullW, y, x+getWidth(), y+getHeight(), fullW,0,getWidth(), getHeight(), null);
    }
}
