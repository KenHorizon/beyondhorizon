package com.kenhorizon.libs.client.data;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.datagen.BHBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public abstract class BlockStateBuilder extends BlockStateProvider {
    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

    public enum ModelPath {
        ITEM("item"),
        BLOCK("block");
        private final String modelPath;
        private ModelPath(String modelPath) {
            this. modelPath = modelPath;
        }

        public String getModelPath() {
            return modelPath;
        }
    }

    public BlockStateBuilder(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, BeyondHorizon.ID, existingFileHelper);
    }

    protected void builtinEntity(RegistryObject<Block> b, String particle) {
        ModelFile models = models()
                .getBuilder(name(b.get()))
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .texture("particle", particle);
        simpleBlockWithItem(b.get(), models);
    }

    protected void builtinEntity(RegistryObject<Block> b, RegistryObject<Block> particle) {
        this.builtinEntity(b, blockTexture(particle.get()).getPath());
    }

    public void doorBlocks(RegistryObject<Block> block) {
        blockGeneratedItem(block, ModelPath.ITEM);
        doorBlockWithRenderType((DoorBlock) block.get(), BeyondHorizon.resource("block/" + name(block.get()) + "_bottom"), BeyondHorizon.resource("block/" + name(block.get()) + "_top"), "cutout");
    }
    public void trapdoorBlocks(RegistryObject<Block> block) {
        blockItem(block, "bottom");
        trapdoorBlockWithRenderType((TrapDoorBlock) block.get(), BeyondHorizon.resource("block/" + name(block.get())), true, "cutout");
    }
    public void paneBlocks(RegistryObject<Block> block) {
        paneBlockInternals(block.get(), name(block.get()));
        blockItem(block, "inventory");
    }
    protected void paneBlockInternals(Block block, String baseName) {
        ResourceLocation main = BeyondHorizon.resource("block/" + name(block));
        ModelFile post = models().panePost(baseName + "_post", main, main);
        ModelFile side = models().paneSide(baseName + "_side", main, main);
        ModelFile sideAlt = models().paneSideAlt(baseName + "_side_alt", main, main);
        ModelFile noSide = models().paneNoSide(baseName + "_noside", main);
        ModelFile noSideAlt = models().paneNoSideAlt(baseName + "_noside_alt", main);;
        paneBlocks(block, post, side, sideAlt, noSide, noSideAlt);
    }
    public void paneBlocks(Block block, ModelFile post, ModelFile side, ModelFile sideAlt, ModelFile noSide, ModelFile noSideAlt) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block)
                .part().modelFile(post).addModel().end();
        PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach(e -> {
            Direction dir = e.getKey();
            if (dir.getAxis().isHorizontal()) {
                boolean alt = dir == Direction.SOUTH;
                builder.part().modelFile(alt || dir == Direction.WEST ? sideAlt : side).rotationY(dir.getAxis() == Direction.Axis.X ? 90 : 0).addModel()
                        .condition(e.getValue(), true).end()
                        .part().modelFile(alt || dir == Direction.EAST ? noSideAlt : noSide).rotationY(dir == Direction.WEST ? 270 : dir == Direction.SOUTH ? 90 : 0).addModel()
                        .condition(e.getValue(), false);
            }
        });
    }

    public void fenceGates(RegistryObject<Block> block, RegistryObject<Block> texture) {
        fenceGateBlock((FenceGateBlock) block.get(), blockTexture(texture.get()));
        ModelFile model = models().withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_" + "inventory", ResourceLocation.parse("template_fence_gate")).texture("texture", blockTexture(texture.get()));
        simpleBlockItem(block.get(), model);
        blockItem(block, "inventory");
    }

    public void wallBlocks(RegistryObject<Block> block, Block texture) {
        wallBlock((WallBlock) block.get(), blockTexture(texture));
        ModelFile model = models().withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_" + "inventory", ResourceLocation.parse("wall_inventory")).texture("wall", blockTexture(texture));
        simpleBlockItem(block.get(), model);
        blockItem(block, "inventory");
    }

    public void wallBlocks(RegistryObject<Block> block, RegistryObject<Block> texture) {
        wallBlock((WallBlock) block.get(), blockTexture(texture.get()));
        ModelFile model = models().withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_" + "inventory", ResourceLocation.parse("wall_inventory")).texture("wall", blockTexture(texture.get()));
        simpleBlockItem(block.get(), model);
        blockItem(block, "inventory");
    }

    public void logBlocks(RegistryObject<Block> block) {
        ResourceLocation textureSide = BeyondHorizon.resource("block/" + name(block.get()));
        ResourceLocation textureTop = BeyondHorizon.resource("block/" + name(block.get()) + "_top");
        axisBlock((RotatedPillarBlock) block.get(), textureSide, textureTop);
        simpleBlockItem(block.get(), models().cubeColumn(name(block.get()), textureSide, textureTop));
    }
    public void axisBlocks(RegistryObject<Block> block, RegistryObject<Block> side, RegistryObject<Block> top) {
        ResourceLocation textureSide = BeyondHorizon.resource("block/" + name(side.get()));
        ResourceLocation textureTop = BeyondHorizon.resource("block/" + name(top.get()));
        axisBlock((RotatedPillarBlock) block.get(), textureSide, textureTop);
        simpleBlockItem(block.get(), models().cubeColumn(name(block.get()), textureSide, textureTop));
    }
    public void axisBlocks(RegistryObject<Block> block, RegistryObject<Block> textures) {
        ResourceLocation texture = BeyondHorizon.resource("block/" + name(textures.get()));
        axisBlock((RotatedPillarBlock) block.get(), texture, texture);
        simpleBlockItem(block.get(), models().cubeAll(name(block.get()), texture));
    }
    public void axisBlocks(RegistryObject<Block> block, boolean oneTexture) {
        ResourceLocation side = BeyondHorizon.resource("block/" + name(block.get()));
        ResourceLocation top = BeyondHorizon.resource("block/" + name(block.get()) + "_top");
        axisBlock((RotatedPillarBlock) block.get(), side, oneTexture ? side : top);
        simpleBlockItem(block.get(), models().cubeColumn(name(block.get()), side, top));
    }
    public void axisBlocks(RegistryObject<Block> block) {
        axisBlocks(block, false);
    }

    public void directionalBlock(RegistryObject<Block> block) {
        ResourceLocation face = BeyondHorizon.resource(String.format("block/%s", name(block.get())));
        ResourceLocation side = BeyondHorizon.resource(String.format("block/%s_%s", name(block.get()), "side"));
        directionalBlock(block.get(), side, face, side, side);
        blockItem(block);
    }
    public void directionalBlock(Block block, ResourceLocation side, ResourceLocation front, ResourceLocation top, ResourceLocation bottom) {
        directionalBlock(block, models().orientableWithBottom(name(block), side, front, top, bottom));
    }
    public void directionalBlock(Block block, ModelFile model) {
        directionalBlock(block, model, 180);
    }

    public void directionalBlock(Block block, ModelFile model, int angleOffset) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360)
                            .build();
                });
    }

    public void furnaceBlock(RegistryObject<Block> block) {
        ResourceLocation front = BeyondHorizon.resource(String.format("block/%s_%s", name(block.get()), "front"));
        ResourceLocation frontOn = BeyondHorizon.resource(String.format("block/%s_%s_on", name(block.get()), "front"));
        ResourceLocation side = BeyondHorizon.resource(String.format("block/%s_%s", name(block.get()), "side"));
        ResourceLocation top = BeyondHorizon.resource(String.format("block/%s_%s", name(block.get()), "top"));
        ResourceLocation bottom = BeyondHorizon.resource(String.format("block/%s_%s", name(block.get()), "bottom"));
        furnaceBlock(block.get(), side, frontOn, front, top, bottom);
        blockItem(block);
    }
    public void furnaceBlock(Block block, ResourceLocation side, ResourceLocation frontOn, ResourceLocation front, ResourceLocation top, ResourceLocation bottom) {
        furnaceBlock(block, models().orientableWithBottom(name(block), side, front, top, bottom), models().orientableWithBottom(name(block) + "_on", side, frontOn, top, bottom));
    }
    public void furnaceBlock(Block block, ModelFile unlit, ModelFile lit) {
        furnaceBlock(block, unlit, lit, 180);
    }

    public void furnaceBlock(Block block, ModelFile unlit, ModelFile lit, int angleOffset) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    boolean isLit = state.getValue(BlockStateProperties.LIT);
                    return ConfiguredModel.builder()
                            .modelFile(isLit ? lit : unlit)
                            .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360)
                            .build();
                });
    }

    public void pressurePlate(RegistryObject<Block> block, RegistryObject<Block> texture) {
        pressurePlateBlocks((BasePressurePlateBlock) block.get(), blockTexture(texture.get()));
        blockItem(block);
    }
    public void pressurePlate(RegistryObject<Block> block) {
        pressurePlateBlocks((BasePressurePlateBlock) block.get(), blockTexture(block.get()));
        blockItem(block);
    }
    public void pressurePlateBlocks(BasePressurePlateBlock block, ResourceLocation texture) {
        ModelFile pressurePlate = models().pressurePlate(name(block), texture);
        ModelFile pressurePlateDown = models().pressurePlateDown(name(block) + "_down", texture);
        pressurePlateBlocks(block, pressurePlate, pressurePlateDown);
    }

    public void pressurePlateBlocks(BasePressurePlateBlock block, ModelFile pressurePlate, ModelFile pressurePlateDown) {
        getVariantBuilder(block)
                .partialState().with(PressurePlateBlock.POWERED, true).addModels(new ConfiguredModel(pressurePlateDown))
                .partialState().with(PressurePlateBlock.POWERED, false).addModels(new ConfiguredModel(pressurePlate));
    }
    public void stairsBlocks(RegistryObject<Block> block, RegistryObject<Block> texture) {
        stairsBlock((StairBlock) block.get(), blockTexture(texture.get()));
        blockItem(block);
    }

    public void slabBlocks(RegistryObject<Block> block, RegistryObject<Block> texture) {
        slabBlock((SlabBlock) block.get(), blockTexture(texture.get()), blockTexture(texture.get()));
        blockItem(block);
    }

    public void slabBlocks(RegistryObject<Block> block, RegistryObject<Block> doubleBlock, RegistryObject<Block> texture) {
        slabBlock((SlabBlock) block.get(), blockTexture(doubleBlock.get()), blockTexture(texture.get()));
        blockItem(block);
    }
    public void horizontalActiveBlocks(RegistryObject<Block> block, ResourceLocation side) {
        horizontalActiveBlock(block, side, BeyondHorizon.resource(String.format("block/%s", name(block.get()) + "_front")), side, BeyondHorizon.resource(String.format("block/%s", name(block.get()) + "_front_active")));
        blockItem(block);
    }
    public void horizontalBlocks(RegistryObject<Block> block, ResourceLocation side) {
        horizontalBlock(block.get(), side, BeyondHorizon.resource(String.format("block/%s", name(block.get()) + "_front")), side);
        blockItem(block);
    }

    public void horizontalActiveBlock(RegistryObject<Block> block, ResourceLocation side, ResourceLocation front, ResourceLocation top, ResourceLocation active) {
        horizontalActiveBlock(block, models().orientable(name(block.get()), side, front, top), models().orientable(name(block.get()) + "_active", side, active, top));
    }
    public void horizontalActiveBlock(RegistryObject<Block> block, ModelFile idle, ModelFile active) {
        horizontalActiveBlock(block, active, idle, 180);
    }

    public void horizontalActiveBlock(RegistryObject<Block> block, ModelFile active, ModelFile noActive, int angleOffset) {
        getVariantBuilder(block.get())
                .forAllStates(blockState -> {
                    int yRot = ((int) blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360;
                    boolean isLit = blockState.getValue(BlockStateProperties.LIT);
                    return ConfiguredModel.builder()
                            .rotationY(yRot)
                            .modelFile(isLit ? active : noActive)
                            .build();
                });
    }

    protected void flowerItem(RegistryObject<Block> flowerBlocks) {
        blockGeneratedItem(flowerBlocks);
        ModelFile flowerBlock = models().cross(blockTexture(flowerBlocks.get()).getPath(), blockTexture(flowerBlocks.get())).renderType("cutout");
        simpleBlock(flowerBlocks.get(), flowerBlock);
    }

    protected void flowerPottedItem(RegistryObject<Block> flowerPotted, RegistryObject<Block> textures) {
        ModelFile flower = models().singleTexture(name(flowerPotted.get()), ResourceLocation.parse("flower_pot_cross"), "plant", blockTexture(textures.get())).renderType("cutout");
        simpleBlock(flowerPotted.get(), flower);
    }

    protected void blockItem(RegistryObject<Block> block, String appendix) {
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(String.format("%s:block/", BeyondHorizon.ID) + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_" + appendix));
    }

    protected void chestBlocks(RegistryObject<Block> block, RegistryObject<Block> particle) {
        ModelFile models = models().withExistingParent(name(block.get()), BaseModels.CHEST_BASE).texture("particle", blockTexture(particle.get()));
        chestBuiltinItem(block);
        simpleBlock(block.get(), models);
    }

    protected void blockItem(RegistryObject<Block> object) {
        simpleBlockItem(object.get(), new ModelFile.UncheckedModelFile(String.format("%s:block/", BeyondHorizon.ID) + ForgeRegistries.BLOCKS.getKey(object.get()).getPath()));
    }

    protected void saplingBlocks(RegistryObject<Block> block) {
        ItemModelProvider provider = itemModels();
        String formatted = String.format("%s/%s", BHBlockStateProvider.ModelPath.BLOCK.getModelPath(), name(block.get()));
        provider.withExistingParent(name(block.get()), BaseModels.GENERATED).texture("layer0", BeyondHorizon.resource( formatted));
        simpleBlock(block.get(), models().cross(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), blockTexture(block.get())).renderType("cutout"));
    }

    protected void blockGeneratedItem(RegistryObject<Block> block) {
        ItemModelProvider provider = itemModels();
        String formatted = String.format("%s/%s", BHBlockStateProvider.ModelPath.BLOCK.getModelPath(), name(block.get()));
        provider.withExistingParent(name(block.get()), BaseModels.GENERATED).texture("layer0", BeyondHorizon.resource( formatted));
    }

    protected void blockGeneratedItem(RegistryObject<Block> block, BHBlockStateProvider.ModelPath path) {
        ItemModelProvider provider = itemModels();
        String formatted = String.format("%s/%s", path.getModelPath(), name(block.get()));
        provider.withExistingParent(name(block.get()), BaseModels.GENERATED).texture("layer0", BeyondHorizon.resource( formatted));
    }

    public void chestBuiltinItem(RegistryObject<Block> block) {
        ResourceLocation texture = BeyondHorizon.resource(String.format("entity/chest/%s", name(block.get())));
        ItemModelProvider provider = itemModels();
        provider.withExistingParent(name(block.get()), BaseModels.CHEST_MODEL).texture("chest", texture);
    }

    protected void blockWithItemRenderType(RegistryObject<Block> block, String side, String top, String renderType) {
        ResourceLocation sideTexture = BeyondHorizon.resource("block/" + side + "_side");
        ResourceLocation topTexture = BeyondHorizon.resource("block/" + top + "_top");
        ModelFile cube = models().cube(name(block.get()), topTexture, topTexture, sideTexture, sideTexture, sideTexture, sideTexture).texture("particle", topTexture).renderType(renderType);
        simpleBlockWithItem(block.get(), cube);
    }

    protected void blockWithItemSBT(RegistryObject<Block> block) {
        ResourceLocation top = BeyondHorizon.resource(String.format("block/%s_top", name(block.get())));
        ResourceLocation side = BeyondHorizon.resource(String.format("block/%s", name(block.get())));
        ResourceLocation bottom = BeyondHorizon.resource(String.format("block/%s_bottom", name(block.get())));
        blockWithItem(block, top, bottom, side);
    }

    protected void blockWithItemExistingRenderType(RegistryObject<Block> block) {
        ModelFile models = models().getExistingFile(BeyondHorizon.resource(String.format("block/%s", name(block.get()))));
        simpleBlockWithItem(block.get(), models);
    }

    protected void decorativePanelBlock(RegistryObject<Block> block) {
        ResourceLocation panel = BeyondHorizon.resource(String.format("block/%s", name(block.get())));
        ResourceLocation side = BeyondHorizon.resource(String.format("block/%s_side", name(block.get())));
        ResourceLocation back = BeyondHorizon.resource(String.format("block/%s_back", name(block.get())));
        ResourceLocation parent = BeyondHorizon.resource("block/decorative_panel");
        ModelFile models = models().withExistingParent(name(block.get()), parent).texture("back", back).texture("panel", panel).texture("side", side).texture("particle", panel);
        horizontalBlock(block.get(), models);
        blockItem(block);
    }

    protected void axisBlockWithItemExisting(RegistryObject<Block> block) {
        ModelFile models = models().getExistingFile(BeyondHorizon.resource(String.format("block/%s", name(block.get()))));
        horizontalBlock(block.get(), models);
        blockItem(block);
    }

    protected void blankBlock(RegistryObject<Block> block) {
        ResourceLocation particle = getResourceLocation(block);
        ResourceLocation texture = getBlank();
        ModelFile models = models().cube(name(block.get()), texture, texture, texture, texture, texture, texture).texture("particle", particle);
        simpleBlockWithItem(block.get(), models);
    }
    protected void blockWithItem(RegistryObject<Block> block, ResourceLocation top, ResourceLocation bottom, ResourceLocation side) {
        ModelFile models = models().cube(name(block.get()), bottom, top, side, side, side, side).texture("particle", side);
        simpleBlockWithItem(block.get(), models);
    }

    protected void blockWithItem(RegistryObject<Block> block, ResourceLocation top, ResourceLocation bottom) {
        ModelFile models = models().cube(name(block.get()), top, bottom, blockTexture(block.get()), blockTexture(block.get()), blockTexture(block.get()), blockTexture(block.get())).texture("particle", blockTexture(block.get()));
        simpleBlockWithItem(block.get(), models);
    }
    protected void fullFaceBlock(RegistryObject<Block> block) {
        ResourceLocation top = this.getResourceLocation(block, "top");
        ResourceLocation bottom = this.getResourceLocation(block, "bottom");
        ResourceLocation north = this.getResourceLocation(block, "side_0");
        ResourceLocation east = this.getResourceLocation(block, "side_1");
        ResourceLocation south = this.getResourceLocation(block, "side_0");
        ResourceLocation west = this.getResourceLocation(block, "side_1");
        ModelFile models = models().cube(name(block.get()), top, bottom, north, south, east, west).texture("particle", north);
        simpleBlockWithItem(block.get(), models);
    }
    protected void blockWithItem(RegistryObject<Block> block, ResourceLocation top, ResourceLocation bottom, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west) {
        ModelFile models = models().cube(name(block.get()), top, bottom, north, south, east, west).texture("particle", north);
        simpleBlockWithItem(block.get(), models);
    }

    protected void leaveBlocks(RegistryObject<Block> block) {
        ModelFile model = models().withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), ResourceLocation.parse("leaves")).texture("all", blockTexture(block.get())).renderType("cutout_mipped");
        simpleBlockWithItem(block.get(), model);
    }

    protected void blockWithItem(RegistryObject<Block> block) {
        ModelFile model = models().cubeAll(name(block.get()), blockTexture(block.get()));
        simpleBlockWithItem(block.get(), model);
    }

    public String name(Block block) {
        return key(block).getPath();
    }

    public ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private @NotNull ResourceLocation getResourceLocation(RegistryObject<Block> block, String textName) {
        ResourceLocation bottomTexture;
        if (isEmpty(textName)) {
            bottomTexture = BeyondHorizon.resource(String.format("block/%s", name(block.get())));
        } else {
            bottomTexture = BeyondHorizon.resource(String.format("block/%s_%s", name(block.get()), textName));
        }
        return bottomTexture;
    }

    private @NotNull ResourceLocation getResourceLocation(RegistryObject<Block> block) {
        return getResourceLocation(block, "");
    }

    private @NotNull ResourceLocation getBlank() {
        return BeyondHorizon.resource(String.format("block/%s", "blank_block"));
    }

    private boolean isEmpty(String string) {
        return string.isBlank() || string.isEmpty();
    }

}
