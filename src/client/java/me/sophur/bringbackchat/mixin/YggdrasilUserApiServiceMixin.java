package me.sophur.bringbackchat.mixin;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilUserApiService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

import static com.mojang.authlib.minecraft.UserApiService.UserFlag.*;

@Mixin(YggdrasilUserApiService.class)
public class YggdrasilUserApiServiceMixin {
    @Inject(method = "fetchProperties()Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;", at = @At("HEAD"), cancellable = true)
    private static void injectFetchProperties(CallbackInfoReturnable<UserApiService.UserProperties> cir) {
        cir.setReturnValue(new UserApiService.UserProperties(
                Set.of(CHAT_ALLOWED, REALMS_ALLOWED, SERVERS_ALLOWED), Map.of()));
    }
}
