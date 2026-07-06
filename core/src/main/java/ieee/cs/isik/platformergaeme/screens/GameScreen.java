package ieee.cs.isik.platformergaeme.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ieee.cs.isik.platformergaeme.AssetPair;
import ieee.cs.isik.platformergaeme.GameManager;
import ieee.cs.isik.platformergaeme.game.BulletEntity;
import ieee.cs.isik.platformergaeme.game.CarriableEntity;
import ieee.cs.isik.platformergaeme.game.CharacterEntity;
import ieee.cs.isik.platformergaeme.game.ConsumableEntity;
import ieee.cs.isik.platformergaeme.game.Entity;
import ieee.cs.isik.platformergaeme.game.MapManager;
import ieee.cs.isik.platformergaeme.game.Material;
import ieee.cs.isik.platformergaeme.game.Pack16Character;
import ieee.cs.isik.platformergaeme.game.StateMaterial;
import ieee.cs.isik.platformergaeme.game.WeaponEntity;
import ieee.cs.isik.platformergaeme.game.mapmanagers.TestMap;
import ieee.cs.isik.platformergaeme.game.weapons.RangedWeapon;
import ieee.cs.isik.platformergaeme.game.weapons.ThrowableWeapon;

public class GameScreen implements Screen, ieee.cs.isik.platformergaeme.IAssetfull {

    AssetManager assetManager = new AssetManager();
    TiledMap map;

    OrthographicCamera camera = new OrthographicCamera();
    {
        camera.zoom = 1.5f;
        camera.update();
    }

    MapManager mapManager;

    public final World physicsWorld = new World(new Vector2(0, -9.8f), true);

    Box2DDebugRenderer box2DDebugRenderer = new Box2DDebugRenderer();
    {
        box2DDebugRenderer.setDrawBodies(true);
        box2DDebugRenderer.setDrawJoints(true);
    }

    final LinkedList<Entity> entities = new LinkedList<>();
    SpriteBatch batch = new SpriteBatch();

    // --- Silah sistemi ---
    private CharacterEntity myChar;
    private final List<Entity> toRemove = new ArrayList<>();
    private static final float PICKUP_RANGE = 1.5f;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private CarriableEntity nearestItem;

    // --- Duzeltme 1: Hareket ---
    private boolean isOnGround = false;
    private static final float MOVE_SPEED   = 6f;
    private static final float JUMP_IMPULSE = 8f;

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        {
            map = assetManager.get("testmap/map.tmx", TiledMap.class);
            var prop = map.getProperties();
            float meters2PixelsRatio = prop.get("tileheight", Integer.class);
            GameManager.setMeter2PixelsRatio(meters2PixelsRatio);
            mapManager = new TestMap(map, camera);
        }

