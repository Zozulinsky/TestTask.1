package zo.den.imageprocessing.CommandsForSelectImage;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import zo.den.imageprocessing.ICommand;
import zo.den.imageprocessing.R;
/**
 * В классе вызывается кастомизированный диалог (dialogforinputurl.xml) для ввода URL
 * и последующей передачи введенного URL в класс HelperScaledAndSetImage, в котором будет обработано скачивание картинки,
 * ее сжатие, и установка в ImageView chooseImage.
 * В случае неверного ввода URL пользователю отображается Toast "Введен неверный URL."
 * */
public class TakeImageFromURL implements ICommand {
    private Context contextOfActivity;
    private ImageView chooseImage;

    TakeImageFromURL(Context context, ImageView imageView) {
        this.contextOfActivity = context;
        this.chooseImage = imageView;
    }

    @Override
    public void execute() {
        AlertDialog.Builder builderInputURLOfImage = new AlertDialog.Builder(contextOfActivity);
        View dialogOfInputURL = LayoutInflater.from(contextOfActivity)
                .inflate(R.layout.dialogofinputurl, null);
        builderInputURLOfImage.setView(dialogOfInputURL);
        final EditText userInput = (EditText) dialogOfInputURL.findViewById(R.id.input_url);
        builderInputURLOfImage.setTitle(contextOfActivity.getResources().getString(R.string.downloadFromURL));
        builderInputURLOfImage
                .setCancelable(false)
                .setPositiveButton(contextOfActivity.getResources().getString(R.string.Ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String url = userInput.getText().toString();
                                if (android.util.Patterns.WEB_URL.matcher(url).matches() == true) {
                                    new HelperScaledAndSetImage(contextOfActivity, chooseImage).execute(url);
                                } else {
                                    Toast toast = Toast.makeText(contextOfActivity,
                                            contextOfActivity.getResources().getString(R.string.wrongURL),
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        })
                .setNegativeButton(contextOfActivity.getResources().getString(R.string.Cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog inputURLOfImage = builderInputURLOfImage.create();
        inputURLOfImage.show();
    }
}