package edu.odu.cs.air411.wherearfthou;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindow(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.maps_infowindow, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView details_tv = view.findViewById(R.id.details);
        ImageView img = view.findViewById(R.id.pic);

        TextView description_tv = view.findViewById(R.id.description);
        TextView tags_tv = view.findViewById(R.id.tags);
        TextView contactInfo_tv = view.findViewById(R.id.contactInfo);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        ReportData reportWindowData = (ReportData) marker.getTag();

        int imageId = context.getResources().getIdentifier(reportWindowData.getImage().toLowerCase(),
                "drawable", context.getPackageName());
        img.setImageResource(imageId);

        description_tv.setText(reportWindowData.getDescription());
        tags_tv.setText(reportWindowData.getTags());
        contactInfo_tv.setText(reportWindowData.getContact());

        return view;
    }
}