package dev.stashy.mixinswap;

import dev.stashy.mixinswap.model.MixinSwapConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MixinSwap {
    public static final String CONFIG_PATH = "mixinswap.mixins.json";

    public static List<String> getMatchingMixins(String mixinPackage, ModContainer mod, FabricLoader loader) {
        return getConfig(mod).getMatchingMixins(getModVersions(loader)).stream().map(it -> mixinPackage + "." + it).toList();
    }

    public static MixinSwapConfig getConfig(ModContainer mod) {
        return getConfig(mod.findPath(CONFIG_PATH)
                .orElseThrow(() -> new NullPointerException("Configuration was not found in the default path. Expected: " + CONFIG_PATH)));
    }

    public static MixinSwapConfig getConfig(Path path) {
        try {
            return MixinSwapConfig.fromJson(Files.newBufferedReader(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Version> getModVersions(FabricLoader loader) {
        return loader.getAllMods().stream()
                .map(ModContainer::getMetadata)
                .collect(Collectors.toMap(ModMetadata::getName, ModMetadata::getVersion));
    }
}
