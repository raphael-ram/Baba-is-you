package src.fr.uge.memory;

import java.util.Objects;

public record Name(String name, int id) implements Word{
	public Name{
		Objects.requireNonNull(name);
	}
	@Override
	public String Name() {
		return name;
	}
}
