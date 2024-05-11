package src.fr.uge.memory;

public sealed interface Word permits Name,Operator,Property{
	String Name();
	int id();
}
