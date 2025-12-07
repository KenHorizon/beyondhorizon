package com.kenhorizon.beyondhorizon.server.classes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRoleClass extends INBTSerializable<CompoundTag> {

    RoleClass getInstance();

}
