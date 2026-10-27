package com.renyigesai.sip_moment.common.mixin;

import com.renyigesai.sip_moment.common.entitys.ISlowResistant;
import com.renyigesai.sip_moment.common.init.SMMobEffects;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class PlayerMixin extends Avatar implements ISlowResistant {
    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public float getSlowResistance() {
        if ((Object)this instanceof Player player){
            if (player.hasEffect(SMMobEffects.EDGERUNNERS)){
                return 1.0f;
            }
        }
        return 0.5f;
    }
}
