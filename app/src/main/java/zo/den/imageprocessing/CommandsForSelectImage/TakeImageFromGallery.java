package zo.den.imageprocessing.CommandsForSelectImage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import zo.den.imageprocessing.ICommand;

import static android.content.ContentValues.TAG;
/**
 * Класс обрабатывает выбор пользователя "Выбрать из галереи". Перед тем как откроется галерея,
 * пользователю будет предложено предоставить приложению доступ к памяти телефона при условии, что версия
 * Android >= 6.0
 * Выбранная картинка помещается в ImageView chooseImage через метод onActivityResult класса MainActivity.
 * */
public class TakeImageFromGallery implements ICommand {
    private final int SELECT_FILE_OR_URL = 0;
    private Context contextOfActivity;
    TakeImageFromGallery(Context context) {
        this.contextOfActivity = context;
    }
    @Override
    public void execute() {
        if (isStoragePermissionGranted()) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            ((Activity) contextOfActivity).startActivityForResult(intent, SELECT_FILE_OR_URL);
        }
    }

    private  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (contextOfActivity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions((Activity) contextOfActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}

