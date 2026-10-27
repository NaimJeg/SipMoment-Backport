package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.items.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SMItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SipMomentMod.MODID);

    public static final FoodProperties COMMON_WINE_FOOD = new FoodProperties.Builder().alwaysEdible().build();
    public static final Consumable COMMON_WINE_CONSUMABLE = Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false).build();

    public static final DeferredItem<Item> HIGHBALL;
    public static final DeferredItem<Item> KYOHO_WINE;
    public static final DeferredItem<Item> KYOHO_WINE_CUP;
    public static final DeferredItem<Item> BUTTER_BEER;
    public static final DeferredItem<Item> BLACK_STAR_COFFEE;
    public static final DeferredItem<Item> WHISKY;
    public static final DeferredItem<Item> DAVID_MARTINEZ;
    public static final DeferredItem<Item> CHAMPAGNE;
    public static final DeferredItem<Item> CHAMPAGNE_CUP;
    public static final DeferredItem<Item> ORANGE_JUICE ;
    public static final DeferredItem<Item> SHIPWRECK_VINTAGE ;
    public static final DeferredItem<Item> GOBLET;
    public static final DeferredItem<Item> CHAMPAGNE_GLASS;
    public static final DeferredItem<Item> BAR_TABLE;
    public static final DeferredItem<Item> BAR_CHAIR;

    public static final DeferredItem<Item> MUSIC_DISC_DUST_TO_DUST;


    static {
        HIGHBALL = wineItem(SMBlocks.HIGHBALL, 6,SMConsumables.HIGHBALL,true);
        KYOHO_WINE = ITEMS.register("kyoho_wine",()-> new BottledWineItem(SMBlocks.KYOHO_WINE.get(),new Item.Properties().setId(modItemId("kyoho_wine")).useBlockDescriptionPrefix(),SMItems.GOBLET,SMItems.KYOHO_WINE_CUP,6,16733525,0.0f));
        KYOHO_WINE_CUP = wineItem(SMBlocks.KYOHO_WINE_CUP,4,false);
        BUTTER_BEER = drinkItem(SMBlocks.BUTTER_BEER,6,SMConsumables.BUTTER_BEER,true);
        BLACK_STAR_COFFEE = drinkItem(SMBlocks.BLACK_STAR_COFFEE,4,true);
        WHISKY = wineItem(SMBlocks.WHISKY,4,false);
        DAVID_MARTINEZ = ITEMS.register("david_martinez",()-> new DavidMartinezItem(SMBlocks.DAVID_MARTINEZ.get(),new Item.Properties().food(COMMON_WINE_FOOD,SMConsumables.DAVID_MARTINEZ).stacksTo(1).setId(modItemId("david_martinez")).useBlockDescriptionPrefix(),4,true));
        CHAMPAGNE = ITEMS.register("champagne",()-> new BottledWineItem(SMBlocks.CHAMPAGNE.get(),new Item.Properties().setId(modItemId("champagne")).useBlockDescriptionPrefix(),SMItems.CHAMPAGNE_GLASS,SMItems.CHAMPAGNE_CUP,6,16755200,0.0f));
        CHAMPAGNE_CUP = wineItem(SMBlocks.CHAMPAGNE_CUP,4,false);
        ORANGE_JUICE = ITEMS.register("orange_juice",()-> new OrangeJuiceItem(SMBlocks.ORANGE_JUICE.get(),new Item.Properties().food(COMMON_WINE_FOOD,SMConsumables.ORANGE_JUICE).setId(modItemId("orange_juice")).useBlockDescriptionPrefix(),4,false,false));
        SHIPWRECK_VINTAGE = wineItem(SMBlocks.SHIPWRECK_VINTAGE,2,SMConsumables.SHIPWRECK_VINTAGE,true);
        GOBLET = ITEMS.register("goblet",()-> new PileItem(SMBlocks.GOBLET.get(),new Item.Properties().setId(modItemId("goblet")).useBlockDescriptionPrefix()));
        CHAMPAGNE_GLASS = ITEMS.register("champagne_glass",()-> new PileItem(SMBlocks.CHAMPAGNE_GLASS.get(),new Item.Properties().setId(modItemId("champagne_glass")).useBlockDescriptionPrefix()));
        BAR_TABLE = block(SMBlocks.BAR_TABLE);
        BAR_CHAIR = block(SMBlocks.BAR_CHAIR);
        MUSIC_DISC_DUST_TO_DUST = ITEMS.register("music_disc_dust_to_dust",MusicDiscDustToDustItem::new);

    }

    private static DeferredItem<Item> wineItem(Holder<Block> block, int eatCount,Consumable consumable,boolean customField){
        Identifier identifier = block.unwrapKey().get().identifier();
        return ITEMS.register(identifier.getPath(),()-> new WineItem(block.value(),new Item.Properties().food(COMMON_WINE_FOOD,consumable).stacksTo(1).useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM,identifier)),eatCount,customField));
    }

    private static DeferredItem<Item> wineItem(Holder<Block> block,int eatCount,boolean customField){
        Identifier identifier = block.unwrapKey().get().identifier();
        return ITEMS.register(identifier.getPath(),()-> new WineItem(block.value(),new Item.Properties().food(COMMON_WINE_FOOD,COMMON_WINE_CONSUMABLE).stacksTo(1).useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM,identifier)),eatCount,customField));
    }

    private static DeferredItem<Item> drinkItem(Holder<Block> block, int eatCount,Consumable consumable,boolean customField){
        Identifier identifier = block.unwrapKey().get().identifier();
        return ITEMS.register(identifier.getPath(),()-> new DrinkItem(block.value(),new Item.Properties().food(COMMON_WINE_FOOD,consumable).stacksTo(1).useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM,identifier)),eatCount,customField,false));
    }

    private static DeferredItem<Item> drinkItem(Holder<Block> block,int eatCount,boolean customField){
        Identifier identifier = block.unwrapKey().get().identifier();
        return ITEMS.register(identifier.getPath(),()-> new DrinkItem(block.value(),new Item.Properties().food(COMMON_WINE_FOOD,COMMON_WINE_CONSUMABLE).stacksTo(1).useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM,identifier)),eatCount,customField,false));
    }

    public static DeferredItem<Item> block(Holder<Block> block){
        Identifier identifier = block.unwrapKey().get().identifier();
        return ITEMS.register(identifier.getPath(),()-> new BlockItem(block.value(),new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM,identifier))));
    }

    public static ResourceKey<Item> modItemId(String name){
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SipMomentMod.MODID,name));
    }
}
