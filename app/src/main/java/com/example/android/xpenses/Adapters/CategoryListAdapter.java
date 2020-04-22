package com.example.android.xpenses.Adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.xpenses.DataTypes.SubCategory;
import com.example.android.xpenses.DataTypes.Category;
import com.example.android.xpenses.R;

import java.util.ArrayList;

public class CategoryListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Category> categoryList;
    private ArrayList<Category> originalList;


    public CategoryListAdapter(Context context, ArrayList<Category> categoryList){
        this.context = context;
        this.categoryList = new ArrayList<Category>();
        this.categoryList.addAll(categoryList);
        this.originalList = new ArrayList<Category>();
        this.originalList.addAll(categoryList);
    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<SubCategory> subCategoryList = categoryList.get(groupPosition).getSubCategoryList();
        return subCategoryList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<SubCategory> subCategoryList = categoryList.get(groupPosition).getSubCategoryList();
        return subCategoryList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Category category = (Category) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_item_layout, null);
        }
        TextView categorytv = convertView.findViewById(R.id.category_tv);
        categorytv.setText(category.getName().trim());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        SubCategory subCategory = (SubCategory) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_item_layout, null);
            convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        TextView subCategorytv = convertView.findViewById(R.id.sub_category_tv);
        subCategorytv.setText(subCategory.getName().trim());


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void filterData(String query){
        query = query.toLowerCase();
        categoryList.clear();

        if(query.isEmpty()){
            categoryList.addAll(originalList);
        } else{
            for(Category category : originalList){
                ArrayList<SubCategory>  subCategoryList = category.getSubCategoryList();
                ArrayList<SubCategory> newSubCategoryList = new ArrayList<>();
                for(SubCategory subCategory : subCategoryList){
                    if(subCategory.getName().toLowerCase().contains(query)){

                        newSubCategoryList.add(subCategory);
                    }
                }
                if(newSubCategoryList.size() > 0){
                    Category nCategory = new Category(category.getName(), newSubCategoryList);
                    categoryList.add(nCategory);
                }
            }
        }
        notifyDataSetChanged();
    }
}
