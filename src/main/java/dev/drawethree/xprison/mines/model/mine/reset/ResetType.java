package dev.drawethree.xprison.mines.model.mine.reset;

import dev.drawethree.xprison.mines.model.mine.BlockPalette;
import dev.drawethree.xprison.mines.model.mine.Mine;
import lombok.Getter;

public abstract class ResetType {

	public static final GradualReset GRADUAL = new GradualReset();
	public static final InstantReset INSTANT = new InstantReset();
	public static final WorldEditReset WORLDEDIT = new WorldEditReset();

	@Getter
	private final String name;

	ResetType(String paramString) {
		this.name = paramString;
	}

	public static ResetType of(String name) {
		switch (name.toLowerCase()) {
			case "gradual":
				return GRADUAL;
			case "instant":
				return INSTANT;
			case "worldedit":
				return WORLDEDIT;
		}
		return null;
	}

	public abstract void reset(Mine paramMine, BlockPalette blockPalette);
}