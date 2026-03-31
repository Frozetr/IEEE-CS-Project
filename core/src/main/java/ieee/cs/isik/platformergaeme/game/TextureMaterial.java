package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureMaterial extends Material {

    /// The texture that going to be drawn
    public TextureRegion texture;

    public TextureMaterial(TextureRegion texture) {
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
