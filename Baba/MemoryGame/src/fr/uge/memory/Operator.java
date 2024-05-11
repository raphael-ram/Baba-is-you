package src.fr.uge.memory;

import java.util.Objects;

public record Operator(String name, int id) implements Word{
	public Operator{
		Objects.requireNonNull(name);
	}
	@Override
	public String Name() {
		return name;
	}
}
