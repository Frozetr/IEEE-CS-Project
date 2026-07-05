package ieee.cs.isik.platformergaeme.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import ieee.cs.isik.platformergaeme.AssetPair;
import ieee.cs.isik.platformergaeme.GameManager;
import ieee.cs.isik.platformergaeme.IAssetfull;
import ieee.cs.isik.platformergaeme.game.*;
import ieee.cs.isik.platformergaeme.game.Character;
import ieee.cs.isik.platformergaeme.game.entity.CharacterEntity;import ieee.cs.isik.platformergaeme.game.entity.Entity;import ieee.cs.isik.platformergaeme.game.mapmanagers.TestMap;
import ieee.cs.isik.platformergaeme.game.materials.Material;import ieee.cs.isik.platformergaeme.game.materials.StateMaterial;import ieee.cs.isik.platformergaeme.stages.GameStage;
import ieee.cs.isik.platformergaeme.util.*;
import kotlin.Unit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameScreen implements Screen, IAssetfull {

    CharacterEntity mainCharacter;
    private final InputProcessorGroup inputProcessor = new InputProcessorGroup(
        new CharacterSkillsBufferedInputAdapter(input -> {
            switch (input.getType()) {
                case JUMP -> {
                    mainCharacter.body.applyLinearImpulse(
                        new Vector2(0f, Character.MASS * 4.43f * 2),
                        mainCharacter.body.getWorldCenter(),
                        true
                    );
                }
                case ATTACK, CONSUME -> {
                    throw new RuntimeException("Not implemented yet");
                }
            }

            return Unit.INSTANCE;
        }),
        new CharacterMovementInputAdapter(
            (moveSign, lookingLeft) -> {
                mainCharacter.lookingLeft = lookingLeft;
                Vector2 currentSpeed = mainCharacter.body.getLinearVelocity();
                currentSpeed.x = AcceleratorKt.accelerate(
                    currentSpeed.x,
                    Character.MOVE_SPEED,
                    moveSign,
                    Gdx.graphics.getDeltaTime(),
                    15f
                );

                mainCharacter.body.setLinearVelocity(
                    currentSpeed
                );


                return Unit.INSTANCE;
            }
        )
    );

    private boolean isStageBuild = false;

    AssetManager assetManager = new AssetManager();
    TiledMap map;


    OrthographicCamera camera = new OrthographicCamera();
    {
        camera.zoom = 1.5f;
        camera.update();
    }


    MapManager mapManager;



    /** This {@link World} object is part of the physics engine 'box2d'
     * @see World
     */
    public final World physicsWorld = new World(
        new Vector2(0, -9.8f), // Default gravity of the World, 9.8 m / s^2 to the down
        true // Allow sleep state, this will ignore in active bodies which is going to improve  game performance
    );


    Box2DDebugRenderer box2DDebugRenderer = new Box2DDebugRenderer();
    {
        box2DDebugRenderer.setDrawBodies(true);
        box2DDebugRenderer.setDrawJoints(true);
    }


    final LinkedList<Entity> entities = new LinkedList<Entity>();

    SpriteBatch batch = new SpriteBatch();

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        // Set default background color
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        {
            map = assetManager.get("testmap/map.tmx", TiledMap.class);
            var prop = map.getProperties();
            float meters2PixelsRatio = prop.get("tileheight", Integer.class);
            GameManager.setMeter2PixelsRatio(meters2PixelsRatio);

            mapManager = new TestMap(map, camera);
        }

        for (TiledMapTileLayer layer : map.getLayers().getByType(TiledMapTileLayer.class)) {

            for (int col = 0; col < layer.getWidth(); col++) {
                for (int row = 0; row < layer.getHeight(); row++) {
                    var cell = layer.getCell(col, row);

                    if (cell != null && cell.getTile() != null) {

                        float x = col + 0.5f;
                        float y = row + 0.5f;

                        BodyDef bodyDef = new BodyDef();
                        bodyDef.type = BodyDef.BodyType.StaticBody;
                        bodyDef.position.set(x, y);
                        bodyDef.fixedRotation = true;

                        Body body = physicsWorld.createBody(bodyDef);

                        PolygonShape shape = new PolygonShape();
                        shape.setAsBox(0.5f, 0.5f);

                        Fixture fix = body.createFixture(shape, 1f);
                        fix.setUserData("wall");
                        shape.dispose();
                    }
                }
            }
        }

        if(entities.isEmpty()) {
            mainCharacter = addMainChar();
            mainCharacter.body.setTransform(10f, 10f, 0);
        }

        if(!isStageBuild) {
            Stage stage = new GameStage(new FillViewport(16 * 40f, 9 * 40f), assetManager);
            inputProcessor.addFirst(stage); // Make sure the stage is the first invoked input processor, this will prevent bugs like; pressing home button but ESC key is overridden by another input processor
            isStageBuild = true;
        }

        Gdx.input.setInputProcessor(inputProcessor);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render/frame.
     */
    @Override
    public void render(float delta) {

        /*
         * Clear previous frame
         * This will paint entire screen to the default color that we decided in show() with Gdx.gl20.glClearColor function
         */
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Iterate the physics world according to delta time
        physicsWorld.step(
            delta,
            6, // Since the game not going to have high speed entities this much of velocity iteration is enough
            2 // If entities gets conflict so much we must increase position iterations.
        );

        for (InputProcessor processor : inputProcessor) {
            if(processor instanceof ActableInputAdapter)
                ((ActableInputAdapter)processor).act();
        }

        mapManager.render(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Entity entity: entities) {
            Material mat = entity.material;
            mat.act(delta);
            final Vector2 pos = entity.body.getPosition();
            TextureRegion texture = mat.getFrame();

            float whRatio = (float)texture.getRegionWidth()  / texture.getRegionHeight();

            float height = GameManager.getCharacterHeightInPixels();
            float width = height * whRatio;

            float halfW = width / 2,
                halfH = height / 2;

            float direction = entity.lookingLeft ? -1 : 1;
            batch.draw (
                mat.getFrame(),

                pos.x * GameManager.getMeter2PixelsRatio() - halfW,
                pos.y * GameManager.getMeter2PixelsRatio() - halfH,
                halfW,
                halfH,
                width,
                height,
                direction,
                1,
                90 * direction,
                true
            );
        }
        batch.end();

        Matrix4 debugMatrix = new Matrix4(camera.combined);
        debugMatrix.scale(GameManager.getMeter2PixelsRatio(), GameManager.getMeter2PixelsRatio(), 1f);
        box2DDebugRenderer.render(physicsWorld, debugMatrix);
    }

    /** Called when screen resized or when {@link Game#setScreen(Screen)} get called
     *
     * @param width is Width of the screen in pixels
     * @param height is Height of the screen in pixels
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    /** Called when application/screen paused
     *
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /** Called when application/screen resumed
     *
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        physicsWorld.dispose();
        for(Entity e: entities)
            e.dispose();
        mapManager.dispose();
        physicsWorld.dispose();
        assetManager.dispose();
        box2DDebugRenderer.dispose();
    }

    public CharacterEntity addMainChar() {
        CharacterEntity myChar = Pack16Character.C_PUNPKIN.loadEntity(
            assetManager,
            0,
            physicsWorld
        );

        ((StateMaterial)myChar.material).state = 1;
        entities.add(myChar);

        return myChar;
    }


    private static final List<AssetPair> assetsList;
    static {
        LinkedList<AssetPair> list = new LinkedList<>();
        list.push(new AssetPair("16_Character_Pack/PunpKin.png", Texture.class));
        assetsList = Collections.unmodifiableList(list);

    }
    public List<AssetPair> getAssets() {
        return assetsList;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
