package com.unclecole.dominionban.objects;

import lombok.Getter;
import org.checkerframework.checker.index.qual.GTENegativeOne;

public class BanObject {

    @Getter private String bannedBy;
    @Getter private String banned;
    @Getter private String reason;
    @Getter private long time; //EXACT TIME BANNED
    @Getter private long banLength;

    public BanObject(String bannedBy, String banned, String reason, long time, long banLength) {
        this.bannedBy = bannedBy;
        this.banned = banned;
        this.reason = reason;
        this.time = time;
        this.banLength = banLength;
    }
}
