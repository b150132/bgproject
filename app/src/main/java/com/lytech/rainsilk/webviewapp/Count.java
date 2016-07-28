package com.lytech.rainsilk.webviewapp;

/**
 * Created by rainsilk on 2015/12/18.
 */
public class Count {
    //variables
    String id;

    String type; //sale move use gift
    String fromid,toid,fromcount,tocount,fromgcount,togcount;
    String time;

    //another constructor
    public Count(String type,String fromid,String toid,String fromcount,String tocount,String fromgcount,String togcount,
                 String time)
    {
        this.type=type;
        this.fromid=fromid;
        this.toid=toid;
        this.fromcount=fromcount;
        this.tocount=tocount;
        this.fromgcount=fromgcount;
        this.togcount=togcount;

        this.time=time;
    }

    public String getID()
    {
        return this.id;
    }
    public void setID(String id)
    {
        this.id=id;
    }

    public String getType()
    {
        return this.type;
    }
    public void setType(String type)
    {
        this.type=type;
    }

    public String getFromid()
    {
        return this.fromid;
    }
    public void setFromid(String fromid)
    {
        this.fromid=fromid;
    }
    //String address,String job,String introperson,String lineid
    public String getToid()
    {
        return this.toid;
    }
    public void setToids(String toid)
    {
        this.toid=toid;
    }

    public String getFromcount()
    {
        return this.fromcount;
    }
    public void setFromcount(String fromcount)
    {
        this.fromcount=fromcount;
    }

    public String getTocount()
    {
        return this.tocount;
    }
    public void setTocount(String tocount)
    {
        this.tocount=tocount;
    }

    public String getFromgcount()
    {
        return this.fromgcount;
    }
    public void setFromgcount(String fromgcount)
    {
        this.fromgcount=fromgcount;
    }

    public String getTogcount()
    {
        return this.togcount;
    }
    public void setTogcount(String togcount)
    {
        this.togcount=togcount;
    }

    public String getTime()
    {
        return this.time;
    }
    public void setTime(String time)
    {
        this.time=time;
    }

}
