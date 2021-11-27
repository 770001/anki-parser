package com.company;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Парсер под anki
 * Подкладываем txt в source, результат забираем в result
 * Преобразует строку вида
 * 10 - for - для; ибо
 * в
 * For    Для, ибо
 */
public class AnkiParser {

    /**
     * Путь до исходника
     */
    private static final String PATH_TO_SOURCE = "com/company/source.txt";

    /**
     * Путь до результата
     */
    private static final String PATH_TO_RESULT = "src/com/company/result.txt";

    public static void main(String[] args) throws IOException, URISyntaxException {
        String data = getDataFromFileTxt();
        String result = replaceData(data);
        System.out.println(result);
        writeToFileTxt(result);
    }

    /*
    Парсит txt файл со словами
     */
    private static String getDataFromFileTxt() throws URISyntaxException, IOException {
        Path path = Paths.get(Objects.requireNonNull(AnkiParser.class.getClassLoader().getResource(PATH_TO_SOURCE)).toURI());
        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
        return data;
    }

    /*
    Перезаписывает содержимое в нужный вид
     */
    private static String replaceData(String data) {
        data = replace(data, "\\d", ""); //убрать цифры
        data = replace(data, "\"", ""); //убрать "
        data = replace(data, " - ", "\t"); //убрать -
        data = replace(data, "(?m)^\\s+|\\s+$", "");
        data = replace(data, ";", ","); //замена ; на ,
        String preResult = changeFirstLetterToCapital(data, "\t");
        return changeFirstLetterToCapital(preResult, "\n");
    }

    /*
    Перезаписывает данные по регулярному выражению
     */
    private static String replace(String data, String regex, String replacement) {
        return data.replaceAll(regex, replacement);
    }

    /*
    Делает первые буквы большими
     */
    private static String changeFirstLetterToCapital(String words, String splitSymbol) {
        StringBuilder res = new StringBuilder();
        String[] strArr = words.split(splitSymbol);
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            str = new String(stringArray);
            res.append(str).append(splitSymbol);
        }
        return res.toString();
    }

    /*
    Записывает результат в файл
     */
    private static void writeToFileTxt(String result) throws IOException {
        Files.write(Paths.get(PATH_TO_RESULT), result.getBytes());
    }
}
