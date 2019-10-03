package textadventure;

/** Interface to wrap logic to be done later. 
We could use java's `java.lang.Runnable` type instead, 
but this naming is closer to C#'s version... */
public interface Action {
	 void execute();
}
