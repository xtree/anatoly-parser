package com.xtree.anatolij.data;

import com.xtree.anatolij.FileMessenger;

import java.util.Random;

public class Sucking extends Event {
    private final String vampire;
    private String address;

    public int getEnergy() {
        return energy*people;
    }

    public String getVampire() {
        return vampire;
    }

    private final int energy;
    private final int people;

    private Sucking(String originator, int people, int energy, String vampire, String address) {
        this.originator = originator;

        this.people = people;
        this.energy = energy;
        this.vampire = vampire;
        this.address = address;
        SuckingStorage.noteSucking(vampire, System.currentTimeMillis());
    }

    @Override
    public boolean isSignificant() {

        int amount = people * energy;
        if (amount > 200) {
            return true;
        }
        if (amount > 20) {
            Random rand = new Random(System.currentTimeMillis());
            if (rand.nextInt(100) < 95) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean notifyNeeded() {
        return false;
    }

    @Override
    public String getAnalyticHeader(Affiliation affiliation) {
        int amount = people * energy;
        String message;
        if (amount > 150) {
            message = "velke cerpani energie " + amount;
        } else if (amount > 50) {
            message = "cerpani energie " + amount;
        } else {
            message = "malo vyznamne cerpani energie " + amount;
        }
        return message;

        //return "Doslo k sani!\n"+ address +"\n"+ amount +" magu vysato.";
    }

    @Override
    public String getAnalyticMessage(Affiliation affiliation) {
        int amount = people * energy;
        String message;
        if (amount > 150) {
            message = "Velke cerpani energie " + amount + " magu";
        } else if (amount > 50) {
            message = "Cerpani energie " + amount + " magu";
        } else {
            message = "Malo vyznamne cerpani energie " + amount + " magu";
        }
        String end;
        switch (affiliation)
        {
            case LIGHT:
                end = new FileMessenger(FileMessenger.LIGHT_SUCK).getRandomMessage();
                break;
            case DARK :
                end = new FileMessenger(FileMessenger.DARK_SUCK).getRandomMessage();
                break;
            default: end = "Chyba: Bez komentare.";
        }
        return message + "\n" + address + "\n\n" + end;
    }

    @Override
    public String getMeta() {
        String significant = isSignificant() ? "" : "Nezajimave ";
        return significant + "Sani: " + people + "x" + energy + ", " + address + " - " + vampire;
    }

    @Override
    public String getConfirmationMessage() {
        String begin = "Vysal jsi " + energy + " magu z kazdeho z " + people + " lidi. ";
        String end = new FileMessenger(FileMessenger.SUCK_CONFIRM).getRandomMessage();
        return begin + end;
    }

    @Override
    public Event dependentEvent() {
        if (SuckingStorage.isSuspicious(vampire)) {
            return new RepeatedSucking(vampire);
        }
        return null;
    }

    public static Event parse(String originator, String[] parts) {
        //cerpani [pocet lidi ze kterych jste cerpali]x[pocet magu cerpanych z jedne osoby] [jmeno postavy bez mezer] [urceni mista]
        try {
            if (parts.length < 4) throw new Exception("Zprava ma malo casti.");
            if (!parts[1].contains("x")) throw new Exception("Druhe slovo neni rozdeleno x.");
            String[] energySplitted = parts[1].split("x");
            StringBuilder address = new StringBuilder();
            for (int i = 3; i < parts.length; i++) {
                address.append(parts[i]).append(" ");
            }
            return new Sucking(
                    originator,
                    Integer.parseInt(energySplitted[0]),
                    Integer.parseInt(energySplitted[1]),
                    parts[2],
                    address.toString()
            );
        } catch (Exception e) {
            return new FailedEvent(originator, parts, e.getMessage());
        }
    }
}
