package dev.xkmc.l2hostility.content.logic;

public record LevelEditor(DifficultyLevel difficulty, int extra) {

	public boolean setBase(int level) {
		int old = difficulty().level;
		difficulty().level = level;
		difficulty().exp = 0;
		return level != old;
	}

	public boolean addBase(int level) {
		int old = difficulty().level;
		difficulty().level = Math.max(0, difficulty().level + level);
		if (level < 0) {
			difficulty().exp = 0;
		}
		return difficulty().level != old;
	}

	public int getBase() {
		return difficulty().level;
	}

	public boolean setTotal(int level) {
		return addTotal(level - getTotal());
	}

	public boolean addTotal(int level) {
		int old = difficulty().getLevel();
		if (level + getTotal() < 0) {
			level = -getTotal();
		}
		difficulty().extraLevel += level;
		return old != difficulty().getLevel();
	}

	public int getTotal() {
		return difficulty().getLevel() + extra;
	}

}
