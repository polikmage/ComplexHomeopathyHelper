package org.mpo.cxhelper.service;

import org.mpo.cxhelper.utils.IOUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class TextProcessor {
    public static List<String> remedies;
    public static Map<String,Integer> occurences;
    public TextProcessor(){
    }


    public List<String> getRemedies() {
        return remedies;
    }

    public void setRemedies(List<String> remedies) {
        this.remedies = remedies;
    }

    public static List preProcessText(File file) {
        remedies = new ArrayList<>();
        String fileText = null;
        try {
            fileText = IOUtils.readTextFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String [] commaText = fileText.split(";");
        for (int i = 0; i < commaText.length; i++) {
            if(commaText[i].contains("--")){
                Collections.addAll(remedies, commaText[i].split("--")[1].split(","));
            }
        }
        return remedies;
    }

    public static Map<String, Integer> findOccurences(){

        //group remedies together by keys
        Map<String, Integer> collect =
                remedies.stream().map(m->m.replace(" ","")).collect(groupingBy(Function.identity(), summingInt(e -> 1)));

        //remedies.stream().forEach(s -> System.out.println(s));
        //remedies.stream().map(m->m.replace(" ","")).forEach(s -> System.out.println(s));
        //collect.forEach((k,v)-> System.out.println(k+":"+v));

        occurences = collect.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> {
                            throw new IllegalStateException();
                        },
                        LinkedHashMap::new
                ));
        return occurences;
    }

    public static Matcher findString(String searchedString, String text)
    {
        //Pattern pattern = Pattern.compile("\\b" + searchedString + "\\b");
        Pattern pattern = Pattern.compile(searchedString);
        Matcher matcher = pattern.matcher(text); //Where input is a TextInput class
        boolean found = matcher.find(0);
        if(found) {
            return matcher;
        }
        return null;
    }
}
