package dev.pavatus.stargate.datagen;

import dev.pavatus.lib.datagen.lang.LanguageType;
import dev.pavatus.lib.datagen.lang.SakitusLanguageProvider;
import dev.pavatus.lib.datagen.sound.SakitusSoundProvider;
import dev.pavatus.lib.datagen.tag.SakitusBlockTagProvider;
import dev.pavatus.stargate.core.StargateBlocks;
import dev.pavatus.stargate.core.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SGDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();

        genLang(pack);
        genSounds(pack);
        genTags(pack);
    }

    private void genTags(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> new SakitusBlockTagProvider(output, registriesFuture).withBlocks(StargateBlocks.class))));
    }

    private void genLang(FabricDataGenerator.Pack pack) {
        genEnglish(pack);
    }

    private void genEnglish(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            SakitusLanguageProvider provider = new SakitusLanguageProvider(output, LanguageType.EN_US);

            provider.translateBlocks(StargateBlocks.class);
            provider.translateItems(StargateItems.class);

            provider.addTranslation(StargateItems.Groups.MAIN, "STARGATE");

            return provider;
        })));
    }

    private void genSounds(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            SakitusSoundProvider provider = new SakitusSoundProvider(output);

            return provider;
        })));
    }
}