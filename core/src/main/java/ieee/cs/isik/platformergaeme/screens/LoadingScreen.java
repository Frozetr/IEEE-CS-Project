package ieee.cs.isik.platformergaeme.screens;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ieee.cs.isik.platformergaeme.AssetPair;

public class LoadingScreen<T extends Screen & ieee.cs.isik.platformergaeme.IAssetfull> implements Screen {

    @NotNull
    T targetScreen;

    BitmapFont font = new BitmapFont();
    {
        font.getData().setScale(2);
    }

    OrthographicCamera cam = new OrthographicCamera();
    SpriteBatch batch = new SpriteBatch();


    public LoadingScreen(@NotNull T targetScreen, List<AssetPair> extraAssets) {
        this.targetScreen = targetScreen;
        LinkedList<AssetPair> completeAssets = new LinkedList<>();
        completeAssets.addAll(targetScreen.getAssets());
        if(extraAssets != null)
            completeAssets.addAll(extraAssets);

        var assetManager = targetScreen.getAssetManager();
        for(AssetPair asset: completeAssets)
            if(!assetManager.isLoaded(asset.assetPath, asset.assetClass))
                assetManager.load(asset.assetPath, asset.assetClass);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.gl20.glClearColor(0f, 0f, 0f, 0f);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */

    static int screenCount = 0;
    {
        screenCount++;
    }
    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        boolean loadStatus = targetScreen.getAssetManager().update(10);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        font.draw(batch, String.format(Locale.ENGLISH, "Loading: %.2f%%", targetScreen.getAssetManager().getProgress() * 100), 100, 150, 500, 1, false);
        batch.end();

        if(loadStatus) {
            ieee.cs.isik.platformergaeme.GameManager.getGame().setScreen(targetScreen);
            return;
        }
    }

    /**
     * @param width
     * @param height
     * @see Screen#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        cam.setToOrtho(false, width, height);
    }

    /**
     * @see Screen#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see Screen#resume()
     */
    @Override
    public void resume() {

    }

    
    @Override
    public void hide() {

    }

    
    @Override
    public void dispose() {

    }
}