        for (com.badlogic.gdx.maps.tiled.TiledMapTileLayer layer :
                map.getLayers().getByType(com.badlogic.gdx.maps.tiled.TiledMapTileLayer.class)) {

            // Duzeltme 10: sadece collision=true katmanlari isle
            Boolean isCollision = layer.getProperties().get("collision", false, Boolean.class);
            if (!Boolean.TRUE.equals(isCollision)) continue;

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

        // Duzeltme 5: show() icerisindeki spawn blogu kaldirildi, render()'da kalacak

        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();

        // Duzeltme 1: zemin tespiti eklendi
        physicsWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                handleBulletContact(contact.getFixtureA(), contact.getFixtureB());
                handleBulletContact(contact.getFixtureB(), contact.getFixtureA());
                checkGroundContact(contact.getFixtureA(), contact.getFixtureB(), true);
                checkGroundContact(contact.getFixtureB(), contact.getFixtureA(), true);
            }
            @Override
            public void endContact(Contact contact) {
                checkGroundContact(contact.getFixtureA(), contact.getFixtureB(), false);
                checkGroundContact(contact.getFixtureB(), contact.getFixtureA(), false);
            }
            @Override public void preSolve(Contact contact, Manifold manifold) {}
            @Override public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    @Override
    public void render(float delta) {
        if (!assetManager.update()) {
            return;
        } else {
            // Duzeltme 5: tek spawn noktasi burasi
            if (entities.isEmpty()) {
                myChar = addMainChar();
                myChar.body.setTransform(10f, 10f, 0);
            }
        }

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float m2p = GameManager.getMeter2PixelsRatio();

        // --- Duzeltme 1: Hareket & Ziplama ---
        if (myChar != null) {
            float vx = 0f;
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))  vx = -MOVE_SPEED;
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) vx =  MOVE_SPEED;

            myChar.body.setLinearVelocity(vx, myChar.body.getLinearVelocity().y);

            if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
                    || Gdx.input.isKeyJustPressed(Input.Keys.W)
                    || Gdx.input.isKeyJustPressed(Input.Keys.UP)) && isOnGround) {
                myChar.body.applyLinearImpulse(0, JUMP_IMPULSE,
                        myChar.body.getWorldCenter().x, myChar.body.getWorldCenter().y, true);
                isOnGround = false;
            }

            // RangedWeapon yuz yonu
            if (myChar.weapon instanceof RangedWeapon) {
                ((RangedWeapon) myChar.weapon).facingRight = (vx >= 0f);
            }

            // Animasyon durumu: 0=idle, 1=yurume, 2=ziplama
            if (myChar.material instanceof StateMaterial) {
                StateMaterial sm = (StateMaterial) myChar.material;
                if (!isOnGround)   sm.state = 2;
                else if (vx != 0f) sm.state = 1;
                else               sm.state = 0;
            }
        }

        // --- NEAREST ITEM TARAMA ---
        nearestItem = findNearestCarriableItem();

        // --- E — Esya Al ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) && nearestItem != null) {
            if (nearestItem instanceof WeaponEntity) {
                if (myChar.weapon != null) {
                    myChar.weapon.onUnequip();
                    myChar.weapon.drop();
                }
                WeaponEntity w = (WeaponEntity) nearestItem;
                w.carrier = myChar;
                myChar.weapon = w;
                myChar.weapon.onEquip(myChar);
                // Duzeltme 4c: toRemove referansi ver
                if (myChar.weapon instanceof RangedWeapon) {
                    ((RangedWeapon) myChar.weapon).setToRemoveList(toRemove);
                }
            } else if (nearestItem instanceof ConsumableEntity) {
                if (myChar.consumable != null) myChar.consumable.drop();
                nearestItem.carrier = myChar;
                myChar.consumable = (ConsumableEntity) nearestItem;
            }
        }

        // --- Q — Birak / Firlat ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q) && myChar != null && myChar.weapon != null) {
            Vector3 mouseScreen = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 mouseWorld  = camera.unproject(mouseScreen);
            Vector2 throwDir = new Vector2(
                mouseWorld.x / m2p - myChar.body.getPosition().x,
                mouseWorld.y / m2p - myChar.body.getPosition().y
            ).nor();
            WeaponEntity dropped = myChar.weapon;
            dropped.onUnequip();
            dropped.drop();
            dropped.body.applyLinearImpulse(
                throwDir.scl(dropped.stats.throwForce),
                dropped.body.getWorldCenter(), true
            );
        }

        // --- Sol Tik — Kullan ---
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && myChar != null && myChar.weapon != null) {
            if (myChar.weapon instanceof ThrowableWeapon) {
                ThrowableWeapon tw = (ThrowableWeapon) myChar.weapon;
                Vector3 mw = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                Vector2 dir = new Vector2(
                    mw.x / m2p - myChar.body.getPosition().x,
                    mw.y / m2p - myChar.body.getPosition().y
                ).nor();
                tw.throwDirection = dir;
            }
            myChar.weapon.use();
            if (myChar.weapon instanceof RangedWeapon) {
                RangedWeapon rw = (RangedWeapon) myChar.weapon;
                entities.addAll(rw.entityBuffer);
                rw.entityBuffer.clear();
            }
        }

        // --- Sag Tik — Consumable Kullan ---
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && myChar != null && myChar.consumable != null) {
            myChar.consumable.use();
        }

        // --- R — Sarjor ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && myChar != null && myChar.weapon instanceof RangedWeapon) {
            ((RangedWeapon) myChar.weapon).reload(2.0f);
        }

        // --- Bullet guncelle ---
        for (Entity e : entities) {
            if (e instanceof BulletEntity) ((BulletEntity) e).update(delta);
        }

        // --- Throwable fuse ---
        for (Entity e : entities) {
            if (e instanceof ThrowableWeapon) ((ThrowableWeapon) e).act(delta);
        }

        // --- Reload timer ---
        if (myChar != null && myChar.weapon instanceof RangedWeapon) {
            ((RangedWeapon) myChar.weapon).act(delta);
        }

        // --- Duzeltme 4d: kusanilan silahi karaktere sabitle ---
        // onEquip() silahin body'sini pasif (setActive(false)) hale getiriyor,
        // bu yuzden yer cekiminden etkilenmiyor; burada da her karede
        // karakterin pozisyonuna tasiyoruz ki gorsel olarak da onu takip etsin.
        if (myChar != null && myChar.weapon != null) {
            Vector2 charPos = myChar.body.getPosition();
            myChar.weapon.body.setTransform(charPos.x, charPos.y, 0f);
        }

        // --- Duzeltme 6: Health kontrolu ---
        for (Entity e : entities) {
            if (e.maxHealth > 0 && e.health <= 0 && !toRemove.contains(e)) {
                if (e == myChar) {
                    handlePlayerDeath();
                    return;
                }
                toRemove.add(e);
            }
        }

        // --- Physics Step ---
        physicsWorld.step(delta, 6, 2);

        // --- Olu entity'leri temizle (step'ten SONRA) ---
        for (Entity dead : toRemove) {
            entities.remove(dead);
            dead.dispose();
        }
        toRemove.clear();

        // --- Duzeltme 2: Kamera takibi ---
        if (myChar != null) {
            Vector2 charPos = myChar.body.getPosition();
            camera.position.set(charPos.x * m2p, charPos.y * m2p, 0);
            camera.update();
        }

        // --- Render ---
        mapManager.render(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity entity : entities) {
            if (entity.material == null) continue;
            Material mat = entity.material;
            mat.act(delta);
            final Vector2 pos = entity.body.getPosition();
            TextureRegion texture = mat.getFrame();
            batch.draw(texture,
                pos.x * m2p, pos.y * m2p,
                texture.getRegionWidth() * 2,
                texture.getRegionHeight() * 2);
        }
        drawHUD();
        batch.end();

        drawCooldownBar();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (font != null) font.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        physicsWorld.dispose();
        for (Entity e : entities) e.dispose();
        mapManager.dispose();
        assetManager.dispose();
        box2DDebugRenderer.dispose();
    }

    public CharacterEntity addMainChar() {
        CharacterEntity c = Pack16Character.C_PUNPKIN.loadEntity(assetManager, 0, physicsWorld);
        // Duzeltme 11: baslangic durumu idle (0)
        ((StateMaterial) c.material).state = 0;
        entities.add(c);
        return c;
    }

    private static final List<AssetPair> assetsList;
    static {
        LinkedList<AssetPair> list = new LinkedList<>();
        list.push(new AssetPair("16_Character_Pack/PunpKin.png", Texture.class));
        assetsList = Collections.unmodifiableList(list);
    }

    public List<AssetPair> getAssets() { return assetsList; }
    public AssetManager getAssetManager() { return assetManager; }

    // -----------------------------------------------------------------------
    // Yardimci metodlar
    // -----------------------------------------------------------------------

    /** Duzeltme 1: zemin tespiti */
    private void checkGroundContact(Fixture charFixture, Fixture wallFixture, boolean touching) {
        if (myChar == null) return;
        Object charUD = charFixture.getBody().getUserData();
        Object wallUD = wallFixture.getUserData();
        if (charUD instanceof CharacterEntity && "wall".equals(wallUD)) {
            isOnGround = touching;
        }
    }

    /** Duzeltme 6: oyuncu oldu */
    private void handlePlayerDeath() {
        myChar = null;
        GameManager.show(GameManager.ScreenType.MenuType);
    }

    private CarriableEntity findNearestCarriableItem() {
        if (myChar == null) return null;
        CarriableEntity nearest = null;
        float bestDist = Float.MAX_VALUE;
        Vector2 playerPos = myChar.body.getPosition();
        for (Entity e : entities) {
            if (!(e instanceof CarriableEntity)) continue;
            CarriableEntity ce = (CarriableEntity) e;
            if (ce.carrier != null) continue;
            float dist = ce.body.getPosition().dst(playerPos);
            if (dist < bestDist) { bestDist = dist; nearest = ce; }
        }
        return (nearest != null && bestDist <= PICKUP_RANGE) ? nearest : null;
    }

    private void drawHUD() {
        int sw = Gdx.graphics.getWidth();
        if (myChar != null && myChar.weapon != null) {
            WeaponEntity w = myChar.weapon;
            String ammoText = (w.stats.ammo == -1) ? "inf" : w.currentAmmo + "/" + w.stats.ammo;
            String label = w.name + "  " + ammoText;
            if (w instanceof RangedWeapon && ((RangedWeapon) w).reloading) label += "  [Sarj...]";
            font.draw(batch, label, sw - 180f, 40f);
        }
        if (nearestItem != null) {
            font.draw(batch, "[E] Al: " + nearestItem.name, sw / 2f - 60f, 40f);
        }
    }

    /** Duzeltme 8: ShapeRenderer ekran koordinatlarinda cizilir */
    private void drawCooldownBar() {
        if (myChar == null || myChar.weapon == null) return;
        WeaponEntity w = myChar.weapon;
        if (w.stats.coolDown <= 0f) return;
        float now = com.badlogic.gdx.utils.TimeUtils.millis() / 1000f;
        float ratio = Math.min((now - w.lastUsedTime) / w.stats.coolDown, 1f);
        float barX = Gdx.graphics.getWidth() - 180f;

        shapeRenderer.setProjectionMatrix(
            new com.badlogic.gdx.math.Matrix4().setToOrtho2D(
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
            )
        );
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);
        shapeRenderer.rect(barX, 50f, 100f, 6f);
        shapeRenderer.setColor(1f - ratio, ratio, 0f, 1f);
        shapeRenderer.rect(barX, 50f, ratio * 100f, 6f);
        shapeRenderer.end();
    }

    private void handleBulletContact(Fixture fixtureA, Fixture fixtureB) {
        Object udA = fixtureA.getBody().getUserData();
        Object udB = fixtureB.getBody().getUserData();
        if (!(udA instanceof BulletEntity)) return;
        BulletEntity bullet = (BulletEntity) udA;
        if (bullet.destroyed) return;
        if (!(udB instanceof CharacterEntity)) return;
        CharacterEntity target = (CharacterEntity) udB;
        if (target == bullet.owner) return;
        target.health -= bullet.damage;
        bullet.destroy();
    }

    public void addWeaponToWorld(WeaponEntity weapon, float x, float y) {
        weapon.body.setTransform(x, y, 0f);
        weapon.body.setUserData(weapon);
        entities.add(weapon);
    }
}