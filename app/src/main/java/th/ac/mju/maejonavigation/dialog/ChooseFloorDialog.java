package th.ac.mju.maejonavigation.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.List;

import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.intent.PlanIntent;
import th.ac.mju.maejonavigation.model.Floor;

/**
 * Created by Teh on 5/12/2017.
 */

public class ChooseFloorDialog {
    private Dialog dialog;

    public ChooseFloorDialog(final Context context,final String locationName,final List<Floor> listFloor) {
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_floor);
        dialog.setCancelable(false);
        TextView closeIcon = (TextView) dialog.findViewById(R.id.change_type_close);
        TextView locationNameTextView = (TextView) dialog.findViewById(R.id.location_name);
        locationNameTextView.setText(locationName);
        HorizontalPicker hpText = (HorizontalPicker) dialog.findViewById(R.id.picker);
        List<HorizontalPicker.PickerItem> textItems = new ArrayList<>();
        for(Floor floor : listFloor){
            textItems.add(new HorizontalPicker.TextItem(floor.getFloorName()));
        }
        hpText.setItems(textItems);
        HorizontalPicker.OnSelectionChangeListener listener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                context.startActivity(new PlanIntent(context,locationName,listFloor.get(index)));
            }
        };
        hpText.setChangeListener(listener);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void show() {
        dialog.show();
    }
}
