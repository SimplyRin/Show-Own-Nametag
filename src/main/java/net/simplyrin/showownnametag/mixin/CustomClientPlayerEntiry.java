package net.simplyrin.showownnametag.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;
import net.simplyrin.showownnametag.ShowOwnNametag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class CustomClientPlayerEntiry {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void onSendChatMessage(String message, CallbackInfo info) {
        if (message != null && message.startsWith("/")) {
            String[] args = message.split(" ");

            if (args.length > 0 && args[0].equalsIgnoreCase("/nametag")) {
                info.cancel();

                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("on")) {
                        ShowOwnNametag.getInstance().setStatus(true);
                        this.info("§aShow Own Nametag: §bEnabled§a.");
                        return;
                    }

                    if (args[1].equalsIgnoreCase("off")) {
                        ShowOwnNametag.getInstance().setStatus(false);
                        this.info("§aShow Own Nametag: §cDisabled§a.");
                        return;
                    }
                }

                this.info("§cUsage: /nametag <on|off>");
                return;
            }
        }
    }

    public void info(String message) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        player.sendMessage(new LiteralText(message), false);
    }

}
