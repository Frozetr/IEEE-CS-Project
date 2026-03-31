package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * This class holds multiple Texture's via {@link Animation}.class
 * Provides different Textures according to their timestamp.
 *
 * @see Material
 * @see Animation
 */
public class AnimationMaterial extends Material {

    private final Animation<TextureRegion> animation;

    /// This is the total time this animation been played
    private float stateTime;

    public AnimationMaterial(Animation<TextureRegion> animation) {
        this.animation = animation;
        this.stateTime = 0f;
    }

    @Override
    public void act(float deltatime) {
        stateTime += deltatime;
    }

    @Override
    public TextureRegion getFrame() {
        return animation.getKeyFrame(stateTime);
    }

}
