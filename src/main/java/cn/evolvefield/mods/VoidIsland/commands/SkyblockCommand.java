package cn.evolvefield.mods.VoidIsland.commands;

import cn.evolvefield.mods.VoidIsland.world.IslandPos;
import cn.evolvefield.mods.VoidIsland.world.SkyblockChunkGenerator;
import cn.evolvefield.mods.VoidIsland.world.SkyblockSavedData;
import cn.evolvefield.mods.VoidIsland.world.SkyblockWorldEvents;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.UUIDArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class SkyblockCommand {
    private static final SimpleCommandExceptionType NOT_SKYBLOCK_WORLD = new SimpleCommandExceptionType(new TranslationTextComponent("voidisland.command.skyblock.world"));
    private static final SimpleCommandExceptionType NO_ISLAND = new SimpleCommandExceptionType(new TranslationTextComponent("voidisland.command.skyblock.noisland"));

    public SkyblockCommand() {
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> commandBuilder = (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) Commands.literal("voidisland").requires((s) -> {
            return s.hasPermissionLevel(2);
        })).then(Commands.literal("help").executes(SkyblockCommand::printHelp))).then(Commands.literal("my").then(Commands.argument("player", EntityArgument.player()).executes(SkyblockCommand::createIsland)))).then(Commands.literal("spawn").executes(SkyblockCommand::teleportToSpawn))).then(((LiteralArgumentBuilder)Commands.literal("visit").then(Commands.argument("player", EntityArgument.player()).executes((ctx) -> {
            return teleportToIsland(ctx, (PlayerEntity)EntityArgument.getPlayer(ctx, "player"));
        }))).then(Commands.argument("playerUuid", UUIDArgument.func_239194_a_()).suggests((ctx, builder) -> {
            return ISuggestionProvider.suggest(SkyblockSavedData.get(((CommandSource)ctx.getSource()).getWorld()).skyblocks.values().stream().map(UUID::toString), builder);
        }).executes((ctx) -> {
            return teleportToIsland(ctx, UUIDArgument.func_239195_a_(ctx, "playerUuid"));
        })))).then(((LiteralArgumentBuilder)Commands.literal("regen").then(Commands.argument("player", EntityArgument.player()).executes((ctx) -> {
            return rebuildIsland(ctx, EntityArgument.getPlayer(ctx, "player"));
        }))).then(Commands.argument("playerUuid", UUIDArgument.func_239194_a_()).suggests((ctx, builder) -> {
            return ISuggestionProvider.suggest(SkyblockSavedData.get(((CommandSource)ctx.getSource()).getWorld()).skyblocks.values().stream().map(UUID::toString), builder);
        }).executes((ctx) -> {
            return rebuildIsland(ctx, UUIDArgument.func_239195_a_(ctx, "playerUuid"));
        })));
        LiteralCommandNode<CommandSource> command = dispatcher.register(commandBuilder);
        //dispatcher.register((LiteralArgumentBuilder)Commands.literal("voidisland").redirect(command));
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("is").redirect(command));
    }

    private static int printHelp(CommandContext<CommandSource> ctx) {
        for(int i = 0; i < 5; ++i) {
            ((CommandSource)ctx.getSource()).sendFeedback(new TranslationTextComponent("voidisland.command.skyblock.help." + i), false);
        }

        return 1;
    }

    private static int doTeleportToIsland(CommandContext<CommandSource> ctx, UUID owner, ITextComponent feedback) throws CommandSyntaxException {
        ServerWorld world = getSkyblockWorld(ctx);
        IslandPos pos = getIslandForUUID(owner, SkyblockSavedData.get(world));
        ServerPlayerEntity player = ((CommandSource)ctx.getSource()).asPlayer();
        BlockPos blockPos = pos.getCenter();
        player.teleport(world, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, player.rotationYaw, player.rotationPitch);
        ((CommandSource)ctx.getSource()).sendFeedback(feedback, true);
        return 1;
    }

    private static int createIsland(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        PlayerEntity player = EntityArgument.getPlayer(ctx, "player");
        SkyblockSavedData data = SkyblockSavedData.get(getSkyblockWorld(ctx));
        UUID uuid = player.getUniqueID();
        if (data.skyblocks.containsValue(uuid)) {
            doTeleportToIsland(ctx, uuid, new TranslationTextComponent("voidisland.command.skyblock.island.teleported", new Object[]{((CommandSource)ctx.getSource()).getDisplayName()}));
            return 1;
        } else {
            SkyblockWorldEvents.spawnPlayer(player, data.create(uuid));
            ((CommandSource)ctx.getSource()).sendFeedback(new TranslationTextComponent("voidisland.command.skyblock.island.success", new Object[]{player.getDisplayName()}), true);
            return 1;
        }
    }

    private static int doRebuildIsland(CommandContext<CommandSource> ctx, UUID player, ITextComponent feedback) throws CommandSyntaxException {
        ServerWorld world = getSkyblockWorld(ctx);
        IslandPos pos = getIslandForUUID(player, SkyblockSavedData.get(world));
        SkyblockWorldEvents.createSkyblock(world, pos.getCenter());
        ((CommandSource)ctx.getSource()).sendFeedback(feedback, true);
        return 1;
    }

    private static IslandPos getIslandForUUID(UUID player, SkyblockSavedData data) throws CommandSyntaxException {
        IslandPos pos = (IslandPos)data.skyblocks.inverse().get(player);
        if (pos == null) {
            throw NO_ISLAND.create();
        } else {
            return pos;
        }
    }

    private static ServerWorld getSkyblockWorld(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        ServerWorld world = ((CommandSource)ctx.getSource()).getWorld();
        if (!SkyblockChunkGenerator.isWorldSkyblock(world)) {
            throw NOT_SKYBLOCK_WORLD.create();
        } else {
            return world;
        }
    }

    private static int teleportToIsland(CommandContext<CommandSource> ctx, PlayerEntity owner) throws CommandSyntaxException {
        return doTeleportToIsland(ctx, owner.getUniqueID(), new TranslationTextComponent("voidisland.command.skyblock.teleport.success", new Object[]{((CommandSource)ctx.getSource()).getDisplayName(), owner.getName()}));
    }

    private static int teleportToIsland(CommandContext<CommandSource> ctx, UUID owner) throws CommandSyntaxException {
        return doTeleportToIsland(ctx, owner, new TranslationTextComponent("voidisland.command.skyblock.teleport.success", new Object[]{((CommandSource)ctx.getSource()).getDisplayName(), owner}));
    }

    private static int teleportToSpawn(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        return doTeleportToIsland(ctx, Util.DUMMY_UUID, new TranslationTextComponent("voidisland.command.skyblock.spawn.success", new Object[]{((CommandSource)ctx.getSource()).getDisplayName()}));
    }

    private static int rebuildIsland(CommandContext<CommandSource> ctx, ServerPlayerEntity owner) throws CommandSyntaxException {
        return doRebuildIsland(ctx, owner.getUniqueID(), new TranslationTextComponent("voidisland.command.skyblock.regenisland.success", new Object[]{owner.getDisplayName()}));
    }

    private static int rebuildIsland(CommandContext<CommandSource> ctx, UUID owner) throws CommandSyntaxException {
        return doRebuildIsland(ctx, owner, new TranslationTextComponent("voidisland.command.skyblock.regenisland.success", new Object[]{owner}));
    }
}
