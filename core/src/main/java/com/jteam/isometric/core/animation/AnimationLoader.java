package com.jteam.isometric.core.animation;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.sun.tools.javac.util.List;

public class AnimationLoader extends AsynchronousAssetLoader<Animation, AnimationLoader.AnimationParameter> {

    private final static Json json = new Json();
    private Animation animation;

    static final class AnimationParameter extends AssetLoaderParameters<Animation> {}

    public AnimationLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, AnimationParameter parameter) {
        animation = json.fromJson(Animation.class, file);
    }

    @Override
    public Animation loadSync(AssetManager manager, String fileName, FileHandle file, AnimationParameter parameter) {
        Animation animation = this.animation;
        this.animation = null;
        if (animation.getTexturePath() != null) {
            final Texture texture = new Texture(resolve(animation.getTexturePath()));
            animation.setTextures(List.of(texture));
        }
        return animation;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AnimationParameter parameter) {
        return null;
    }

}
