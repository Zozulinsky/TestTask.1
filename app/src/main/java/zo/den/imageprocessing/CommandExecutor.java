package zo.den.imageprocessing;
/*
* Класс посредник
* */
public class CommandExecutor {
    public static void execute(ICommand command){
        command.execute();
    }
}
