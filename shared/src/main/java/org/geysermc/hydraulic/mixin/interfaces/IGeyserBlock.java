package org.geysermc.hydraulic.mixin.interfaces;

import org.geysermc.geyser.level.block.type.BlockState;

import java.util.List;

public interface IGeyserBlock {
    List<BlockState> hydraulic$getAllStates();
}
