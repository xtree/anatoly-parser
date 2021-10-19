package com.xtree.anatolij.data;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class EventTest {
    @Test
    @Ignore("pokus")
    public void correctSucking() throws Exception {
        Event event = Event.parse("cerpani 5x50 Zeleny U malych cervenych domku","");
        assertThat(event, instanceOf(Sucking.class));
        Sucking s = (Sucking) event;
        assertThat(s.isSignificant(),is(true));
        assertThat(s.getConfirmationMessage(),is("Vysal jsi celkem 250 magu z lidi. Budou nemocni, mel bys o tom premyslet."));
        assertThat(s.getAnalyticMessage(Affiliation.LIGHT),is("Doslo k sani!\nU malych cervenych domku \n5 vysatych po 50."));
    }

    @Test
    public void correctRitual() throws Exception {
        Event event = Event.parse("ritual 23:45 123magu Zeleny U malych cervenych domku","000111222");
        assertThat(event, instanceOf(Ritual.class));
        Ritual s = (Ritual) event;
        assertThat(s.isSignificant(),is(true));
        assertThat(s.getConfirmationMessage(),containsString("Prijat ritual v 23:45, U malych cervenych domku , 123magu."));
        assertThat(s.getAnalyticMessage(Affiliation.LIGHT),containsString("V 23:45 se odehraje magicka aktivita na adrese U malych cervenych domku .\n" +
                "Sere linky zarily/zari/budou zarit s energii 123 magu."));
    }
    //Cerpani 5x20 Ragulin Pod Zizkovskou věží dole.


    @Test
    public void diakritika() throws Exception {
        Event event = Event.parse("Cerpani 5x20 Ragulin Pod Zizkovskou věží dole.", "000111222");
        assertThat(event, instanceOf(Sucking.class));



    }
}