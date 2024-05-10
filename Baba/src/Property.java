import java.util.Objects;

public record Property(String s) implements Word{
	public Property{
		Objects.requireNonNull(s);
		
	}
}
