package com.xtree.anatolij;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

@Slf4j
public class FileMessenger {

    public static final String LIGHT_RITUAL = "light-ritual";
    public static final String DARK_RITUAL = "dark-ritual";
    public static final String LIGHT_SUCK = "light-suck";
    public static final String DARK_SUCK = "dark-suck";
    public static final String RITUAL_CONFIRM = "ritual-confirm";
    public static final String SUCK_CONFIRM = "suck-confirm";
    private String name;

    public FileMessenger(String name) {
        this.name = name;
    }

    public String getRandomMessage() {
        ArrayList<String> messages = new ArrayList<>();
        try {

            try (
                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(
                                            new FileInputStream(name),
                                            "UTF8")
                            )
            )
            {

                String line;
                while ((line = br.readLine()) != null) {
                    messages.add(line.replaceAll("<br>", "\n"));
                }
            }
        } catch (Exception e) {
            log.warn("error reading message from file", e);
        }

        return messages.size() > 0 ? messages.get(new Random(System.currentTimeMillis()).nextInt(messages.size())) : "Chyba: Bez komentare.";
    }
}
