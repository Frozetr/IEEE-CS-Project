package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import ieee.cs.isik.platformergaeme.GameManager;
import ieee.cs.isik.platformergaeme.game.entity.CharacterEntity;import ieee.cs.isik.platformergaeme.game.materials.AnimationMaterial;import ieee.cs.isik.platformergaeme.game.materials.Material;import ieee.cs.isik.platformergaeme.game.materials.StateMaterial;import ieee.cs.isik.platformergaeme.game.materials.TextureMaterial;import org.jetbrains.annotations.NotNull;

public enum Pack16Character implements Character {
    C_12("12"),
    C_ALIEN("Alien"),
    C_BANDITO("Bandito"),
    C_BEETLEJUIX("BeetleJuix"),
    C_DRACULA("Dracula"),
    C_FREDDY_K("Freddy K"),
    C_GANDAL("Gandal"),
    C_HARLEY_QUEEN("Harley Queen"),
    C_JODA("Joda"),
    C_JOKE("Joke"),
    C_MARTY_MC("Marty Mc"),
    C_PUNPKIN("PunpKin"),
    C_SICARIO("Sicario"),
    C_SUORA("Suora"),
    C_TERMİNX("Terminx"),
    C_TREEPWOO("TreepWoo")
    ;

    final String skin_name;

    Pack16Character(@NotNull String skin_name) {
        this.skin_name = skin_name;
    }

    @Override
    public CharacterEntity loadEntity(final AssetManager assets, final int id, @NotNull World physicsWorld) {

        Material mat = getMaterial(assets);

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        Body body = physicsWorld.createBody(def);

        PolygonShape circle = new PolygonShape();

        float hHeight = GameManager.getCharacterHeightInPixels() / GameManager.getMeter2PixelsRatio() / 2;
        float hWidth = hHeight * 3 / 4 * 0.75f; // Multiply with 0.75f for game experience
        circle.setAsBox(hWidth, hHeight);

        double area = hWidth * hHeight * 4;
        float density = (float)(MASS / area);

        Fixture Fix = body.createFixture(circle, density);
        CharacterEntity characterEntity = new CharacterEntity(id, 0, "Character", 100f, 100f, body, mat, null, null);
        Fix.setUserData(new EntityFixtureData(
            characterEntity,
            FixtureData.Type.SOLID
        ));

        return characterEntity;
    }

    private Material getMaterial(final AssetManager assets) {
        Texture atlas = assets.get(String.format("16_Character_Pack/%1$s.png", skin_name));
        int frameWidth = atlas.getWidth() / 4;
        int frameHeight = atlas.getHeight() / 2;
        TextureRegion[][] frames = new TextureRegion(atlas).split(frameWidth, frameHeight);

        TextureRegion[] idleFrames = frames[0];
        TextureRegion[] walkFrames = frames[1];
        TextureRegion jumpFrame = frames[0][1];

        Animation<TextureRegion> idleAnimation = new Animation<>(0.1f, idleFrames);
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        AnimationMaterial idleMat = new AnimationMaterial(idleAnimation);

        Animation<TextureRegion> walkAnimation = new Animation<>(0.1f, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        AnimationMaterial walkMat = new AnimationMaterial(walkAnimation);

        TextureMaterial jumpMat = new TextureMaterial(jumpFrame);

        StateMaterial stateMaterial = new StateMaterial();
        stateMaterial.materials.add(idleMat);
        stateMaterial.materials.add(walkMat);
        stateMaterial.materials.add(jumpMat);

        for(int i=0; i < 3; i++)
            stateMaterial.stateIndexes.put(i, i);

        return stateMaterial;
    }
}
