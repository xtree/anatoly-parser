package com.xtree.anatolij.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@Data
public abstract class Event {
    String originator;
    //Ritual [hodina ve kterou se ritual bude delat ve formanu HH:MM] [pocet magu vkladanych do ritualu beze slev]magu [jmeno postavy bez mezer] [urceni mista]
    //cerpani [pocet lidi ze kterych jste cerpali]x[pocet magu cerpanych z jedne osoby] [jmeno postavy bez mezer] [urceni mista]

    public static Event parse(String text, String originator) {
        if (    "KAKTUS".equals(originator) ||
                "5602".equals(originator) ||
                originator.length() < 9
                ) return null;
        log.info("Parsing: "+text);
        //cerpani [pocet lidi ze kterych jste cerpali]x[pocet magu cerpanych z jedne osoby] [jmeno postavy bez mezer] [urceni mista]
        //Ritual [hodina ve kterou se ritual bude delat ve formanu HH:MM] [pocet magu vkladanych do ritualu beze slev]magu [jmeno postavy bez mezer] [urceni mista]
        String[] parts = text.split(" ");
        switch (parts[0].toLowerCase()){
            case "cerpani":
                return Sucking.parse(originator,parts);
            case "ritual":
                return Ritual.parse(originator, parts);
            default: return new FailedEvent(originator, text, " nezacina cerpani/ritual ");
        }
    }

    public abstract boolean isSignificant();
    public abstract boolean notifyNeeded();
    public abstract String getAnalyticHeader(Affiliation affiliation);
    public abstract String getAnalyticMessage(Affiliation affiliation);
    public abstract String getMeta();
    public abstract String getConfirmationMessage();
    public abstract Event dependentEvent();
}
