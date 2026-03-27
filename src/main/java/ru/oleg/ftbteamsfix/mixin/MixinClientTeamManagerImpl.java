package ru.oleg.ftbteamsfix.mixin;

import dev.ftb.mods.ftbteams.data.ClientTeamManagerImpl;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(ClientTeamManagerImpl.class)
public abstract class MixinClientTeamManagerImpl {

    @Shadow private Map<UUID, KnownClientPlayer> knownPlayers;
    @Shadow private KnownClientPlayer selfKnownPlayer;

    @Inject(
            method = "initSelfDetails",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"
            ),
            cancellable = true
    )
    private void ftbteamsfix$injectOfflineUUID(UUID selfTeamID, CallbackInfo ci) {
        String username = Minecraft.getInstance().getUser().getName();
        UUID offlineId = UUIDUtil.createOfflinePlayerUUID(username);

        KnownClientPlayer offlinePlayer = knownPlayers.get(offlineId);
        if (offlinePlayer != null) {
            this.selfKnownPlayer = offlinePlayer;
            ci.cancel();
        }
    }
}
