package dev.drawethree.xprison.mines.gui;

import dev.drawethree.xprison.mines.model.mine.Mine;
import dev.drawethree.xprison.mines.model.mine.reset.ResetType;
import dev.drawethree.xprison.utils.compat.CompMaterial;
import dev.drawethree.xprison.utils.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MineResetOptionsGUI extends Gui {

	private final Mine mine;

	public MineResetOptionsGUI(Mine mine, Player player) {
		super(player, 5, "Reset Options");
		this.mine = mine;
	}

	@Override
	public void redraw() {

		for (int i = 0; i < 5 * 9; i++) {
			this.setItem(i,
					ItemStackBuilder.of(CompMaterial.BLACK_STAINED_GLASS_PANE.toItem()).name("&a").buildItem().build());
		}

		this.setItem(11, ItemStackBuilder.of(CompMaterial.STONE_BUTTON.toItem()).name("&eReset NOW")
				.lore(" ", "&7Resets the mine now.").build(() -> {
					this.close();
					this.mine.getManager().resetMine(this.mine);
				}));

		this.setItem(13,
				ItemStackBuilder.of(CompMaterial.COMPARATOR.toItem())
						.name("&eReset type: " + this.mine.getResetType().getName())
						.lore(" ", "&7Instant: Will use more CPU power", "&7but the mine will reset instantly.", " ",
								"&7Gradual: Will use less CPU power", "&7but mine reset may take more time.", " ",
								"&7WorldEdit: Will use WorldEdit to reset", "&7the mine. If you're using FAWE then", "&7this is the recommended setting.", " ",
								"&aClick &7to change.")
						.build(() -> {
							// INSTANT => GRADUAL => WORLDEDIT
							if (this.mine.getResetType() == ResetType.INSTANT) {
								this.mine.setResetType(ResetType.GRADUAL);
							} else if (this.mine.getResetType() == ResetType.GRADUAL) {
								this.mine.setResetType(ResetType.WORLDEDIT);
							} else {
								this.mine.setResetType(ResetType.INSTANT);
							}
							this.redraw();
						}));

		this.setItem(15, ItemStackBuilder.of(CompMaterial.CLOCK.toItem()).name("&eEdit reset percentage")
				.lore(" ", "&7Click to edit mine's reset percentage").build(() -> {
					this.close();
					new MineEditResetPercentageGUI(this.mine, this.getPlayer()).open();
				}));

		this.setItem(29, ItemStackBuilder.of(CompMaterial.CLOCK.toItem()).name("&eEdit timed reset")
				.lore(" ", "&7Click to edit timed reset").build(() -> {
					this.close();
					new MineEditTimedResetGUI(this.mine, this.getPlayer()).open();
				}));

		this.setItem(31,
				ItemStackBuilder.of(CompMaterial.PAPER.toItem())
						.name("&eBroadcast reset: " + this.mine.isBroadcastReset())
						.lore(" ", "&aTrue &7- All players will get message", "&7on mine's reset.",
								"&cFalse &7- No broadcast message.")
						.build(() -> {
							this.mine.setBroadcastReset(!this.mine.isBroadcastReset());
							this.redraw();
						}));

		this.setItem(36,
				ItemStackBuilder.of(Material.ARROW).name("&cBack").lore("&7Click to go back to panel").build(() -> {
					this.close();
					new MinePanelGUI(this.mine, this.getPlayer()).open();
				}));

	}
}
