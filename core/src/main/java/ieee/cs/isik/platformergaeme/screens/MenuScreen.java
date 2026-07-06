package ieee.cs.isik.platformergaeme.screens;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;

import ieee.cs.isik.platformergaeme.AssetPair;
import ieee.cs.isik.platformergaeme.GameManager;

public class MenuScreen implements Screen, ieee.cs.isik.platformergaeme.IAssetfull {
    private Stage stage = new Stage(new FillViewport(16 * 40,9*40));
    public final AssetManager assets = new AssetManager();


    private boolean isStageBuild = false;
    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     * Initializes the menu UI and input when this screen is shown.
     */
    @Override
    public void show() {
        // Set default background color
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Set current input processor to the stage
        Gdx.input.setInputProcessor(stage);

        if(!isStageBuild) {
            buildStage();
            isStageBuild = true;
        }
    }

    private void buildStage() {
        TextureRegion mainButtonTextureUp = new TextureRegion(assets.get("UI/Buttons.png", Texture.class), 3, 19 * 5 + 1, 9 * 5 - 3, 3 * 5 + 1);
        TextureRegion mainButtonTextureDown = new TextureRegion(assets.get("UI/Buttons.png", Texture.class), 10 * 5 + 0, 19 * 5 + 1, 9 * 5 - 3, 3 * 5 + 1);
        TextureRegion mainButtonTextureHover = new TextureRegion(assets.get("UI/Buttons.png", Texture.class), 3, 22 * 5 + 2, 9 * 5 - 3, 3 * 5 + 1);

        TextButton.TextButtonStyle mainButtonStyle = new TextButton.TextButtonStyle();

        mainButtonStyle.up = new TextureRegionDrawable(mainButtonTextureUp);
        mainButtonStyle.down = new TextureRegionDrawable(mainButtonTextureDown);
        mainButtonStyle.over = new TextureRegionDrawable(mainButtonTextureHover);
        mainButtonStyle.font = new BitmapFont();

        TextButton singlePlayerButton = new TextButton("Single Player", mainButtonStyle);
        TextButton multiPlayerButton = new TextButton("Multi Player", mainButtonStyle);

        multiPlayerButton.setPosition(0, 9 * 20  - 16 * 4);
        multiPlayerButton.setHeight(16  * 3);
        multiPlayerButton.setWidth(42 * 3);

        singlePlayerButton.setPosition(0, 9 * 20);
        singlePlayerButton.setHeight(16  * 3);
        singlePlayerButton.setWidth(42 * 3);

        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameManager.show(GameManager.ScreenType.GameType);
            }
        });

        multiPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameManager.show(GameManager.ScreenType.GameType);
            }
        });

        stage.addActor(singlePlayerButton);
        stage.addActor(multiPlayerButton);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw GUI
        stage.act(delta);
        stage.draw();
    }

    /** Called when screen resized or when {@link Game#setScreen(Screen)} get called
     *
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        assets.dispose();
    }

    private static final List<AssetPair> assetsList;
    static {
        LinkedList<AssetPair> list = new LinkedList<>();
        list.push(new AssetPair("UI/Buttons.png", Texture.class));
        assetsList = Collections.unmodifiableList(list);
    }
    public List<AssetPair> getAssets() {
        return assetsList;
    }

    public AssetManager getAssetManager() {
        return assets;
    }
}
