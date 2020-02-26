package org.mpo.cxhelper.service;

import org.mpo.cxhelper.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
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
        /*remedies.stream().collect(Collectors.groupingBy(Function.<String>identity(), HashMap::new, counting())).entrySet()
                .forEach(System.out::println);
        return null;*/
        Map<String, Integer> collect =
                remedies.stream().collect(groupingBy(Function.identity(), summingInt(e -> 1)));
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
}
