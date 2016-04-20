package com.concom.concom.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.concom.concom.ClickListener;
import com.concom.concom.HttpRequest;
import com.concom.concom.ListViewItem;
import com.concom.concom.R;
import com.concom.concom.TypeFaces;
import com.concom.concom.model.Hotel;
import com.concom.concom.model.Services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brian on 29/08/2015.
 */
public class HotelsListActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView mRecyclerView;
    private TextView stickHeader;
    private RecyclerView.LayoutManager mLayoutManager;
    private HotelListAdapter mAdapter;
    ListViewItem listViewItem;
    private ProgressDialog pDialogGetHotels;
    String cityName=null,code = null,guestCount = null, startDate = null,endDate = null,price = null;
    private Toolbar mToolbar;
    List<ListViewItem> listOfViewItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotels_list_activity);
        context = this;

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            cityName = extras.getString("cityName");
            code = extras.getString("hotelCode");
            guestCount = extras.getString("guestCount");
            startDate = extras.getString("startDate");
            endDate = extras.getString("endDate");
            price = extras.getString("maxPrice");
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar_hotels_list);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("O ConCom encontrou:");

        stickHeader = (TextView) findViewById(R.id.stick_header_hotels_list);
        stickHeader.setTypeface(TypeFaces.getTypeFace(this, "Roboto_Light.ttf"));
        stickHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_hotels_list);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        new GetHotels().execute(code,guestCount,startDate,endDate,price);

        mAdapter = new HotelListAdapter(this, listOfViewItemList);

        if (mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private class GetHotels extends AsyncTask<String, String, Map<String,Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogGetHotels = new ProgressDialog(context);
            pDialogGetHotels.setMessage("Por favor, aguarde. Estamos buscando os hotéis");
            pDialogGetHotels.setIndeterminate(false);
            pDialogGetHotels.setCancelable(false);
            pDialogGetHotels.show();
        }

        @Override
        protected Map<String, Object> doInBackground(String... args) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> map = null;
            try {
                StringBuilder url = new StringBuilder();
                url.append("http://concom-rod3go.rhcloud.com/hotel/index.json?");
                url.append("hotelCityCode=");
                url.append(args[0]); //cityCode
                url.append("&guestCounts=");
                url.append(args[1]);//guestCount
                url.append("&startDate=");
                url.append(args[2]); //startDate
                url.append("&endDate=");
                url.append(args[3]); //endDate
                url.append("&maxPrice=");
                url.append(args[4]); //ratePerPerson

                String response = HttpRequest.getInstance().get(url.toString());

                if (response != null) {
                    JsonNode rootNode = mapper.readValue(response, JsonNode.class);
                    map=new HashMap<>();
                    map.put("hotels", rootNode);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("BBB", "errror");

            }
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            if (map != null) {
                if (!map.isEmpty()) {
                    try {
                        listOfViewItemList.clear();
                        List<Hotel> listAllHotels = new ArrayList<>();
                        ObjectMapper mapper = new ObjectMapper();
                        if (map.containsKey("hotels")) {
                            JsonNode rootNode = (JsonNode) map.get("hotels");
                            for (JsonNode jNode : rootNode) {
                                if (jNode.isMissingNode()) {
                                    throw new IllegalArgumentException("Could not find id!");
                                } else {
                                    Hotel hotel = null;
                                    hotel = mapper.treeToValue(jNode, Hotel.class);

                                    listAllHotels.add(hotel);
                                /*int cityId = rootNode.path("city").path("id").intValue();
                                hotel.setCityId(cityId);*/
                                }
                            }
                            if (!listAllHotels.isEmpty()) {
                                for (int i = 0; i < listAllHotels.size(); i++) {
                                    listViewItem = new ListViewItem(listAllHotels.get(i), HotelListAdapter.VIEW_TYPE_ITEM);
                                    listOfViewItemList.add(listViewItem);
                                }
                                if(guestCount.equals("1")){
                                    stickHeader.setText(listAllHotels.size()+" Hotéis em " +cityName +" para " +guestCount+ " colega de quarto");
                                }else{
                                    stickHeader.setText(listAllHotels.size()+" Hotéis em " +cityName +" para " +guestCount+ " colegas de quarto");
                                }
                            }else{
                                stickHeader.setText("Nenhum hotel foi encontrado!");
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        Log.e("CCC", "errror");

                    }
                }
            }
            pDialogGetHotels.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pDialogGetHotels != null)
            pDialogGetHotels.dismiss();
    }

    public class HotelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_ITEM = 1;
        private List<ListViewItem> listViewItemList;
        private Context mContext;
        private Hotel hotel;
        private String rating;
        List<Services> services;

        public HotelListAdapter(Context c, List<ListViewItem> list) {
            mContext = c;
            listViewItemList = list;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hotels_list_listitem, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolderBody vh = new ViewHolderBody(v);

            return vh;
        }

        @Override
        public int getItemViewType(int position) {
            return VIEW_TYPE_ITEM;
        }

        @Override
        public int getItemCount() {
            return listViewItemList.size();

        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            ViewHolderBody vh = (ViewHolderBody) holder;
            hotel = (Hotel) listViewItemList.get(position).getObject();
            if (hotel != null) {
                String name = hotel.getName().toLowerCase();
                name = name.replace(name.charAt(0), Character.toUpperCase(name.charAt(0)));
                vh.name.setText(name);
                services = hotel.getServicesList();

                if (position % 6 == 0) {
                    vh.image.setImageResource(R.drawable.hoteis001);
                    rating = "3,28";
                } else if (position % 6 == 1) {
                    vh.image.setImageResource(R.drawable.hoteis002);
                    rating = "4,07";
                } else if (position % 6 == 3) {
                    vh.image.setImageResource(R.drawable.hoteis003);
                    rating = "3,28";
                } else if (position % 6 == 4) {
                    vh.image.setImageResource(R.drawable.hoteis004);
                    rating = "2,80";
                } else if (position % 6 == 5) {
                    vh.image.setImageResource(R.drawable.hoteis005);
                    rating = "3.78";
                } else if (position % 6 == 6) {
                    vh.image.setImageResource(R.drawable.hoteis006);
                    rating = "4,87";
                }
                vh.rating.setText(rating);
                vh.numberOfRoomMates.setText(hotel.getInterestedPeople());
                vh.price.setText(hotel.getRatePerPerson());

                vh.setClickListener(new ClickListener() {
                    @Override
                    public void onClick(View v, int position, boolean isLongClick) {
                        Intent intent = new Intent(mContext, HotelDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("hotelName", hotel.getName());
                        bundle.putString("hotelCode", hotel.getCode());
                        bundle.putString("ratePerPerson", hotel.getRatePerPerson());
                        bundle.putString("rating", rating);
                        bundle.putString("hotelRoom", hotel.getHotelRoom());
                        bundle.putString("guestCount", guestCount);
                        bundle.putString("interestedPeople", hotel.getInterestedPeople());
                        bundle.putInt("position", position);
                        if(services!=null && !services.isEmpty()){
                            ArrayList<CharSequence> servList= new ArrayList<CharSequence>();
                            for(Services s:services){
                                servList.add(s.getName());
                            }
                            bundle.putCharSequenceArrayList("services", servList);
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        }

        public class ViewHolderBody extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
            // each data item is just a string in this case
            public RelativeLayout relativeLayout;
            public TextView name,price, rating,numberOfRoomMates, priceNote;
            public ImageView image;

            private ClickListener clickListener;

            public ViewHolderBody(View itemView) {
                super(itemView);

                image = (ImageView) itemView.findViewById(R.id.imageViewHotelsList);
                name = (TextView) itemView.findViewById(R.id.tvHotelsListName);
                price = (TextView) itemView.findViewById(R.id.textViewHotelsListPrice);
                priceNote = (TextView) itemView.findViewById(R.id.textViewHotelsListPriceNote);
                rating = (TextView) itemView.findViewById(R.id.textViewHotelsListRating);
                numberOfRoomMates = (TextView) itemView.findViewById(R.id.textViewHotelsListRoomMate);

                name.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));
                price.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));
                priceNote.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));
                rating.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));
                numberOfRoomMates.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));

                itemView.setOnClickListener(this);
                //itemView.setOnLongClickListener(this);
            }

            /* Setter for listener. */
            public void setClickListener(ClickListener clickListener) {
                this.clickListener = clickListener;
            }

            @Override
            public void onClick(View v) {
                // If not long clicked, pass last variable as false.
                clickListener.onClick(v, getAdapterPosition(), false);
            }

            /*@Override
            public boolean onLongClick(View v) {
                // If long clicked, passed last variable as true.
                clickListener.onClick(v, getAdapterPosition(), true);
                return true;
            }*/
        }
    }



}
