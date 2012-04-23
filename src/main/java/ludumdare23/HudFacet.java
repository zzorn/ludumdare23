package ludumdare23;

import net.zzorn.gameflow.BaseFacet;
import net.zzorn.gameflow.camera.Camera;

import java.awt.*;

/**
 *
 */
public class HudFacet extends BaseFacet {

    private final Game game;

    private boolean visible = false;
    private PlayerShip player = null;
    private Planet planet = null;

    private HealthBar playerHpBar = null;
    private HealthBar planetHpBar = null;

    private final int margin = 10;

    public HudFacet(Game game) {
        this.game = game;
    }

    @Override
    public void init() {
        playerHpBar = new HealthBar(game.pictureStore().getImage("images/playerhealth.png"), 0.5);
        planetHpBar = new HealthBar(game.pictureStore().getImage("images/earthhealth.png"), 0.5);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPlayer(PlayerShip player) {
        this.player = player;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    @Override
    public void update(double durationSeconds) {
    }

    @Override
    public void render(Graphics2D g, Camera camera) {
        if (visible) {
            if (player != null) {
                int w = playerHpBar.getWidth();
                int h = playerHpBar.getHeight();
                playerHpBar.draw(g, margin, camera.screenH() - margin - h, player.getHitPointsPercent());
            }

            if (planet != null) {
                int w = planetHpBar.getWidth();
                int h = planetHpBar.getHeight();
                planetHpBar.draw(g, camera.screenW() - margin - w, camera.screenH() - margin - h, planet.getHitPointsPercent());
            }
        }
    }
}
