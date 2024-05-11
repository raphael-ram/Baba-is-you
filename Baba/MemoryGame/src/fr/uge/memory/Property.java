package src.fr.uge.memory;

import java.util.Objects;

public record Property(String name, int id) implements Word{
	public Property{
		Objects.requireNonNull(name);
	}
	@Override
	public String Name() {
		return name;
	}
}
