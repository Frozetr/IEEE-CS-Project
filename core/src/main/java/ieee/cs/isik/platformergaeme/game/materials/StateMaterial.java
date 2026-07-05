package ieee.cs.isik.platformergaeme.game.materials;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * This material presents frames according to state number.
 * Multiple materials can belong to more than one state.
 */
public class StateMaterial extends Material {

    public final ArrayList<Material> materials = new ArrayList<>();
    public final HashMap<Integer, Integer> stateIndexes = new HashMap<>();
    public int state = 0;

    @Override
    public void act(float deltatime) {
        materials.get(stateIndexes.get(state)).act(deltatime);
    }

    @Override
    public TextureRegion getFrame() {
        return materials.get(stateIndexes.get(state)).getFrame();
    }

    @Override
    public Vector2 getDrawSize() {
        return materials.get(stateIndexes.get(state)).getDrawSize();
    }
}
