
import java.util.Objects;

import Word;

public record Name(String s) implements Word{
	
	public Name{
		Objects.requireNonNull(s);	
	}
}
