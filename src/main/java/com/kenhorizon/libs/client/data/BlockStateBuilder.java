package com.kenhorizon.libs.client.data;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.datagen.BHBlockStateProvider;
import com.kenhorizon.beyondhorizon.server.block.AdvancePipeBlock;
import com.kenhorizon.beyondhorizon.server.block.BHBlockProperties;
import com.kenhorizon.beyondhorizon.server.api.block.AdvanceFenceBlock;
import com.kenhorizon.beyondhorizon.server.block.redstone_lane.RedstoneWiredBlock;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerState;
import com.kenhorizon.beyondhorizon.server.block.redstone_lane.RedstoneLaneBlock;
import com.kenhorizon.beyondhorizon.server.block.redstone_lane.RedstoneLaneMode;
import com.kenhorizon.libs.registry.RegistryBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public abstract class BlockStateBuilder extends BlockStateProvider {

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
    public void axisBlock(RegistryObject<Block> block) {
        axisBlock(block, blockTexture(block.get()));
    }

    public void axisBlock(RegistryObject<Block> block, ResourceLocation baseName) {
        axisBlock((RotatedPillarBlock) block.get(), extend(baseName, "_side"), extend(baseName, "_end"));
        axisBlockWithItem(block, extend(baseName, "_side"), extend(baseName, "_end"));
    }

    protected void standWallBasinBlocks(RegistryObject<Block> block) {
        ResourceLocation base = BeyondHorizon.resource("block/basin_base");
        ResourceLocation flame = BeyondHorizon.resource("block/basin_fire");
        ResourceLocation magma = BeyondHorizon.resource("block/basin_magma");
        ResourceLocation chained = BeyondHorizon.resource("block/basin_chain");
        this.standWallBasinBlocks(block, base, flame, magma, chained);
    }
    protected void standWallBasinBlocks(RegistryObject<Block> block, ResourceLocation base, ResourceLocation flame, ResourceLocation magma, ResourceLocation chain) {
        ModelFile lit = models().withExistingParent(name(block.get()) + "_lit", BeyondHorizon.resource("block/base/stand_fire_basin_side_lit"))
                .texture("base", base).texture("flame", flame).texture("magma", magma).texture("chain", chain).texture("particle", base).renderType("cutout_mipped");
        ModelFile off = models().withExistingParent(name(block.get()) + "_unlit", BeyondHorizon.resource("block/base/stand_fire_basin_side_unlit"))
                .texture("base", base).texture("flame", flame).texture("magma", magma).texture("chain", chain).texture("particle", base).renderType("cutout_mipped");

        standWallBasinBlocks(block, lit, off);
        simpleBlockItem(block.get(), off);
    }
    protected void standWallBasinBlocks(RegistryObject<Block> block, ModelFile lit, ModelFile off) {
        getVariantBuilder(block.get())
                .forAllStates(blockState -> {
                    int yRot = ((int) blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360;
                    boolean isLit = blockState.getValue(BlockStateProperties.LIT);
                    return ConfiguredModel.builder()
                            .rotationY(yRot)
                            .modelFile(isLit ? lit : off)
                            .build();
                });
    }
    protected void standBasinBlocks(RegistryObject<Block> block) {
        ResourceLocation base = BeyondHorizon.resource("block/basin_base");
        ResourceLocation flame = BeyondHorizon.resource("block/basin_fire");
        ResourceLocation magma = BeyondHorizon.resource("block/basin_magma");
        ResourceLocation chained = BeyondHorizon.resource("block/basin_chain");
        this.standBasinBlocks(block, base, flame, magma, chained);
    }

    protected void standBasinBlocks(RegistryObject<Block> block, ResourceLocation base, ResourceLocation flame, ResourceLocation magma, ResourceLocation chain) {
        ModelFile lit = models().withExistingParent(name(block.get()) + "_lit", BeyondHorizon.resource("block/base/stand_fire_basin_lit"))
                .texture("base", base).texture("flame", flame).texture("magma", magma).texture("particle", base).renderType("cutout_mipped");
        ModelFile off = models().withExistingParent(name(block.get()) + "_unlit", BeyondHorizon.resource("block/base/stand_fire_basin_unlit"))
                .texture("base", base).texture("flame", flame).texture("magma", magma).texture("particle", base).renderType("cutout_mipped");

        ModelFile hangingLit = models().withExistingParent("hanging_" + name(block.get()) + "_lit", BeyondHorizon.resource("block/base/stand_hanging_fire_basin_lit"))
                .texture("base", base).texture("flame", flame).texture("magma", magma).texture("chain", chain).texture("particle", base).renderType("cutout_mipped");
        ModelFile hangingOff = models().withExistingParent("hanging_" + name(block.get()) + "_unlit", BeyondHorizon.resource("block/base/stand_hanging_fire_basin_unlit"))
                .texture("base", base).texture("flame", flame).texture("magma", magma).texture("chain", chain).texture("particle", base).renderType("cutout_mipped");

        this.standBasinBlocks(block, lit, off, hangingLit, hangingOff);
        this.simpleBlockItem(block.get(), off);
    }
    protected void standBasinBlocks(RegistryObject<Block> block, ModelFile lit, ModelFile off, ModelFile hangingLit, ModelFile hangingOff) {
        this.getVariantBuilder(block.get())
                .partialState().with(BlockStateProperties.HANGING, true).with(BlockStateProperties.LIT, true).modelForState().modelFile(hangingLit).addModel()
                .partialState().with(BlockStateProperties.HANGING, true).with(BlockStateProperties.LIT, false).modelForState().modelFile(hangingOff).addModel()
                .partialState().with(BlockStateProperties.HANGING, false).with(BlockStateProperties.LIT, true).modelForState().modelFile(lit).addModel()
                .partialState().with(BlockStateProperties.HANGING, false).with(BlockStateProperties.LIT, false).modelForState().modelFile(off).addModel();
    }
    protected void baseSpawnerBlocks(RegistryObject<Block> block, ResourceLocation top, ResourceLocation bottom, ResourceLocation side) {
        ResourceLocation inactiveTop = top.withSuffix("_inactive");
        ResourceLocation inactiveSide = side.withSuffix("_inactive");
        ResourceLocation activeTop = top.withSuffix("_active");
        ResourceLocation activeSide = side.withSuffix("_active");

        String inactiveName = String.format("%s_inactive", name(block.get()));
        ModelFile inactive = models().cube(inactiveName, bottom, inactiveTop, inactiveSide, inactiveSide, inactiveSide, inactiveSide).texture("particle", inactiveSide).renderType("cutout_mipped");;

        String activeName = String.format("%s_active", name(block.get()));
        ModelFile active = models().cube(activeName, bottom, activeTop, activeSide, activeSide, activeSide, activeSide).texture("particle", activeSide).renderType("cutout_mipped");;

        String cooldownName = String.format("%s_cooldown", name(block.get()));
        ModelFile cooldown = models().cube(cooldownName, bottom, inactiveTop, inactiveSide, inactiveSide, inactiveSide, inactiveSide).texture("particle", inactiveSide).renderType("cutout_mipped");;

        String ejectRewardName = String.format("%s_eject_reward", name(block.get()));
        ModelFile ejectReward = models().cube(ejectRewardName, bottom, top.withSuffix("_ejecting_reward"), activeSide, activeSide, activeSide, activeSide).texture("particle", activeSide).renderType("cutout_mipped");;

        String waitingForPlayerName = String.format("%s_waiting_for_player", name(block.get()));
        ModelFile waitingForPlayer = models().cube(waitingForPlayerName, bottom, inactiveTop, inactiveSide, inactiveSide, inactiveSide, inactiveSide).texture("particle", inactiveSide).renderType("cutout_mipped");;

        String waitingForRewardEjectionName = String.format("%s_waiting_for_reward_ejection", name(block.get()));
        ModelFile waitingForRewardEjection = models().cube(waitingForRewardEjectionName, bottom, inactiveTop, inactiveSide, inactiveSide, inactiveSide, inactiveSide).texture("particle", inactiveSide).renderType("cutout_mipped");

        this.baseSpawnerBlocks(block, inactive, active, cooldown, ejectReward, waitingForPlayer, waitingForRewardEjection);
        this.simpleBlockItem(block.get(), inactive);
    }

    protected void baseSpawnerBlocks(RegistryObject<Block> block,
                                     ModelFile inactive,
                                     ModelFile active,
                                     ModelFile cooldown,
                                     ModelFile ejectReward,
                                     ModelFile waitingForPlayer,
                                     ModelFile waitingForRewardEjection) {
        this.getVariantBuilder(block.get())
                .partialState().with(BHBlockProperties.SPAWNER_STATE, SpawnerState.INACTIVE).modelForState().modelFile(inactive).addModel()
                .partialState().with(BHBlockProperties.SPAWNER_STATE, SpawnerState.ACTIVE).modelForState().modelFile(active).addModel()
                .partialState().with(BHBlockProperties.SPAWNER_STATE, SpawnerState.COOLDOWN).modelForState().modelFile(cooldown).addModel()
                .partialState().with(BHBlockProperties.SPAWNER_STATE, SpawnerState.EJECTING_REWARD).modelForState().modelFile(ejectReward).addModel()
                .partialState().with(BHBlockProperties.SPAWNER_STATE, SpawnerState.WAITING_FOR_PLAYERS).modelForState().modelFile(waitingForPlayer).addModel()
                .partialState().with(BHBlockProperties.SPAWNER_STATE, SpawnerState.WAITING_FOR_REWARD_EJECTION).modelForState().modelFile(waitingForRewardEjection).addModel();
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
    protected void axisBlockWithItem(RegistryObject<Block> block, ResourceLocation side, ResourceLocation end) {
        ModelFile models = models().cube(name(block.get()), end, end, side, side, side, side).texture("particle", side);
        simpleBlockItem(block.get(), models);
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

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }
    public void latticeBlock(RegistryObject<Block> block, String particlesString, String postsString, String latticeString) {
        ResourceLocation particles = ResourceLocation.parse(particlesString);
        ResourceLocation posts = BeyondHorizon.resource(String.format("block/%s", postsString));
        ResourceLocation lattice = BeyondHorizon.resource(String.format("block/%s", latticeString));
        ModelFile fullSide = models().withExistingParent(String.format("%s:block/", BeyondHorizon.ID) + name(block.get()) + "_" + "side_full", String.format("%s:block/lattice_side_full", BeyondHorizon.ID)).texture("post", posts).texture("lattice", lattice).texture("particle", particles);
        ModelFile topSide = models().withExistingParent(String.format("%s:block/", BeyondHorizon.ID) + name(block.get()) + "_" + "side_top", String.format("%s:block/lattice_side_top", BeyondHorizon.ID)).texture("post", posts).texture("lattice", lattice).texture("particle", particles);
        ModelFile middleSide = models().withExistingParent(String.format("%s:block/", BeyondHorizon.ID) + name(block.get()) + "_" + "side_middle", String.format("%s:block/lattice_side_middle", BeyondHorizon.ID)).texture("post", posts).texture("lattice", lattice).texture("particle", particles);
        ModelFile bottomSide = models().withExistingParent(String.format("%s:block/", BeyondHorizon.ID) + name(block.get()) + "_" + "side_bottom", String.format("%s:block/lattice_side_bottom", BeyondHorizon.ID)).texture("post", posts).texture("lattice", lattice).texture("particle", particles);
        ModelFile post = models().withExistingParent(String.format("%s:block/", BeyondHorizon.ID) + name(block.get()) + "_" + "post", String.format("%s:block/lattice_post", BeyondHorizon.ID)).texture("post", posts).texture("particle", particles);
        ModelFile inventory = models().withExistingParent(String.format("%s:block/", BeyondHorizon.ID) + name(block.get()) + "_" + "inventory", String.format("%s:block/lattice_inventory", BeyondHorizon.ID)).texture("lattice", lattice).texture("post", posts).texture("particle", particles);
        latticeBlockParts(block, post, fullSide, topSide, middleSide, bottomSide);
        simpleBlockItem(block.get(), inventory);
        blockItem(block, "inventory");
    }

    private void latticeBlockParts(RegistryObject<Block> object,
                                   ModelFile post,
                                   ModelFile fullSide,
                                   ModelFile topSide,
                                   ModelFile middleSide,
                                   ModelFile bottomSide) {
        getMultipartBuilder(object.get())
                .part().modelFile(post).addModel().condition(AdvanceFenceBlock.POST, AdvanceFenceBlock.PostState.POST).end()

                .part().modelFile(fullSide).addModel().condition(AdvanceFenceBlock.NORTH_FENCE, AdvanceFenceBlock.FenceSide.FULL).end()
                .part().modelFile(topSide).addModel().condition(AdvanceFenceBlock.NORTH_FENCE, AdvanceFenceBlock.FenceSide.TOP).end()
                .part().modelFile(middleSide).addModel().condition(AdvanceFenceBlock.NORTH_FENCE, AdvanceFenceBlock.FenceSide.MIDDLE).end()
                .part().modelFile(bottomSide).addModel().condition(AdvanceFenceBlock.NORTH_FENCE, AdvanceFenceBlock.FenceSide.BOTTOM).end()

                .part().modelFile(fullSide).rotationY(90).addModel().condition(AdvanceFenceBlock.EAST_FENCE, AdvanceFenceBlock.FenceSide.FULL).end()
                .part().modelFile(topSide).rotationY(90).addModel().condition(AdvanceFenceBlock.EAST_FENCE, AdvanceFenceBlock.FenceSide.TOP).end()
                .part().modelFile(middleSide).rotationY(90).addModel().condition(AdvanceFenceBlock.EAST_FENCE, AdvanceFenceBlock.FenceSide.MIDDLE).end()
                .part().modelFile(bottomSide).rotationY(90).addModel().condition(AdvanceFenceBlock.EAST_FENCE, AdvanceFenceBlock.FenceSide.BOTTOM).end()

                .part().modelFile(fullSide).rotationY(180).addModel().condition(AdvanceFenceBlock.SOUTH_FENCE, AdvanceFenceBlock.FenceSide.FULL).end()
                .part().modelFile(topSide).rotationY(180).addModel().condition(AdvanceFenceBlock.SOUTH_FENCE, AdvanceFenceBlock.FenceSide.TOP).end()
                .part().modelFile(middleSide).rotationY(180).addModel().condition(AdvanceFenceBlock.SOUTH_FENCE, AdvanceFenceBlock.FenceSide.MIDDLE).end()
                .part().modelFile(bottomSide).rotationY(180).addModel().condition(AdvanceFenceBlock.SOUTH_FENCE, AdvanceFenceBlock.FenceSide.BOTTOM).end()

                .part().modelFile(fullSide).rotationY(270).addModel().condition(AdvanceFenceBlock.WEST_FENCE, AdvanceFenceBlock.FenceSide.FULL).end()
                .part().modelFile(topSide).rotationY(270).addModel().condition(AdvanceFenceBlock.WEST_FENCE, AdvanceFenceBlock.FenceSide.TOP).end()
                .part().modelFile(middleSide).rotationY(270).addModel().condition(AdvanceFenceBlock.WEST_FENCE, AdvanceFenceBlock.FenceSide.MIDDLE).end()
                .part().modelFile(bottomSide).rotationY(270).addModel().condition(AdvanceFenceBlock.WEST_FENCE, AdvanceFenceBlock.FenceSide.BOTTOM).end();

    }

    protected void poweredBlocks(RegistryObject<Block> block) {
        ResourceLocation textures = BeyondHorizon.resource("block/" + name(block.get()));
        ResourceLocation textureActive = extend(textures, "_active");
        ModelFile base = models().cube(name(block.get()), textures, textures, textures, textures, textures, textures).texture("particle", textures);
        ModelFile active = models().cube(name(block.get()) + "_active", textureActive, textureActive, textureActive, textureActive, textureActive, textureActive).texture("particle", textureActive).renderType("cutout_mipped");
        poweredBlockState(block, base, active);
        simpleBlockItem(block.get(), base);
    }

    private void poweredBlockState(RegistryObject<Block> block, ModelFile base, ModelFile active) {
        getVariantBuilder(block.get()).forAllStatesExcept(blockState -> {
            boolean powerLevel = blockState.getValue(BlockStateProperties.POWERED);
            return ConfiguredModel.builder().modelFile(powerLevel ? active : base).build();
        });
    }
    protected void redstoneTransmitter(RegistryObject<Block> block) {
        ResourceLocation base_line = BeyondHorizon.resource("block/" + name(block.get()));
        ResourceLocation n = extend(base_line, "_south");
        ResourceLocation s = extend(base_line, "_north");
        ResourceLocation side = extend(base_line, "_side");
        ResourceLocation top = extend(base_line, "_top");
        ResourceLocation nA = extend(base_line, "_south_active");
        ResourceLocation sA = extend(base_line, "_north_active");
        ResourceLocation sideA = extend(base_line, "_side_active");
        ModelFile base = models().cube(name(block.get()),
                top, top, n, s, side, side)
                .texture("particle", top);
        ModelFile active = models().cube(name(block.get()) + "_active",
                        top, top, nA, sA, sideA, sideA)
                .texture("particle", top);
        axisLitBlockState(block, base, active);
        simpleBlockItem(block.get(), base);
    }
    protected void axisLitBlockState(RegistryObject<Block> block, ModelFile base, ModelFile active) {
        getVariantBuilder(block.get()).forAllStates(blockState -> {
            int yRot = ((int) blockState.getValue(BlockStateProperties.FACING).toYRot() + 180) % 360;
            boolean isActive = blockState.getValue(BlockStateProperties.LIT);
            return ConfiguredModel.builder().modelFile(isActive ? active : base).rotationY(yRot).uvLock(true).build();
        });
    }

    protected void litBlocks(RegistryObject<Block> block) {
        ResourceLocation textures = BeyondHorizon.resource("block/" + name(block.get()));
        ResourceLocation textureActive = extend(textures, "_active");
        ModelFile base = models().cube(name(block.get()), textures, textures, textures, textures, textures, textures).texture("particle", textures);
        ModelFile active = models().cube(name(block.get()) + "_active", textureActive, textureActive, textureActive, textureActive, textureActive, textureActive).texture("particle", textureActive).renderType("cutout_mipped");
        litBlockState(block, base, active);
        simpleBlockItem(block.get(), base);
    }

    private void litBlockState(RegistryObject<Block> block, ModelFile base, ModelFile active) {
        getVariantBuilder(block.get()).forAllStatesExcept(blockState -> {
            boolean powerLevel = blockState.getValue(BlockStateProperties.LIT);
            return ConfiguredModel.builder().modelFile(powerLevel ? active : base).build();
        });
    }

    protected void redstoneWiredBlock(RegistryObject<Block> block) {
        ResourceLocation base = BeyondHorizon.resource("block/redstone_wired");
        ResourceLocation dot = this.extend(base, "_dot");
        ResourceLocation hL = this.extend(base, "_horizontal");
        ResourceLocation vL = this.extend(base, "_vertical");
        ResourceLocation vLEU = this.extend(base, "_vertical_end_up");
        ResourceLocation vLEUA = this.extend(base, "_vertical_end_up_alt");
        ResourceLocation hLED = this.extend(base, "_horizontal_end");
        ResourceLocation hLEDA = this.extend(base, "_horizontal_end_alt");
        ResourceLocation fL = this.extend(base, "_face");
        ResourceLocation sL = this.extend(base, "_section");
        ResourceLocation rL = this.extend(base, "_right");
        ResourceLocation lL = this.extend(base, "_left");
        ResourceLocation rLA = this.extend(base, "_right_alt");
        ResourceLocation lLA = this.extend(base, "_left_alt");
        ResourceLocation hlS = this.extend(base, "_horizontal_section");
        ResourceLocation hlSA = this.extend(base, "_horizontal_section_alt");

        ModelFile baseM = models().cube(name(block.get()), vL, vL, fL, fL, hL, hL).texture("particle", vL);
        ModelFile dotM = models().cube(name(block.get()) + "_dot", dot, dot, dot, dot, dot, dot).texture("particle", vL);
        ModelFile upM = models().cube(name(block.get()) + "_horizontal", fL, fL, vL, vL, vL, vL).texture("particle", vL);
        ModelFile upMEU = models().cube(name(block.get()) + "_horizontal_end", fL, fL, vLEU, vLEU, vLEU, vLEU).texture("particle", vLEU);
        ModelFile sideNSE = models().cube(name(block.get()) + "_side_ns", vLEU, vLEUA, fL, fL, hLED, hLEDA).texture("particle", vLEU);
        ModelFile sideEWE = models().cube(name(block.get()) + "_side_ew", hLED, hLED, hLEDA, hLED, fL, fL).texture("particle", vLEU);
        ModelFile sideCorners = models().cube(name(block.get()) + "_side_corners", lL, rL, lL, rL, lL, rL).texture("particle", vLEU);
        ModelFile upSectionW = models().cube(name(block.get()) + "_horizontal_section_west", hL, hL, hlS, hlSA, vL, vL).texture("particle", vL);
        ModelFile upSectionE = models().cube(name(block.get()) + "_horizontal_section_east", hL, hL, hlSA, hlS, vL, vL).texture("particle", vL);
        ModelFile upSectionN = models().cube(name(block.get()) + "_horizontal_section_north", hL, hL, vL, vL, hlS, hlSA).texture("particle", vL);
        ModelFile upSectionS = models().cube(name(block.get()) + "_horizontal_section_south", hL, hL, vL, vL, hlSA, hlS).texture("particle", vL);
        ModelFile upWest = models().cube(name(block.get()) + "_horizontal_west", hL, hL, rL, lL, vL, vL).texture("particle", vL);
        ModelFile upEast = models().cube(name(block.get()) + "_horizontal_east", hL, hL, lL, rL, vL, vL).texture("particle", vL);
        ModelFile upNS = models().cube(name(block.get()) + "_horizontal_ns", vL, vL, vL, vL, rL, lL).texture("particle", vL);
        ModelFile upWestAlt = models().cube(name(block.get()) + "_horizontal_west_alt", hL, hL, rLA, lLA, vL, vL).texture("particle", vL);
        ModelFile upEastAlt = models().cube(name(block.get()) + "_horizontal_east_alt", hL, hL, lLA, rLA, vL, vL).texture("particle", vL);
        ModelFile sectionM = models().cube(name(block.get()) + "_section", sL, sL, hL, hL, hL, hL).texture("particle", vL);
        ModelFile sideM = models().cube(name(block.get()) + "_side", rL, rL, hL, hL, hL, hL).texture("particle", vL);
        ModelFile fullM = models().cube(name(block.get()) + "_full", fL, fL, fL, fL, fL, fL).texture("particle", vL);

        this.getVariantBuilder(block.get()).forAllStates(blockState -> {
            //
            boolean dn = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean ds = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            //
            boolean dne = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean dnw = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean dse = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean dsw = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            //
            boolean dsu = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean dnu = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean deu = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean dwu = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
              //
            boolean ne = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean se = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean nw = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean sw = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean n = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean ns = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean s = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean e = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean ew = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean w = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean full = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean cornerNSE = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean cornerSWE = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean cornerNSW = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean cornerENW = (blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean cornerUPfull = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean up = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean upE = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean upW = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && blockState.getValue(RedstoneWiredBlock.UP)
                    && !blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean downE = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));
            boolean downW = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean downWE = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && blockState.getValue(RedstoneWiredBlock.EAST)
                    && blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean upDown = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));

            boolean down = (!blockState.getValue(RedstoneWiredBlock.NORTH)
                    && !blockState.getValue(RedstoneWiredBlock.SOUTH)
                    && !blockState.getValue(RedstoneWiredBlock.EAST)
                    && !blockState.getValue(RedstoneWiredBlock.WEST)
                    && !blockState.getValue(RedstoneWiredBlock.UP)
                    && blockState.getValue(RedstoneWiredBlock.DOWN));

            if (full || cornerUPfull) {
                return ConfiguredModel.builder().modelFile(fullM).build();
            } else if (dnu) {
                return ConfiguredModel.builder().modelFile(upSectionN).build();
            } else if (dsu) {
                return ConfiguredModel.builder().modelFile(upSectionS).build();
            } else if (dwu) {
                return ConfiguredModel.builder().modelFile(upSectionW).build();
            } else if (deu) {
                return ConfiguredModel.builder().modelFile(upSectionE).build();
            } else if (dn) {
                return ConfiguredModel.builder().modelFile(upNS).build();
            } else if (ds) {
                return ConfiguredModel.builder().modelFile(upNS).rotationY(180).build();
            } else if (downE) {
                return ConfiguredModel.builder().modelFile(upEast).build();
            } else if (downW) {
                return ConfiguredModel.builder().modelFile(upWest).build();
            } else if (upW) {
                return ConfiguredModel.builder().modelFile(upWestAlt).build();
            } else if (upE) {
                return ConfiguredModel.builder().modelFile(upEastAlt).build();
            } else if (ne) {
                return ConfiguredModel.builder().modelFile(sideM).rotationY(270).build();
            } else if (se) {
                return ConfiguredModel.builder().modelFile(sideM).build();
            } else if (dsw) {
                return ConfiguredModel.builder().modelFile(sideCorners).rotationY(90).build();
            } else if (dnw) {
                return ConfiguredModel.builder().modelFile(sideCorners).rotationY(180).build();
            } else if (dse) {
                return ConfiguredModel.builder().modelFile(sideCorners).build();
            } else if (dne) {
                return ConfiguredModel.builder().modelFile(sideCorners).rotationY(270).build();
            }  else if (nw) {
                return ConfiguredModel.builder().modelFile(sideM).rotationY(180).build();
            }  else if (sw) {
                return ConfiguredModel.builder().modelFile(sideM).rotationY(90).build();
            }  else if (n) {
                return ConfiguredModel.builder().modelFile(sideNSE).build();
            }  else if (s) {
                return ConfiguredModel.builder().modelFile(sideNSE).rotationY(180).build();
            }  else if (ns) {
                return ConfiguredModel.builder().modelFile(baseM).build();
            }  else if (e) {
                return ConfiguredModel.builder().modelFile(sideEWE).build();
            }  else if (w) {
                return ConfiguredModel.builder().modelFile(sideEWE).rotationY(180).build();
            }  else if (ew) {
                return ConfiguredModel.builder().modelFile(baseM).rotationY(90).build();
            } else if (cornerSWE) {
                return ConfiguredModel.builder().modelFile(sectionM).build();
            } else if (cornerNSE) {
                return ConfiguredModel.builder().modelFile(sectionM).rotationY(270).build();
            } else if (cornerNSW) {
                return ConfiguredModel.builder().modelFile(sectionM).rotationY(90).build();
            } else if (cornerENW) {
                return ConfiguredModel.builder().modelFile(sectionM).rotationY(180).build();
            } else if (upDown) {
                return ConfiguredModel.builder().modelFile(upM).build();
            } else if (up) {
                return ConfiguredModel.builder().modelFile(upMEU).rotationX(180).build();
            } else if (down) {
                return ConfiguredModel.builder().modelFile(upMEU).build();
            }  else {
                return ConfiguredModel.builder().modelFile(dotM).build();
            }
        });

        simpleBlockItem(block.get(), baseM);
    }

    protected void redstoneLaneWithItem(RedstoneLaneBlock block) {
        ResourceLocation base_lane = BeyondHorizon.resource("block/redstone_lane");
        ResourceLocation side = extend(base_lane, "_side");
        ResourceLocation front = extend(base_lane, "_front");
        ResourceLocation front_powered = extend(base_lane, "_front_powered");
        ResourceLocation bottom = extend(base_lane, "_bottom");
        ResourceLocation top = extend(blockTexture(block), "_top_unpowered");
        ResourceLocation top_powered = extend(blockTexture(block), "_top_powered");

        ModelFile unpowered = models().cube(name(block) + "_unpowered", bottom, top, front, front, side, side).texture("particle", top);
        ModelFile powered = models().cube(name(block) + "_powered", bottom, top_powered, front_powered, front_powered, side, side).texture("particle", top_powered);
         if (name(block).equals("redstone_lane_l")) {
            unpowered = models().cube(name(block) + "_unpowered", bottom, top, side, front, front, side).texture("particle", top);
            powered = models().cube(name(block) + "_powered", bottom, top_powered, side, front_powered, front_powered, side).texture("particle", top_powered);
        } else if (name(block).equals("redstone_lane_t")) {
            unpowered = models().cube(name(block) + "_unpowered", bottom, top, side, front, front, front).texture("particle", top);
            powered = models().cube(name(block) + "_powered", bottom, top_powered, side, front_powered, front_powered, front_powered).texture("particle", top_powered);
        }

        if (name(block).equals("redstone_lane_i")) {
            getVariantBuilder(block)
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.NORTH).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).modelForState().modelFile(unpowered).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.EAST).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).modelForState().modelFile(unpowered).rotationY(90).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.SOUTH).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).modelForState().modelFile(unpowered).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.WEST).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).modelForState().modelFile(unpowered).rotationY(90).addModel()

                    .partialState().with(RedstoneLaneBlock.FACING, Direction.NORTH).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).modelForState().modelFile(powered).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.EAST).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).modelForState().modelFile(powered).rotationY(90).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.SOUTH).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).modelForState().modelFile(powered).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.WEST).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).modelForState().modelFile(powered).rotationY(90).addModel();
            } else {
            getVariantBuilder(block)
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.NORTH).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).modelForState().modelFile(unpowered).rotationY(180).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.EAST).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).modelForState().modelFile(unpowered).rotationY(270).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.SOUTH).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).modelForState().modelFile(unpowered).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.WEST).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).modelForState().modelFile(unpowered).rotationY(90).addModel()

                    .partialState().with(RedstoneLaneBlock.FACING, Direction.NORTH).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).modelForState().modelFile(powered).rotationY(180).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.EAST).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).modelForState().modelFile(powered).rotationY(270).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.SOUTH).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).modelForState().modelFile(powered).addModel()
                    .partialState().with(RedstoneLaneBlock.FACING, Direction.WEST).with(RedstoneLaneBlock.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).modelForState().modelFile(powered).rotationY(90).addModel();
        }
        simpleBlockItem(block, models().getExistingFile(extend(blockTexture(block), "_unpowered")));
    }
    private boolean isEmpty(String string) {
        return string.isBlank() || string.isEmpty();
    }

}
