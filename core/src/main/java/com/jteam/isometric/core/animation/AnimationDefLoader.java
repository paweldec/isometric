package com.jteam.isometric.core.animation;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class AnimationDefLoader extends AsynchronousAssetLoader<AnimationDef, AnimationDefLoader.AnimationInfoParameter> {

    private final static Json json = new Json();
    private AnimationDef animationDef;

    static final class AnimationInfoParameter extends AssetLoaderParameters<AnimationDef> {}

    public AnimationDefLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, AnimationInfoParameter parameter) {
        animationDef = json.fromJson(AnimationDef.class, file);
    }

    @Override
    public AnimationDef loadSync(AssetManager manager, String fileName, FileHandle file, AnimationInfoParameter parameter) {
        AnimationDef animationDef = this.animationDef;
        this.animationDef = null;
        return animationDef;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AnimationInfoParameter parameter) {
        return null;
    }

}
