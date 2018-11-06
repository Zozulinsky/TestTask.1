package zo.den.imageprocessing.CommandsForProcessingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TableRow;

import zo.den.imageprocessing.ICommand;

/**
 * В классе обрабатывается нажатие кнопки mirror (Зеркальное отображение).
 * После изменения картинки, результат отправляется в HelperSetImageInResult для отображения
 * результата в приложении.
 * */

public class Mirror implements ICommand {
    private Context contextOfActivity;
    private ImageView chooseImage;
    private TableRow newRowWithProcessedImage;

    public Mirror(Context context, ImageView imageView, TableRow tableRow) {
        this.contextOfActivity = context;
        chooseImage = imageView;
        newRowWithProcessedImage = tableRow;
    }
    @Override
    public void execute() {
        Bitmap bitmap = ((BitmapDrawable)chooseImage.getDrawable()).getBitmap();
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        Bitmap processedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        ICommand addResult = new HelperSetImageInResult(contextOfActivity, processedBitmap, newRowWithProcessedImage);
        addResult.execute();
    }
}
