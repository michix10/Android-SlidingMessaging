package com.klinker.android.messaging_sliding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.klinker.android.messaging_donate.R;
import com.klinker.android.messaging_donate.utils.SendUtil;

import java.util.ArrayList;

public class ImageArrayAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] images;

    static class ViewHolder {
        public ImageView image;
    }

    public ImageArrayAdapter(Activity context, String[] images) {
        super(context, R.layout.image_view, new ArrayList<String>());
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.image_view, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) rowView.findViewById(R.id.contactBubble1);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        try {
            holder.image.setImageURI(Uri.parse(images[position]));
        } catch (Exception e) {
            try {
                Bitmap image = SendUtil.getThumbnail(context, Uri.parse(images[position]));
                holder.image.setImageBitmap(image);
            } catch (Exception f) {

            }
        }

        holder.image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra("SingleItemOnly", true);
                intent.setDataAndType(Uri.parse(images[position]), "image/*");
                context.startActivity(intent);

            }

        });

        return rowView;
    }
}
