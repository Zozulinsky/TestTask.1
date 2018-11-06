package zo.den.imageprocessing.CommandsForProcessingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;

import zo.den.imageprocessing.CommandsOfHistory.SaveHistory;
import zo.den.imageprocessing.ICommand;
import zo.den.imageprocessing.MainActivity;

/**
 * Класс-команда создан как "помощник" в создании новой строки в tableWithResults с обработанной картинкой.
 * Перед помещением обработанной картинки отображается Progressbar в течении 5-30 секунд (искусственое замедление).
 * Каждой новой строке задается id и цвет, в зависимости от четности(ЗЕЛЕНЫЙ) или нечетности(ЖЕЛТЫЙ) заданного id.
 * Также обработанная картинка помещается в папку приложения (cache/ProcessedImages).
 * */

public class HelperSetImageInResult implements ICommand {
    private Context contextOfActivity;
    private Bitmap processedBitmap;
    private ImageView viewWithProcessedImage;
    private TableRow newRowWithProcessedImage;

    HelperSetImageInResult(Context context, Bitmap bitmap, TableRow tableRow) {
        this.contextOfActivity = context;
        processedBitmap = bitmap;
        newRowWithProcessedImage = tableRow;
    }
    @Override
    public void execute() {
        viewWithProcessedImage = new ImageView(contextOfActivity);
        final ProgressBar progressBar = new ProgressBar(contextOfActivity, null, android.R.attr.progressBarStyleHorizontal);
        setIdAndColor(newRowWithProcessedImage);

        ICommand saveResult = new SaveHistory(contextOfActivity, processedBitmap);
        saveResult.execute();

        newRowWithProcessedImage.addView(progressBar);
        progressBar.setProgress(0);
        final Handler h = new Handler(){
            public void handleMessage(android.os.Message msg){
                if (msg.what==100||msg.what>100) {
                    newRowWithProcessedImage.removeAllViews();
                    newRowWithProcessedImage.addView(viewWithProcessedImage);
                    viewWithProcessedImage.setImageBitmap(processedBitmap);
                }
            }
        };
        int max = 30;
        int min =5;
        max -= min;
        final int wait = (int) (Math.random() * ++max) + min;
        final int valueOfProgress = 100/wait +1;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=1; i<=wait; i++){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.setProgress(i*valueOfProgress);
                }
                if (progressBar.getProgress()==100||progressBar.getProgress()>100) {
                    h.sendEmptyMessage(progressBar.getProgress());
                }
            }
        });
        t.start();
    }
    private void setIdAndColor(TableRow tableRow){
        MainActivity.idOfRow++;
        tableRow.setId(MainActivity.idOfRow);
        int id=tableRow.getId();
        if (id%2 == 0){
            tableRow.setBackgroundColor(Color.GREEN);
        }
        else{
            tableRow.setBackgroundColor(Color.YELLOW);
        }
    }
}
