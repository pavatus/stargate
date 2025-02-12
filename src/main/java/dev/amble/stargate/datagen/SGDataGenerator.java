package dev.amble.stargate.datagen;

import dev.pavatus.lib.datagen.lang.LanguageType;
import dev.pavatus.lib.datagen.lang.SakitusLanguageProvider;
import dev.pavatus.lib.datagen.loot.SakitusBlockLootTable;
import dev.pavatus.lib.datagen.model.SakitusModelProvider;
import dev.pavatus.lib.datagen.sound.SakitusSoundProvider;
import dev.pavatus.lib.datagen.tag.SakitusBlockTagProvider;
import dev.amble.stargate.core.StargateBlocks;
import dev.amble.stargate.core.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SGDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();

        genLang(pack);
        genSounds(pack);
        genTags(pack);
        genLoot(pack);
        genModels(pack);
    }

    private void genModels(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            SakitusModelProvider provider = new SakitusModelProvider(output);

            provider.withBlocks(StargateBlocks.class);
            provider.withItems(StargateItems.class);

            return provider;
        })));
    }
    private void genTags(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> new SakitusBlockTagProvider(output, registriesFuture).withBlocks(StargateBlocks.class))));
    }
    private void genLoot(FabricDataGenerator.Pack pack) {
         pack.addProvider((((output, registriesFuture) -> new SakitusBlockLootTable(output).withBlocks(StargateBlocks.class))));
    }

    private void genLang(FabricDataGenerator.Pack pack) {
        genEnglish(pack);
    }

    private void genEnglish(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            SakitusLanguageProvider provider = new SakitusLanguageProvider(output, LanguageType.EN_US);

            provider.translateBlocks(StargateBlocks.class);
            provider.addTranslation(StargateBlocks.DHD, "Dial-Home Device");

            provider.translateItems(StargateItems.class);

            provider.addTranslation(StargateItems.Groups.MAIN, "STARGATE");

            provider.addTranslation("tooltip.stargate.link_item.holdformoreinfo", "Hold shift for more info");
            provider.addTranslation("tooltip.stargate.dialer.hint", "Use on stargate to link, then use on another stargate to dial");

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