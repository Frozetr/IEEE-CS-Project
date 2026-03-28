package ieee.cs.isik.platformergaeme;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import ieee.cs.isik.platformergaeme.screens.GameScreen;
import ieee.cs.isik.platformergaeme.screens.MenuScreen;

import java.awt.*;


/**
 * GameManager.class manages Screens and transition between them.
 */
public class GameManager {


    /**
     * This enumeration provides scalability and modularity among the screens, reduces the mount of time we create new Screen object
     */
    public enum ScreenType {
        MenuType,
        GameType
    }

    /**
     * It's a global and constant variable,
     * Whole game must be controlled by this unique 'Game' object.
     *
     * @see Game
     */
    protected static Game game;

    /**
     * Set the Screen of the Game
     *
     * @param type is The Screen Type that going to be shown
     *
     * @see Game#setScreen(Screen)
     */
    public static void show(ScreenType type) {
        switch (type) {
            case MenuType:
                game.setScreen(new MenuScreen());
                break;
            case GameType:
                game.setScreen(new GameScreen());
                break;
        }
    }
}
