# MixinSwap

Allows you to choose mixins based on versions of the game, or other mods.  
MixinSwap uses no Minecraft internals, so it should work on pretty much any Minecraft version.

Currently in a pre-release stage, use at your own discretion.

## Setup

Snapshot builds are available on [my repository](https://repo.stashy.dev/#/snapshots).

```groovy
repositories {
    maven {
        url = uri("https://repo.stashy.dev/snapshots")
    }
}

dependencies {
    include(modApi("dev.stashy:MixinSwap:1.0.0-SNAPSHOT"))
}
```

To set up MixinSwap, you must create a class that extends `MixinSwapPlugin`. In the `onLoad` function, set
the `mixinClasses` list to what is returned from `MixinSwap.getMatchingMixins()`.

```java
public class YourModMixinConfig extends MixinSwapPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        // Note: DO NOT reference any other class here.
        // Any reference will load classes that injectors have not yet injected into, which will result in catastrophic failure.
        // This includes a MODID variable in your main class, if you have one.
        var loader = FabricLoader.getInstance();
        var modContainer = loader.getModContainer("modid").orElseThrow();
        mixinClasses = MixinSwap.getMatchingMixins(mixinPackage, modContainer, loader);
    }
}
```

Then, insert this class as a plugin in your mixin config file (`mod.mixins.json`).

```
"plugin": "com.example.mixins.YourModMixinConfig"
```

Finally, create a `mixinswap.config.json` file in the same directory as your mixin config.
You can check the tests to see what the config should look like.

