package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rainsilk on 2016/3/31.
 */
public class ShopCart extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<Product> datas; //数据源
    private ShopAdapter adapter; //自定义适配器
    private ListView listView;   //ListView控件
    int ProductNumber = 6;    //商品數量
    private List<Integer> list;
    private TextView text;
    boolean[] getprice = new boolean[6];
    String[] cost = new String[6];
    String ShopName; //商家名稱 username

    //创建观察者
    private DataSetObserver sumObserver = new DataSetObserver() {
        /**
         * 当Adapter的notifyDataSetChanged方法执行时被调用
         */
        @Override
        public void onChanged() {
            super.onChanged();
            //执行相应的操作
            int sum = 0;
            for (int i = 0; i < datas.size(); i++) {
                //計算總金額
                sum += datas.get(i).getPrice();
            }
            text.setText("總金额: " + sum + "元");
        }

        /**
         * 当Adapter 调用 notifyDataSetInvalidate方法执行时被调用
         */
        @Override
        public void onInvalidated() {
            super.onInvalidated();
            //执行相应的操作
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopcart_main);

        getprice = new boolean[]{false, false, false, false, false, false};

        Intent intent = getIntent();
        String[] s = new String[6];
        String[] snum = {"s1", "s2", "s3", "s4", "s5", "s6"};

        String[] costnum = {"cost1", "cost2", "cost3", "cost4", "cost5", "cost6"};
        for (int i = 0; i < ProductNumber; i++) {
            s[i] = intent.getStringExtra(snum[i]);
            cost[i] = intent.getStringExtra(costnum[i]);
        }
        ShopName = intent.getStringExtra("shopname");

        listView = (ListView) findViewById(R.id.listView);

        // 模拟数据
        datas = new ArrayList<Product>();
        Product product = null;
        for (int i = 0; i < ProductNumber; i++) {
            product = new Product();
            product.setName("商品：" + s[i] + "   價格:" + cost[i]);
            product.setNum(1);
            product.setPrice(Integer.parseInt(cost[i].substring(0, cost[i].length() - 1))); //減去元字
            datas.add(product);
        }
        adapter = new ShopAdapter(datas, this);
        listView.setAdapter(adapter);

        //以上就是我们常用的自定义适配器ListView展示数据的方法了

//解决问题：在哪里处理按钮的点击响应事件，是适配器 还是 Activity或者Fragment，这里是在Activity本身处理接口
        //执行添加商品数量，减少商品数量的按钮点击事件接口回调
        adapter.setOnAddNum(this);
        adapter.setOnSubNum(this);
        listView.setOnItemClickListener(this);

        //注册观察者
        adapter.registerDataSetObserver(sumObserver);
        text = (TextView) findViewById(R.id.text);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //list.set(i,2);
                //将列表项的0变为2 更新适配器，
                adapter.notifyDataSetChanged();
                //执行该方法后DataSetObserver观察者观察到
            }
        });

    }

    //点击某个按钮的时候，如果列表项所需的数据改变了，如何更新UI
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销观察者
        adapter.unregisterDataSetObserver(sumObserver);
    }

    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        switch (view.getId()) {
            case R.id.item_btn_add: //点击添加数量按钮，执行相应的处理
                // 获取 Adapter 中设置的 Tag

                if (tag != null && tag instanceof Integer) { //解决问题：如何知道你点击的按钮是哪一个列表项中的，通过Tag的position
                    int position = (Integer) tag;
                    //更改集合的数据
                    int num = datas.get(position).getNum();
                    num++;

                    datas.get(position).setNum(num); //修改集合中商品数量
                    int price = Integer.parseInt(cost[position].substring(0, cost[position].length() - 1));
                    datas.get(position).setPrice(price * num); //修改集合中该商品总价 数量*单价
                    //解决问题：点击某个按钮的时候，如果列表项所需的数据改变了，如何更新UI
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.item_btn_sub: //点击减少数量按钮 ，执行相应的处理
                // 获取 Adapter 中设置的 Tag
                if (tag != null && tag instanceof Integer) {
                    int position = (Integer) tag;

                    //更改集合的数据
                    int num = datas.get(position).getNum();
                    int price = Integer.parseInt(cost[position].substring(0, cost[position].length() - 1));
                    if (num > 0) {
                        num--;
                        datas.get(position).setNum(num); //修改集合中商品数量
                        datas.get(position).setPrice(price * num); //修改集合中该商品总价 数量*单价
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.imgbtnBuynow:
                AlertDialog.Builder ab = new AlertDialog.Builder(ShopCart.this);

                ab.setTitle("購物確認")
                        .setMessage("請再次確認您的商品 並出示此頁面給您的店家 一經確認即無法再修改是否確認?")
                        .setCancelable(true);

                ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int sum = 0;
                        String[] pname = new String[datas.size()];
                        String[] n = new String[datas.size()];
                        String[] nu = new String[datas.size()];
                        int[] pnum = new int[datas.size()];

                        for (int i = 0; i < datas.size(); i++) {
                            //記錄最後資訊
                            pname[i] = datas.get(i).getName();
                            pnum[i] = datas.get(i).getNum();
                            sum += datas.get(i).getPrice();
                            n[i] = "n" + i;
                            nu[i] = "nu" + i;
                        }

                        //先記錄商品訂購數量 計算銷售量
                        Commodity commodity;
                        Context mCtx;
                        mCtx = ShopCart.this;

                        for (int k = 0; k < datas.size(); k++) {
                            commodity = new Commodity(Integer.toString(k+1) , ShopName, Integer.toString(pnum[k]));

                            ServerRequests serverRequests = new ServerRequests(mCtx);
                            serverRequests.storeCommSaleInBackground(commodity, new GetUserCallback() {
                                @Override
                                public void done(Contact returnedContact) {

                                }
                            });
                        }
                        //Display info on Displaybuything intent
                        Intent intent = new Intent(ShopCart.this, DisplayBuyThing.class);
                        for (int j = 0; j < datas.size(); j++) {
                            intent.putExtra(n[j], pname[j]);
                            intent.putExtra(nu[j], pnum[j]);
                        }
                        intent.putExtra("productnum", datas.size());
                        intent.putExtra("sum", sum);
                        startActivity(intent);


                        //TODO:指派配送並結帳
                    }
                });
                ab.setNegativeButton(android.R.string.cancel, null);
                ab.show();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(ShopCart.this, "選擇了第" + i + "項商品", Toast.LENGTH_SHORT).show();
    }
}