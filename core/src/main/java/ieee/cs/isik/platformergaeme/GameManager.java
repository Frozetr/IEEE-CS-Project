package ieee.cs.isik.platformergaeme;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapLoader;
import ieee.cs.isik.platformergaeme.screens.GameScreen;
import ieee.cs.isik.platformergaeme.screens.LoadingScreen;
import ieee.cs.isik.platformergaeme.screens.MenuScreen;
import org.jetbrains.annotations.NotNull;

import java.util.List;


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
    public static void show(@NotNull ScreenType type) {
        switch (type) {
            case GameType:
                GameScreen gameScreen = new GameScreen();
                AssetManager assets = gameScreen.getAssetManager();
                if(assets.getLoader(TiledMap.class) == null)
                    assets.setLoader(TiledMap.class, new TiledMapLoader());
                game.setScreen(new LoadingScreen<>(gameScreen, List.of(new AssetPair("testmap/map.tmx", TiledMap.class))));
                break;

            case MenuType:
            default:
                game.setScreen(new LoadingScreen<>(new MenuScreen(), null));
                break;
        }
    }

    public static Game getGame() {
        return game;
    }

    public final static float characterDrawHeightInMeters=2f;
    public final static float characterHeightInMeters=1.8f;
    private static float meter2PixelsRatio=0, characterHeightInPixels, characterDrawHeightInPixels;

    public static void setMeter2PixelsRatio(float r) {
        meter2PixelsRatio = r;
        characterHeightInPixels = characterHeightInMeters * r;
        characterDrawHeightInPixels = characterDrawHeightInMeters * r;
    }

    public static float getMeter2PixelsRatio() {
        return meter2PixelsRatio;
    }

    public static float getCharacterHeightInPixels() {
        return characterHeightInPixels;
    }
    public static float getCharacterDrawHeightInPixels() {
        return characterDrawHeightInPixels;
    }
}
