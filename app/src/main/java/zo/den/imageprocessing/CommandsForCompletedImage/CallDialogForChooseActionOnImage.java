package zo.den.imageprocessing.CommandsForCompletedImage;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import zo.den.imageprocessing.CommandExecutor;
import zo.den.imageprocessing.ICommand;
import zo.den.imageprocessing.R;

/**
* Этот класс вызывает диалоговое окно после тапа на обработанную картинку.
* На выбор предлагается Удалить картинку или же Выбрать источником для последующей обработки.
* Выбранное действия выполняется в соответствующем классе: RemoveImageFromResults или TakeImageFromResults.
*/

public class CallDialogForChooseActionOnImage implements ICommand {
    private Context contextOfActivity;
    private TableLayout tableWithResults;
    private TableRow tableRow;
    private ImageView chooseImage;

    public CallDialogForChooseActionOnImage(Context context, TableLayout tableLayout, TableRow tableRow, ImageView imageView) {
        this.contextOfActivity = context;
        this.tableWithResults = tableLayout;
        this.tableRow = tableRow;
        this.chooseImage = imageView;
    }

    @Override
    public void execute() {
        AlertDialog.Builder builderActionWithCell = new AlertDialog.Builder(contextOfActivity);
        builderActionWithCell.setTitle(contextOfActivity.getResources().getString(R.string.titleDialogChooseAction));
        builderActionWithCell
                .setCancelable(true)
                .setPositiveButton(contextOfActivity.getResources().getString(R.string.delete),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ICommand remove = new RemoveImageFromResults(contextOfActivity, tableWithResults, tableRow);
                                CommandExecutor.execute(remove);
                            }
                        })
                .setNegativeButton(contextOfActivity.getResources().getString(R.string.chooseAsSource),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ICommand chooseAsSource = new TakeImageFromResults(contextOfActivity, chooseImage, tableRow);
                                CommandExecutor.execute(chooseAsSource);
                            }
                        });
        AlertDialog actionWithCell = builderActionWithCell.create();
        actionWithCell.show();
    }
}
