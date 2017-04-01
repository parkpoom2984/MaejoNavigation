package th.ac.mju.maejonavigation.screen.main.location;

import java.util.List;


import io.realm.Realm;
import io.realm.RealmResults;

import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.model.Location;

/**
 * Created by Teh on 3/31/2017.
 */

public class LocationPresenter {

    private Realm realm;
    private View view;
    private static final String CATEGORY_ID = "categoryId";

    public void create(View view,Realm realm) {
        this.view = view;
        this.realm = realm;
    }

    public void queryListLocation(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Location> listLocation = realm.where(Location.class)
                        .findAll();
                view.showListLocation(listLocation);
            }
        });
    }

    public void queryListLocationByCategory(final int categoryId){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
               Category category = realm.where(Category.class).equalTo(CATEGORY_ID,categoryId)
                        .findFirstAsync();
                view.showListLocationByCategory(category.getListLocation());
            }
        });
    }

    interface View{
        void showListLocation(List<Location> listLocation);
        void showListLocationByCategory(List<Location> listLocationByCategory);
    }
}
