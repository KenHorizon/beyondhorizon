package com.kenhorizon.beyondhorizon.server.item.base.tools;

import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class PickaxeBaseItem extends DiggerBaseItem {
    public PickaxeBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, float attackRange, Properties properties, SkillBuilder skillBuilder) {
        super(materials, attackDamage, attackSpeed, attackRange, BlockTags.MINEABLE_WITH_PICKAXE, properties, skillBuilder);
    }

    public PickaxeBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, Properties properties, SkillBuilder skillBuilder) {
        super(materials, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_PICKAXE, properties, skillBuilder);
    }

    public PickaxeBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, Properties properties) {
        super(materials, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }
}
