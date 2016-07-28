package com.lytech.rainsilk.webviewapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rainsilk on 2016/1/22.
 */
public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 15000; //15 second
    public static final String SERVER_ADDRESS = 	"http://lytechly.comxa.com/";//"http://rainsilk.comli.com/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("系統執行中");
        progressDialog.setMessage("請稍後...");
    }

    //register memberdata
    public String doInBackground(Contact contact, String memberID, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "doinbackgroud;");
        new GetCount(contact, memberID, callback).execute();
        return memberID;
    }

    public void storeDataInBackground(Contact contact, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "開始註冊會員資料..");
        new StoreDataAsyncTask(contact, callback).execute();
    }

    //fetch memberdata
    public void fetchDataInBackground(Contact contact, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "開始取得會員資料..");
        new FetchDataAsyncTask(contact, callback).execute();
    }

    //set user location
    public void setLocatDataInBackground(Contact contact, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "開始記錄定位資訊...");
        new SetLocationDataAsyncTask(contact, callback).execute();
    }

    //set service number
    public void setServiceDataInBackground(Contact contact, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "開始設定服務項目...");
        new SetServiceDataAsyncTask(contact, callback).execute();
    }

    //save count detail
    public void storeCountDetailInBackground(Count count, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "開始點數紀錄....");
        new StoreCountDetailAsyncTask(count, callback).execute();
    }

    //save marquee text
    public void storeMarDetailInBackground(Cmarquee cmarquee, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "開始紀錄跑馬燈文字....");
        new StoreMarDetailAsyncTask(cmarquee, callback).execute();
    }

    //save commodity detail
    public void storeCommDetailInBackground(Commodity commodity, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "開始產品紀錄....");
        new StoreCommDetailAsyncTask(commodity, callback).execute();
    }

    //save commodity sale number
    public void storeCommSaleInBackground(Commodity commodity, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "寫入商品訂購資訊....");
        new StoreCommSaleAsyncTask(commodity, callback).execute();
    }

    //save service and user detail
    public void storeUserDetailInBackground(Contact contact, String service, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "開始配對....");
        new GetServiceData(contact, service, callback).execute();
    }

    //check A 欄位 = B value
    public void checkCompetDataInBackground(Contact contact, String item, String id, GetUserCallback callback) {
        progressDialog.show();
        Log.e("rainsilkinfo", "權限檢查....");
        new GetCompetData(contact, item, id, callback).execute();
    }


    //AsyncTask START HERE
    //註冊
    public class StoreDataAsyncTask extends AsyncTask<Void, Void, Void> {
        Contact contact;
        GetUserCallback callback;

        public StoreDataAsyncTask(Contact contact, GetUserCallback callback) {
            this.contact = contact;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();
            Log.e("rainsilkinfo", "set name = " + contact.name);
            data_to_send.add(new BasicNameValuePair("name", contact.name));
            data_to_send.add(new BasicNameValuePair("email", contact.email));
            data_to_send.add(new BasicNameValuePair("username", contact.uname));
            data_to_send.add(new BasicNameValuePair("password", contact.pass));
            data_to_send.add(new BasicNameValuePair("phone", contact.phone_num));

            data_to_send.add(new BasicNameValuePair("address", contact.address));
            data_to_send.add(new BasicNameValuePair("assoiation", contact.assoiation));
            data_to_send.add(new BasicNameValuePair("job", contact.job));
            data_to_send.add(new BasicNameValuePair("introperson", contact.introperson));
            data_to_send.add(new BasicNameValuePair("lineid", contact.lineid));
            data_to_send.add(new BasicNameValuePair("level", contact.level));
            data_to_send.add(new BasicNameValuePair("giftcount", contact.giftcount));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send, HTTP.UTF_8));
                client.execute(post);
                Log.e("rainsilkinfo", "setEntity");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("rainsilkinfo", "failed setEntity");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.done(null);

            super.onPostExecute(aVoid);
        }
    }

    //登入
    public class FetchDataAsyncTask extends AsyncTask<Void, Void, Contact> {
        Contact contact;
        GetUserCallback callback;

        public FetchDataAsyncTask(Contact contact, GetUserCallback callback) {
            this.contact = contact;
            this.callback = callback;
        }

        @Override
        protected Contact doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("username", contact.uname));
            data_to_send.add(new BasicNameValuePair("password", contact.pass));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            Contact retunedContact = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                Log.e("rainsilkinfo", "result =" + result);

                JSONObject jsonObject = new JSONObject(result);
                retunedContact = null;
                Log.e("rainsilkinfo", "FetchsetEntity");

                if (jsonObject.length() == 0) {
                    retunedContact = null;
                    Log.e("rainsilkinfo", "retunedContact = null;");
                } else {
                    String name, email, phone, address, assoiation, job, introperson, lineid;
                    String id;
                    String count, giftcount, carcount, evaluation, performance;
                    String level;

                    name = null;
                    email = null;
                    phone = null;
                    address = null;
                    assoiation = null;
                    job = null;
                    introperson = null;
                    lineid = null;

                    id = null;
                    count = null;
                    giftcount = null;
                    carcount = null;
                    evaluation = null;
                    performance = null;
                    level = null;
//
                    if (jsonObject.has("name"))
                        name = jsonObject.getString("name");
                    if (jsonObject.has("email"))
                        email = jsonObject.getString("email");
                    if (jsonObject.has("phone"))
                        phone = jsonObject.getString("phone");
                    if (jsonObject.has("address"))
                        address = jsonObject.getString("address");

                    if (jsonObject.has("assoiation"))
                        assoiation = jsonObject.getString("assoiation");
                    if (jsonObject.has("job"))
                        job = jsonObject.getString("job");
                    if (jsonObject.has("introperson"))
                        introperson = jsonObject.getString("introperson");
                    if (jsonObject.has("lineid"))
                        lineid = jsonObject.getString("lineid");

                    if (jsonObject.has("id"))
                        id = jsonObject.getString("id");

                    if (jsonObject.has("countnum"))
                        count = jsonObject.getString("countnum");

                    if (jsonObject.has("giftcount"))
                        giftcount = jsonObject.getString("giftcount");

                    if (jsonObject.has("carcount"))
                        carcount = jsonObject.getString("carcount");


                    if (jsonObject.has("evaluation"))
                        evaluation = jsonObject.getString("evaluation");

                    if (jsonObject.has("performance"))
                        performance = jsonObject.getString("performance");


                    if (jsonObject.has("level"))
                        level = jsonObject.getString("level");
                    Log.e("rainsilkinfo", "login in count =" + count);

                    retunedContact = new Contact(name, email, contact.uname, contact.pass, phone, address, assoiation, job, introperson, lineid, id, count, giftcount, carcount, evaluation, performance, level);

                }


            } catch (Exception e) {
                Log.e("rainsilkinfo", "Fetch failed");
                e.printStackTrace();
            }
            return retunedContact;
        }

        private boolean userexists(JSONArray jsonArray, String usernameToFind) {
            return jsonArray.toString().contains(usernameToFind);
        }


        @Override
        protected void onPostExecute(Contact returnedContact) {
            progressDialog.dismiss();
            callback.done(returnedContact);

            super.onPostExecute(returnedContact);
        }
    }

    //定位
    public class SetLocationDataAsyncTask extends AsyncTask<Void, Void, Contact> {
        Contact contact;
        GetUserCallback callback;

        public SetLocationDataAsyncTask(Contact contact, GetUserCallback callback) {
            this.contact = contact;
            this.callback = callback;
        }

        @Override
        protected Contact doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();
            data_to_send.add(new BasicNameValuePair("id", contact.id));
            Log.e("rainsilkinfo", "queryid =" + contact.id);
            data_to_send.add(new BasicNameValuePair("location1", contact.location1));
            data_to_send.add(new BasicNameValuePair("location2", contact.location2));
            Log.e("rainsilkinfo", "sentlocation =" + contact.location1);
            Log.e("rainsilkinfo", "sentlocation2 =" + contact.location2);

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateUserLocation.php");

            Contact retunedContact = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                Log.e("rainsilkinfo", "result =" + result);

                JSONObject jsonObject = new JSONObject(result);

                retunedContact = null;
                Log.e("rainsilkinfo", "SetLocation");

                if (jsonObject.length() == 0) {
                    retunedContact = null;
                    Log.e("rainsilkinfo", "retunedContact = null;");
                } else {
                    String name, email, phone, address, assoiation, job, introperson, lineid;
                    String id;
                    String count;
                    String level;

                    name = null;
                    email = null;
                    phone = null;
                    address = null;
                    assoiation = null;
                    job = null;
                    introperson = null;
                    lineid = null;

                    id = null;
                    count = null;
                    level = null;
//
                    if (jsonObject.has("name"))
                        name = jsonObject.getString("name");
                    if (jsonObject.has("email"))
                        email = jsonObject.getString("email");
                    if (jsonObject.has("phone"))
                        phone = jsonObject.getString("phone");
                    if (jsonObject.has("address"))
                        address = jsonObject.getString("address");
                    if (jsonObject.has("assoiation"))
                        assoiation = jsonObject.getString("assoiation");
                    if (jsonObject.has("job"))
                        job = jsonObject.getString("job");
                    if (jsonObject.has("introperson"))
                        introperson = jsonObject.getString("introperson");
                    if (jsonObject.has("lineid"))
                        lineid = jsonObject.getString("lineid");

                    if (jsonObject.has("id"))
                        id = jsonObject.getString("id");
                    if (jsonObject.has("count"))
                        count = jsonObject.getString("count");

                    if (jsonObject.has("level"))
                        level = jsonObject.getString("level");

                    retunedContact = null;//new Contact(name,email,contact.uname,contact.pass,phone,address,assoiation,job,introperson,lineid,id,count,"0","0","0",level);
                    Log.e("rainsilkinfo", "retunedContact =new contact;");
                }


            } catch (Exception e) {
                Log.e("rainsilkinfo", "SetLocation failed");
                e.printStackTrace();
            }
            return retunedContact;
        }

        private boolean userexists(JSONArray jsonArray, String usernameToFind) {
            return jsonArray.toString().contains(usernameToFind);
        }


        @Override
        protected void onPostExecute(Contact returnedContact) {
            progressDialog.dismiss();
            callback.done(returnedContact);

            super.onPostExecute(returnedContact);
        }
    }

    //寫入服務代碼
    public class SetServiceDataAsyncTask extends AsyncTask<Void, Void, Contact> {
        Contact contact;
        GetUserCallback callback;

        public SetServiceDataAsyncTask(Contact contact, GetUserCallback callback) {
            this.contact = contact;
            this.callback = callback;
        }

        @Override
        protected Contact doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("id", contact.id));
            Log.e("rainsilkinfo", "setservice id =" + contact.id);
            data_to_send.add(new BasicNameValuePair("service", contact.service));
            Log.e("rainsilkinfo", "sent service =" + contact.service);

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateUserService.php");

            Contact retunedContact = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.e("rainsilkinfo", "result =" + result);

                JSONObject jsonObject = new JSONObject(result);

                retunedContact = null;
                if (jsonObject.length() == 0) {
                    retunedContact = null;
                    Log.e("rainsilkinfo", "retunedContact = null;");
                } else {
                    String name, email, phone, address, assoiation, job, introperson, lineid;
                    String id;
                    String count;
                    String level;

                    name = null;
                    email = null;
                    phone = null;
                    address = null;
                    assoiation = null;
                    job = null;
                    introperson = null;
                    lineid = null;

                    id = null;
                    count = null;
                    level = null;
//
                    if (jsonObject.has("name"))
                        name = jsonObject.getString("name");
                    if (jsonObject.has("email"))
                        email = jsonObject.getString("email");
                    if (jsonObject.has("phone"))
                        phone = jsonObject.getString("phone");
                    if (jsonObject.has("address"))
                        address = jsonObject.getString("address");
                    if (jsonObject.has("assoiation"))
                        assoiation = jsonObject.getString("assoiation");
                    if (jsonObject.has("job"))
                        job = jsonObject.getString("job");
                    if (jsonObject.has("introperson"))
                        introperson = jsonObject.getString("introperson");
                    if (jsonObject.has("lineid"))
                        lineid = jsonObject.getString("lineid");

                    if (jsonObject.has("id"))
                        id = jsonObject.getString("id");
                    if (jsonObject.has("count"))
                        count = jsonObject.getString("count");

                    if (jsonObject.has("level"))
                        level = jsonObject.getString("level");

                    retunedContact = null;//new Contact(name,email,contact.uname,contact.pass,phone,address,assoiation,job,introperson,lineid,id,count,"0","0","0",level);
                    Log.e("rainsilkinfo", "retunedContact =new contact;");
                }


            } catch (Exception e) {
                Log.e("rainsilkinfo", "SetService failed");
                e.printStackTrace();
            }
            return retunedContact;
        }

        private boolean userexists(JSONArray jsonArray, String usernameToFind) {
            return jsonArray.toString().contains(usernameToFind);
        }

        @Override
        protected void onPostExecute(Contact returnedContact) {
            progressDialog.dismiss();
            callback.done(returnedContact);

            super.onPostExecute(returnedContact);
        }
    }

    //根據id取得點數資料
    private class GetCount extends AsyncTask<Void, String, String> {

        Contact contact;
        GetUserCallback callback;
        String id;

        public GetCount(Contact contact, String id, GetUserCallback callback) {
            this.contact = contact;
            this.callback = callback;
            this.id = id;
            Log.e("rainsilkinfo", "GetCount function id=" + id);
        }

        @Override
        protected String doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("id", id));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserCount.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                Log.e("rainsilkinfo", "getCount result =" + result);

                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.length() == 0) {
                    Log.e("rainsilkinfo", "retuned count fetch = null;");
                } else {
                    String count = null, giftcount = null, carcount = null;

                    if (jsonObject.has("countnum"))
                        count = jsonObject.getString("countnum");
                    if (jsonObject.has("giftcount"))
                        giftcount = jsonObject.getString("giftcount");
                    if (jsonObject.has("carcount"))
                        carcount = jsonObject.getString("carcount");

                    Log.e("rainsilklog", "json count" + count);
                    Log.e("rainsilklog", "json giftcount" + giftcount);
                    Log.e("rainsilklog", "json carcount" + carcount);

                    new Contact("guest", "mail", "guest", "pass", "phone", "add", "ass", "job", "intro", "line", id, count, giftcount, carcount, "0", "0", "0");

                    Log.e("rainsilklog", "Contact count" + contact.count); //上一次的
                    Log.e("rainsilklog", "Contact giftcount" + contact.giftcount);
                    contact.setCount(count);//在這改記別人的點數!!!!!!!!!!
                    contact.setGiftCount(giftcount);
                    contact.setCarCount(carcount);
                    Log.e("rainsilklog", "會員 count" + contact.count);
                    Log.e("rainsilklog", "會員 giftcount" + contact.giftcount);
                    Log.e("rainsilklog", "會員 carcount" + contact.carcount);
                }
            } catch (Exception e) {
                //TODO: 查無此count show message?
                Log.e("rainsilkinfo", "查無此會員,請重試");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            progressDialog.dismiss();
            callback.done(null);

            super.onPostExecute(aVoid);
        }
    }

    //紀錄點數異動狀況
    public class StoreCountDetailAsyncTask extends AsyncTask<Void, Void, Void> {
        Count count;
        GetUserCallback callback;

        public StoreCountDetailAsyncTask(Count count, GetUserCallback callback) {
            this.count = count;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();
            Log.e("rainsilkinfo", "set type = " + count.type);
            data_to_send.add(new BasicNameValuePair("type", count.type));
            data_to_send.add(new BasicNameValuePair("fromid", count.fromid));
            data_to_send.add(new BasicNameValuePair("toid", count.toid));
            data_to_send.add(new BasicNameValuePair("fromcount", count.fromcount));
            data_to_send.add(new BasicNameValuePair("tocount", count.tocount));
            data_to_send.add(new BasicNameValuePair("fromgcount", count.fromgcount));
            data_to_send.add(new BasicNameValuePair("togcount", count.togcount));
            data_to_send.add(new BasicNameValuePair("time", count.time));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "SaveCountDetail.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send, HTTP.UTF_8));
                client.execute(post);
                Log.e("rainsilkinfo", "setEntity");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("rainsilkinfo", "failed setEntity");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.done(null);

            super.onPostExecute(aVoid);
        }
    }

    //紀錄跑馬燈文字
    public class StoreMarDetailAsyncTask extends AsyncTask<Void, Void, Void> {
        Cmarquee cmarquee;
        GetUserCallback callback;

        public StoreMarDetailAsyncTask(Cmarquee cmarquee, GetUserCallback callback) {
            this.cmarquee = cmarquee;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("sendby", cmarquee.sendby));
            data_to_send.add(new BasicNameValuePair("area", cmarquee.area));
            data_to_send.add(new BasicNameValuePair("text", cmarquee.text));
            data_to_send.add(new BasicNameValuePair("time", cmarquee.time));
            Log.e("rainsilkinfo", "set mar text= " + cmarquee.text);
            Log.e("rainsilkinfo", "set mar time = " + cmarquee.time);
            Log.e("rainsilkinfo", "set mar sendby = " + cmarquee.sendby);
            Log.e("rainsilkinfo", "set mar area = " + cmarquee.area);

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "SaveMarDetail.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send, HTTP.UTF_8));
                client.execute(post);
                Log.e("rainsilkinfo", "set marEntity");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("rainsilkinfo", "failed set marEntity");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.done(null);

            super.onPostExecute(aVoid);
        }
    }

    //紀錄產品上傳
    public class StoreCommDetailAsyncTask extends AsyncTask<Void, Void, Void> {
        Commodity commodity;
        GetUserCallback callback;

        public StoreCommDetailAsyncTask(Commodity commodity, GetUserCallback callback) {
            this.commodity = commodity;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("name", commodity.name));
            data_to_send.add(new BasicNameValuePair("num", commodity.num));
            data_to_send.add(new BasicNameValuePair("shop", commodity.shop));
            data_to_send.add(new BasicNameValuePair("price", commodity.price));
            data_to_send.add(new BasicNameValuePair("goodcount", commodity.goodcount));
            data_to_send.add(new BasicNameValuePair("photoname", commodity.photoname));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "SaveCommDetail.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send, HTTP.UTF_8));
                client.execute(post);
                Log.e("rainsilkinfo", "setEntity");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("rainsilkinfo", "failed setEntity");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.done(null);

            super.onPostExecute(aVoid);
        }
    }

    //紀錄產品銷售量
    public class StoreCommSaleAsyncTask extends AsyncTask<Void, Void, Void> {
        Commodity commodity;
        GetUserCallback callback;

        public StoreCommSaleAsyncTask(Commodity commodity, GetUserCallback callback) {
            this.commodity = commodity;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("shop", commodity.shop));
            data_to_send.add(new BasicNameValuePair("num", commodity.num));
            data_to_send.add(new BasicNameValuePair("addnum", commodity.sale));
            Log.e("rainsilkinfo", "shop,num,sale="+ commodity.shop+"."+commodity.num+","+commodity.sale);

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateCommSale.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send, HTTP.UTF_8));
                client.execute(post);
                Log.e("rainsilkinfo", "setEntity");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("rainsilkinfo", "failed setEntity");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.done(null);

            super.onPostExecute(aVoid);
        }
    }

    //根據服務 查詢服務狀態:獲得派車配對的資料
    private class GetServiceData extends AsyncTask<Void, String, String> {

        Contact contact;
        GetUserCallback callback;
        String service;

        public GetServiceData(Contact contact, String service, GetUserCallback callback) {
            this.contact = contact;
            this.callback = callback;
            this.service = service;
            Log.e("rainsilkinfo", "GetUserbyService service=" + service);
        }

        @Override
        protected String doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("service", service));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchService.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                Log.e("rainsilkinfo", "getUser result =" + result);

                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.length() <= 2) {
                    Log.e("rainsilkinfo", "retuned user fetch = null;");
                } else {
                    String guestid = null, lineid = null;
                    String phone = null;
                    String location1 = null, location2 = null;

                    if (jsonObject.has("id"))
                        guestid = jsonObject.getString("id");
                    if (jsonObject.has("lineid"))
                        lineid = jsonObject.getString("lineid");
                    if (jsonObject.has("phone"))
                        phone = jsonObject.getString("phone");
                    if (jsonObject.has("locationone"))
                        location1 = jsonObject.getString("locationone");
                    if (jsonObject.has("locationtwo"))
                        location2 = jsonObject.getString("locationtwo");

                    Log.e("rainsilklog", "json guestid" + guestid);
                    Log.e("rainsilklog", "json lineid" + lineid);

                    new Contact("name", "mail", "guest", "pass", phone, "add", "ass", "job", "intro", lineid, guestid, "count", "giftcount", "carcount", "evalu", "0", "0");
//                    String name,String email,String uname,String pass,String phone_num,
//                        String address,String assoiation,String job,String introperson,String lineid,String id,String count,String giftcount,String evaluaion,String performance,String level)
//                    {
                    //new Contact(guestid,location1,location2);
                    Log.e("rainsilklog", "new Contact id" + contact.id);
                    Log.e("rainsilklog", "new Contact giftcount" + contact.lineid);

                    contact.setID(guestid);//在這改記客戶的資料!!!!!!!!!!
                    contact.setlineid(lineid);
                    contact.setPhone_num(phone);
                    contact.setLocation1(location1);
                    contact.setLocation2(location2);

                    Log.e("rainsilklog", "Contact guestid" + contact.id);
                    Log.e("rainsilklog", "Contact guestline" + contact.lineid);
                    Log.e("rainsilklog", "Contact phone" + contact.phone_num);
//                    new Count(id,memberCount);
//                    Log.e("rainsilklog","Count count"+count.count);


                }
            } catch (Exception e) {
                Log.e("rainsilkinfo", "Fetch user failed");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            progressDialog.dismiss();
            callback.done(null);

            super.onPostExecute(aVoid);
        }
    }

    //根據item 取得value資料
    private class GetCompetData extends AsyncTask<Void, String, String> {

        Contact contact;
        GetUserCallback callback;
        String item, id;

        public GetCompetData(Contact contact, String item, String id, GetUserCallback callback) {
            this.contact = contact;
            this.callback = callback;
            this.item = item;
            this.id = id;
            Log.e("rainsilkinfo", "GetCompetData function item=" + item + ",id=" + id);
        }

        @Override
        protected String doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            //根據ID 查LEVEL
            data_to_send.add(new BasicNameValuePair("id", id));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchCompetData.php");
            String ritem = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                Log.e("rainsilkinfo", "getCompet result =" + result);

                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.length() == 0) {
                    //查無權限資料
                    Log.e("rainsilkinfo", "retuned Compet fetch = null;");
                } else {
                    if (jsonObject.has(item))
                        ritem = jsonObject.getString(item);

                    Log.e("rainsilklog", "json ritem" + ritem);

                    new Contact("guest", "mail", "guest", "pass", "phone", "add", "ass", "job", "intro", "line", "id", "count", "giftcount", "carcount", "0", "0", ritem);

                    Log.e("rainsilklog", "Contact level" + contact.level);
                    contact.setLevel(ritem);//在這改記別人的權限!!!!!!!!!!
                    Log.e("rainsilklog", "Change Contact level" + contact.level);
                }
            } catch (Exception e) {
                //TODO: 查無此level參數
                Log.e("rainsilkinfo", "level Fetch failed");
                e.printStackTrace();
            }
            Log.e("rainsilklog", "return member level " + ritem);
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            progressDialog.dismiss();
            callback.done(null);

            super.onPostExecute(aVoid);
        }
    }


}
