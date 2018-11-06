package zo.den.imageprocessing.CommandsForCompletedImage;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;
import zo.den.imageprocessing.CommandExecutor;
import zo.den.imageprocessing.CommandsOfHistory.DeleteFromHistory;
import zo.den.imageprocessing.ICommand;
import zo.den.imageprocessing.MainActivity;

/**
* Если пользователь выберет удалить картинку из результата, то:
* 1. Выполнятся проверки:
*   1.1 содержится ли удаляемая картинка в последней строке. Если да, то перед удалением картинки
*   из tableWithResults уменьшается на единицу idOfRow. Это необходимо для правильного выбора цвета.
*   1.2 является ли удаляемая картинка единственной в tableWithResults. Если да, то idOfRow обнуляется.
*
* 2. Обработанная картинки удалится из папки приложения, чтобы между запусками она больше не появлялась. Функционал
* реализуется и описан в классе DeleteFromHistory.
*/

public class RemoveImageFromResults implements ICommand {
    private TableLayout tableWithResults;
    private TableRow tableRow;
    private Context contextOfActivity;

    RemoveImageFromResults(Context context, TableLayout tableLayout, TableRow tableRow) {
        this.contextOfActivity = context;
        this.tableWithResults = tableLayout;
        this.tableRow = tableRow;
    }

    @Override
    public void execute() {
        if (tableRow.getId()==MainActivity.idOfRow)
            MainActivity.idOfRow--;
        tableWithResults.removeView(tableRow);
        if (tableWithResults.getChildCount() == 0)
            MainActivity.idOfRow = 0;

        ICommand delete = new DeleteFromHistory(contextOfActivity, tableRow.getId());
        CommandExecutor.execute(delete);
    }
}
