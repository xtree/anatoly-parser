package com.xtree.anatolij;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HttpSenderTest {
    @Test
    public void GetValueGets() throws Exception {
        String value = HttpSender.getValue("<input type=\"hidden\" name=\"creation_time\" value=\"1458173262\" />", "creation_time");
        assertThat(value,is("1458173262"));

    }

//    @Test
//    public void testName() throws Exception {
//        try(HttpSender sender = new HttpSender()){
//            int status = sender.submitTopic("33", "Příliš žluťoučký kůň úpěl ďábelské ódy", "UTF-8");
//        }
//
//    }
}