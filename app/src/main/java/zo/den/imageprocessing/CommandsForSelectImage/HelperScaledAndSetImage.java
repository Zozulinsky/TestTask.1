package zo.den.imageprocessing.CommandsForSelectImage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import zo.den.imageprocessing.MainActivity;
import zo.den.imageprocessing.R;
/**
 * Класс-помощник. Если пользователь выбрал картинку из Интернета или из галереи, то в данном классе
 * происходит сжатие картинки для корректного отображения, а затем помещается в ImageView chooseImage (MainActivity).
 * Если скачивание/сжатие занимает время, то отображается {@link ProgressDialog} в виде обычной крутилки.
 * */

public class HelperScaledAndSetImage extends AsyncTask<String, Void, Bitmap> {
    private Context contextOfActivity;
    private ImageView chooseImage;
    private ProgressDialog bar;

    public HelperScaledAndSetImage(Context context, ImageView imageView) {
        this.contextOfActivity = context;
        this.chooseImage = imageView;
    }

    @Override
    public Bitmap doInBackground(String... params) {
        Bitmap bitmap;
        InputStream in;
        try {
            in = checkParam(params[0]);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();
            int origWidth = o.outWidth;
            int origHeight = o.outHeight;
            int bytesPerPixel = 2;
            int maxSize = 480 * 800 * bytesPerPixel;
            int desiredWidth = 200;
            int desiredHeight = 200;
            int desiredSize = desiredWidth * desiredHeight * bytesPerPixel;
            if (desiredSize < maxSize) maxSize = desiredSize;
            int scale = 1;
            if (origWidth > origHeight) {
                scale = Math.round((float) origHeight / (float) desiredHeight);
            } else {
                scale = Math.round((float) origWidth / (float) desiredWidth);
            }
            o = new BitmapFactory.Options();
            o.inSampleSize = scale;
            o.inPreferredConfig = Bitmap.Config.RGB_565;
            in = checkParam(params[0]);
            bitmap = BitmapFactory.decodeStream(in, null, o);
            in.close();
        } catch (Exception exception) {
            Log.e("Ошибка загрузки", exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    public void onPreExecute() {
        bar = new ProgressDialog((Activity) contextOfActivity);
        bar.setMessage(contextOfActivity.getResources().getString(R.string.processingDownload));
        bar.setIndeterminate(false);
        bar.show();
        super.onPreExecute();
    }

    protected void onPostExecute(final Bitmap result) {
        bar.dismiss();
        if (result == null) {
            Toast toast = Toast.makeText(
                    contextOfActivity,
                    contextOfActivity.getResources().getString(R.string.errorSetImage),
                    Toast.LENGTH_SHORT);
            toast.show();
        }else {
            chooseImage.setImageBitmap(result);
        }
    }
    private InputStream checkParam(String param){
        InputStream in = null;
        if (android.util.Patterns.WEB_URL.matcher(param).matches()==true) {
            try {
                MainActivity.pathToChooseImage="";
                java.net.URL url = new java.net.URL(param);
                in = url.openStream();
            } catch (IOException ioException) {
                Log.e("Fail download", ioException.getMessage());
                ioException.printStackTrace();
            }
        }else {
            Uri uri = Uri.parse(param);
            try {
                in = contextOfActivity.getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException fileNotFound) {
                Log.e("File do not exist", fileNotFound.getMessage());
                fileNotFound.printStackTrace();
            }catch (Exception exception){}
        }
        return in;
    }
}