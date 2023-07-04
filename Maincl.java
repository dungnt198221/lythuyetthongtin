/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package caub;

import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Node {

    char character;
    int frequency;
    Node left;
    Node right;

    public Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public Node(char character, int frequency, Node left, Node right) {
        this.character = character;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return (left == null) && (right == null);
    }
}

class HuffmanEncoder {

    public static Map<Character, String> encode(Map<Character, Integer> characterCount) {
//        tạo một hàng đợi ưu tiên (PriorityQueue) pq để lưu trữ các nút (Node). Hàng đợi được sắp xếp theo tần số tăng dần.
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);
//        với mỗi cặp key-value trong characterCount, 
//          chúng ta tạo một nút mới với ký tự và tần số tương ứng và thêm nút đó vào hàng đợi ưu tiên pq
        for (Map.Entry<Character, Integer> entry : characterCount.entrySet()) {
            pq.offer(new Node(entry.getKey(), entry.getValue()));
        }
/* trong vòng lặp while, chúng ta lặp lại quá trình sau cho đến khi pq chỉ còn lại một nút duy nhất:

Lấy hai nút có tần số thấp nhất từ hàng đợi pq.
Tạo một nút cha mới với ký tự '\0' (ký tự không có ý nghĩa) và tần số là tổng tần số của hai nút con.
Gán nút con trái và nút con phải cho nút cha vừa tạo.
Thêm nút cha vào hàng đợi pq.
        */
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.frequency + right.frequency, left, right);
            pq.offer(parent);
        }
/*Sau khi hoàn thành vòng lặp, hàng đợi pq chỉ còn lại một nút duy nhất,
        đó chính là nút gốc của cây Huffman. Ta lấy nút gốc đó từ hàng đợi.
        */
        Node root = pq.poll();
        Map<Character, String> encodingMap = new HashMap<>();
        buildEncodingMap(root, "", encodingMap);

        return encodingMap;
    }

    /*
    Tiếp theo, chúng ta tạo một Map encodingMap để lưu trữ bản đồ mã hóa Huffman. 
    Chúng ta gọi phương thức buildEncodingMap để xây dựng bản đồ mã hóa từ nút gốc của cây Huffman. 
    Phương thức này được triển khai theo đệ quy.
     */
    private static void buildEncodingMap(Node node, String encoding, Map<Character, String> encodingMap) {
//         Nếu nút hiện tại là một nút lá, tức là không có nút con trái và nút con phải, 
//         chúng ta thêm ký tự của nút lá và mã hiện tại vào encodingMap.
        if (node.isLeaf()) {
            encodingMap.put(node.character, encoding);
            return;
        }
/*chúng ta gọi đệ quy cho nút con trái với mã hiện tại kết hợp với "0" và nút con phải với mã hiện tại kết hợp với "1".*/
        buildEncodingMap(node.left, encoding + "0", encodingMap);
        buildEncodingMap(node.right, encoding + "1", encodingMap);
    }
}

public class Maincl {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập chuỗi ký tự: ");
        String input = scanner.nextLine();

        Map<Character, Integer> characterCount = countCharacters(input);

        Map<Character, String> huffmanEncodingMap = HuffmanEncoder.encode(characterCount);

        System.out.println("Mã hóa Huffman:");
        for (Map.Entry<Character, String> entry : huffmanEncodingMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
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
}
