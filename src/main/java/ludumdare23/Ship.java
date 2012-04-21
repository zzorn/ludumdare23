package ludumdare23;

import net.zzorn.gameflow.entity.Entity3D;

import java.awt.*;

/**
 * Something that can drive around on its own and fire weapons.
 */
public class Ship extends Entity3D {

    private int shipSize = 40;
    private Color shipColor = Color.ORANGE;

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y) {
        g.setColor(shipColor);
        g.fillOval(x - shipSize/2, y - shipSize/4, shipSize, shipSize/2);
    }
}
