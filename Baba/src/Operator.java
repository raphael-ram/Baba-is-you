import java.util.Objects;

public record Operator(String s) implements Word{
	public Operator{
		Objects.requireNonNull(s);
		
	}
}
