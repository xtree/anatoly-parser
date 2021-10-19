package com.xtree.anatolij.stats;

import com.xtree.anatolij.Database;
import com.xtree.anatolij.dao.Inbound;
import com.xtree.anatolij.data.Event;
import com.xtree.anatolij.data.Ritual;
import com.xtree.anatolij.data.Sucking;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static java.util.stream.Collectors.toList;

/**
 * Created by tree on 30.11.2016.
 */
@Ignore
public class Computer {
    @Test
    public void getsmessages() throws Exception {
        List<Inbound> messages = new ArrayList<>();
        try (Database db = new Database()) {
            Connection connection = db.getDbConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"smslib_in\" WHERE \"gateway_id\"=\'huawei\' AND \"message_date\" > \'2016-11-17 19:37:06.924\' ");
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                messages.add(Inbound.getFromResult(results));
            }
        }
        List<Event> events = messages.stream().map(new Function<Inbound, Event>() {
            @Override
            public Event apply(Inbound inbound) {
                return Event.parse(inbound.getText(), inbound.getAddress());
            }
        }).collect(toList());

        List<Event> rituals = events.stream().filter(event -> event instanceof Ritual).collect(toList());

        List<Event> vampirisms = events.stream().filter(event -> event instanceof Sucking).collect(toList());

        int sumR = rituals.stream().mapToInt(new ToIntFunction<Event>() {
            @Override
            public int applyAsInt(Event value) {
                return ((Ritual) value).getEnergy();
            }
        }).sum();

        int sumS = vampirisms.stream().mapToInt(value -> ((Sucking) value).getEnergy()).sum();

        System.out.println("ritualu:"+rituals.size()+ " v cene celkem: " + sumR);
        System.out.println("sani:"+vampirisms.size()+ " v cene celkem: " + sumS);

//        Map<String,Integer> ritMap = new HashMap<>();
//        for (Event ritual : rituals) {
//
//            Ritual r = (Ritual) ritual;
//            String caster = r.getCaster();
//            if (ritMap.containsKey(caster)){
//                ritMap.put(caster,ritMap.get(caster)+r.getEnergy());
//            } else {
//                ritMap.put(caster,r.getEnergy());
//            }
//        }
//
//        ValueComparator bvc = new ValueComparator(ritMap);
//        TreeMap<String, Integer> sorted_map = new TreeMap<>(bvc);
//        sorted_map.putAll(ritMap);
//        System.out.println("results: " + sorted_map);

//        Map<String,Integer> vamMap = new HashMap<>();
//        for (Event vampirism : vampirisms) {
//
//            Sucking r = (Sucking) vampirism;
//            String caster = r.getVampire();
//            if (vamMap.containsKey(caster)){
//                vamMap.put(caster,vamMap.get(caster)+1);
//            } else {
//                vamMap.put(caster,1);
//            }
//        }
//
//        ValueComparator bvc = new ValueComparator(vamMap);
//        TreeMap<String, Integer> sorted_map = new TreeMap<>(bvc);
//        sorted_map.putAll(vamMap);
//        System.out.println("results: " + sorted_map);

    }

    class ValueComparator implements Comparator<String> {
        Map<String, Integer> base;

        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
