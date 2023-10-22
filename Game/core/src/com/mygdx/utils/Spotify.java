package com.mygdx.utils;

import com.mygdx.models.SongInfo;
import org.json.JSONArray;
import org.json.JSONObject;

public class Spotify {
    public static void main(String[] args) {
        SpotifyAuthenticator auth = new SpotifyAuthenticator();

        //obtine el analisis de la cancion
        SongInfo info = auth.getSongInfo("billi+jean");
        System.out.println(info);

        /*String playSong = auth.playSong("jazz");
        System.out.println(playSong);*/
    }
}