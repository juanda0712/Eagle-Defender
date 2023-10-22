package com.mygdx.utils;

import com.mygdx.models.SongInfo;


public class Spotify {
    public static void main(String[] args) {
        SpotifyAuthenticator spotify = new SpotifyAuthenticator();

        //obtine el analisis de la cancion
        SongInfo info = spotify.getSongInfo("billi+jean");
        System.out.println(info);

        /*String playSong = auth.playSong("jazz");
        System.out.println(playSong);*/
    }
}