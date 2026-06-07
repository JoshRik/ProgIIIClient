package it.unito.progiii.progiiiclient.utils;

import com.google.gson.Gson;

public class Constants {

    public static final int PORT=5000;
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final Gson GSON = new Gson();
    public static final String CP_ROOT = "/it/unito/progiii/progiiiclient/"; //for initialize fxml from internal package positions

}
