package safro.apocalypse;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ApocalypseTags {
    /**
     * Blocks that should additionally burn up during a solar apocalypse
     * Meant for blocks that do not randomly tick by default
     */
    public static final TagKey<Block> SOLAR_BURNS = BlockTags.create(Apocalypse.id("solar_burns"));
}
