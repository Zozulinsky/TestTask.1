package zo.den.imageprocessing.CommandsForSelectImage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import zo.den.imageprocessing.ICommand;

/**
 * Класс вызывает системную камеру. После того, как пользователь сделал снимок,
 * результат отправляется в метод onActivityResult класса MainActivity, а после отображается в ImageView chooseImage.
 * */

public class TakeImageFromCamera implements ICommand {
    private final int REQUEST_CAMERA = 1;
    private Context contextOfActivity;

    public TakeImageFromCamera(Context context) {
        this.contextOfActivity = context;
    }

    @Override
    public void execute() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ((Activity) contextOfActivity).startActivityForResult(intent, REQUEST_CAMERA);
    }
}
