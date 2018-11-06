package zo.den.imageprocessing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import zo.den.imageprocessing.CommandsForCompletedImage.CallDialogForChooseActionOnImage;
import zo.den.imageprocessing.CommandsForProcessingImage.GetExif;
import zo.den.imageprocessing.CommandsForProcessingImage.InvertColors;
import zo.den.imageprocessing.CommandsForProcessingImage.Mirror;
import zo.den.imageprocessing.CommandsForProcessingImage.Rotate;
import zo.den.imageprocessing.CommandsForSelectImage.CallDialogForChooseSourceOfImage;
import zo.den.imageprocessing.CommandsForSelectImage.HelperScaledAndSetImage;
import zo.den.imageprocessing.CommandsOfHistory.GetFromHistory;

/**
 * Приложение спроектировано по паттерну "Команда".
 * Выбранная картинка устанавливается в ImageView chooseImage.
 * Обработка вызывается методами onClickRotate, onClickMirror, onClickInvertColors.
 * onClickGetExif - выводит EXIF информацию, предлагая изменить 1 из аттрибутов EXIF (модель камеры на имя приложения)
 * После обработки результат помещается TableLayout tableWithResults, а также в папку приложения /cache/ProcessedImage. Результаты доступны только этому приложению.
 * Перед помещением результата в tableWithResults создается новая строка при помощи метода createNewRow.
 * Переменная idOfRow служит для того, чтобы новая строка изменяла свой цвет в зависимости от четности/нечетности ее порядкового номера.
 * pathToChooseImage использутся для отображения EXIF и хранит в себе путь к выбранной картинки из галереи.
 * bitmapForRestoreChooseImage сохраняет в себе выбранную картинку из chooseImage для последующего восстановления при изменении ориентации и восстановлении состояния.
 *
 * Всем обработанным картинкам присваивается имя от id строки (idOfRow), в которую она была помещена.
 */

public class MainActivity extends AppCompatActivity {

    private final int SELECT_FILE = 0;
    private final int REQUEST_CAMERA = 1;

    private ImageView chooseImage;
    private TableLayout tableWithResults;
    public static int idOfRow = 0;
    public static String pathToChooseImage="";

    private Bitmap bitmapForRestoreChooseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chooseImage = (ImageView) findViewById(R.id.chooseImage);
        tableWithResults = (TableLayout) findViewById(R.id.tableWithResults);

        ICommand getHistory = new GetFromHistory(this, tableWithResults, chooseImage);
        CommandExecutor.execute(getHistory);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bitmapForRestoreChooseImage = ((BitmapDrawable) chooseImage.getDrawable()).getBitmap();
        outState.putParcelable("chooseImage", bitmapForRestoreChooseImage);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bitmapForRestoreChooseImage = savedInstanceState.getParcelable("chooseImage");
        chooseImage.setImageBitmap(bitmapForRestoreChooseImage);
    }

    public void onClickChooseImage(View view) {
        ICommand callDialog = new CallDialogForChooseSourceOfImage(this, chooseImage);
        CommandExecutor.execute(callDialog);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == RESULT_OK) {
                    new HelperScaledAndSetImage(this, chooseImage).execute(imageReturnedIntent.getData().toString());
                    pathToChooseImage =imageReturnedIntent.getData().toString();
                }
                break;
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    chooseImage.setImageBitmap(bitmap);
                    pathToChooseImage = "";
                }
                break;
        }
    }

    private TableRow createNewRow(TableLayout tableLayout) {
        final TableRow tableRow = new TableRow(this);
        tableLayout.addView(tableRow);
        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                ICommand callDialog = new CallDialogForChooseActionOnImage(MainActivity.this, tableWithResults, tableRow, chooseImage);
                CommandExecutor.execute(callDialog);
            }
        });
        return tableRow;
    }

    public void onClickRotate(View view) {
        TableRow newRowWithProcessedImage = createNewRow(tableWithResults);
        ICommand rotate = new Rotate(this, chooseImage, newRowWithProcessedImage);
        CommandExecutor.execute(rotate);
    }

    public void onClickMirror(View view) {
        TableRow newRowWithProcessedImage = createNewRow(tableWithResults);
        ICommand mirror = new Mirror(this, chooseImage, newRowWithProcessedImage);
        CommandExecutor.execute(mirror);
    }

    public void onClickInvertColors(View view) {
        TableRow newRowWithProcessedImage = createNewRow(tableWithResults);
        ICommand invertColors = new InvertColors(this, chooseImage, newRowWithProcessedImage);
        CommandExecutor.execute(invertColors);
    }

    public void onClickGetExif(View view) {
        ICommand getExif = new GetExif(this);
        CommandExecutor.execute(getExif);
    }
}
