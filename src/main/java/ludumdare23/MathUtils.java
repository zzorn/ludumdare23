package ludumdare23;

import net.zzorn.utils.ColorUtils$;
import net.zzorn.utils.Vec3;

import java.awt.*;
import java.util.Random;

/**
 *
 */
public class MathUtils {

    public static double mix(double t, double min, double max) {
        return min * (1.0-t) + max * t;
    }

    public static double mixWithVariation(Random random, double t, double variation, double start, double end, double absoluteMin, double absoluteMax) {
        double variated = t + random.nextGaussian() * variation;
        return clamp(mix(variated, start, end), absoluteMin, absoluteMax);
    }

    public static double map(double t, double sourceMin, double sourceMax, double destMin, double destMax) {
        if (sourceMax == sourceMin) return 0.5 * (destMax + destMin);
        else {
            double rel = (t - sourceMin) / (sourceMax - sourceMin);
            return destMin * (1.0-rel) + destMax * rel;
        }
    }

    public static double clampZeroToOne(double v) {
        if (v > 1) return 1;
        else if (v < 0) return 0;
        else return v;
    }

    public static double clampMinusOneToOne(double v) {
        if (v > 1) return 1;
        else if (v < -1) return -1;
        else return v;
    }

    public static double clamp(double v, double min, double max) {
        if (v > max) return max;
        else if (v < min) return min;
        else return v;
    }

    public static double randomValue(Random random, double start, double end) {
        return random.nextDouble() * (end - start) + start;
    }

    public static double gaussianValue(Random random, double average, double deviation, double min, double max) {
        return clamp(random.nextGaussian() * average * deviation + average, min, max);
    }

    public static Color createRandomColor(Random random, double hue, double hueVar, double sat, double satVar, double lum, double lumVar) {
        double h = random.nextGaussian() * hueVar + hue;
        double s = clampZeroToOne(random.nextGaussian() * satVar + sat);
        double l = clampZeroToOne(random.nextGaussian() * lumVar + lum);

        return new Color(ColorUtils$.MODULE$.HSLtoRGB(h, s, l, 1), false);
    }

    public static Vec3 createRandomVec(Random random, double area) {
        Vec3 randomPos = Vec3.random(random.nextLong());
        randomPos.setMul(area);
        randomPos.setZ(0);
        return randomPos;
    }
}
