package org.mpo.cxhelper.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mpo.cxhelper.utils.IOUtils;

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
    public static Map<String, Integer> occurences;
    public static Map<String, List<String>> symptomeRemedies;
    public static Map<String, List<String>> selectedSymptomesWithRemedies;

    public TextProcessor() {
    }

    public static Map<String, List<String>> processHtml(File file) {
        String htmlText = getFileText(file);
//        String asteriskHtmlText = htmlText.replace(" ","").replace("\n","").replace("<i><fontcolor=\"#0000ff\">","*");//.replace("<i><font\n" + "      color=\"#0000ff\">","*");
        String asteriskHtmlText = htmlText.replace("<i><font color=\"#0000ff\">", "*").replace("<i><font\n" + "      color=\"#0000ff\">", "*").replace("</p>", ";</p>");
        Document doc = Jsoup.parse(asteriskHtmlText);
        String extractedText = doc.body().text();
        //TODO save to file
        return processHtmlText(extractedText);
    }


    public List<String> getRemedies() {
        return remedies;
    }

    public void setRemedies(List<String> remedies) {
        this.remedies = remedies;
    }

    public static List preProcessText(File file) {
        remedies = new ArrayList<>();
        String fileText = getFileText(file);

        String[] commaText = fileText.split(";");
        for (int i = 0; i < commaText.length; i++) {
            if (commaText[i].contains("--")) {
                Collections.addAll(remedies, commaText[i].split("--")[1].split(","));
            }
        }
        return remedies;
    }

    private static Map<String, List<String>> processHtmlText(String fileText) {
        remedies = new ArrayList<>();
        symptomeRemedies = new LinkedHashMap<String, List<String>>();
        String[] commaText = fileText.split(";");
        int j = 0;
        for (int i = 0; i < commaText.length; i++) {
            String id = "(" + j + ")";
            if (commaText[i].contains("--")) {
                symptomeRemedies.put(id + " " + commaText[i].split("--")[0], Arrays.asList(commaText[i].split("--")[1].split(",")));
                j++;
            } else if (!commaText[i].isEmpty()) {
                symptomeRemedies.put(commaText[i], new LinkedList<String>());
            }

        }
        //TODO save to file
        return symptomeRemedies;
    }


    public static Map<String, Integer> findOccurences() {

        //group remedies together by keys
        Map<String, Integer> collect =
                remedies.stream().map(m -> m.replace(" ", "")).collect(groupingBy(Function.identity(), summingInt(e -> 1)));

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

    public static Matcher findString(String searchedString, String text) {
        //Pattern pattern = Pattern.compile("\\b" + searchedString + "\\b");
        Pattern pattern = Pattern.compile(searchedString);
        Matcher matcher = pattern.matcher(text); //Where input is a TextInput class
        boolean found = matcher.find(0);
        if (found) {
            return matcher;
        }
        return null;
    }

    private static String getFileText(File file) {
        String fileText = null;
        try {
            fileText = IOUtils.readTextFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileText;
    }

    public static Map selectSymptome(String partialKey) {
        Map<String, List<String>> map = symptomeRemedies.entrySet().stream().filter(e -> e.getKey().contains(partialKey)).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        selectedSymptomesWithRemedies.putAll(map);
        //selectedSymptomesWithRemedies.forEach((k,v)->System.out.println(k+v));
        //remedies.addAll(selectedSymptomesWithRemedies.values())
        return selectedSymptomesWithRemedies;
    }

    public static Map unSelectSymptome(String id) {
        for (String key:selectedSymptomesWithRemedies.keySet()){
            if(key.contains(id)){
                selectedSymptomesWithRemedies.remove(key);
            }
        }
        //selectedSymptomesWithRemedies.forEach((k,v)->System.out.println(k+v));
        return selectedSymptomesWithRemedies;
    }
}
