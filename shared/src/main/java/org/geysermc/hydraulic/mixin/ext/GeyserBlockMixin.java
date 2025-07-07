package org.geysermc.hydraulic.mixin.ext;

import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.level.block.type.BlockState;
import org.geysermc.hydraulic.mixin.interfaces.IGeyserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = Block.class, remap = false)
public class GeyserBlockMixin<E> implements IGeyserBlock {
    @Unique
    private List<BlockState> states = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    private E injectBlockStateList(List instance, int i) {
        this.states = instance;

        return (E) instance.get(i);
    }

    @Override
    public List<BlockState> hydraulic$getAllStates() {
        return this.states;
    }
}
