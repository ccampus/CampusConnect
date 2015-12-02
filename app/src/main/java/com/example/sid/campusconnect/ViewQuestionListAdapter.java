package com.example.sid.campusconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Sid on 01-Dec-15.
 */
public class ViewQuestionListAdapter extends ArrayAdapter<ParseObject>
{
    protected Context mContext;
    protected List<ParseObject> mStatus;

    public ViewQuestionListAdapter(Context context, List<ParseObject> status) {
        super(context, R.layout.view_questions_list, status);
        mContext = context;
        mStatus = status;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.view_questions_list, null);

            holder = new ViewHolder();

            holder.QuestionTitle = (TextView) convertView
                    .findViewById(R.id.QsTitle);

            holder.QuestionCategory = (TextView) convertView
                    .findViewById(R.id.QsCategory);

            holder.QuestionId = (TextView) convertView
                    .findViewById(R.id.QsId);

            convertView.setTag(holder);
        }
        else
        {

            holder = (ViewHolder) convertView.getTag();
        }
        ParseObject statusObject = mStatus.get(position);

        // Getting username
        String Qstitle = statusObject.getString("Title");
        holder.QuestionTitle.setText(Qstitle);

        // getting Department
        String Qscategory = statusObject.getString("Category");
        holder.QuestionCategory.setText(Qscategory);

        //getting object id of the user
        String qsid = statusObject.getObjectId();
        holder.QuestionId.setText(qsid);

        return convertView;
    }

    public static class ViewHolder
    {
        TextView QuestionTitle;
        TextView QuestionCategory;
        TextView QuestionId;
    }

}