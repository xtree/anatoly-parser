package com.xtree.anatolij;

import com.xtree.anatolij.data.Affiliation;
import com.xtree.anatolij.data.Event;
import com.xtree.anatolij.dao.Inbound;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class MainRunner {
    //private static final String sender = "000111000";
    public static final String META_FORUM_NO = "33";
    public static final String META_TOPIC_NO = "2067";
    public static final String META_TOPIC = "Re: SMS stream";
    //Vacky: 603 798 940
    //Silentbob: 736 611 271
    //Janes: 724 929 239
    //Hade: 737 866 831
    public static final List<String> orcs = Arrays.asList("420724929239");

    public static final String NIGHTWATCH_FORUM_NO = "32"; //32
    private static final String DAYWATCH_FORUM_NO = "31";
    public static final int DELAY = 10_500;

    public static void main(String[] args) throws InterruptedException {
        MainRunner runner = new MainRunner();
        while (true) {
            log.debug("Running...");
            try {
                runner.processMessages();
            }
            catch (Exception e){
                log.error("Main process error",e);
            }
            Thread.sleep(30_000);
        }
    }

    public void processMessages() {
        List<Inbound> messages = null;
        try (Database db = new Database()) {
            messages = db.readNew();
        } catch (Exception e) {
            log.error("Database error", e);
            return;
        }

        if (messages != null) {
            if (messages.size() > 0) {
                log.info("processing {} messages", messages.size());
            }
            List<Event> events = messages.stream()
                    .map(sms -> {
                        markAsProcessed(sms);
                        Event event = Event.parse(sms.getText(), sms.getAddress());
                        if (event != null) {confirm(sms, event);}
                        return event;
                    })
                    .filter(event -> event != null)
                    .flatMap(event -> Stream.of(event, event.dependentEvent()))
                    .filter(event -> event != null)
                    .collect(Collectors.toList());

            if (events.size() > 0) {
                try (HttpSender sender = new HttpSender()) {
                    Thread.sleep(DELAY);
                    events.forEach(event -> {
                        try {
                            metaFinger(sender, event);
                            if (event.isSignificant()) {
                                finger(sender, event);
                            }
                            if (event.notifyNeeded()) {
                                orcs.forEach(number -> sms(number, event.getMeta())
                                );
                            }
                        } catch (Exception e) {
                            log.error("Fingering error", e);
                        }
                    });
                } catch (Exception e) {
                    log.error("Error forum handler opening", e);
                }
            }
        }
    }

    private void sms(String address, String data) {
        if (address.startsWith("420")) {
            try (Database db = new Database()) {
                log.info("sending message {} to {}", data, address);
                db.insertOutboundSms(address, data);
            } catch (Exception e) {
                log.error("sms sending error", e);
            }
        } else {
            log.info("alien address detected not send {} to {}", data, address);
        }

    }

    private void markAsProcessed(Inbound sms) {
        try (Database db = new Database()) {
            log.info("message {} writing status.", sms.getId());
            db.markAsProcessed(sms.getId());
        } catch (Exception e) {
            log.error("Sms + " + sms.getId() + "processed mark error.", e);
        }
    }

    private void confirm(Inbound sms, Event event) {
        if ("000111222".equals(sms.getAddress())) return;
        try (Database db = new Database()) {
            log.info("sending confirmation {} to {}", event.getConfirmationMessage(), sms.getAddress());
            //log.info("ommiting confirmation {} to {}", event.getConfirmationMessage(), sms.getAddress());
            db.insertOutboundSms(sms.getAddress(), event.getConfirmationMessage());
        } catch (Exception e) {
            log.error("Confirm + " + sms.getId() + ", (" + event.getConfirmationMessage() + ") error.", e);
        }
    }

    private void finger(HttpSender sender, Event event) throws Exception {
        log.info("sending to watches: {}",event.getAnalyticMessage(Affiliation.LIGHT));
        int status = sender.submitTopic(NIGHTWATCH_FORUM_NO, event.getAnalyticHeader(Affiliation.LIGHT), event.getAnalyticMessage(Affiliation.LIGHT));
        log.info("Status was {} nightwatch",status);
        Thread.sleep(DELAY);
        status = sender.submitTopic(DAYWATCH_FORUM_NO, event.getAnalyticHeader(Affiliation.DARK), event.getAnalyticMessage(Affiliation.DARK));
        log.info("Status was {} daywatch",status);
        Thread.sleep(DELAY);

    }

    private void metaFinger(HttpSender sender, Event event) throws Exception {
        log.info("Sending meta: {}", event.getMeta());
        int status = sender.submitLog(META_FORUM_NO, META_TOPIC_NO, META_TOPIC, event.getMeta());
        log.info("Status was {}",status);
        Thread.sleep(DELAY);
    }
}

/*
format zprav pro SMS branu:

Cerpani
_________________________

cerpani <pocet lidi ze kterych jste cerpali>x<pocet magu cerpanych z jedne osoby> <jmeno postavy bez mezer> <urceni mista>

priklad: "cerpani 2x20 richardnovotny Zitna 12"

Zpracovani: oddelovac je mezera zpracovavaji se udaj pred a za prvni mezerou, udaj za druhou a treti mezerou
1) spocita se zda je cerpani nad dvacet magu
2) pokud je cerpani nad dvacet magu, odesle se zprava obsahujici "cerpani [spocitana celkova vyse cerpani] [urceni mista]" na obe analyticka oddeleni
3) odesle se mail s predmetem "cerpani [pocet lidi ze kterych jste cerpali]x[pocet magu cerpanych z jedne osoby] [jmeno postavy bez mezer]" na sidestories@praguebynight.eu ve kterem bude zkopirovan obsah sms

Ritualy
________________________

Ritual <hodina ve kterou se ritual bude delat ve formanu HH:MM> <pocet magu vkladanych do ritualu beze slev>magu <jmeno postavy bez mezer> <urceni mista>

priklad: "Ritual 17:30 300magu richardnovotny zridlo v bezrucovych sadech"

Zpracovani: oddelovac je mezera zpracovavaji se udaj pred a za prvni mezerou, udaj za druhou, treti a ctvrtou mezerou

1) odesle se zprava obsahujici "Ritual [hodina ve kterou se ritual bude delat ve formanu HH:MM] [pocet magu vkladanych do ritualu beze slev]magu [urceni mista]" na obe analyticka oddeleni
2) odesle se mail s predmetem "Ritual [hodina ve kterou se ritual bude delat ve formanu HH:MM] [pocet magu vkladanych do ritualu beze slev]magu [jmeno postavy bez mezer]" na sidestories@praguebynight.eu ve kterem bude zkopirovan obsah sms
 */
