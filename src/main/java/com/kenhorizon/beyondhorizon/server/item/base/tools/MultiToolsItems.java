package com.kenhorizon.beyondhorizon.server.item.base.tools;

import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.tags.BHBlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.ToolAction;

public class MultiToolsItems extends DiggerBaseItem {
    public MultiToolsItems(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, float attackRange, Properties properties, SkillBuilder skillBuilder) {
        super(materials, attackDamage, attackSpeed, attackRange, BHBlockTags.MINEABLE_WITH_MULTITOOLS, properties, skillBuilder);
    }

    public MultiToolsItems(MeleeWeaponMaterials materials, float[] stats, Properties properties, SkillBuilder skillBuilder) {
        super(materials, stats, BHBlockTags.MINEABLE_WITH_MULTITOOLS, properties, skillBuilder);
    }

    public MultiToolsItems(MeleeWeaponMaterials materials, float[] stats, Properties properties) {
        super(materials, stats, BHBlockTags.MINEABLE_WITH_MULTITOOLS, properties);
    }

    public MultiToolsItems(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, Properties properties, SkillBuilder skillBuilder) {
        super(materials, attackDamage, attackSpeed, BHBlockTags.MINEABLE_WITH_MULTITOOLS, properties, skillBuilder);
    }

    public MultiToolsItems(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, Properties properties) {
        super(materials, attackDamage, attackSpeed, BHBlockTags.MINEABLE_WITH_MULTITOOLS, properties);
    }

    private static InteractionResult onAxeItemUse(UseOnContext context) {
        return Items.DIAMOND_AXE.useOn(context);
    }

    private static InteractionResult onHoeItemUse(UseOnContext context) {
        return Items.DIAMOND_HOE.useOn(context);
    }

    private static InteractionResult onShovelItemUse(UseOnContext context) {
        return Items.DIAMOND_SHOVEL.useOn(context);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction action) {
        return new ItemStack(Items.DIAMOND_AXE).canPerformAction(action)
                || new ItemStack(Items.DIAMOND_HOE).canPerformAction(action)
                || new ItemStack(Items.DIAMOND_SHOVEL).canPerformAction(action)
                || new ItemStack(Items.DIAMOND_PICKAXE).canPerformAction(action);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = onAxeItemUse(context);
        if (result == InteractionResult.PASS) {
            if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
                result = onHoeItemUse(context);
                if (result == InteractionResult.PASS) {
                    result = onShovelItemUse(context);
                }
            } else {
                result = onShovelItemUse(context);
                if (result == InteractionResult.PASS) {
                    result = onHoeItemUse(context);
                }
            }
        }
        return result;
    }
}
