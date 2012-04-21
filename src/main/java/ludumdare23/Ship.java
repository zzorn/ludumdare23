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
        drawColoredOval(g, x, y, Color.BLACK, shipSize + 4);
        drawColoredOval(g, x, y, shipColor, shipSize);
    }

    private void drawColoredOval(Graphics2D g, int x, int y, Color color, int size) {
        g.setColor(color);
        g.fillOval(x - size /2, y - size /4, size, size /2);
    }
}
