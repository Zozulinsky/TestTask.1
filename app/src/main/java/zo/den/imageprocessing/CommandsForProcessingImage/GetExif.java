package zo.den.imageprocessing.CommandsForProcessingImage;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import zo.den.imageprocessing.ICommand;
import zo.den.imageprocessing.MainActivity;
import zo.den.imageprocessing.R;

/**
 * Класс отображает EXIF-информация выбранной картинки, при условии, что картинка выбрана из галереи.
 * В этом же классе обрабатывается Изменение и сохранение новой Exif-информации.
 * В новой EXIF изменяется модель аппарата, сделавшего снимок, на имя приложения.
 * */

public class GetExif implements ICommand {
    private Context contextOfActivity;
    private String realPathToChooseImage;
    private ExifInterface exif;

    public GetExif(Context context) {
        this.contextOfActivity = context;
    }

    @Override
    public void execute() {
        AlertDialog.Builder getExif = new AlertDialog.Builder(contextOfActivity);
        View dialogOfGetExif = LayoutInflater.from(contextOfActivity)
                .inflate(R.layout.dialogofchangeexif, null);
        getExif.setView(dialogOfGetExif);
        final TextView textViewWithExif = (TextView) dialogOfGetExif.findViewById(R.id.readexif);
        if (!MainActivity.pathToChooseImage.equals("")) {
            realPathToChooseImage = getRealPathFromURI(contextOfActivity, Uri.parse(MainActivity.pathToChooseImage));
            try {
                exif = new ExifInterface(realPathToChooseImage);
                String myAttribute = "---\n";
                myAttribute += contextOfActivity.getResources().getString(R.string.date) +
                        exif.getAttribute(ExifInterface.TAG_DATETIME) + "\n";
                myAttribute += contextOfActivity.getResources().getString(R.string.resolution) +
                        exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) + " х " +
                        exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) + "\n";
                myAttribute += contextOfActivity.getResources().getString(R.string.createWith) +
                        exif.getAttribute(ExifInterface.TAG_MODEL) + "\n";
                textViewWithExif.setText(myAttribute);
            } catch (IOException e) {
                e.printStackTrace();
            }
            getExif.setTitle(contextOfActivity.getResources().getString(R.string.exiftags));
            getExif
                    .setCancelable(true)
                    .setPositiveButton(contextOfActivity.getResources().getString(R.string.changeInfoAboutCamera),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(realPathToChooseImage);
                                    saveImageWithChangedExif(bitmap);
                                }
                            })
                    .setNegativeButton(contextOfActivity.getResources().getString(R.string.Cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog showExif = getExif.create();
            showExif.show();
        } else {
            Toast toast = Toast.makeText(contextOfActivity,
                    contextOfActivity.getResources().getString(R.string.imageNoHaveExif),
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }
    private void saveImageWithChangedExif(Bitmap bitmap) {
        StringBuilder changePath = new StringBuilder("/storage/emulated/0/" + System.currentTimeMillis() + ".jpg");
        File pathForWriting = new File(changePath.toString());
        FileOutputStream fos = null;
        try {
            ExifInterface oldExif = new ExifInterface(realPathToChooseImage);
            String exifModel = oldExif.getAttribute(ExifInterface.TAG_MODEL);
            if (exifModel == null) {
                exifModel = contextOfActivity.getResources().getString(R.string.unknownModel);
            }
            fos = new FileOutputStream(pathForWriting);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos);
            ExifInterface newExif = new ExifInterface(pathForWriting.getPath());
            newExif.setAttribute(ExifInterface.TAG_MODEL, contextOfActivity.getResources().getString(R.string.app_name));
            newExif.saveAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                MediaScannerConnection.scanFile(contextOfActivity,
                        new String[]{changePath.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getRealPathFromURI(Context context, Uri contentUri) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
