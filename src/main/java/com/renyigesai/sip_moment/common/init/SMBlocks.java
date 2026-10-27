package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.blocks.BarTableBlock;
import com.renyigesai.sip_moment.common.blocks.CupBlock;
import com.renyigesai.sip_moment.common.blocks.WineBlock;
import com.renyigesai.sip_moment.common.blocks.mix_block.BeverageDisplayBlock;
import com.renyigesai.sip_moment.common.blocks.mix_block.BeverageDisplayBlockEntity;
import com.renyigesai.sip_moment.common.blocks.sofa.BarChairBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SMBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SipMomentMod.MODID);

    public static final DeferredBlock<BeverageDisplayBlock> BEVERAGE_DISPLAY_BLOCK;
    public static final DeferredBlock<Block> HIGHBALL;
    public static final DeferredBlock<Block> KYOHO_WINE;
    public static final DeferredBlock<Block> KYOHO_WINE_CUP;
    public static final DeferredBlock<Block> BUTTER_BEER;
    public static final DeferredBlock<Block> BLACK_STAR_COFFEE;
    public static final DeferredBlock<Block> WHISKY;
    public static final DeferredBlock<Block> DAVID_MARTINEZ;
    public static final DeferredBlock<Block> CHAMPAGNE;
    public static final DeferredBlock<Block> CHAMPAGNE_CUP;
    public static final DeferredBlock<Block> ORANGE_JUICE;
    public static final DeferredBlock<Block> SHIPWRECK_VINTAGE;
    public static final DeferredBlock<Block> GOBLET;
    public static final DeferredBlock<Block> CHAMPAGNE_GLASS;
    public static final DeferredBlock<Block> BAR_TABLE;
    public static final DeferredBlock<Block> BAR_CHAIR;

    static {
        BEVERAGE_DISPLAY_BLOCK = BLOCKS.register("beverage_display_block", BeverageDisplayBlock::new);

        HIGHBALL = wineBlock("highball",4);

        KYOHO_WINE = wineBlock("kyoho_wine",2);

        KYOHO_WINE_CUP = wineBlock("kyoho_wine_cup",4);

        BUTTER_BEER = wineBlock("butter_beer",4);

        BLACK_STAR_COFFEE = wineBlock("black_star_coffee",2);

        WHISKY = wineBlock("whisky",4);

        DAVID_MARTINEZ = wineBlock("david_martinez",4);

        CHAMPAGNE = wineBlock("champagne",2);

        CHAMPAGNE_CUP = wineBlock("champagne_cup",4);

        ORANGE_JUICE = wineBlock("orange_juice",2);

        SHIPWRECK_VINTAGE = wineBlock("shipwreck_vintage",2);

        GOBLET = BLOCKS.register("goblet",()-> new CupBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).setId(modBlockId("goblet")),4));

        CHAMPAGNE_GLASS = BLOCKS.register("champagne_glass",()-> new CupBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).setId(modBlockId("champagne_glass")),4));

        BAR_TABLE = BLOCKS.register("bar_table",BarTableBlock::new);

        BAR_CHAIR = BLOCKS.register("bar_chair",()-> new BarChairBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).setId(modBlockId("bar_chair"))));
    }

    private static DeferredBlock<Block> wineBlock(String name,int maxPile){
        return BLOCKS.register(name,()-> new WineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).setId(modBlockId(name)),maxPile));
    }

    public static ResourceKey<Block> modBlockId(String name){
        return ResourceKey.create(Registries.BLOCK,Identifier.fromNamespaceAndPath(SipMomentMod.MODID,name));
    }

    public static class Entitys{
        public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SipMomentMod.MODID);
        public static final Supplier<BlockEntityType<BeverageDisplayBlockEntity>> BEVERAGE_DISPLAY_BLOCK_ENTITY = REGISTER.register("beverage_display_block", () -> new BlockEntityType<>(BeverageDisplayBlockEntity::new, BEVERAGE_DISPLAY_BLOCK.get()));
    }
}
