/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.manianis.utils;

/**
 *
 * @author hp
 */
public class StringUtil {
    public static String removeSpaces(String ch) {
        StringBuilder newCh = new StringBuilder();
        for (int i = 0; i < ch.length(); i++) {
            char car = ch.charAt(i);
            if (car == ' ') {
                if (newCh.length() == 0) {
                    continue;
                } else if (newCh.charAt(newCh.length() - 1) != ' ') {
                    newCh.append(car);
                }
            } else {
                newCh.append(car);
            }
        }
        return newCh.toString().trim();
    }
    
    public static String filterLetters(String ch) {
        StringBuilder newCh = new StringBuilder();
        for (int i = 0; i < ch.length(); i++) {
            char car = ch.charAt(i);
            if ((car >= 'a' && car <= 'z') || (car >= 'A' && car <= 'Z') || (car >= '0' && car <= '9') || car == ' ') {
                newCh.append(car);
            }
        }
        return newCh.toString();
    }
}
