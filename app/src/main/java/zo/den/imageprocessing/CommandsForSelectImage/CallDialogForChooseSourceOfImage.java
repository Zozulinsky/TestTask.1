package zo.den.imageprocessing.CommandsForSelectImage;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import zo.den.imageprocessing.ICommand;
import zo.den.imageprocessing.R;
/**
 * Этот класс создан для вызова диалогового с предложением пользователю выбрать источник картинки для
 * последующей обработки: из камеры, из галереи, из интернета.
 * После выбора источника создается соответствующий класс-команда.
 * */
public class CallDialogForChooseSourceOfImage implements ICommand {
    private Context contextOfActivity;
    private ImageView chooseImage;

    public CallDialogForChooseSourceOfImage(Context context, ImageView imageView) {
        this.contextOfActivity = context;
        this.chooseImage = imageView;
    }
    @Override
    public void execute() {
        final CharSequence[] items = {contextOfActivity.getResources().getString(R.string.takePhoto),
                                        contextOfActivity.getResources().getString(R.string.takeFromGallery),
                                        contextOfActivity.getResources().getString(R.string.downloadFromURL)};
        final AlertDialog.Builder sourceOfImage = new AlertDialog.Builder(contextOfActivity);
        sourceOfImage.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(contextOfActivity.getResources().getString(R.string.takePhoto))) {
                    new TakeImageFromCamera(contextOfActivity).execute();
                } else if (items[item].equals( contextOfActivity.getResources().getString(R.string.takeFromGallery))) {
                    new TakeImageFromGallery(contextOfActivity).execute();
                }else if (items[item].equals(contextOfActivity.getResources().getString(R.string.downloadFromURL))) {
                    new TakeImageFromURL(contextOfActivity, chooseImage).execute();
                }
            }
        });
        sourceOfImage.show();
    }
}
