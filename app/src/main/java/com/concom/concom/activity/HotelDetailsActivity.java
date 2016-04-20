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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brian on 29/08/2015.
 */
public class HotelDetailsActivity extends AppCompatActivity {
    private Context context;
    private Button button;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HotelViewAdapter mAdapter;
    private Toolbar mToolbar;
    List<ListViewItem> listOfViewItemList = new ArrayList<>();
    String hotelName=null, hotelCode = null,guestCount = null, interestedPeople = null, ratePerPerson = null, hotelRoom =null;
    int position;

    ArrayList<CharSequence> servList= new ArrayList<CharSequence>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_details_activity);
        context = this;

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            hotelName = extras.getString("hotelName");
            hotelRoom = extras.getString("hotelRoom");
            hotelCode = extras.getString("hotelCode");
            interestedPeople = extras.getString("interestedPeople");
            guestCount = extras.getString("guestCount");
            ratePerPerson = extras.getString("ratePerPerson");
            position= extras.getInt("position");
            servList= extras.getCharSequenceArrayList("services");
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar_hotel_view);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(guestCount.equals("1")){
            getSupportActionBar().setTitle("Quarto p/ " +guestCount+" colega no:");
        }else{
            getSupportActionBar().setTitle("Quarto p/ " +guestCount+" colegas no:");
        }

        button = (Button) findViewById(R.id.buttonReservation);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_hotel_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        ListViewItem listViewItem;
        Hotel hotel = new Hotel();
        hotel.setName(hotelName);
        hotel.setCode(hotelCode);
        hotel.setHotelRoom(hotelRoom);
        hotel.setInterestedPeople(interestedPeople);
        hotel.setGuestCount(guestCount);
        hotel.setRatePerPerson(ratePerPerson);

        if(servList!=null && !servList.isEmpty()){
            List<Services> services = new ArrayList<>();
            for(CharSequence charSequence: servList){
                Services services1 = new Services();
                services1.setName(charSequence.toString());
                services.add(services1);
            }
            hotel.setServicesList(services);
        }

        listViewItem = new ListViewItem(hotel, HotelViewAdapter.VIEW_TYPE_ITEM);
        listOfViewItemList.add(listViewItem);
        mAdapter = new HotelViewAdapter(this, listOfViewItemList,position);

        if (mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void onClickReservation(View view) {
        new SendReservation().execute("1",hotelCode, hotelRoom);
    }
    ProgressDialog pDialogGetHotels;
    private class SendReservation extends AsyncTask<String, String, Map<String,Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogGetHotels = new ProgressDialog(context);
            pDialogGetHotels.setMessage("Por favor, aguarde. Estamos confirmando seu interesse nesse quarto");
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
                url.append("http://concom-rod3go.rhcloud.com/interest/save.json");

                Map<String,Object> personMap = new HashMap<String,Object>();
                personMap.put("person", args[0]);
                personMap.put("hotelCode", args[1]);
                personMap.put("hotelRoom", args[2]);

                //CONVERTING MAP IN STRING
                String person= mapper.writeValueAsString(personMap);
                String response = HttpRequest.getInstance().post(url.toString(), person);

                if (response != null) {
                    map=new HashMap<>();
                }

            } catch (IOException e) {
                Log.e("AAA", "errror");
                e.printStackTrace();

            }
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            if (map != null) {
                Intent i = new Intent(context, ConfirmarReserva.class);
                Bundle b = new Bundle();
                b.putString("guestCount",guestCount);
                b.putString("interestedPeople",interestedPeople);
                i.putExtras(b);
                pDialogGetHotels.dismiss();
                startActivity(i);
                finish();
            }
            pDialogGetHotels.dismiss();
        }
    }


    public class HotelViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_ITEM = 1;
        private List<ListViewItem> listViewItemList;
        private Context mContext;
        private Hotel hotel;
        private String rating;
        private int pos;
        public HotelViewAdapter(Context c, List<ListViewItem> list, int position) {
            mContext = c;
            listViewItemList = list;
            this.pos= position;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hotel_details_listitem, parent, false);
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
                //List<Services> list = hotel.getServicesList();
                if (pos % 6 == 0) {
                    vh.image.setImageResource(R.drawable.hoteis001);
                    rating = "3,28";
                } else if (pos % 6 == 1) {
                    vh.image.setImageResource(R.drawable.hoteis002);
                    rating = "4,07";
                } else if (pos % 6 == 3) {
                    vh.image.setImageResource(R.drawable.hoteis003);
                    rating = "3,28";
                } else if (pos % 6 == 4) {
                    vh.image.setImageResource(R.drawable.hoteis004);
                    rating = "2,80";
                } else if (pos % 6 == 5) {
                    vh.image.setImageResource(R.drawable.hoteis005);
                    rating = "3.78";
                } else if (pos % 6 == 6) {
                    vh.image.setImageResource(R.drawable.hoteis006);
                    rating = "4,87";
                }
                vh.rating.setText(rating);
                vh.numberOfRoomMates.setText(interestedPeople);
                vh.price.setText(ratePerPerson);

                StringBuilder str = new StringBuilder();
                for(CharSequence services:servList){
                    str.append(services.toString() );
                    str.append(", ");
                }
                //vh.services.setText(str.toString());
            }
        }

        public class ViewHolderBody extends RecyclerView.ViewHolder  {
            public RelativeLayout relativeLayout;
            public TextView name,price, rating,numberOfRoomMates, priceNote, description;
            public ImageView image;

            private ClickListener clickListener;

            public ViewHolderBody(View itemView) {
                super(itemView);

                image = (ImageView) itemView.findViewById(R.id.imageViewHotelDetails);
                name = (TextView) itemView.findViewById(R.id.tvHotelDetailsName);
                price = (TextView) itemView.findViewById(R.id.textViewHotelDetailsPrice);
                priceNote = (TextView) itemView.findViewById(R.id.textViewHotelDetailsPriceNote);
                rating = (TextView) itemView.findViewById(R.id.textViewHotelDetailsRating);
                numberOfRoomMates = (TextView) itemView.findViewById(R.id.textViewHotelsDetailsRoomMate);
                description = (TextView) itemView.findViewById(R.id.tvHotelViewDescription);

                name.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));
                price.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));
                priceNote.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));
                rating.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));
                numberOfRoomMates.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));
                description.setTypeface(TypeFaces.getTypeFace(mContext, "Roboto_Thin.ttf"));

            }

        }
    }
}
