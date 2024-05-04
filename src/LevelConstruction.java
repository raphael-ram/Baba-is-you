import java.util.ArrayList;
import java.util.Objects;

public class LevelConstruction {

	private int level;
	private ArrayList<String> keywords = new ArrayList<>();

	public LevelConstruction(int level, ArrayList<String> keywords) {
		Objects.requireNonNull(keywords);
		if (level <= 0 || level > 7) {
			throw new IllegalArgumentException("level given is not correct");
		}
		this.level = level;
	}

	public Grid startLevelGame() {
		return switch (this.level) {
		case 1 -> levelOne();
		default -> throw new IllegalArgumentException("Unexpected value: " + this.level);
		};
	}

	public Grid levelOne() {
		Grid grid = null;
		if (this.level == 1) {
			grid = new Grid(20, 1, keywords);
		}

		return grid;
	}

}
