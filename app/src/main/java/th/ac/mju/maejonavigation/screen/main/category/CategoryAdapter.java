package th.ac.mju.maejonavigation.screen.main.category;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.unity.SettingValues;

/**
 * Created by Teh on 2/14/2017.
 */

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements View.OnClickListener{

    private CategoryClick listener;
    private List<Category> listCategory;
    private static final String DRAWABLE = "drawable";

    public CategoryAdapter(List<Category> listCategory,CategoryClick listener){
        this.listCategory = listCategory;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category,null);
        ViewHolder viewHolder = new ViewHolder(card_view);
        card_view.setOnClickListener(this);
        viewHolder.cardView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = listCategory.get(position);
        holder.cardView.setTag(category);
        Context context = holder.icon_category.getContext();
        int categoryId = category.getCategoryId();
        String categoryName  = category.getCategoryName();
        holder.icon_category.setImageResource(context.getResources().getIdentifier(
                SettingValues.CATEGORY_ICON+categoryId, DRAWABLE, context.getPackageName()));
        holder.name_category.setText(categoryName);
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.card_view) {
            listener.onClick((Category) view.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.category_icon_imageview) ImageView icon_category;
        @InjectView(R.id.category_name_textview) TextView name_category;
        @InjectView(R.id.card_view)
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }

    public interface CategoryClick {
        void onClick(Category category);
    }
}

