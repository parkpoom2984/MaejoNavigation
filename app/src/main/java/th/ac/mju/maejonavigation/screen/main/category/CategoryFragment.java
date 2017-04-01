package th.ac.mju.maejonavigation.screen.main.category;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import de.greenrobot.event.EventBus;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.event.SelectCategoryEvent;
import th.ac.mju.maejonavigation.fragment.MjnFragment;
import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.screen.main.MainActivity;

import static th.ac.mju.maejonavigation.screen.main.MainActivity.State.LOCATION_PAGE;


public class CategoryFragment extends MjnFragment implements CategoryPresenter.View,CategoryAdapter.CategoryClick {


    @InjectView(R.id.category_recycler_view)
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.inject(this,view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        CategoryPresenter categoryPresenter = new CategoryPresenter();
        categoryPresenter.create(this);
        categoryPresenter.queryListCategory(getRealm());
        return view;
    }



    @Override
    public void showListCategory(List<Category> listCategory) {
        CategoryAdapter categoryAdapter = new CategoryAdapter(listCategory,this);
        mRecyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void onClick(Category category) {
        EventBus bus = EventBus.getDefault();
        bus.post(new SelectCategoryEvent(category));
        ((MainActivity)getActivity()).switchTabTo(LOCATION_PAGE.getPosition());
    }
}
