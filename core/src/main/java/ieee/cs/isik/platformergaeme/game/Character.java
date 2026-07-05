package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.jetbrains.annotations.NotNull;

public interface Character {
    /**
     * Box size in pixels
     */
    public static final Vector2 boxSize = new Vector2(100, 100);
    public final static float MASS = 80;
    public final static float MOVE_SPEED = 5;

    public CharacterEntity loadEntity(final AssetManager assets, final int id, @NotNull World physicsWorld);
}
