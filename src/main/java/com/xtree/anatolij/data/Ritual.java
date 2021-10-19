package com.xtree.anatolij.data;


import com.xtree.anatolij.FileMessenger;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Slf4j
public class Ritual extends Event {
    private String time;

    public int getEnergy() {
        return energy;
    }

    public String getCaster() {
        return caster;
    }

    private int energy;
    private String caster;
    private String address;

    public Ritual(String originator, String time, int energy, String caster, String address) {
        this.originator = originator;

        this.time = time;
        this.energy = energy;
        this.caster = caster;
        this.address = address;
    }

    @Override
    public boolean isSignificant() {
        return true;
    }

    @Override
    public boolean notifyNeeded() {
        return false;
    }

    @Override
    public String getAnalyticHeader(Affiliation affiliation) {
        return "Ritual v " + time + " o sile " + energy + "magu adresa je " + address;
    }

    @Override
    public String getAnalyticMessage(Affiliation affiliation) {
        String begin = "V " + time + " se odehraje magicka aktivita na adrese " + address + ".\nSere linky zarily/zari/budou zarit s energii " + energy + " magu.";

        String end;
        switch (affiliation)
        {
            case LIGHT:
                end = new FileMessenger(FileMessenger.LIGHT_RITUAL).getRandomMessage();
                break;
            case DARK :
                end = new FileMessenger(FileMessenger.DARK_RITUAL).getRandomMessage();
                break;
            default: end = "Chyba: Bez komentare.";
        }
        return begin + "\n\n" + end;
    }


    @Override
    public String getMeta() {
        return "Ritual: " + time + ", " + address + ", " + energy + "magu - " + caster;
    }

    @Override
    public String getConfirmationMessage() {
        return "Prijat ritual v " + time + ", " + address + ", " + energy + "magu. " + new FileMessenger(FileMessenger.RITUAL_CONFIRM).getRandomMessage();
    }

    @Override
    public Event dependentEvent() {
        return null;
    }

    public static Event parse(String originator, String[] parts) {
        //Ritual [hodina ve kterou se ritual bude delat ve formanu HH:MM] [pocet magu vkladanych do ritualu beze slev]magu [jmeno postavy bez mezer] [urceni mista]
        try {
            if (parts.length < 5) throw new Exception("Zprava ma malo casti.");
            if (!parts[1].contains(":")) throw new Exception("Druhe slovo neobsahuje :");
            StringBuilder address = new StringBuilder();
            for (int i = 4; i < parts.length; i++) {
                address.append(parts[i]).append(" ");
            }
            //Integer.parseInt(s.replaceAll("[\\D]", ""))
            return new Ritual(
                    originator,
                    parts[1],
                    Integer.parseInt(parts[2].replaceAll("[\\D]", "")),
                    parts[3],
                    address.toString()
            );
        } catch (Exception e) {
            return new FailedEvent(originator, parts, e.toString());
        }
    }
}
