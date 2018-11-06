package zo.den.imageprocessing.CommandsForCompletedImage;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TableRow;

import zo.den.imageprocessing.ICommand;

/**
* Класс достает из папки приложения /cache/ProcessedImage обработанную картинку и помещает в chooseImage
* для дальнейшей повторной обработки.
*/

public class TakeImageFromResults implements ICommand {
    private ImageView chooseImage;
    private TableRow tableRow;
    private Context contextOfActivity;

    TakeImageFromResults(Context context, ImageView imageView, TableRow tableRow) {
        this.contextOfActivity = context;
        this.chooseImage = imageView;
        this.tableRow = tableRow;
    }
    @Override
    public void execute() {
        chooseImage.setImageBitmap(BitmapFactory.decodeFile(contextOfActivity.getCacheDir()+"/ProcessedImages/"+tableRow.getId()));
    }
}
