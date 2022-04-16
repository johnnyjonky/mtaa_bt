package com.example.mtaaa;

public class JSONSaved {

    private static String user_name;
    private static int user = 0;
    private static int isadmin = 0;
    private static int placetype;
    private static int placeid;
    private static String placetypeName;
    private static String url = "https://6ff6-95-102-14-246.eu.ngrok.io";

    public static void setUser(int user){
        JSONSaved.user = user;
    }
    public static int getUser(){
        return JSONSaved.user;
    }

    public static void setUser_name(String name){ JSONSaved.user_name = name; }
    public static String getUser_name(){ return JSONSaved.user_name; }

    public static void setIsadmin(int admin){
        JSONSaved.isadmin = admin;
    }

    public static int getIsadmin(){
        return JSONSaved.isadmin;
    }

    public static void setPlacetype(int placetype){
        JSONSaved.placetype = placetype;
    }
    public static int getPlacetype(){
        return JSONSaved.placetype;
    }

    public static void setPlaceid(int placeid){
        JSONSaved.placeid = placeid;
    }
    public static int getPlaceid(){
        return JSONSaved.placeid;
    }

    public static void setPlacetypeName(String placetypeName){
        JSONSaved.placetypeName = placetypeName;
    }
    public static String getPlacetypeName(){
        return JSONSaved.placetypeName;
    }

    public static String getUrl(){
        return JSONSaved.url;
    }

}
