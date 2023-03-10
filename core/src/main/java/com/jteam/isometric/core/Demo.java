package com.jteam.isometric.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jteam.isometric.core.animation.Animation;
import com.jteam.isometric.core.animation.AnimationController;
import com.jteam.isometric.core.animation.AnimationLoader;
import com.jteam.isometric.core.camera.OrthoCameraController;
import com.jteam.isometric.core.creature.Creature;
import com.jteam.isometric.core.creature.CreatureRenderer;
import com.jteam.isometric.core.debug.DebugInput;
import com.jteam.isometric.core.debug.DebugRenderer;
import com.jteam.isometric.core.map.StaggeredIsometricMapRenderer;
import com.jteam.isometric.core.movement.MovementController;
import com.jteam.isometric.core.path.PathFinder;
import com.jteam.isometric.core.player.PlayerInput;
import com.jteam.isometric.core.renderer.Renderer;
import com.jteam.isometric.core.util.math.CordMath;
import com.jteam.isometric.core.util.math.Direction;
import com.sun.tools.javac.util.List;
import lombok.extern.slf4j.Slf4j;
import org.xguzm.pathfinding.gdxbridge.NavTmxMapLoader;

@Slf4j
public class Demo implements ApplicationListener {

	private OrthographicCamera camera;
	private Viewport viewport;
	private AssetManager assetManager;
	private TiledMap map;
	private Animation minotaurAnimation;
	private Animation playerAnimation;
	private Texture leatherHelmTexture;
	private Texture leatherArmorTexture;
	private Texture leatherPantsTexture;
	private Texture leatherBootsTexture;
	private Texture leatherGlovesTexture;
	private AnimationController minotaurAnimationController;
	private AnimationController playerAnimationController;
	private PathFinder pathFinder;
	private MovementController minotaurMovementController;
	private MovementController playerMovementController;
	private CreatureRenderer minotaurCreatureRenderer;
	private CreatureRenderer playerCreatureRenderer;
	private Renderer renderer;
	private StaggeredIsometricMapRenderer staggeredIsometricMapRenderer;
	private DebugRenderer debugRenderer;

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		viewport = new FitViewport(w, h, camera);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();

		OrthoCameraController orthoCamController = new OrthoCameraController(camera);
		inputMultiplexer.addProcessor(orthoCamController);

		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new NavTmxMapLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(Animation.class, new AnimationLoader(new InternalFileHandleResolver()));
		assetManager.load(Asset.MAP_DEMO, TiledMap.class);
		assetManager.load(Asset.ANIMATION_MINOTAUR, Animation.class);
		assetManager.load(Asset.ANIMATION_PLAYER, Animation.class);
		assetManager.load(Asset.TEXTURE_ITEM_LEATHER_HELM, Texture.class);
		assetManager.load(Asset.TEXTURE_ITEM_LEATHER_ARMOR, Texture.class);
		assetManager.load(Asset.TEXTURE_ITEM_LEATHER_PANTS, Texture.class);
		assetManager.load(Asset.TEXTURE_ITEM_LEATHER_BOOTS, Texture.class);
		assetManager.load(Asset.TEXTURE_ITEM_LEATHER_GLOVES, Texture.class);
		assetManager.finishLoading();

		map = assetManager.get(Asset.MAP_DEMO);
		minotaurAnimation = assetManager.get(Asset.ANIMATION_MINOTAUR);
		playerAnimation = assetManager.get(Asset.ANIMATION_PLAYER);
		leatherHelmTexture = assetManager.get(Asset.TEXTURE_ITEM_LEATHER_HELM);
		leatherArmorTexture = assetManager.get(Asset.TEXTURE_ITEM_LEATHER_ARMOR);
		leatherPantsTexture = assetManager.get(Asset.TEXTURE_ITEM_LEATHER_PANTS);
		leatherBootsTexture = assetManager.get(Asset.TEXTURE_ITEM_LEATHER_BOOTS);
		leatherGlovesTexture = assetManager.get(Asset.TEXTURE_ITEM_LEATHER_GLOVES);
        playerAnimation.setTextures(List.of(
            leatherHelmTexture,
            leatherArmorTexture,
            leatherPantsTexture,
            leatherBootsTexture,
            leatherGlovesTexture
        ));

		Vector2 minotaurPosition = new Vector2();
		CordMath.cordToPosition(new Vector2(0, 0), minotaurPosition);
		Creature minotaurCreature = Creature.builder()
            .position(minotaurPosition)
			.animation(minotaurAnimation)
            .isMoving(false)
            .facingDir(Direction.E)
            .build();

        Vector2 playerPosition = new Vector2();
        CordMath.cordToPosition(new Vector2(4, 4), playerPosition);
        Creature playerCreature = Creature.builder()
            .position(playerPosition)
            .animation(playerAnimation)
            .isMoving(false)
            .facingDir(Direction.E)
            .build();

		renderer = new Renderer();
        pathFinder = new PathFinder(map);

		minotaurAnimationController = new AnimationController(minotaurCreature);
		playerAnimationController = new AnimationController(playerCreature);
		minotaurMovementController = new MovementController(minotaurCreature, pathFinder);
		playerMovementController = new MovementController(playerCreature, pathFinder);

        DebugInput debugInput = new DebugInput();
        PlayerInput playerInput = new PlayerInput(playerMovementController, viewport);

        inputMultiplexer.addProcessor(debugInput);
        inputMultiplexer.addProcessor(playerInput);

        minotaurCreatureRenderer = new CreatureRenderer(minotaurCreature, renderer, camera);
        playerCreatureRenderer = new CreatureRenderer(playerCreature, renderer, camera);
		staggeredIsometricMapRenderer = new StaggeredIsometricMapRenderer(renderer, map);
		debugRenderer = new DebugRenderer(renderer, viewport);

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

	@Override
	public void resize (int width, int height) {
		log.info("resize: {}x{}", width, height);
		viewport.update(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		staggeredIsometricMapRenderer.setView(camera);
		staggeredIsometricMapRenderer.render();

		minotaurAnimationController.update();
        playerAnimationController.update();
		minotaurMovementController.update();
        playerMovementController.update();
		minotaurCreatureRenderer.update();
        playerCreatureRenderer.update();

		debugRenderer.render();

		renderer.render();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
