package th.ac.mju.maejonavigation.screen.main.category;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import th.ac.mju.maejonavigation.model.Category;

/**
 * Created by Teh on 3/24/2017.
 */

public class CategoryPresenter{
    View view;
    public void create(View view){
        this.view = view;
    }

    public void queryListCategory(Realm realm){
        RealmResults<Category> listCategory = realm.where(Category.class)
                .findAll();
        view.showListCategory(listCategory);
    }


    public interface View {
        void showListCategory( List<Category> sortContacts);
    }
}
