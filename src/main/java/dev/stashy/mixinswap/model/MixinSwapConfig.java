package dev.stashy.mixinswap.model;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import net.fabricmc.loader.impl.util.version.VersionPredicateParser;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixinSwapConfig {
    private final Map<String, Map<String, List<String>>> mixinConfig;

    public MixinSwapConfig(Map<String, Map<String, List<String>>> config) {
        mixinConfig = config;
    }

    public List<String> getMatchingMixins(Map<String, Version> mods) {
        return mixinConfig.entrySet().stream().filter(mixin -> {
            var modVersion = mixin.getValue();
            return modVersion.entrySet().stream().allMatch(modVersionEntry ->
            {
                var modName = modVersionEntry.getKey();
                var entries = modVersionEntry.getValue();
                return entries.stream().allMatch(version -> {
                    try {
                        VersionPredicate matcher = VersionPredicateParser.parse(version);
                        return mods.containsKey(modName) && matcher.test(mods.get(modName));
                    } catch (VersionParsingException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        }).map(Map.Entry::getKey).toList();
    }

    public static MixinSwapConfig fromJson(Reader jsonReader) throws IOException {
        var json = Json.parse(jsonReader);
        Map<String, Map<String, List<String>>> config = new HashMap<>();
        var root = json.asObject();
        for (JsonObject.Member mixin : root) {
            var mixinName = mixin.getName();
            Map<String, List<String>> modVersions = new HashMap<>();
            for (JsonObject.Member mod : mixin.getValue().asObject()) {
                modVersions.put(mod.getName(), mod.getValue().asArray().values().stream().map(JsonValue::asString).toList());
            }
            config.put(mixinName, modVersions);
        }
        jsonReader.close();
        return new MixinSwapConfig(config);
    }
}
