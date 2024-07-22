package dev.drawethree.xprison.mines.model.mine.reset;

import dev.drawethree.xprison.XPrison;
import dev.drawethree.xprison.mines.model.mine.BlockPalette;
import dev.drawethree.xprison.mines.model.mine.Mine;
import me.lucko.helper.serialize.Position;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;

public class WorldEditReset extends ResetType {

    WorldEditReset() {
        super("WorldEdit");
    }

    @Override
    public void reset(Mine mine, BlockPalette blockPalette) {
        if (blockPalette.isEmpty()) {
            XPrison.getInstance().getLogger()
                    .warning("Reset for Mine " + mine.getName() + " aborted. Block palette is empty.");
            return;
        }

        Position min = mine.getMineRegion().getMin();
        Position max = mine.getMineRegion().getMax();

        World world = Bukkit.getServer().getWorld(min.getWorld());
        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
        BlockVector3 minVector = BlockVector3.at(min.getX(), min.getY(), min.getZ());
        BlockVector3 maxVector = BlockVector3.at(max.getX(), max.getY(), max.getZ());
        CuboidRegion region = new CuboidRegion(weWorld, minVector, maxVector);

        RandomPattern pat = new RandomPattern();
        for (var mat : blockPalette.getValidMaterials()) {
            double percentage = blockPalette.getPercentage(mat) / 100;
            var material = mat.toMaterial();

            var adapt = BukkitAdapter.adapt(material.createBlockData());
            BlockPattern pattern = new BlockPattern(adapt);

            pat.add(pattern, percentage);
        }

        new Thread(new Runnable() {
            public void run() {
                try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(
                        weWorld,
                        -1)) {
                    editSession.setBlocks(region, pat);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mine.setResetting(false);
                mine.updateCurrentBlocks();
                mine.updateHolograms();
            }
        }).start();
    }
}
