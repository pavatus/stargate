package dev.pavatus.stargate.client;

import dev.pavatus.stargate.client.renderers.DHDBlockEntityRenderer;
import dev.pavatus.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.pavatus.stargate.core.StargateBlockEntities;
import dev.pavatus.stargate.core.StargateBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class StargateModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerBlockEntityRenderers();
        setupBlockRendering();
    }

    public void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(StargateBlockEntities.STARGATE, StargateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(StargateBlockEntities.DHD, DHDBlockEntityRenderer::new);
    }

    public static void setupBlockRendering() {
        BlockRenderLayerMap map = BlockRenderLayerMap.INSTANCE;
        map.putBlock(StargateBlocks.STARGATE, RenderLayer.getCutout());
    }
}

