package com.mindorks.placeholderview;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janisharali on 18/08/16.
 */

public class ExpandableViewAdapter<T, V extends View> extends ViewAdapter<T>
        implements ExpandableViewBinder.ExpansionCallback<T, V>{

    private ExpandableViewBinder<T, V> mParentBinder;

    public ExpandableViewAdapter(Context context) {
        super(context);
    }

    @Override
    protected void addView(T viewResolver) throws IndexOutOfBoundsException {
        ExpandableViewBinder<T, V> expandableViewBinder = new ExpandableViewBinder(viewResolver);
        if(expandableViewBinder.bindViewType(expandableViewBinder.getResolver())){
            mParentBinder = expandableViewBinder;
            mParentBinder.setCallback(this);
            getViewBinderList().add(expandableViewBinder);
            notifyItemInserted(getViewBinderList().size() - 1);
        }
        else if(mParentBinder != null){
            int position = mParentBinder.getChildList().size() - 1;
            if(position == -1){
                position = getViewBinderList().indexOf(mParentBinder) + 1;
            }else{
                position = getViewBinderList().indexOf(mParentBinder.getChildList().get(position)) + 1;
            }
            mParentBinder.getChildList().add(expandableViewBinder);
            getViewBinderList().add(position, expandableViewBinder);
            notifyItemInserted(position);
        }
    }

    @Override
    protected void addView(int position, T viewResolver) throws IndexOutOfBoundsException {

    }

    @Override
    protected void addView(T resolverOld, T resolverNew, boolean after) throws Resources.NotFoundException {

    }

    @Override
    public void onExpand(ExpandableViewBinder<T, V> parentBinder) {
        int position = getViewBinderList().indexOf(parentBinder) + 1;
        for(ExpandableViewBinder<T, V> viewBinder : parentBinder.getChildList()) {
            getViewBinderList().add(position, viewBinder);
            notifyItemInserted(position);
        }
    }

    @Override
    public void onCollapse(ExpandableViewBinder<T, V> parentBinder) {
        for(ExpandableViewBinder<T, V> viewBinder : parentBinder.getChildList()){
            removeView(viewBinder.getResolver());
        }
    }

    private void collapseOthers(){

    }
}
