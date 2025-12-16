package com.kenhorizon.beyondhorizon.server.item.base;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryBuilder;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryItemGroup;

public class BasicAccessoryItem extends AccessoryItem {
    public BasicAccessoryItem(Properties properties, AccessoryBuilder builder) {
        super(AccessoryItemGroup.NONE, properties, builder);
    }
}
