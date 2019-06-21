package com.jteam.isometric.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.jteam.isometric.core.animation.Animation;
import com.jteam.isometric.core.animation.AnimationController;
import com.jteam.isometric.core.animation.AnimationDirection;
import com.jteam.isometric.core.animation.AnimationLoader;
import com.jteam.isometric.core.path.PathFinder;
import com.jteam.isometric.core.renderer.Renderer;
import lombok.extern.slf4j.Slf4j;
import org.xguzm.pathfinding.grid.GridCell;

import java.util.List;

@Slf4j
public class Demo implements ApplicationListener {

	private OrthographicCamera camera;
	private AssetManager assetManager;
	private TiledMap map;
	private Animation minotaurAnimation;
	private AnimationController minotaurAnimationController;
	private PathFinder pathFinder;
	private Renderer renderer;
	private IsometricMapRenderer isometricMapRenderer;

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * 10, 10);
		camera.zoom = 1;
		camera.update();

		OrthoCameraController orthoCamController = new OrthoCameraController(camera);
		Gdx.input.setInputProcessor(orthoCamController);

		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(Animation.class, new AnimationLoader(new InternalFileHandleResolver()));
		assetManager.load(Asset.MAP_DEMO, TiledMap.class);
		assetManager.load(Asset.ANIMATION_MINOTAUR, Animation.class);
		assetManager.finishLoading();

		map = assetManager.get(Asset.MAP_DEMO);
		minotaurAnimation = assetManager.get(Asset.ANIMATION_MINOTAUR);

		renderer = new Renderer();

		minotaurAnimationController = new AnimationController(renderer, minotaurAnimation);
		minotaurAnimationController.setState("stance");
		minotaurAnimationController.setDirection(AnimationDirection.E);

		pathFinder = new PathFinder(map);
		List<GridCell> path = pathFinder.find(5, 28, 6, 29);
		log.debug(path.toString());

		isometricMapRenderer = new IsometricMapRenderer(renderer, map);
	}

	@Override
	public void resize (int width, int height) {
		log.info("resize: {}x{}", width, height);
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		isometricMapRenderer.setView(camera);
		isometricMapRenderer.render();

		minotaurAnimationController.setView(camera);
		minotaurAnimationController.update();
		minotaurAnimationController.render();

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
