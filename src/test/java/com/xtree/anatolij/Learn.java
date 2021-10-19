package com.xtree.anatolij;

import org.junit.Test;

/**
 * Created by tree on 17.11.2016.
 */
public class Learn {
    @Test
    public void replaced() throws Exception {
        System.out.println("blah<br>blah<br>3rd".replaceAll("<br>","\n"));

    }

    @Test
    public void readed() throws Exception {
        String blaa = new FileMessenger(FileMessenger.RITUAL_CONFIRM).getRandomMessage();
        System.out.println(blaa);

    }
}
