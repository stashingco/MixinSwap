import dev.stashy.mixinswap.model.MixinSwapConfig;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelTest {
    public MixinSwapConfig getConfig() throws URISyntaxException, IOException {
        URL configResource = ModelTest.class.getResource("mixinswap.config.json");
        assert configResource != null;
        return MixinSwapConfig.fromJson(Files.newBufferedReader(new File(configResource.toURI()).toPath()));
    }

    @Test
    public void testJson() throws IOException, VersionParsingException, URISyntaxException {
        MixinSwapConfig config = getConfig();

        Map<String, Version> mods = new HashMap<>();
        mods.put("minecraft", Version.parse("1.19"));
        mods.put("testmod", Version.parse("1.0.0"));

        List<String> matching = config.getMatchingMixins(mods);
        assertTrue(matching.contains("mixinByWildcard"));
        assertTrue(matching.contains("mixinRange"));
        assertFalse(matching.contains("mixinByComparison"));
    }

    @Test
    public void testOutOfRange() throws URISyntaxException, IOException, VersionParsingException {
        MixinSwapConfig config = getConfig();

        Map<String, Version> mods = new HashMap<>();
        mods.put("minecraft", Version.parse("1.20"));

        List<String> matching = config.getMatchingMixins(mods);
        assertFalse(matching.contains("mixinRange"));
        assertFalse(matching.contains("mixinByWildcard"));
        assertFalse(matching.contains("mixinByComparison"));
    }
}
