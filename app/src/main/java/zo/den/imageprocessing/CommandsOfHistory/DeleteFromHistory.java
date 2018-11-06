package zo.den.imageprocessing.CommandsOfHistory;

import android.content.Context;

import java.io.File;

import zo.den.imageprocessing.ICommand;
/**
 * Класс-команда удаляет из папки с результатами  обработки картинку (cache/ProcessedImages), которую пользователь выбрал для удаления.
 * Всем обработанным картинкам присваивается имя от id строки (idOfRow), в которую она была помещена.
 * */
public class DeleteFromHistory implements ICommand {
    private Context contextOfActivity;
    private int nameFile;

    public DeleteFromHistory(Context context, int idRow) {
        this.contextOfActivity = context;
        this.nameFile = idRow;
    }

    @Override
    public void execute() {
        File imagesFolder = new File(contextOfActivity.getCacheDir(), "ProcessedImages");
        File imageForDelete = new File(imagesFolder+"/"+nameFile);
        imageForDelete.delete();
    }
}
