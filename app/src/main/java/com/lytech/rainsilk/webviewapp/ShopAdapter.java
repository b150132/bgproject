package com.lytech.rainsilk.webviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;


/**
 * Created by rainsilk on 2016/3/31.
 */
public class ShopAdapter extends BaseAdapter {

    //集合 ，存放ListView的商品实体类数据
    private List<Product> products;
    //上下文
    private Context context;

    //第一步，设置接口
    private View.OnClickListener onAddNum;
    private View.OnClickListener onSubNum;

    //第二步，设置接口方法
    public void setOnAddNum(View.OnClickListener onAddNum){
        this.onAddNum = onAddNum;
    }

    public void setOnSubNum(View.OnClickListener onSubNum){
        this.onSubNum = onSubNum;
    }
    public ShopAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (products != null) {
            ret = products.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = null;
        if (view != null) {
            v = view;
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.item_cart,viewGroup,false);
        }

        ViewHolder holder = (ViewHolder) v.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.item_product_name = (TextView) v.findViewById(R.id.item_product_name);
            holder.item_product_num = (TextView) v.findViewById(R.id.item_product_num);
            holder.item_product_price = (TextView) v.findViewById(R.id.item_product_price);

            //第三步,设置接口回调，注意参数不是上下文，它需要ListView所在的Activity或者Fragment处理接口回调方法
            holder.item_btn_add = (ImageButton) v.findViewById(R.id.item_btn_add);
            holder.item_btn_add.setOnClickListener(onAddNum);

            holder.item_btn_sub = (ImageButton) v.findViewById(R.id.item_btn_sub);
            holder.item_btn_sub.setOnClickListener(onSubNum);

        }

        holder.item_product_name.setText(products.get(i).getName());
        holder.item_product_num.setText(products.get(i).getNum()+"");
        holder.item_product_price.setText(products.get(i).getPrice() + "");

        //第四步，设置Tag，用于判断用户当前点击的哪一个列表项的按钮
        holder.item_btn_add.setTag(i);
        holder.item_btn_sub.setTag(i);

        v.setTag(holder);
        return v;
    }
    private static class ViewHolder{
        //商品名称，数量，总价
        private TextView item_product_name;
        private TextView item_product_num;
        private TextView item_product_price;
        //增减商品数量按钮
        private ImageButton item_btn_add;
        private ImageButton item_btn_sub;

    }
}
