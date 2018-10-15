package com.jteam.isometric.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Demo implements ApplicationListener {

	private OrthographicCamera camera;
	private AssetManager assetManager;
	private TiledMap map;
	private IsometricMapRenderer renderer;

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
		assetManager.load("demo/demo.tmx", TiledMap.class);
		assetManager.finishLoading();

		map = assetManager.get("demo/demo.tmx");

		renderer = new IsometricMapRenderer(map);
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
		renderer.setView(camera);
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
