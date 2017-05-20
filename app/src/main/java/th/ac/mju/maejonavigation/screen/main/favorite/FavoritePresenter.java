package th.ac.mju.maejonavigation.screen.main.favorite;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.screen.main.location.LocationPresenter;

/**
 * Created by Teh on 5/20/2017.
 */

public class FavoritePresenter {
    private Realm realm;
    private View view;
    private static final String FAVORITE_STATUS = "favoriteStatus";

    public void create(View view,Realm realm) {
        this.view = view;
        this.realm = realm;
    }

    public void queryLocationFavorite(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Locations> listFavoriteLocation = realm.where(Locations.class).equalTo(FAVORITE_STATUS,1).findAll();
                view.showListLocationFavorite(listFavoriteLocation);
            }
        });
    }

    interface View{
        void showListLocationFavorite(List<Locations> listLocation);
    }
}
