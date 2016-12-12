package com.gogtz.yuanshun.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogtz.yuanshun.R;

import java.util.List;

import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.model.Entry;
import zuo.biao.library.util.StringUtil;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class HistoryAdapter extends BaseAdapter<Entry<String, String>> {
    public HistoryAdapter(Activity context, List<Entry<String, String>> list) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //示例代码<<<<<<<<<<<<<<<<
        ViewHolder holder = convertView == null ? null : (ViewHolder) convertView.getTag();
        if (holder == null) {
            // 显示内容
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();

            holder.tvListItem = (TextView) convertView.findViewById(R.id.tvListItem);

            convertView.setTag(holder);
        }

        final Entry<String, String> data = getItem(position);

        holder.tvListItem.setText(StringUtil.getTrimedString(data.getValue()));

        return super.getView(position, convertView, parent);
        //示例代码>>>>>>>>>>>>>>>>
    }

    static class ViewHolder {
        //示例代码<<<<<<<<<<<<<<<<
        public TextView tvListItem;
        //示例代码>>>>>>>>>>>>>>>>
    }
}
