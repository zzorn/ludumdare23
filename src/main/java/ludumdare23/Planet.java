package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.picture.BufferedImagePicture;
import net.zzorn.gameflow.picture.Picture;
import net.zzorn.utils.ColorUtils$;
import net.zzorn.utils.RawImage;
import net.zzorn.utils.SimplexNoise$;
import net.zzorn.utils.Vec3;

import java.awt.*;
import java.util.Random;

/**
 * A home-world to defend
 */
public class Planet implements Entity {
    private Vec3 pos = new Vec3(0, 0, 0);
    private double mass_kg = 100000000000000000.0;
    private double radius_m = 1000;
    private final Picture picture;

    public Planet() {
        picture = createPicture(42, (int)radius_m);
    }

    public double getRadius_m() {
        return radius_m;
    }

    public double getMass_kg() {
        return mass_kg;
    }

    @Override
    public Vec3 pos() {
        return pos;
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y, double scale) {
        g.setColor(Color.GREEN);
        int r = (int) (radius_m * scale);
        g.fillOval(x - r, y - r, 2 * r, 2 * r);

        picture.drawCentered(g, x, y, scale);
    }

    @Override
    public void update(double durationSeconds) {

    }


    public double getSurfaceDist (Vec3 point, double radius) {
        double midDist = point.distance(pos());
        double distance = midDist - radius_m - radius;
        return (distance);
    }




    public Vec3 normalAt(Vec3 point) {
        Vec3 normal = new Vec3(0, 0, 0);
        normal.set(point);
        normal.setMinus(pos());
        normal.setNormalized();
        return normal;
    }



    private Picture createPicture(long seed, int size) {
        Random random = new Random(seed);

        RawImage rawImage = new RawImage(size, size);

        int colorCenterBot = ColorUtils$.MODULE$.HSLtoRGB(0.1, 0.3, 0.2, 1.0);
        int colorCenterTop = ColorUtils$.MODULE$.HSLtoRGB(0.1, 0.3, 0.3, 1.0);
        int colorCrustBot = ColorUtils$.MODULE$.HSLtoRGB(0.15, 0.34, 0.3, 1.0);
        int colorCrustTop = ColorUtils$.MODULE$.HSLtoRGB(0.12, 0.34, 0.35, 1.0);
        int colorGrassBot = ColorUtils$.MODULE$.HSLtoRGB(0.36, 0.34, 0.3, 1.0);
        int colorGrassTop = ColorUtils$.MODULE$.HSLtoRGB(0.26, 0.55, 0.5, 1.0);
        int colorAtmosphereBase = ColorUtils$.MODULE$.HSLtoRGB(0.5, 0.4, 0.66, 1.0);
        int colorAtmosphereTop = ColorUtils$.MODULE$.HSLtoRGB(0.6, 0.5, 0.5, 0.0);
        int colorSpaceTop = ColorUtils$.MODULE$.HSLtoRGB(0.7, 0, 0, 0.0);

        int i =0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                double rx = 2.0 * x / size - 1.0;
                double ry = 2.0 * y / size - 1.0;
                double pixelHeight = r2(rx, ry);

                // Calculate color depending on what layer we are in
                int color;
                if (     pixelHeight < calculateLayerHeight(rx, ry, 0.3, 0.2, 0.15))   color = mixColor(pixelHeight, 0.1, 0.4, colorCenterBot, colorCenterTop);
                else if (pixelHeight < calculateLayerHeight(rx, ry, 0.45, 0.1, 0.1))  color = mixColor(pixelHeight, 0.3, 0.5, colorCrustBot, colorCrustTop);
                else if (pixelHeight < calculateLayerHeight(rx, ry, 0.6, 0.03, 0.25)) color = mixColor(pixelHeight, 0.4, 0.63, colorGrassBot, colorGrassTop);
                else if (pixelHeight < 0.7)                                           color = mixColor(pixelHeight, 0.6, 0.7, colorAtmosphereBase, colorAtmosphereTop);
                else                                                                  color = mixColor(pixelHeight, 0.7, 1.0, colorAtmosphereTop, colorSpaceTop);

                rawImage.raster().data()[i] = color;

                i++;
            }
        }

        return new BufferedImagePicture(rawImage.createBufferedImage());
    }

    private double calculateLayerHeight(double rx, double ry, double baseHeight, double heightVariation, double detailSize) {
        return baseHeight + heightVariation * SimplexNoise$.MODULE$.noise(rx / detailSize, ry / detailSize);
    }

    private int mixColor(double pixelHeight, double baseH, double topH, int baseColor, int topColor) {
        // position between base and top height for this color band
        double relPos = (pixelHeight - baseH) / (topH - baseH);

        // Clamp to range 0..1
        relPos = relPos > 1.0 ? 1.0 : (relPos < 0.0 ? 0.0 : relPos);

        // Mix base and top colors of the color band
        return ColorUtils$.MODULE$.mixRGBA(relPos, topColor, baseColor);
    }

    private double theta(double x, double y) {
        return Math.atan2(y, x);
    }

    private double r2(double x, double y) {
        return x*x + y*y;
    }

}
