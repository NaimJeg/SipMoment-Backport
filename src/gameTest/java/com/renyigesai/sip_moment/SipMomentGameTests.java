package com.renyigesai.sip_moment;

import com.renyigesai.sip_moment.common.blocks.AbstractPileBlock;
import com.renyigesai.sip_moment.common.entitys.AfterimageEntity;
import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import com.renyigesai.sip_moment.common.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(SipMomentMod.MODID)
@PrefixGameTestTemplate(false)
public class SipMomentGameTests {
    @GameTest(template = "empty", batch = "slow_ticks")
    public static void slowedTicksExpire(GameTestHelper helper) {
        Cow cow = cow(helper, 1);
        cow.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1000));
        ITimeScaledEntity scaled = (ITimeScaledEntity) cow;
        scaled.setSlow(0.1F, 20);
        helper.runAfterDelay(10, () -> helper.assertTrue(cow.getEffect(MobEffects.GLOWING).getDuration() >= 998,
                "Entity logic should advance at one tenth of normal speed"));
        helper.runAfterDelay(25, () -> {
            helper.assertTrue(scaled.getTimeScale() == 1.0F && scaled.getTimeStopLease() == 0,
                    "Slow lease must expire even while entity ticks are skipped");
            helper.assertTrue(cow.getEffect(MobEffects.GLOWING).getDuration() < 998, "Normal ticking must resume");
            helper.succeed();
        });
    }

    @GameTest(template = "empty", batch = "passenger")
    public static void passengerSlowExpires(GameTestHelper helper) {
        Cow vehicle = cow(helper, 1);
        Cow passenger = cow(helper, 2);
        helper.assertTrue(passenger.startRiding(vehicle, true), "Passenger should mount the vehicle");
        ITimeScaledEntity scaled = (ITimeScaledEntity) passenger;
        scaled.setSlow(0.1F, 5);
        helper.runAfterDelay(8, () -> {
            helper.assertTrue(scaled.getTimeScale() == 1.0F, "Passengers must recover from slowing");
            helper.succeed();
        });
    }

    @GameTest(template = "empty", batch = "aura")
    public static void edgerunnersAuraAndAfterimage(GameTestHelper helper) {
        Cow source = cow(helper, 1);
        Cow nearby = cow(helper, 2);
        Cow outside = cow(helper, 3);
        outside.setPos(source.getX() + 33.0, source.getY(), source.getZ());
        source.setDeltaMovement(new Vec3(0.2, 0.0, 0.0));
        SMMobEffects.EDGERUNNERS.value().applyEffectTick(source, 0);
        helper.assertTrue(((ITimeScaledEntity) nearby).getTimeScale() == 0.1F, "Nearby entities must slow down");
        helper.assertTrue(((ITimeScaledEntity) outside).getTimeScale() == 1.0F, "Entities outside 32 blocks must stay at normal speed");
        helper.assertTrue(source.getEffect(MobEffects.MOVEMENT_SPEED).getAmplifier() == 2, "Speed III must be applied");
        var ghosts = helper.getLevel().getEntitiesOfClass(AfterimageEntity.class, source.getBoundingBox().inflate(3));
        helper.assertTrue(ghosts.size() == 1 && ghosts.getFirst().getOwner() == source, "Movement must create an owned afterimage");
        helper.runAfterDelay(5, () -> {
            helper.assertTrue(((ITimeScaledEntity) nearby).getTimeScale() == 1.0F, "Aura slowing must expire after its source stops");
            outside.discard();
            helper.succeed();
        });
    }

    @GameTest(template = "empty", batch = "afterimage")
    public static void afterimageImmunityAndLifetime(GameTestHelper helper) {
        AfterimageEntity ghost = helper.spawn(SMEntityTypes.AFTERIMAGE.get(), new BlockPos(1, 1, 1));
        helper.assertTrue(ghost.getMaxHealth() == 20.0F, "Afterimage attributes must be registered on the mod bus");
        ((ITimeScaledEntity) ghost).setSlow(0.1F, 100);
        helper.assertTrue(((ITimeScaledEntity) ghost).getTimeScale() == 1.0F, "Afterimages must resist time slowing");
        helper.assertTrue(!ghost.hurt(helper.getLevel().damageSources().generic(), 1.0F), "Afterimages must not take damage");
        helper.runAfterDelay(35, () -> {
            helper.assertTrue(ghost.isRemoved(), "Afterimages must disappear after 30 real ticks");
            helper.succeed();
        });
    }

    @GameTest(template = "empty", batch = "drinks")
    public static void drinkEffectsAndSips(GameTestHelper helper) {
        Cow drinker = cow(helper, 1);
        ItemStack david = new ItemStack(SMItems.DAVID_MARTINEZ.get());
        david = david.finishUsingItem(helper.getLevel(), drinker);
        helper.assertTrue(david.getOrDefault(SMDataComponents.EAT_COUNT, -1) == 3 && drinker.hasEffect(SMMobEffects.EDGERUNNERS),
                "David Martinez must apply Edgerunners and consume exactly one of four sips");
        drinker.removeEffect(SMMobEffects.EDGERUNNERS);
        ItemStack orange = new ItemStack(SMItems.ORANGE_JUICE.get()).finishUsingItem(helper.getLevel(), drinker);
        helper.assertTrue(orange.getOrDefault(SMDataComponents.EAT_COUNT, -1) == 3 && drinker.hasEffect(SMMobEffects.SINCERELY_FOR_YOU),
                "Orange juice must retain its effect and four servings");
        ItemStack vintage = new ItemStack(SMItems.SHIPWRECK_VINTAGE.get()).finishUsingItem(helper.getLevel(), drinker);
        helper.assertTrue(vintage.getOrDefault(SMDataComponents.EAT_COUNT, -1) == 1
                        && drinker.hasEffect(MobEffects.CONFUSION) && drinker.hasEffect(MobEffects.WITHER),
                "Shipwreck Vintage must apply both effects and retain its second sip");
        vintage = vintage.finishUsingItem(helper.getLevel(), drinker);
        helper.assertTrue(vintage.isEmpty(), "The last serving of Shipwreck Vintage must consume the item");
        helper.succeed();
    }

    @GameTest(template = "empty", batch = "loot")
    public static void shipwreckVintageDrops(GameTestHelper helper) {
        AbstractPileBlock block = (AbstractPileBlock) SMBlocks.SHIPWRECK_VINTAGE.get();
        for (int pile = 1; pile <= 2; pile++) {
            var state = block.defaultBlockState().setValue(block.getPileProperty(), pile);
            var drops = Block.getDrops(state, helper.getLevel(), helper.absolutePos(new BlockPos(1, 1, 1)), null);
            int count = drops.stream().filter(stack -> stack.is(SMItems.SHIPWRECK_VINTAGE.get())).mapToInt(ItemStack::getCount).sum();
            helper.assertTrue(count == pile, "Breaking a drink pile must return every drink");
        }
        helper.succeed();
    }

    @GameTest(template = "empty", batch = "player")
    public static void playerResistanceAndRecovery(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(Vec3.atCenterOf(helper.absolutePos(new BlockPos(1, 1, 1))));
        helper.getLevel().addFreshEntity(player);
        ITimeScaledEntity scaled = (ITimeScaledEntity) player;
        scaled.setSlow(0.1F, 5);
        helper.assertTrue(Math.abs(scaled.getTimeScale() - 0.55F) < 0.001F, "Players must have 50 percent slow resistance");
        helper.runAfterDelay(8, () -> {
            helper.assertTrue(scaled.getTimeScale() == 1.0F, "Players must recover when their slow lease expires");
            player.addEffect(new MobEffectInstance(SMMobEffects.EDGERUNNERS, 100));
            scaled.setSlow(0.1F, 5);
            helper.assertTrue(scaled.getTimeScale() == 1.0F, "Players with Edgerunners must be immune to slowing");
            player.discard();
            helper.succeed();
        });
    }

    private static Cow cow(GameTestHelper helper, int x) {
        Cow cow = helper.spawn(EntityType.COW, new BlockPos(x, 1, 1));
        cow.setNoAi(true);
        cow.setNoGravity(true);
        return cow;
    }
}
