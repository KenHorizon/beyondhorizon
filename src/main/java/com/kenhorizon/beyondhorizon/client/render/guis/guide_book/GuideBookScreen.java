package com.kenhorizon.beyondhorizon.client.render.guis.guide_book;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.Fonts;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.item.GuideBookItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GuideBookScreen extends Screen {
    public enum Pages {
        INTRODUCTION(0),
        DAMAGE_TYPES(0),
        EFFECT_TYPES(1),
        GAME_MECHANICS(0),
        ACCESSORY(1),
        LEVEL_SYSTEM(1),
        DIFFICULTY(1),
        STATS(2);

        public int pages;
        public static final ImmutableList<Pages> ALL_PAGES = ImmutableList.copyOf(Pages.values());
        public static final ImmutableList<Integer> ALL_INDEXES = ImmutableList
                .copyOf(IntStream.range(0, Pages.values().length).iterator());

        Pages(int pages) {
            this.pages = pages;
        }
        public static Set<Pages> containedPages(Collection<Integer> pages) {
            return pages.stream().map(ALL_PAGES::get).collect(Collectors.toSet());
        }
        public static boolean hasAllPages(ItemStack book) {
            return Ints.asList(book.getTag().getIntArray("Pages")).containsAll(ALL_INDEXES);
        }

        public static List<Integer> enumToInt(List<Pages> pages) {
            return pages.stream().map(Pages::ordinal).collect(Collectors.toList());
        }

        public static Pages getRand() {
            return Pages.values()[ThreadLocalRandom.current().nextInt(Pages.values().length)];

        }

        public static void addRandomPage(ItemStack book) {
            if (book.getItem() instanceof GuideBookItem) {
                List<Pages> list = Pages.possiblePages(book);
                if (!list.isEmpty()) {
                    addPage(list.get(ThreadLocalRandom.current().nextInt(list.size())), book);
                }
            }
        }

        public static List<Pages> possiblePages(ItemStack book) {
            if (book.getItem() instanceof GuideBookItem) {
                CompoundTag tag = book.getTag();
                Collection<Pages> containedPages = containedPages(Ints.asList(tag.getIntArray("Pages")));
                List<Pages> possiblePages = new ArrayList<>(ALL_PAGES);
                possiblePages.removeAll(containedPages);
                return possiblePages;
            }
            return Collections.emptyList();
        }
        public static boolean addPage(Pages page, ItemStack book) {
            boolean flag = false;
            if (book.getItem() instanceof GuideBookItem) {
                CompoundTag tag = book.getTag();
                final List<Integer> already = new ArrayList<>(Ints.asList(tag.getIntArray("Pages")));
                if (!already.contains(page.ordinal())) {
                    already.add(page.ordinal());
                    flag = true;
                }
                tag.putIntArray("Pages", Ints.toArray(already));
            }
            return flag;
        }
    }

    protected static final int X = 390;
    protected static final int Y = 245;
    private static final ResourceLocation LOCATION = BeyondHorizon.resourceGui("guide_book/guidebook.png");
    private static final ResourceLocation DRAW0 = BeyondHorizon.resourceGui("guide_book/drawings_0.png");
    public List<Pages> allPageTypes = new ArrayList<>();
    public Pages pageType;
    public List<GuideBookIndexButton> indexButtons = new ArrayList<>();
    public GuideBookChangePageButton previousPage;
    public GuideBookChangePageButton nextPage;
    private static final Map<String, ResourceLocation> PICTURE_LOCATION_CACHE = Maps.newHashMap();
    public int bookPages;
    public int bookPagesTotal = 1;
    public int indexPages;
    public int indexPagesTotal = 1;
    protected ItemStack itemStack;
    protected boolean index;
    protected Font font = getFont();

    public GuideBookScreen(ItemStack itemStack) {
        super(Component.empty());
        this.itemStack = itemStack;
        if (!itemStack.isEmpty() && itemStack.getItem() != null && itemStack.getItem() == BHItems.GUIDE_BOOK.get()) {
            if (itemStack.getTag() != null) {
                Set<Pages> pages = Pages.containedPages(Ints.asList(itemStack.getTag().getIntArray("Pages")));
                allPageTypes.addAll(pages);
                // Make sure the pages are sorted according to the enum
                allPageTypes.sort(Comparator.comparingInt(Enum::ordinal));
                indexPagesTotal = (int) Math.ceil(pages.size() / 10D);
            }
        }
        this.index = true;
    }
    private static Font getFont() {
        if (!Minecraft.getInstance().options.languageCode.equalsIgnoreCase("en_us")) {
            BeyondHorizon.LOGGER.debug("Using vanilla fonts");
            return Minecraft.getInstance().font;
        } else {
            BeyondHorizon.LOGGER.debug("Using custom fonts");
            return (Font) BeyondHorizon.PROXY.getFontRenderer();
        }
    }

    private static Item getItemByRegistryName(String registryName) {
        return ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(registryName));
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        this.indexButtons.clear();
        int centerX = (width - X) / 2;
        int centerY = (height - Y) / 2;
        this.previousPage = new GuideBookChangePageButton(centerX + 15, centerY + 215, false, 0, (p_214132_1_) -> {
            if ((this.index ? this.indexPages > 0 : this.pageType != null)) {
                if (this.index) {
                    this.indexPages--;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                } else {
                    if (this.bookPages > 0) {
                        this.bookPages--;
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                    } else {
                        this.index = true;
                    }
                }
            }
        });
        this.addRenderableWidget(previousPage);
        this.nextPage = new GuideBookChangePageButton(centerX + 357, centerY + 215, true, 0, (p_214132_1_) -> {
            if ((this.index ? this.indexPages < this.indexPagesTotal - 1 : this.pageType != null && this.bookPages < this.pageType.pages)) {
                if (this.index) {
                    this.indexPages++;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                } else {
                    this.bookPages++;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                }
            }
        });
        this.addRenderableWidget(this.nextPage);
        if (!allPageTypes.isEmpty()) {
            for (int i = 0; i < allPageTypes.size(); i++) {
                int xIndex = i % -2;
                int yIndex = i % 10;
                int id = 2 + i;
                GuideBookIndexButton button = new GuideBookIndexButton(centerX + 15 + (xIndex * 200),
                        centerY + 10 + (yIndex * 20) - (xIndex == 1 ? 20 : 0),
                        Component.translatable("guidebooks."
                                + Pages.values()[allPageTypes.get(i).ordinal()].toString().toLowerCase()),
                        (btns) -> {
                            if (this.indexButtons.get(id - 2) != null && allPageTypes.get(id - 2) != null) {
                                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                                this.index = false;
                                this.bookPages = 0;
                                this.pageType = allPageTypes.get(id - 2);
                            }
                        });
                this.indexButtons.add(button);
                this.addRenderableWidget(button);
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mx, int my, float partialTick) {
        this.renderBackground(guiGraphics);
        for (Renderable widget : this.renderables) {
            if (widget instanceof GuideBookIndexButton button) {
                button.active = index;
                button.visible = index;
            }

        }
        for (int i = 0; i < this.indexButtons.size(); i++) {
            this.indexButtons.get(i).active = i < 10 * (this.indexPages + 1) && i >= 10 * (this.indexPages) && this.index;
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int cornerX = (width - X) / 2;
        int cornerY = (height - Y) / 2;
        guiGraphics.blit(LOCATION, cornerX, cornerY, 0, 0, X, Y, 390, 390);
        RenderSystem.disableDepthTest();
        super.render(guiGraphics, mx, my, partialTick);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(cornerX, cornerY, 0.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (!index) {
            drawPerPage(guiGraphics, bookPages);
        }
        guiGraphics.pose().popPose();
        this.renderables.forEach((widget -> widget.render(guiGraphics, mx, my, partialTick)));
        RenderSystem.enableDepthTest();
    }
    public void drawPerPage(GuiGraphics grap, int bookPages) {
//        imageFromTxt(grap);
        switch (this.pageType) {
            case INTRODUCTION:
                if (bookPages == 0) {
//                    drawItemStack(grap, new ItemStack(BHItems.GUIDE_BOOK.get()), 8, 20, 1.0F);
                    drawImage(grap, DRAW0, 12, 22, 0, 0, 168, 40, 512F);
                    drawImage(grap, DRAW0, 12, 22 + 42, 0, 40, 168, 168, 512F);
                }
            case LEVEL_SYSTEM:
                if (bookPages == 0) {
                    drawImage(grap, DRAW0, 30, 256, 168, 0, 143, 82, 256F);
                }
            case DAMAGE_TYPES:
                break;
            case STATS:
                break;
            case ACCESSORY:
                if (bookPages == 0) {
                    drawItemStack(grap, new ItemStack(BHItems.POWER_GLOVES.get()), 8, 20, 1.0F);
                }
            default:
                break;
        }
        writeFromTxt(grap);
    }
    public void writeFromTxt(GuiGraphics guiGraphics) {
        String fileName = this.pageType.toString().toLowerCase(Locale.ROOT) + "_" + this.bookPages + ".txt";
        String languageName = Minecraft.getInstance().options.languageCode.toLowerCase(Locale.ROOT);
        ResourceLocation fileLoc = BeyondHorizon.resource("lang/guidebooks/" + languageName + "_0/" + fileName);
        ResourceLocation backupLoc = BeyondHorizon.resource("lang/guidebooks/en_us_0/" + fileName);
        Optional<Resource> resource;

        resource = Minecraft.getInstance().getResourceManager().getResource(fileLoc);
        if (resource.isEmpty()) {
            resource = Minecraft.getInstance().getResourceManager().getResource(backupLoc);
        }
        try {
            final List<String> lines = IOUtils.readLines(resource.get().open(), "UTF-8");
            int linenumber = 0;
            for (String line : lines) {
                line = line.trim();
                if (line.contains("<") || line.contains(">")) {
                    continue;
                }
                guiGraphics.pose().pushPose();
                if (this.usingVanillaFont()) {
                    guiGraphics.pose().scale(0.945F, 0.945F, 0.945F);
                    guiGraphics.pose().translate(0, 5.5F, 0);
                }
                if (linenumber <= 19) {
                    font.drawInBatch(line, 15, 20 + linenumber * 10, 0X303030, false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
                } else {
                    font.drawInBatch(line, 220, (linenumber - 19) * 10, 0X303030, false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
                }
                linenumber++;
                guiGraphics.pose().popPose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        guiGraphics.pose().pushPose();
        Component title = Component.translatable("guidebooks." + this.pageType.toString().toLowerCase(Locale.ROOT));
        float scale = font.width(title) <= 100 ? 2 : font.width(title) * 0.0125F;
        guiGraphics.pose().scale(scale, scale, scale);
        font.drawInBatch8xOutline(title.getVisualOrderText(), 10, 2, 0XFFE7BF, 0XAA977F, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), 15728880);
        guiGraphics.pose().popPose();
    }
    public void imageFromTxt(GuiGraphics ms) {
        String fileName = this.pageType.toString().toLowerCase(Locale.ROOT) + "_" + this.bookPages + ".txt";
        String languageName = Minecraft.getInstance().options.languageCode.toLowerCase(Locale.ROOT);
        ResourceLocation fileLoc = BeyondHorizon.resource("lang/guidebooks/" + languageName + "_0/" + fileName);
        ResourceLocation backupLoc = BeyondHorizon.resource("lang/guidebooks/en_us_0/" + fileName);
        Optional<Resource> resource;

        resource = Minecraft.getInstance().getResourceManager().getResource(fileLoc);
        if (resource.isEmpty()) {
            resource = Minecraft.getInstance().getResourceManager().getResource(backupLoc);
        }
        try {
            if (resource.isPresent()) {
                final List<String> lines = IOUtils.readLines(resource.get().open(), StandardCharsets.UTF_8);
                int zLevelAdd = 0;
                for (String line : lines) {
                    line = line.trim();
                    if (line.contains("<") || line.contains(">")) {
                        if (line.contains("<image>")) {
                            line = line.substring(8, line.length() - 1);
                            String[] split = line.split(" ");
                            String texture = "guidebooks/" + split[0];
                            ResourceLocation resourcelocation = PICTURE_LOCATION_CACHE.get(texture);
                            if (resourcelocation == null) {
                                resourcelocation = BeyondHorizon.resource(texture);
                                PICTURE_LOCATION_CACHE.put(texture, resourcelocation);
                            }
                            ms.pose().pushPose();
                            drawImage(ms, resourcelocation, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]), Float.parseFloat(split[7]) * 512F);
                            ms.pose().popPose();
                        }
                    }
                    if (line.contains("<item>")) {
                        line = line.substring(7, line.length() - 1);
                        String[] split = line.split(" ");
                        RenderSystem.enableDepthTest();
                        drawItemStack(ms, new ItemStack(getItemByRegistryName(split[0]), 1), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Float.parseFloat(split[4]) * 2F);
                    }
                    if (line.contains("<block>")) {
                        zLevelAdd += 1;
                        line = line.substring(8, line.length() - 1);
                        String[] split = line.split(" ");
                        RenderSystem.enableDepthTest();
                        drawBlockStack(ms, new ItemStack(getItemByRegistryName(split[0]), 1), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Float.parseFloat(split[4]) * 2F, zLevelAdd);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void drawImage(GuiGraphics ms, ResourceLocation texture, int x, int y, int u, int v, int width, int height, float scale) {
        ms.pose().pushPose();
        RenderSystem.setShaderTexture(0, texture);
        ms.pose().scale(scale / 512F, scale / 512F, scale / 512F);
        ms.blit(texture, x, y, u, v, width, height, 512, 512);
        ms.pose().popPose();
    }

    private void drawItemStack(GuiGraphics ms, ItemStack stack, int x, int y, float scale) {
        ms.pose().pushPose();
        ms.pose().scale(scale, scale, scale);
        ms.renderItem(stack, x, y);
        ms.pose().popPose();
    }
    private void drawBlockStack(GuiGraphics ms, ItemStack stack, int x, int y, float scale, int zScale) {
        ms.pose().pushPose();
        ms.pose().scale(scale, scale, scale);
        ms.pose().translate(0, 0, zScale * 10);
        ms.renderItem(stack, x, y);
        ms.pose().popPose();
    }
    private boolean usingVanillaFont() {
        return this.font == Minecraft.getInstance().font;
    }
}
