package com.kenhorizon.beyondhorizon.client.level.tooltips;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class AttributeReader {
    public static final AttributeReader INSTANCE = new AttributeReader();

    private static final Map<Attribute, Set<String>> attributePercentages = new LinkedHashMap<>();
    private static final HashSet<Attribute> attributePercentageCache = new HashSet<>();

    private static final Map<Attribute, Set<String>> playerStatusAttributeblacklist = new LinkedHashMap<>();
    private static final HashSet<Attribute> playerStatusAttributeblacklistCache = new HashSet<>();

    public void addPlayerStatusAttributeBlacklist(List<String> blacklist) {
        for (Attribute attribute : ForgeRegistries.ATTRIBUTES) {
            for (String entry : blacklist) {
                if (this.isAttributeMatches(attribute, entry)) {
                    Set<String> blacklistSet = new LinkedHashSet<>();
                    if (playerStatusAttributeblacklist.containsKey(attribute)) {
                        blacklistSet.addAll(playerStatusAttributeblacklist.get(attribute));
                    }
                    blacklistSet.addAll(blacklist);
                    playerStatusAttributeblacklist.put(attribute, blacklistSet);
                }
            }
        }
    }

    public void addAttributePercentages(List<String> attributePercentageList) {
        for (Attribute attribute : ForgeRegistries.ATTRIBUTES) {
            for (String entry : attributePercentageList) {
                if (this.isAttributeMatches(attribute, entry)) {
                    Set<String> attributePercentageSet = new LinkedHashSet<>();
                    if (attributePercentages.containsKey(attribute)) {
                        attributePercentageSet.addAll(attributePercentages.get(attribute));
                    }
                    attributePercentageSet.addAll(attributePercentageList);
                    attributePercentages.put(attribute, attributePercentageSet);
                }
            }
        }
    }

    public boolean getAttributePercentages(Attribute attribute) {
        if (attributePercentages.containsKey(attribute)) {
            List<Attribute> sortedDefinitions = attributePercentages.keySet().stream().toList();
            for (Attribute attributes : sortedDefinitions) {
                for (String entry : attributePercentages.get(attributes)) {
                    if (this.isAttributeMatches(attributes, entry)) {
                        attributePercentageCache.add(attributes);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isAttributeMatches(Attribute attribute, String entry) {
        final ResourceLocation id = ForgeRegistries.ATTRIBUTES.getKey(attribute);
        return id.toString().equals(entry);
    }

    public boolean getBlacklistAttribute(Attribute attribute) {
        if (playerStatusAttributeblacklist.containsKey(attribute)) {
            List<Attribute> sortedDefinitions = playerStatusAttributeblacklist.keySet().stream().toList();
            for (Attribute attributes : sortedDefinitions) {
                for (String entry : playerStatusAttributeblacklist.get(attributes)) {
                    if (this.isAttributeMatches(attributes, entry)) {
                        playerStatusAttributeblacklistCache.add(attributes);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static void reset() {
        playerStatusAttributeblacklistCache.clear();
        attributePercentageCache.clear();
    }

    public boolean isBlacklistAttributePresent(Attribute attribute) {
        playerStatusAttributeblacklistCache.add(attribute);
        return !playerStatusAttributeblacklist.containsKey(attribute);
    }

    public void clearCache() {
        for (Attribute attributes : ForgeRegistries.ATTRIBUTES) {
            attributePercentages.entrySet().removeIf(entry -> entry.getValue() == attributes);
            playerStatusAttributeblacklist.entrySet().removeIf(entry -> entry.getValue() == attributes);
        }
    }
}