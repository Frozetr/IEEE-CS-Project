package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.jetbrains.annotations.NotNull;

public class TextureMaterial extends Material {

    /// The texture that going to be drawn
    @NotNull
    public TextureRegion texture;

    public TextureMaterial(@NotNull TextureRegion texture) {
        this.texture = texture;
    }

    @Override
    public void act(float deltatime) {
        // Nothing to do since Texture is not animated
    }

    @Override
    public TextureRegion getFrame() {
        return texture;
    }
}
