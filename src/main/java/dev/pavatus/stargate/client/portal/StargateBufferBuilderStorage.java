/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package dev.pavatus.stargate.client.portal;

import java.util.SortedMap;

import dev.pavatus.stargate.client.renderers.StargateRenderLayers;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Util;

@Environment(value=EnvType.CLIENT)
public class StargateBufferBuilderStorage extends BufferBuilderStorage {
    private final SortedMap<RenderLayer, BufferBuilder> portalBuilder = Util.make(new Object2ObjectLinkedOpenHashMap(), map -> {
        StargateBufferBuilderStorage.assignBufferBuilder(map, StargateRenderLayers.getPortal());
    });
    private final VertexConsumerProvider.Immediate portalVertexConsumer = VertexConsumerProvider.immediate(this.portalBuilder, new BufferBuilder(256));

    private static void assignBufferBuilder(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> builderStorage, RenderLayer layer) {
        builderStorage.put(layer, new BufferBuilder(layer.getExpectedBufferSize()));
    }

    public VertexConsumerProvider.Immediate getPortalVertexConsumer() {
        return this.portalVertexConsumer;
    }
}