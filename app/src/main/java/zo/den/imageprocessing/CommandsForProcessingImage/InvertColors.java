package zo.den.imageprocessing.CommandsForProcessingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TableRow;

import zo.den.imageprocessing.ICommand;

/**
 * В классе обрабатывается нажатие кнопки invertColors (В черно-белое).
 * После конвертирования цветов в ч/б, результат отправляется в HelperSetImageInResult для отображения
 * результата в приложении.
 * */

public class InvertColors implements ICommand {
    private Context contextOfActivity;
    private ImageView chooseImage;
    private TableRow newRowWithProcessedImage;

    public InvertColors(Context context, ImageView imageView, TableRow tableRow) {
        this.contextOfActivity = context;
        chooseImage = imageView;
        newRowWithProcessedImage = tableRow;
    }
    @Override
    public void execute() { final ImageView imageView = new ImageView(contextOfActivity);
        Bitmap bitmap = ((BitmapDrawable)chooseImage.getDrawable()).getBitmap();
        final Bitmap processedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(processedBitmap);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(processedBitmap, 0, 0, paint);

        ICommand addResult = new HelperSetImageInResult(contextOfActivity, processedBitmap, newRowWithProcessedImage);
        addResult.execute();
    }
}
