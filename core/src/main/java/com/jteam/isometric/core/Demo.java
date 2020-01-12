package com.jteam.isometric.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import lombok.extern.slf4j.Slf4j;
import org.xguzm.pathfinding.gdxbridge.NavTmxMapLoader;

@Slf4j
public class Demo implements ApplicationListener {

	private OrthographicCamera camera;
	private Viewport viewport;
	private AssetManager assetManager;
	private TiledMap map;
	private Animation minotaurAnimation;
	private AnimationController minotaurAnimationController;
	private PathFinder pathFinder;
	private MovementController minotaurMovementController;
	private CreatureRenderer creatureRenderer;
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
		assetManager.finishLoading();

		map = assetManager.get(Asset.MAP_DEMO);
		minotaurAnimation = assetManager.get(Asset.ANIMATION_MINOTAUR);

		Vector2 minotaurPosition = new Vector2();
		CordMath.cordToPosition(new Vector2(0, 0), minotaurPosition);
		Creature minotaurCreature = Creature.builder()
            .position(minotaurPosition)
			.animation(minotaurAnimation)
            .isMoving(false)
            .facingDir(Direction.E)
            .build();

		renderer = new Renderer();
        pathFinder = new PathFinder(map);

		minotaurAnimationController = new AnimationController(minotaurCreature);
		minotaurMovementController = new MovementController(minotaurCreature, pathFinder);

        DebugInput debugInput = new DebugInput();
        PlayerInput playerInput = new PlayerInput(minotaurMovementController, viewport);

        inputMultiplexer.addProcessor(debugInput);
        inputMultiplexer.addProcessor(playerInput);

        creatureRenderer = new CreatureRenderer(minotaurCreature, renderer, camera);
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
		minotaurMovementController.update();
		creatureRenderer.update();

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
