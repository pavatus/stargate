package dev.amble.stargate.client;

import dev.amble.stargate.api.ClientStargateNetwork;
import dev.amble.stargate.client.renderers.DHDBlockEntityRenderer;
import dev.amble.stargate.client.renderers.DHDControlEntityRenderer;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.client.util.ClientStargateUtil;
import dev.amble.stargate.core.StargateBlockEntities;
import dev.amble.stargate.core.StargateBlocks;
import dev.amble.stargate.core.StargateEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class StargateModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientStargateNetwork.getInstance();
        registerBlockEntityRenderers();
        setupBlockRendering();
        registerEntityRenderers();

        ClientStargateUtil.init();
    }

    public void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(StargateBlockEntities.STARGATE, StargateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(StargateBlockEntities.DHD, DHDBlockEntityRenderer::new);
    }

    public void registerEntityRenderers() {
        EntityRendererRegistry.register(StargateEntities.DHD_CONTROL_TYPE, DHDControlEntityRenderer::new);
    }

    public static void setupBlockRendering() {
        BlockRenderLayerMap map = BlockRenderLayerMap.INSTANCE;
        map.putBlock(StargateBlocks.STARGATE, RenderLayer.getCutout());
    }
}

