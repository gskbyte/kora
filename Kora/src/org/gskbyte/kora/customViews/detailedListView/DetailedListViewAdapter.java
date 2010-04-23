package org.gskbyte.kora.customViews.detailedListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class DetailedListViewAdapter extends ArrayAdapter<DetailedViewModel>
{
    private List<DetailedViewModel> mModels = new ArrayList<DetailedViewModel>();
    
    public DetailedListViewAdapter(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
    }

    public void addAllModels(List<DetailedViewModel> models)
    {
        mModels.addAll(models);
    }
    
    public void addModel(DetailedViewModel model)
    {
        mModels.add(model);
    }
    
    public void addModel(int position, DetailedViewModel model)
    {
        mModels.add(position, model);
    }
    
    public void clear()
    {
        mModels.clear();
    }

    public int getCount()
    {
        return mModels.size();
    }

    public DetailedViewModel getItem(int position)
    {
        return mModels.get(position);
    }

    public Collection<DetailedViewModel> getAllItems()
    {
        return mModels;
    }
    
    public long getItemId(int position)
    {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DetailedListViewItem v;
        if (convertView == null) {
            v = new DetailedListViewItem(parent.getContext(),
                    mModels.get(position).mMainText,
                    mModels.get(position).mDescription,
                    mModels.get(position).mImage
                    );
            
        } else {
            v = (DetailedListViewItem) convertView;
            v.setMainText(mModels.get(position).mMainText);
            v.setDescription(mModels.get(position).mDescription);
            v.setImage(mModels.get(position).mImage);
        }

        return v;
    }    
}
