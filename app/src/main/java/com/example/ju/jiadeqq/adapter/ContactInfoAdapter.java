package com.example.ju.jiadeqq.adapter;

import java.util.List;

import com.example.ju.jiadeqq.R;
import com.example.ju.jiadeqq.activity.ImApp;
import com.example.ju.jiadeqq.domain.ContactInfo;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactInfoAdapter extends ArrayAdapter<ContactInfo> {

	public ContactInfoAdapter(Context context, List<ContactInfo> objects) {
		super(context, 0, objects);
	}

	class ViewHolder {
		ImageView icon;
		TextView title;
		TextView desc;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactInfo info = getItem(position);

		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(getContext(),
					R.layout.view_item_contact, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (info.avatar == 0) {
			holder.icon.setImageResource(R.drawable.ic_launcher);
		} else {
			holder.icon.setImageResource(info.avatar);
		}

		holder.title.setText(info.account + "");

		holder.desc.setText(info.nick);
		return convertView;
	}
}
