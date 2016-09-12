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
        new GetCount(contact, memberID, callback).execute();
        return memberID;
    }

    public void storeDataInBackground(Contact contact, GetUserCallback callback) {
        progressDialog.show();
        new StoreDataAsyncTask(contact, callback).execute();
    }

    //fetch memberdata
    public void fetchDataInBackground(Contact contact, GetUserCallback callback) {
        progressDialog.show();
        new FetchDataAsyncTask(contact, callback).execute();
    }

    //set user location
    public void setLocatDataInBackground(Contact contact, GetUserCallback callback) {
        progressDialog.show();
        new SetLocationDataAsyncTask(contact, callback).execute();
    }

    //set service number
    public void setServiceDataInBackground(Contact contact, GetUserCallback callback) {
        progressDialog.show();
        new SetServiceDataAsyncTask(contact, callback).execute();
    }

    //save count detail
    public void storeCountDetailInBackground(Count count, GetUserCallback callback) {
        progressDialog.show();
        new StoreCountDetailAsyncTask(count, callback).execute();
    }

    //save marquee text
    public void storeMarDetailInBackground(Cmarquee cmarquee, GetUserCallback callback) {
        progressDialog.show();
        new StoreMarDetailAsyncTask(cmarquee, callback).execute();
    }

    //save commodity detail
    public void storeCommDetailInBackground(Commodity commodity, GetUserCallback callback) {
        progressDialog.show();
        new StoreCommDetailAsyncTask(commodity, callback).execute();
    }

    //save commodity sale number
    public void storeCommSaleInBackground(Commodity commodity, GetUserCallback callback) {
        progressDialog.show();
        new StoreCommSaleAsyncTask(commodity, callback).execute();
    }

    //save service and user detail
    public void storeUserDetailInBackground(Contact contact, String service, GetUserCallback callback) {
        progressDialog.show();
        new GetServiceData(contact, service, callback).execute();
    }

    //check A 欄位 = B value
    public void checkCompetDataInBackground(Contact contact, String item, String id, GetUserCallback callback) {
        progressDialog.show();
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
            } catch (Exception e) {
                e.printStackTrace();
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

                JSONObject jsonObject = new JSONObject(result);
                retunedContact = null;

                if (jsonObject.length() == 0) {
                    retunedContact = null;
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

                    retunedContact = new Contact(name, email, contact.uname, contact.pass, phone, address, assoiation, job, introperson, lineid, id, count, giftcount, carcount, evaluation, performance, level);

                }


            } catch (Exception e) {
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
            data_to_send.add(new BasicNameValuePair("location1", contact.location1));
            data_to_send.add(new BasicNameValuePair("location2", contact.location2));

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

                JSONObject jsonObject = new JSONObject(result);

                retunedContact = null;

                if (jsonObject.length() == 0) {
                    retunedContact = null;
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
                }


            } catch (Exception e) {
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
            data_to_send.add(new BasicNameValuePair("service", contact.service));

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

                JSONObject jsonObject = new JSONObject(result);

                retunedContact = null;
                if (jsonObject.length() == 0) {
                    retunedContact = null;
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
                }


            } catch (Exception e) {
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

                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.length() == 0) {

                } else {
                    String count = null, giftcount = null, carcount = null;

                    if (jsonObject.has("countnum"))
                        count = jsonObject.getString("countnum");
                    if (jsonObject.has("giftcount"))
                        giftcount = jsonObject.getString("giftcount");
                    if (jsonObject.has("carcount"))
                        carcount = jsonObject.getString("carcount");

                    new Contact("guest", "mail", "guest", "pass", "phone", "add", "ass", "job", "intro", "line", id, count, giftcount, carcount, "0", "0", "0");
                    contact.setCount(count);//在這改記別人的點數!!!!!!!!!!
                    contact.setGiftCount(giftcount);
                    contact.setCarCount(carcount);
                }
            } catch (Exception e) {
                //TODO: 查無此count show message?
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
            } catch (Exception e) {
                e.printStackTrace();
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

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "SaveMarDetail.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send, HTTP.UTF_8));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
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
            } catch (Exception e) {
                e.printStackTrace();
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

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateCommSale.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send, HTTP.UTF_8));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
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

                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.length() <= 2) {

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

                    new Contact("name", "mail", "guest", "pass", phone, "add", "ass", "job", "intro", lineid, guestid, "count", "giftcount", "carcount", "evalu", "0", "0");

                    contact.setID(guestid);//在這改記客戶的資料!!!!!!!!!!
                    contact.setlineid(lineid);
                    contact.setPhone_num(phone);
                    contact.setLocation1(location1);
                    contact.setLocation2(location2);

                }
            } catch (Exception e) {
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

                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.length() == 0) {
                    //查無權限資料
                } else {
                    if (jsonObject.has(item))
                        ritem = jsonObject.getString(item);

                    new Contact("guest", "mail", "guest", "pass", "phone", "add", "ass", "job", "intro", "line", "id", "count", "giftcount", "carcount", "0", "0", ritem);

                    contact.setLevel(ritem);//在這改記別人的權限!!!!!!!!!!

                }
            } catch (Exception e) {
                //TODO: 查無此level參數
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


}
