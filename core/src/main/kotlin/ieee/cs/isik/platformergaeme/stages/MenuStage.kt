package ieee.cs.isik.platformergaeme.stages

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ieee.cs.isik.platformergaeme.GameManager

class MenuStage : Stage {
    constructor(
        viewport: com.badlogic.gdx.utils.viewport.Viewport,
        assets: AssetManager
    ) : super(viewport) {


        val mainButtonTextureUp: TextureRegion =
            TextureRegion(assets.get("UI/Buttons.png", Texture::class.java), 3, 19 * 5 + 1, 9 * 5 - 3, 3 * 5 + 1)
        val mainButtonTextureDown: TextureRegion = TextureRegion(
            assets.get("UI/Buttons.png", Texture::class.java),
            10 * 5 + 0,
            19 * 5 + 1,
            9 * 5 - 3,
            3 * 5 + 1
        )
        val mainButtonTextureHover: TextureRegion =
            TextureRegion(assets.get("UI/Buttons.png", Texture::class.java), 3, 22 * 5 + 2, 9 * 5 - 3, 3 * 5 + 1)

        val mainButtonStyle: TextButton.TextButtonStyle = TextButton.TextButtonStyle()

        mainButtonStyle.up = TextureRegionDrawable(mainButtonTextureUp)
        mainButtonStyle.down = TextureRegionDrawable(mainButtonTextureDown)
        mainButtonStyle.over = TextureRegionDrawable(mainButtonTextureHover)
        mainButtonStyle.font = BitmapFont()

        val singlePlayerButton: TextButton = TextButton("Single Player", mainButtonStyle)
        val multiPlayerButton: TextButton = TextButton("Multi Player", mainButtonStyle)

        multiPlayerButton.setPosition(0f, 9 * 20 - 16 * 4f)
        multiPlayerButton.setHeight(16 * 3f)
        multiPlayerButton.setWidth(42 * 3f)

        singlePlayerButton.setPosition(0f, 9 * 20f)
        singlePlayerButton.setHeight(16 * 3f)
        singlePlayerButton.setWidth(42 * 3f)

        singlePlayerButton.addListener(object : ClickListener() {
            public override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                GameManager.show(GameManager.ScreenType.GameType)
            }
        })

        multiPlayerButton.addListener(object : ClickListener() {
            public override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                GameManager.show(GameManager.ScreenType.GameType)
            }
        })

        addActor(singlePlayerButton)
        addActor(multiPlayerButton)
    }


}
