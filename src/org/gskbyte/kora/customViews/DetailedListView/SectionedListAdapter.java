package org.gskbyte.kora.customViews.DetailedListView;

import java.util.LinkedList;
import java.util.List;

import org.gskbyte.kora.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SectionedListAdapter extends ArrayAdapter<Object>
{
    private LinkedList<HeaderInfo> mHeaderIndices = new LinkedList<HeaderInfo>();
    private List<Object> mModels = new LinkedList<Object>();
    private int mSelectedIndex = -1;
    
    public final static int TYPE_SECTION_HEADER = 0;
    public final static int TYPE_SECTION_CONTENT = 1;

    public SectionedListAdapter(Context context)
    {
        super(context, R.layout.list_header);
    }

    public boolean addSection(String name, List<DetailedViewModel> models)
    {
        if(!sectionExists(name)){
            int index = mModels.size();
            mModels.add(new Header(name));
            mModels.addAll(models);
            mHeaderIndices.add(new HeaderInfo(name, index, models.size()));
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean addSection(int location, String name, List<DetailedViewModel> models)
    {
        if(!sectionExists(name)){
            if(location<mHeaderIndices.size()){
                int start_index = mHeaderIndices.get(location).index;
                mModels.add(start_index, new Header(name));
                mModels.addAll(start_index+1, models);
                mHeaderIndices.add(location, new HeaderInfo(name, start_index, models.size()));
                for(int i = location+1; i<mHeaderIndices.size(); ++i)
                    mHeaderIndices.get(i).index += models.size()+1;
            } else {
                addSection(name, models);
            }
            return true;
        } else {
            return false;
        }
    }
    
    public boolean sectionExists(String name)
    {
        return getSectionIndex(name)>=0;
    }
    
    public int getSectionIndex(String name)
    {
        int index = -1;
        for(int i=0; i< mHeaderIndices.size(); ++i)
            if(mHeaderIndices.get(i).tag.equals(name))
                index = i;
        return index;
    }
    
    public boolean removeSection(int section_index)
    {
        if(section_index<mHeaderIndices.size()){
            int index = mHeaderIndices.get(section_index).index,
                nItems = mHeaderIndices.get(section_index).nItems;
            mHeaderIndices.remove(section_index);
            for(int i= index; i<nItems+1; ++i)
                mModels.remove(index);
            for(int i = section_index; i<mHeaderIndices.size(); ++i)
                mHeaderIndices.get(i).index-=nItems+1;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean removeSection(String name)
    {
        int index = getSectionIndex(name);
        if(index>=0)
            return removeSection(index);
        else
            return false;
    }
    
    public void clear()
    {
        mModels.clear();
        mSelectedIndex = -1;
    }

    public Object getItem(int position)
    {
        return mModels.get(position);
    }

    public int getCount()
    {
        return mModels.size();
    }
    
    public int getSelected()
    {
        return mSelectedIndex;
    }
    
    public void setSelected(int index)
    {
        mSelectedIndex = index;
    }

    public int getViewTypeCount()
    {
        /*
        // assume that headers count as one, then total all sections
        int total = 1;
        for(ArrayAdapter<?> adapter : this.sections.values())
            total += adapter.getViewTypeCount();
        return total;
        */
        return mModels.size();
    }

    public int getItemViewType(int position) {
        if(mModels.get(position).getClass() == Header.class)
            return TYPE_SECTION_HEADER;
        else
            return TYPE_SECTION_CONTENT;
    }

    public boolean areAllItemsSelectable()
    {
        return false;
    }

    public boolean isEnabled(int position)
    {
        return (getItemViewType(position) != TYPE_SECTION_HEADER);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Object model = mModels.get(position);
        if(model.getClass() == Header.class){
            Header h = (Header) model;
            if(convertView==null){
                 convertView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, null);
            }
            ((TextView)convertView).setText(h.tag);
        } else if (model.getClass() == DetailedViewModel.class){
            DetailedViewModel m = (DetailedViewModel) model;
            if(convertView==null){
                convertView = new DetailedListViewItem(parent.getContext(),
                        m.mMainText, m.mDescription, m.mImage);
            }
            else{
                ((DetailedListViewItem)convertView).setMainText(m.mMainText);
                ((DetailedListViewItem)convertView).setDescription(m.mDescription);
                ((DetailedListViewItem)convertView).setImage(m.mImage);
            }
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    class Header
    {
        String tag;

        public Header(String tag)
        {
            this.tag = tag;
        }
        
    }
    
    class HeaderInfo
    {
        String tag;
        int index;
        int nItems;
        
        public HeaderInfo(String tag, int index, int nItems)
        {
            this.tag=tag;
            this.index=index;
            this.nItems=nItems;
        }
    }
}
