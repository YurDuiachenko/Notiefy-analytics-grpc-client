package com.example.notiefytoplistclient.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SongCount {
    private String name;
    private int playsCount;
}
