import dev.stashy.mixinswap.model.MixinSwapConfig;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelTest {
    @Test
    public void testJson() throws IOException, VersionParsingException, URISyntaxException {
        var configResource = ModelTest.class.getResource("mixinswap.config.json");
        assert configResource != null;
        var config = MixinSwapConfig.fromJson(Files.newBufferedReader(Path.of(configResource.toURI())));
        Map<String, Version> mods = new HashMap<>();
        mods.put("minecraft", Version.parse("1.19"));

        var matching = config.getMatchingMixins(mods);
        assertTrue(matching.contains("mixinByWildcard"));
        assertFalse(matching.contains("mixinByComparison"));
    }
}
