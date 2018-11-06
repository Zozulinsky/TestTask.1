package zo.den.imageprocessing.CommandsOfHistory;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import zo.den.imageprocessing.CommandExecutor;
import zo.den.imageprocessing.CommandsForCompletedImage.CallDialogForChooseActionOnImage;
import zo.den.imageprocessing.ICommand;
import zo.den.imageprocessing.MainActivity;
/**
 * Класс-команда, который перед каждым запуском сканит наличие обработанных изображений.
 * В случае их наличия добавляет в tableWithResults новые строки и помещает в них ImageView с картинкой.
 * Каждой новой строке задается действия в случае для того, чтобы пользователь смог удалить картинку из результатов,
 * либо выбрать ее источником для следующей обработки.
 * */

public class GetFromHistory implements ICommand {
    private Context contextOfActivity;
    private TableLayout tableWithResults;
    private ImageView chooseImage;
    private ImageView viewWithProcessedImage;
    private TableRow newRowWithProcessedImage;


    public GetFromHistory(Context context, TableLayout tableLayout, ImageView imageView) {
        this.contextOfActivity = context;
        this.tableWithResults = tableLayout;
        this.chooseImage = imageView;
    }
    @Override
    public void execute() {
        File imagesFolder = new File(contextOfActivity.getCacheDir(), "ProcessedImages");
        File[] allFiles = imagesFolder.listFiles();
        if (allFiles != null) {
            Arrays.sort(allFiles, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Integer.compare(Integer.parseInt(f1.getName()), Integer.parseInt(f2.getName()));
                }
            });
            int k=0;
            for (File image : allFiles) {
                k++;
                if (k==allFiles.length)
                    MainActivity.idOfRow=Integer.parseInt(image.getName());
                newRowWithProcessedImage = new TableRow(contextOfActivity);
                viewWithProcessedImage = new ImageView(contextOfActivity);
                newRowWithProcessedImage.setId(Integer.parseInt(image.getName()));
                newRowWithProcessedImage.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
                if ((int) newRowWithProcessedImage.getId() % 2 == 0) {
                    newRowWithProcessedImage.setBackgroundColor(Color.GREEN);
                } else {
                    newRowWithProcessedImage.setBackgroundColor(Color.YELLOW);
                }
                tableWithResults.addView(newRowWithProcessedImage);
                newRowWithProcessedImage.addView(viewWithProcessedImage);
                viewWithProcessedImage.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
                newRowWithProcessedImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View V) {
                        ICommand callDialog = new CallDialogForChooseActionOnImage(contextOfActivity, tableWithResults, newRowWithProcessedImage, chooseImage);
                        CommandExecutor.execute(callDialog);
                    }
                });
            }
        }
    }
}
