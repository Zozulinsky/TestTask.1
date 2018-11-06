package zo.den.imageprocessing;
/**
 * Этот интерфейс является связующим звеном между командами и клиентом (MainActivity)
 * */
public interface ICommand {
    void execute();
}
