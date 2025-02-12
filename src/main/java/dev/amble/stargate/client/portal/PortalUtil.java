package dev.amble.stargate.client.portal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.stargate.StargateMod;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static net.minecraft.util.math.MathHelper.lerp;

public class PortalUtil {
    public Identifier TEXTURE_LOCATION;
    private final float distortionSpeed;
    private final float distortionSeparationFactor;
    private final float distortionFactor;
    private final float scale;
    private final float rotationFactor;
    private final float rotationSpeed;
    private final float speed;
    private float time = 0;

    public PortalUtil(Identifier texture) {
        TEXTURE_LOCATION = texture;
        this.distortionSpeed = 0.5f;
        this.distortionSeparationFactor = 32f;
        this.distortionFactor = 2;
        this.scale = 32f;
        this.rotationFactor = 1f;
        this.rotationSpeed = 1f;
        this.speed = 4f;
    }
    @ApiStatus.Internal
    public PortalUtil(String name) {
        this(StargateMod.id("textures/portal/" + name + ".png"));
    }

    public void renderPortalInterior(MatrixStack matrixStack) {

        time += MinecraftClient.getInstance().getTickDelta() / 360f;

        matrixStack.push();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);

        matrixStack.scale(scale, scale, scale);

        MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE_LOCATION);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        for (int i = 0; i < 4; ++i) {
            this.renderSection(buffer, i, (MinecraftClient.getInstance().player.age / 200.0f) * -this.speed, (float) Math.sin(i * Math.PI / 4),
                    (float) Math.sin((i + 1) * Math.PI / 4), matrixStack.peek().getPositionMatrix());
        }

        /*StargateModel model = new StargateModel(StargateModel.getTexturedModelData().createModel());
        model.getPart().traverse().forEach(part -> {
            part.forEachCuboid(matrixStack, (d, g, h, s) -> {
                Vector3f[] vertices = new Vector3f[8];
                vertices[0] = new Vector3f(s.minX, s.minY, s.minZ);
                vertices[1] = new Vector3f(s.maxX, s.minY, s.minZ);
                vertices[2] = new Vector3f(s.maxX, s.maxY, s.minZ);
                vertices[3] = new Vector3f(s.minX, s.maxY, s.minZ);
                vertices[4] = new Vector3f(s.minX, s.minY, s.maxZ);
                vertices[5] = new Vector3f(s.maxX, s.minY, s.maxZ);
                vertices[6] = new Vector3f(s.maxX, s.maxY, s.maxZ);
                vertices[7] = new Vector3f(s.minX, s.maxY, s.maxZ);
                this.method(vertices);

                List<ModelPart.Cuboid> cuboids = new ArrayList<>();

                for (int i = 0; i < vertices.length; i += 8) {
                    float[] min = { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE };
                    float[] max = { Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE };

                    for (int j = 0; j < 8; j++) {
                        float x = vertices[i + j].x;
                        float y = vertices[i + j].y;
                        float z = vertices[i + j].z;

                        min[0] = Math.min(min[0], x);
                        min[1] = Math.min(min[1], y);
                        min[2] = Math.min(min[2], z);

                        max[0] = Math.max(max[0], x);
                        max[1] = Math.max(max[1], y);
                        max[2] = Math.max(max[2], z);
                    }

                    cuboids.add(new ModelPart.Cuboid(12, 12, min[0], min[1], min[2], max[0], max[1], max[2], 0, 0, 0, 0));
                }

                AtomicReference<ModelPart> vertexedUp = new AtomicReference<>();

                        ModelData modelData = new ModelData();
                ModelPartData modelPartData = modelData.getRoot();

                ModelPartBuilder partBuilder = ModelPartBuilder.create();
                cuboids.forEach(cuboid -> {
                    partBuilder.cuboid(cuboid.minX, cuboid.minY, cuboid.minZ, cuboid.maxX - cuboid.minX, cuboid.maxY - cuboid.minY, cuboid.maxZ - cuboid.minZ);
                    modelPartData.addChild("nothing", partBuilder, part.getTransform());
                    vertexedUp.set(TexturedModelData.of(modelData, 0, 0).createModel());
                });

                vertexedUp.get().render(matrixStack, buffer, 1, 1);
            });
        });*/

        tessellator.draw();
        matrixStack.pop();
    }

    public void renderSection(VertexConsumer builder, int zOffset, float textureDistanceOffset, float startScale,
                              float endScale, Matrix4f matrix4f) {
        float panel = 3f;//1 / 6f;
        float sqrt =0f;//(float) Math.sqrt(3) / 2.0f;
        int vOffset = 1;//(zOffset * panel + textureDistanceOffset > 1.0) ? zOffset - 6 : zOffset;
        float distortion = 0;//this.computeDistortionFactor(time, zOffset);
        float distortionPlusOne = 0;//this.computeDistortionFactor(time, zOffset + 1);
        float panelDistanceOffset = 0;//panel + textureDistanceOffset;
        float vPanelOffset = 0;//(vOffset * panel) + textureDistanceOffset;

        int uOffset = 0;

        float uPanelOffset = uOffset * panel;

        addVertex(builder, matrix4f, 0f, -startScale + distortion, -zOffset, uPanelOffset, vPanelOffset);

        addVertex(builder, matrix4f, 0f, -endScale + distortionPlusOne, -zOffset - 1, uPanelOffset,
                vOffset * panel + panelDistanceOffset);

        addVertex(builder, matrix4f, endScale * -sqrt, endScale / -2f + distortionPlusOne, -zOffset - 1,
                uPanelOffset + panel, vOffset * panel + panelDistanceOffset);

        addVertex(builder, matrix4f, startScale * -sqrt, startScale / -2f + distortion, -zOffset, uPanelOffset + panel,
                vPanelOffset);

        uOffset = 1;

        uPanelOffset = uOffset * panel;

        addVertex(builder, matrix4f, startScale * -sqrt, startScale / -2f + distortion, -zOffset, uPanelOffset,
                vPanelOffset);

        addVertex(builder, matrix4f, endScale * -sqrt, endScale / -2f + distortionPlusOne, -zOffset - 1, uPanelOffset,
                vOffset * panel + panelDistanceOffset);

        addVertex(builder, matrix4f, endScale * -sqrt, endScale / 2f + distortionPlusOne, -zOffset - 1,
                uPanelOffset + panel, vOffset * panel + panelDistanceOffset);

        addVertex(builder, matrix4f, startScale * -sqrt, startScale / 2f + distortion, -zOffset, uPanelOffset + panel,
                vPanelOffset);

        uOffset = 2;

        uPanelOffset = uOffset * panel;

        addVertex(builder, matrix4f, 0f, endScale + distortionPlusOne, -zOffset - 1, uPanelOffset + panel,
                vOffset * panel + panelDistanceOffset);

        addVertex(builder, matrix4f, 0f, startScale + distortion, -zOffset, uPanelOffset + panel, vPanelOffset);

        addVertex(builder, matrix4f, startScale * -sqrt, startScale / 2f + distortion, -zOffset, uPanelOffset,
                vPanelOffset);

        addVertex(builder, matrix4f, endScale * -sqrt, endScale / 2f + distortionPlusOne, -zOffset - 1, uPanelOffset,
                vOffset * panel + panelDistanceOffset);

        uOffset = 3;

        uPanelOffset = uOffset * panel;

        addVertex(builder, matrix4f, 0f, startScale + distortion, -zOffset, uPanelOffset, vPanelOffset);

        addVertex(builder, matrix4f, 0f, endScale + distortionPlusOne, -zOffset - 1, uPanelOffset,
                vOffset * panel + panelDistanceOffset);

        addVertex(builder, matrix4f, endScale * sqrt, (endScale / 2f + distortionPlusOne), -zOffset - 1,
                uPanelOffset + panel, vOffset * panel + panelDistanceOffset);

        addVertex(builder, matrix4f, startScale * sqrt, (startScale / 2f + distortion), -zOffset, uPanelOffset + panel,
                vPanelOffset);

        uOffset = 4;

        uPanelOffset = uOffset * panel;

        addVertex(builder, matrix4f, startScale * sqrt, (startScale / 2f + distortion), -zOffset, uPanelOffset,
                vPanelOffset);

        addVertex(builder, matrix4f, endScale * sqrt, endScale / 2f + distortionPlusOne, -zOffset - 1, uPanelOffset,
                vOffset * panel + panelDistanceOffset);

        addVertex(builder, matrix4f, endScale * sqrt, endScale / -2f + distortionPlusOne, -zOffset - 1,
                uPanelOffset + panel, vOffset * panel + panelDistanceOffset);

        addVertex(builder, matrix4f, startScale * sqrt, startScale / -2f + distortion, -zOffset, uPanelOffset + panel,
                vPanelOffset);

        uOffset = 5;

        uPanelOffset = uOffset * panel;

        addVertex(builder, matrix4f, 0f, -endScale + distortionPlusOne, -zOffset - 1, uPanelOffset + panel,
                vOffset * panel + panelDistanceOffset);

        addVertex(builder, matrix4f, 0f, -startScale + distortion, -zOffset, uPanelOffset + panel, vPanelOffset);

        addVertex(builder, matrix4f, startScale * sqrt, startScale / -2f + distortion, -zOffset, uPanelOffset,
                vPanelOffset);

        addVertex(builder, matrix4f, endScale * sqrt, endScale / -2f + distortionPlusOne, -zOffset - 1, uPanelOffset,
                vOffset * panel + panelDistanceOffset);
    }

    private void addVertex(VertexConsumer builder, Matrix4f matrix, float x, float y, float z, float u, float v) {
        builder.vertex(matrix, x, y, z).texture(u, v).color(0, 0, 0, 0).next();
    }

    private float computeDistortionFactor(float time, int t) {
        return (float) (Math.sin(time * this.distortionSpeed * 2.0 * Math.PI + (13 - t) *
                this.distortionSeparationFactor) * this.distortionFactor) / 8;
    }

    // Define a noise function (e.g., Perlin noise or Simplex noise)
    /*public float noise(float x, float y, float z) {
        int xi = (int) x;
        int yi = (int) y;
        int zi = (int) z;
        float xf = x - xi;
        float yf = y - yi;
        float zf = z - zi;
        float u = fade(xf);
        float v = fade(yf);
        int aa = perm[xi + perm[yi + perm[zi]]];
        int ba = perm[xi + perm[yi + perm[zi + 1]]];
        int ab = perm[xi + perm[yi + 1 + perm[zi]]];
        int bb = perm[xi + perm[yi + 1 + perm[zi + 1]]];
        float x1 = lerp(u, grad(aa, xf, yf, zf), grad(ba, xf, yf, zf - 1));
        float x2 = lerp(u, grad(ab, xf, yf - 1, zf), grad(bb, xf, yf - 1, zf - 1));
        return lerp(v, x1, x2);
    }

    // Define a fade function for Perlin noise
    float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    // Define a gradient function for Perlin noise
    float grad(int hash, float x, float y, float z) {
        int h = hash & 15;
        float u = h < 8 ? x : y;
        float v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    // Define a permutation array for Perlin noise
    public int[] perm = new int[512];

// Initialize the permutation array
    public void method(Vector3f[] vertices) {
        for (int i = 0; i < 256; i++) {
            perm[i] = i;
        }
        for (int i = 256; i < 512; i++) {
            perm[i] = perm[i - 256];
        }

    // Manipulate the vertex points to give them a noisy look
        for (Vector3f vertex : vertices) {
            float noiseValue = noise(vertex.x, vertex.y, vertex.z);
            vertex.x += noiseValue * 0.1f;
            vertex.y += noiseValue * 0.1f;
            vertex.z += noiseValue * 0.1f;
        }
    }*/


}

