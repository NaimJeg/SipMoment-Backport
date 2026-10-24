package com.renyigesai.sip_moment.common.mixin;

import com.renyigesai.sip_moment.common.entitys.ThrownSpearLonginus;
import com.renyigesai.sip_moment.common.init.SMMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public class TridentItemMixin extends Item {

    public TridentItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "releaseUsing",at = @At("HEAD"), cancellable = true)
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime, CallbackInfo cir) {
        if (entity.hasEffect(SMMobEffects.SINCERELY_FOR_YOU)){
            SMReleaseSpearLonginus(itemStack, level, entity, remainingTime);
            cir.cancel();
        }

    }

    private boolean SMReleaseSpearLonginus(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime){
        if (entity instanceof Player player) {
            int timeHeld = this.getUseDuration(itemStack, entity) - remainingTime;
            if (timeHeld < 10) {
                return false;
            } else {
                float riptideStrength = EnchantmentHelper.getTridentSpinAttackStrength(itemStack, player);
                if (!(riptideStrength > 0.0F) || player.isInWaterOrRain() && !player.isPassenger()) {
                    if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
                        return false;
                    } else {
                        Holder<SoundEvent> sound = EnchantmentHelper.pickHighestLevel(itemStack, EnchantmentEffectComponents.TRIDENT_SOUND)
                                .orElse(SoundEvents.TRIDENT_THROW);
                        player.awardStat(Stats.ITEM_USED.get(this));
                        if (level instanceof ServerLevel serverLevel) {
                            itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
                            if (riptideStrength == 0.0F) {
                                ItemStack thrownItemStack = itemStack.consumeAndReturn(1, player);
                                ThrownSpearLonginus trident = new ThrownSpearLonginus(level, player, thrownItemStack);
                                trident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 5.0F, 1.0F);
                                serverLevel.addFreshEntity(trident);
                                if (player.hasInfiniteMaterials()) {
                                    trident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                                }

                                level.playSound(null, trident, sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                                return true;
                            }
                        }

                        if (riptideStrength > 0.0F) {
                            float yRot = player.getYRot();
                            float xRot = player.getXRot();
                            float xd = -Mth.sin(yRot * (float) (Math.PI / 180.0)) * Mth.cos(xRot * (float) (Math.PI / 180.0));
                            float yd = -Mth.sin(xRot * (float) (Math.PI / 180.0));
                            float zd = Mth.cos(yRot * (float) (Math.PI / 180.0)) * Mth.cos(xRot * (float) (Math.PI / 180.0));
                            float dist = Mth.sqrt(xd * xd + yd * yd + zd * zd);
                            xd *= riptideStrength / dist;
                            yd *= riptideStrength / dist;
                            zd *= riptideStrength / dist;
                            player.push(xd, yd, zd);
                            player.startAutoSpinAttack(20, 8.0F, itemStack);
                            if (player.onGround()) {
                                float heightDifference = 1.1999999F;
                                player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                            }

                            level.playSound(null, player, sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                            return true;
                        } else {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
