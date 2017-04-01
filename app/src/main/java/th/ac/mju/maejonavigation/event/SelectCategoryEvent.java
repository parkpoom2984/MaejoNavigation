package th.ac.mju.maejonavigation.event;

import th.ac.mju.maejonavigation.model.Category;

/**
 * Created by Teh on 2/18/2017.
 */

public class SelectCategoryEvent {
    private Category category;

    public SelectCategoryEvent(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}
