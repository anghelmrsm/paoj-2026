package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private final String name;
    private Song[] songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new Song[0];
    }

    public String getName() {
        return name;
    }

    public void addSong(Song song) {
        Song[] resized = new Song[songs.length + 1];
        System.arraycopy(songs, 0, resized, 0, songs.length);
        resized[resized.length - 1] = song;
        songs = resized;
    }

    public void printSortedByTitle() {
        Song[] copy = songs.clone();
        Arrays.sort(copy);
        for (Song song : copy) {
            System.out.println(song);
        }
    }

    public void printSortedByDuration() {
        Song[] copy = songs.clone();
        Arrays.sort(copy, new SongDurationComparator());
        for (Song song : copy) {
            System.out.println(song);
        }
    }

    public int getTotalDuration() {
        int total = 0;
        for (Song song : songs) {
            total += song.durationSeconds();
        }
        return total;
    }
}
