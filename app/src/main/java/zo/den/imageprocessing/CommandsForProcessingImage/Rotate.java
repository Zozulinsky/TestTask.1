package zo.den.imageprocessing.CommandsForProcessingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TableRow;

import zo.den.imageprocessing.ICommand;

/**
 * В классе обрабатывается нажатие кнопки rotate (Повернуть).
 * Картинка поворачивается на 90 градусов и отправляется в HelperSetImageInResult для отображения
 * результата в приложении.
 * */

public class Rotate implements ICommand {
    private Context contextOfActivity;
    private ImageView chooseImage;
    private TableRow newRowWithProcessedImage;

    public Rotate(Context context, ImageView imageView, TableRow tableRow) {
        this.contextOfActivity = context;
        chooseImage = imageView;
        newRowWithProcessedImage = tableRow;
    }

    @Override
    public void execute() {
        Bitmap bitmap = ((BitmapDrawable)chooseImage.getDrawable()).getBitmap();
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        final Bitmap processedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        ICommand addResult = new HelperSetImageInResult(contextOfActivity, processedBitmap, newRowWithProcessedImage);
        addResult.execute();
    }
}
