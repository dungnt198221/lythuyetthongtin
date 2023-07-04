/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cauc;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.*;

public class cauc {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập chuỗi ký tự: ");
        String input = scanner.nextLine();

        Map<Character, Integer> characterCount = countCharacters(input);

        List<Map.Entry<Character, Integer>> sortedEntries = sortEntriesByValue(characterCount);
        System.out.println("Ký tự đã xuất hiện và số lần xuất hiện tương ứng (sắp xếp theo giá trị giảm dần):");
        for (Map.Entry<Character, Integer> entry : sortedEntries) {

            System.out.println(entry.getKey() + ": " + entry.getValue());

        }

        int numberOfKeys = characterCount.size();
        System.out.println("Số lượng key trong map: " + numberOfKeys);

        String[] a = new String[numberOfKeys];
        for (int i = 0; i < numberOfKeys; i++) {
            a[i] = "";
        }
        for (int i = 1; i < numberOfKeys; i++) {

            for (int j = 1; j <= i; j++) {
                a[i] += "1";
            }
        }
        for (int i = 0; i < numberOfKeys - 1; i++) {
            a[i] += "0";
        }
        //ma hoa Shannon-Fano
        int b = 0;
        for (Map.Entry<Character, Integer> entry : sortedEntries) {
            System.out.println(entry.getKey() + ": " + a[b]);
            b++;
        }
        //tinh tong value
        int c = 0;
        for (Map.Entry<Character, Integer> entry : sortedEntries) {
            c += entry.getValue();
        }

        //tinh tong entropy
        double entropy = 0;
        for (Map.Entry<Character, Integer> entry : sortedEntries) {
            double sx = entry.getValue() / (double) c;
            if (sx != 0) {
                entropy += -(sx * (Math.log(sx) / Math.log(2)));
            }
        }
        double mau = 0;
        double iii = 1;
        for (Map.Entry<Character, Integer> entry : sortedEntries) {
            double sx = entry.getValue() / (double) c;

            if (sx != 0) {
                mau += (sx * iii);
            }
            if (iii == (numberOfKeys - 1)) {
                mau += (sx * iii);
                break;
            }
            iii++;
        }

        System.out.println("Hiệu suất mã hóa :" + (double) (entropy / mau));
        System.out.println("Tính dư thừa :" + (1-(entropy / mau)));

    }

    public static Map<Character, Integer> countCharacters(String input) {
        Map<Character, Integer> characterCount = new HashMap<>();

        for (char c : input.toCharArray()) {
            // Chuyển đổi ký tự thành chữ hoa để không phân biệt chữ hoa, chữ thường
            char uppercaseChar = Character.toUpperCase(c);

            // Kiểm tra xem ký tự đã tồn tại trong map hay chưa
            if (characterCount.containsKey(uppercaseChar)) {
                // Nếu đã tồn tại, tăng số lần xuất hiện lên 1
                characterCount.put(uppercaseChar, characterCount.get(uppercaseChar) + 1);
            } else {
                // Nếu chưa tồn tại, thêm ký tự vào map và đặt số lần xuất hiện là 1
                characterCount.put(uppercaseChar, 1);
            }
        }

        return characterCount;
    }

    public static List<Map.Entry<Character, Integer>> sortEntriesByValue(Map<Character, Integer> map) {
        List<Map.Entry<Character, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        return entries;
    }
}
