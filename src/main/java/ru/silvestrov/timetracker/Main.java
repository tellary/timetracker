package ru.silvestrov.timetracker;

import ru.silvestrov.timetracker.data.ContextUtil;

/**
 * Created by Silvestrov Ilya
 * Date: 7/23/12
 * Time: 12:22 AM
 */
public class Main {
    public static void main(String[] args) {
        ContextUtil.createContext("./timeDB");
    }
}
