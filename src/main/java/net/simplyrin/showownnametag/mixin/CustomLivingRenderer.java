package net.simplyrin.showownnametag.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.simplyrin.showownnametag.ShowOwnNametag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class CustomLivingRenderer {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void onRender(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        this.renderNametag(livingEntity, matrixStack, vertexConsumerProvider, i);
    }

    protected void renderNametag(LivingEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!(entity instanceof ClientPlayerEntity)) {
            return;
        }

        if (!ShowOwnNametag.getInstance().isToggle()) {
            return;
        }

        ClientPlayerEntity player = (ClientPlayerEntity) entity;

        MinecraftClient mc = MinecraftClient.getInstance();

        String uniqueId = player.getGameProfile().getId().toString();
        if (!uniqueId.equals(mc.player.getGameProfile().getId().toString())) {
            return;
        }

        Quaternion rotation = mc.getEntityRenderDispatcher().getRotation();

        Text text = entity.getDisplayName();

        boolean bl = !entity.isSneaky();
        float f = entity.getHeight() + 0.5F;
        int i = "deadmau5".equals(text.getString()) ? -10 : 0;
        matrices.push();
        matrices.translate(0.0D, f, 0.0D);
        matrices.multiply(rotation);
        matrices.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = matrices.peek().getModel();
        float g = mc.options.getTextBackgroundOpacity(0.25F);
        int j = (int)(g * 255.0F) << 24;
        TextRenderer textRenderer = mc.textRenderer;
        float h = (-textRenderer.getWidth(text.getString()) / 2);
        textRenderer.draw(text, h, i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
        if (bl) {
            textRenderer.draw(text, h, i, -1, false, matrix4f, vertexConsumers, false, 0, light);
        }
        matrices.pop();
    }

}
