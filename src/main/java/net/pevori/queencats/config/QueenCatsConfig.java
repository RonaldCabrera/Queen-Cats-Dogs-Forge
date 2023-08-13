package net.pevori.queencats.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

public class QueenCatsConfig {
    public static class Server {
        public final ConfigValue<Boolean> enable_humanoid_cat_sounds;
        public final ConfigValue<Boolean> enable_humanoid_dog_sounds;
        public final ConfigValue<Boolean> enable_humanoid_bunny_sounds;
        public final ConfigValue<Boolean> enable_humanoid_cow_sounds;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("Humanoid Animal Sounds");
            this.enable_humanoid_cat_sounds = builder.translation("text.queencats.config.enable_humanoid_cat_sounds")
                    .define("Enable Humanoid Cat Sounds", true);
            this.enable_humanoid_dog_sounds = builder.translation("text.queencats.config.enable_humanoid_dog_sounds")
                    .define("Enable Humanoid Dog Sounds", true);
            this.enable_humanoid_bunny_sounds = builder.translation("text.queencats.config.enable_humanoid_bunny_sounds")
                    .define("Enable Humanoid Bunny Sounds", true);
            this.enable_humanoid_cow_sounds = builder.translation("text.queencats.config.enable_humanoid_cow_sounds")
                    .define("Enable Humanoid Cow Sounds", true);
            builder.pop();

        }
    }

    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        Pair<Server, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = commonSpecPair.getLeft();
        SERVER_SPEC = commonSpecPair.getRight();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

}
