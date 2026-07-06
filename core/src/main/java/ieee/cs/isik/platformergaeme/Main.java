package ieee.cs.isik.platformergaeme;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;


/**
 * When Game get created, {@link Main#create()} function gets called. In this function we define {@link GameManager#game} to current {@link Main}
 *
 * This project use OpenGL 2.0
 * @see Game
 * @see GameManager
 **/
public class Main extends Game {
    @Override
    public void create() {
        
        GameManager.game = this;
        
        Gdx.graphics.setTitle("IEEE CS Ekibi Mario");

        Gdx.graphics.setFullscreenMode(
            Gdx.graphics.getDisplayMode(
                Gdx.graphics.getMonitor()
            )
        );

        GameManager.show(GameManager.ScreenType.MenuType);
    }
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        // Force window to fullscreen
        Gdx.graphics.setFullscreenMode(
            Gdx.graphics.getDisplayMode(
                Gdx.graphics.getMonitor()
            )
        );
    }}
