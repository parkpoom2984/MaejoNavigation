package th.ac.mju.maejonavigation.screen.plan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;
import th.ac.mju.maejonavigation.model.Floor;
import th.ac.mju.maejonavigation.model.Room;
import th.ac.mju.maejonavigation.unity.SettingValues;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static th.ac.mju.maejonavigation.intent.PlanIntent.FLOOR;
import static th.ac.mju.maejonavigation.intent.PlanIntent.LOCATION_NAME;
import static th.ac.mju.maejonavigation.intent.PlanIntent.ROOM_ID;

public class PlanActivity extends MjnActivity implements PlanPresenter.View {
    Bitmap board;
    Bitmap drawBoard;
    Canvas mCanvas;
    Floor floor;
    String locationName;
    int roomId;
    @InjectView(R.id.plan_card_view)
    CardView planCardView;
    @InjectView(R.id.plan_item_image)
    ImageView planImage;
    @InjectView(R.id.plan_item_capacity)
    TextView roomCapacity;
    @InjectView(R.id.plan_item_room_detail)
    TextView roomDetail;
    @InjectView(R.id.plan_item_room_type)
    TextView roomType;
    @InjectView(R.id.plan_table_button)
    TableLayout tableButtonRoom;
    @InjectView(R.id.plan_item_title)
    TextView title;
    List<Room> listRoom;
    @InjectView(R.id.adView)
    AdView adView;
    @InjectView(R.id.plan_tool_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.inject(this);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.mjn_while));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.floor_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(
                ContextCompat.getDrawable(this, R.drawable.ic_close_white));
        floor = Parcels.unwrap(this.getIntent().getParcelableExtra(FLOOR));
        locationName = getIntent().getStringExtra(LOCATION_NAME);
        roomId = getIntent().getIntExtra(ROOM_ID, -1);
        PlanPresenter planPresenter = new PlanPresenter(this);
        planPresenter.getListRoom(getRealm(), floor.getFloorId());
        roomType.setVisibility(GONE);
        roomCapacity.setVisibility(GONE);
        checkNetworkAvailable();
        initAd();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void showListRoom(List<Room> listRoom) {
        this.listRoom = listRoom;
        updateUI(listRoom);
    }

    private void updateUI(final List<Room> listRoom) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        Thread th = new Thread() {
            @Override
            public void run() {
                board = getBitMapFromURL(
                        SettingValues.IMAGEFlOOR_PATH + floor.getImagePlanPath() + ".jpg");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (board == null) {
                            planImage.setImageResource(R.drawable.img_default);
                        } else {
                            planImage.setImageBitmap(board);
                            makeTableButton(listRoom);
                            if (roomId != -1) {
                                for (int i = 0; i < listRoom.size(); i++) {
                                    if (listRoom.get(i).getRoomId() == roomId) {
                                        Button button = (Button) findViewById(i);
                                        button.setBackgroundResource(R.drawable.button_map);
                                        int width = listRoom.get(i).getWidth();
                                        int high = listRoom.get(i).getHigh();
                                        drawImg(width, high);
                                        break;
                                    }
                                }
                            }
                        }
                        title.setText(locationName + " ชั้น " + floor.getFloorName());
                        roomDetail.setVisibility(VISIBLE);
                        roomType.setVisibility(VISIBLE);
                        roomCapacity.setVisibility(VISIBLE);
                        for (Room room : listRoom) {
                            if (room.getRoomId() == roomId) {
                                roomDetail.setText(getResources().getString(R.string.room_detail,
                                        room.getRoomDetail()));
                                roomType.setText(getResources().getString(R.string.room_type,
                                        room.getRoomType()));
                                roomCapacity.setText(
                                        getResources().getString(R.string.room_capacity,
                                                room.getRoomCapacity()));
                                break;
                            }
                            //title.setTypeface(SettingValues.GET_FONT(PlanActivity.this));
                        }

                        planCardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PlanActivity.this,
                                        ZoomImageActivity.class);


                                int[] screenLocation = new int[2];
                                planImage.getLocationOnScreen(screenLocation);


                                if (drawBoard == null) {
                                    createImageFromBitmap(board);
                                } else {
                                    createImageFromBitmap(drawBoard);
                                }
                                //Pass the image title and url to DetailsActivity
                                intent.putExtra("left", screenLocation[0]).
                                        putExtra("top", screenLocation[1]).
                                        putExtra("width", planImage.getWidth()).
                                        putExtra("height", planImage.getHeight()).
                                        putExtra("title",
                                                locationName + " ชั้น " + floor.getFloorName());

                                //Start details activity
                                startActivity(intent);
                            }
                        });
                        planCardView.setVisibility(VISIBLE);
                        progressDialog.cancel();
                    }
                });
            }
        };
        th.start();
    }


    public void drawImg(int width, int high) {
        if (width != 0 & high != 0) {
            drawBoard = board.copy(board.getConfig().RGB_565, true);
            Rect rect = new Rect();
            rect.set(250, 400, 300, 300);

            mCanvas = new Canvas(drawBoard);
            Paint paint = new Paint();

            paint.setAntiAlias(true);
            paint.setColor(ContextCompat.getColor(this, R.color.mjn_primary));
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(8.5f);

            mCanvas.drawCircle((float) width, (float) high, 30, paint);
            planImage.setImageBitmap(drawBoard);
        }
    }

    public void makeTableButton(final List<Room> listRoom) {
        TableRow tableRow = null;
        for (int i = 0; i < listRoom.size(); i++) {
            if (i % 3 == 0) {
                tableRow = new TableRow(this);
                tableRow.setLayoutParams(
                        new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
                tableButtonRoom.addView(tableRow);
            }
            final Button button = new Button(this);
            button.setText(listRoom.get(i).getRoomName());
            button.setId(i);
            button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < listRoom.size(); i++) {
                        Button button = (Button) findViewById(i);
                        button.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    roomDetail.setVisibility(VISIBLE);
                    roomType.setVisibility(VISIBLE);
                    roomCapacity.setVisibility(VISIBLE);
                    roomDetail.setText(getResources().getString(R.string.room_detail,
                            listRoom.get(v.getId()).getRoomDetail()));
                    roomType.setText(getResources().getString(R.string.room_type,
                            listRoom.get(v.getId()).getRoomType()));
                    roomCapacity.setText(getResources().getString(R.string.room_capacity,
                            listRoom.get(v.getId()).getRoomCapacity()));
                    button.setBackgroundResource(R.drawable.button_map);
                    drawImg(listRoom.get(v.getId()).getWidth(), listRoom.get(v.getId()).getHigh());
                }
            });
            tableRow.addView(button);
        }
    }

    public Bitmap getBitMapFromURL(String src) {
        try {
            URL url = new URL(src);
            Bitmap floorBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return floorBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public void checkNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnect = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Snackbar snackbar = Snackbar.make(planCardView,
                getString(R.string.internet_can_not_connect), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.try_again), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkAvailable();
                updateUI(listRoom);
            }
        });
        if (!isConnect) {
            snackbar.show();
        } else {
            snackbar.dismiss();
        }
    }

    public void initAd() {
        AdRequest.Builder adBuilder = new AdRequest.Builder();
        adBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        AdRequest adRequest = adBuilder.build();
        adView.loadAd(adRequest);
    }
}
