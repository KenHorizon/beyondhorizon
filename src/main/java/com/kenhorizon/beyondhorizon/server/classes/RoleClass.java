package com.kenhorizon.beyondhorizon.server.classes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.accessory.Accessories;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

public class RoleClass {
    @Nullable
    public String descriptionId;
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
    protected String MODID = RoleClasses.REGISTRY.getRegistryName().getNamespace();
    public static final String CLASSES_TAGS = "role_class";
    public static final String ATTRIBUTES_TAGS = "attribute_modifiers";
    public RoleClassTypes roleClassTypes;
    public RoleClass(RoleClassTypes types) {
        this.roleClassTypes = types;
    }

    public String getName() {
        return RoleClasses.SUPPLIER_KEY.get().getKey(this).getPath();
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public String getId() {
        return RoleClasses.SUPPLIER_KEY.get().getKey(this).getNamespace();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = String.format("skills.%s.%s", this.getId(), this.getName());
        }
        return this.descriptionId;
    }
}
