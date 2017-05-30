package th.ac.mju.maejonavigation.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import th.ac.mju.maejonavigation.model.Category;

/**
 * Created by TOON-PC on 20/5/2560.
 */

public class SelectCategoryDialog{

    private View listener;

    private AlertDialog.Builder builder;
    ArrayList<Integer> listPositionCategory = new ArrayList<>();

    public SelectCategoryDialog(Context context,View listener){
        builder = new AlertDialog.Builder(context);
        this.listener = listener;
    }

    public void create(final List<Category> listCategory){
        listCategory.add(0,new Category(0,"กิจกรรม"));
        boolean index[] = new boolean[listCategory.size()];
        String[] listCategoryString = new String[listCategory.size()];
        for (int i = 0; i < listCategory.size(); i++) {
            listCategoryString[i] = listCategory.get(i).getCategoryName();
        }
        builder.setTitle("Please Select Location");
        builder.setMultiChoiceItems(listCategoryString, index, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    int categoryId = listCategory.get(which).getCategoryId();
                    if (isChecked) {
                        listPositionCategory.add(categoryId);
                    } else {
                        for (int i = 0; i < listPositionCategory.size(); i++) {
                            if (listPositionCategory.get(i) == categoryId) {
                                listPositionCategory.remove(i);
                                break;
                            }
                        }
                    }
                }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClickPositiveButton(listPositionCategory);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                listener.onClickNeutralButton();
            }
        });
    }

    public void removeListPosition(){
        listPositionCategory.clear();
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public interface View{
        void onClickPositiveButton(ArrayList<Integer> listPositionCategory);

        void onClickNeutralButton();
    }
}
