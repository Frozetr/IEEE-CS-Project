package ieee.cs.isik.platformergaeme.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import ieee.cs.isik.platformergaeme.AssetPair;
import ieee.cs.isik.platformergaeme.stages.MenuStage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MenuScreen implements Screen, ieee.cs.isik.platformergaeme.IAssetfull {
    private Stage stage;
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
            stage = new MenuStage(new FillViewport(16 * 40f,9*40f), assets);
            isStageBuild = true;
        }
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        /*
         * Clear previous frame
         * This will paint entire screen to the default color that we decided in show() with Gdx.gl20.glClearColor function
         */
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
