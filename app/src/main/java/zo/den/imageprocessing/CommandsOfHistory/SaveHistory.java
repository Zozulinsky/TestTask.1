package zo.den.imageprocessing.CommandsOfHistory;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import zo.den.imageprocessing.ICommand;
import zo.den.imageprocessing.MainActivity;
/**
 * Сохраняет результат обработнной картинки в папку приложения (cache/ProcessedImages).
 * Этот класс создается и вызывается в HelperSetImageInResult , т.е. после каждой обработки (Rotate, Invert Colors, Mirror)
 * и перед отображением результата обработки пользователю.
 * */
public class SaveHistory implements ICommand {
    private Bitmap processedBitmap;
    private Context contextOfActivity;

    public SaveHistory(Context context, Bitmap bitmap) {
        this.contextOfActivity = context;
        this.processedBitmap = bitmap;
    }

    @Override
    public void execute() {
        File imagesFolder = new File(contextOfActivity.getCacheDir(), "ProcessedImages");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, MainActivity.idOfRow + "");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(image);
            processedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }
    }
}
