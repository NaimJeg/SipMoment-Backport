package com.renyigesai.sip_moment.common.mixin;

import com.renyigesai.sip_moment.common.entitys.ISlowResistant;
import com.renyigesai.sip_moment.common.init.SMMobEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class PlayerMixin implements ISlowResistant {
    @Override
    public float getSlowResistance() {
        Player player = (Player) (Object) this;
        return player.hasEffect(SMMobEffects.EDGERUNNERS) ? 1.0F : 0.5F;
    }
}
