import dev.stashy.mixinswap.model.MixinSwapConfig;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelTest {
    public MixinSwapConfig getConfig() throws URISyntaxException, IOException {
        var configResource = ModelTest.class.getResource("mixinswap.config.json");
        assert configResource != null;
        return MixinSwapConfig.fromJson(Files.newBufferedReader(Path.of(configResource.toURI())));
    }

    @Test
    public void testJson() throws IOException, VersionParsingException, URISyntaxException {
        var config = getConfig();

        Map<String, Version> mods = Map.of("minecraft", Version.parse("1.19"), "testmod", Version.parse("1.0.0"));

        var matching = config.getMatchingMixins(mods);
        assertTrue(matching.contains("mixinByWildcard"));
        assertTrue(matching.contains("mixinRange"));
        assertFalse(matching.contains("mixinByComparison"));
    }

    @Test
    public void testOutOfRange() throws URISyntaxException, IOException, VersionParsingException {
        var config = getConfig();

        Map<String, Version> mods = Map.of("minecraft", Version.parse("1.20"));

        var matching = config.getMatchingMixins(mods);
        assertFalse(matching.contains("mixinRange"));
        assertFalse(matching.contains("mixinByWildcard"));
        assertFalse(matching.contains("mixinByComparison"));
    }
}
