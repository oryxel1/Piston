package org.geysermc.hydraulic.mixin.ext;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.level.block.Blocks;
import org.geysermc.geyser.level.chunk.bitarray.BitArray;
import org.geysermc.geyser.level.chunk.bitarray.BitArrayVersion;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.java.level.JavaLevelChunkWithLightTranslator;
import org.geysermc.hydraulic.mixin.interfaces.IGeyserBlock;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundLevelChunkWithLightPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = JavaLevelChunkWithLightTranslator.class, remap = false)
public class JavaLevelChunkWithLightTranslatorMixin {
    @Redirect(method = "translate(Lorg/geysermc/geyser/session/GeyserSession;Lorg/geysermc/mcprotocollib/protocol/packet/ingame/clientbound/level/ClientboundLevelChunkWithLightPacket;)V",
            at = @At(
            value = "INVOKE",
            target = "Lorg/geysermc/geyser/level/chunk/bitarray/BitArrayVersion;createArray(I[I)Lorg/geysermc/geyser/level/chunk/bitarray/BitArray;"
        ))
    private BitArray hookIntoCreateBitArray(BitArrayVersion instance, int size, int[] words) {
        return BitArrayVersion.V2.createArray(size, words); // We need an extra layer for fake sign.
    }

    @Inject(method = "translate(Lorg/geysermc/geyser/session/GeyserSession;Lorg/geysermc/mcprotocollib/protocol/packet/ingame/clientbound/level/ClientboundLevelChunkWithLightPacket;)V",
        at = @At(value = "INVOKE",
                target = "Lit/unimi/dsi/fastutil/ints/IntList;of(II)Lit/unimi/dsi/fastutil/ints/IntList;", shift = At.Shift.AFTER))
    private void hookIntoPostPalette(GeyserSession session, ClientboundLevelChunkWithLightPacket packet, CallbackInfo ci, @Local IntList layer1Palette) {
        List<Integer> list = new ArrayList<>();
        list.add(session.getBlockMappings().getBedrockAir().getRuntimeId());
        list.add(session.getBlockMappings().getBedrockWater().getRuntimeId());

        ((IGeyserBlock)Blocks.ACACIA_SIGN).hydraulic$getAllStates().forEach(state -> list.add(session.getBlockMappings().getBedrockBlockId(state.javaId())));
        ((IGeyserBlock)Blocks.ACACIA_WALL_SIGN).hydraulic$getAllStates().forEach(state -> list.add(session.getBlockMappings().getBedrockBlockId(state.javaId())));
        ((IGeyserBlock)Blocks.ACACIA_HANGING_SIGN).hydraulic$getAllStates().forEach(state -> list.add(session.getBlockMappings().getBedrockBlockId(state.javaId())));
        ((IGeyserBlock)Blocks.ACACIA_WALL_HANGING_SIGN).hydraulic$getAllStates().forEach(state -> list.add(session.getBlockMappings().getBedrockBlockId(state.javaId())));

        layer1Palette.clear();
        layer1Palette.addAll(list);
    }
}
