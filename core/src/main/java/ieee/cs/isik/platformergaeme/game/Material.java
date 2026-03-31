package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;


/**
 * This class holds basic render data such as color filter and the texture that going to be rendered.
 * This is an abstract class, go find complete data in {@link TextureMaterial} and {@link AnimationMaterial}
 *
 * @see Color
 * @see TextureRegion
 */
public abstract class Material {

    /// This is extra color filter/effect, since this is white it has no effects
    public Color color = new Color(Color.WHITE);

    /**
     * Update since according to the time elapsed
     *
     * @param deltatime Represents the time elapsed since render of last frame
     */
    public abstract void act(float deltatime);

    /// Get the texture in question
    public abstract TextureRegion getFrame();
}
