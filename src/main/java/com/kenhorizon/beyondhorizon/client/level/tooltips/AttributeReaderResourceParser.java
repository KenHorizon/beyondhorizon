package com.kenhorizon.beyondhorizon.client.level.tooltips;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class AttributeReaderResourceParser implements ResourceManagerReloadListener {
    public static final AttributeReaderResourceParser INSTANCE = new AttributeReaderResourceParser();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            AttributeReader.INSTANCE.clearCache();
            for (Resource resource : resourceManager.getResourceStack(BeyondHorizon.resource("attributes.json"))) {
                try (InputStream inputStream = resource.open()) {
                    JsonObject root = GsonHelper.parse(new InputStreamReader(inputStream), true);
                    if (root.has("attributes")) {
                        JsonArray blacklist = GsonHelper.getAsJsonArray(root, "attributes");
                        for (int i = 0; i < blacklist.size(); i++) {
                            JsonObject rootObject = GsonHelper.convertToJsonObject(blacklist.get(i), String.format("attributes[%d]", i));
                            List<String> attributePercentages = new ArrayList<String>();
                            List<String> blacklistAttribute = new ArrayList<String>();
                            for (JsonElement selectorElement : GsonHelper.getAsJsonArray(rootObject, "player_stats_blacklist_attributes")) {
                                blacklistAttribute.add(GsonHelper.convertToString(selectorElement, "player_stats_blacklist_attributes"));
                            }
                            for (JsonElement selectorElement : GsonHelper.getAsJsonArray(rootObject, "percentage_attributes")) {
                                attributePercentages.add(GsonHelper.convertToString(selectorElement, "percentage_attributes"));
                            }
                            AttributeReader.INSTANCE.addAttributePercentages(attributePercentages);
                            AttributeReader.INSTANCE.addPlayerStatusAttributeBlacklist(blacklistAttribute);
                        }
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
        } catch (Exception e) {
            BeyondHorizon.LOGGER.warn("An error occurred while parsing definitions data :\n {}", ExceptionUtils.getStackTrace(e));
        }
    }
}