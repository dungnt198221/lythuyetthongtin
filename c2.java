/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package b2c2;

/**
 *
 * @author Admin
 */
import java.util.*;

class Symbol {
    public String symbol;
    public double probability;
    public String code;
    
    public Symbol(String symbol, double probability, String code) {
        this.symbol = symbol;
        this.probability = probability;
        this.code = code;
    }
}

public class c2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your string: ");
        String name = scanner.nextLine().toUpperCase();

        Map<Character, Integer> charFreq = new HashMap<>();
        int charCount = 0;

        for (char c : name.toCharArray()) {
            if (Character.isWhitespace(c)) {
                continue;
            }
            charCount++;
            charFreq.put(c, charFreq.getOrDefault(c, 0) + 1);
        }

        int countDiffChar = charFreq.size();
        Symbol[] symbols = new Symbol[countDiffChar];

        PriorityQueue<Node> nodes = new PriorityQueue<>(Comparator.comparingInt(n -> n.freq));

        int i = 0;
        for (Map.Entry<Character, Integer> entry : charFreq.entrySet()) {
            char symbol = entry.getKey();
            int freq = entry.getValue();
            nodes.offer(new Node(freq, String.valueOf(symbol)));
            symbols[i] = new Symbol(String.valueOf(symbol), (double) freq / charCount, "");
            i++;
        }

        Symbol[] encodedSymbols = shannonFanoEncoding(symbols);

        while (nodes.size() > 1) {
            Node left = nodes.poll();
            Node right = nodes.poll();
            left.huff = 0;
            right.huff = 1;
            Node newNode = new Node(left.freq + right.freq, left.symbol + right.symbol, left, right);
            nodes.offer(newNode);
        }

        printNodes(nodes.peek());
        System.out.println("-------------------------------------------");

        for (Symbol symbol : encodedSymbols) {
            System.out.println("Symbol: " + symbol.symbol + ", Probability: " + symbol.probability + ", Code: " + symbol.code);
        }

        double H1 = calculateEntropy(encodedSymbols);
        double H2 = calculateAverageCodewordLength(encodedSymbols);

        double efficiency = H1 / H2;
        double redundancy = 1 - efficiency;

        System.out.println("\nEfficiency is: " + efficiency);
        System.out.println("Redundancy is: " + redundancy);
    }

    public static Symbol[] shannonFanoEncoding(Symbol[] symbols) {
        Arrays.sort(symbols, (s1, s2) -> Double.compare(s2.probability, s1.probability));
        assignCode(symbols, 0, symbols.length - 1, "");
        return symbols;
    }

    public static void assignCode(Symbol[] symbols, int start, int end, String code) {
        if (start > end) {
            return;
        }

        if (start == end) {
            symbols[start].code += code;
            return;
        }

        double totalProb = 0;
        for (int i = start; i <= end; i++) {
            totalProb += symbols[i].probability;
        }

        double currProb = 0;
        int splitIndex = start;

        for (int i = start; i <= end; i++) {
            currProb += symbols[i].probability;
            if (currProb >= totalProb / 2) {
                splitIndex = i;
                break;
            }
        }

        for (int i = start; i <= splitIndex; i++) {
            symbols[i].code += "0";
        }
        for (int i = splitIndex + 1; i <= end; i++) {
            symbols[i].code += "1";
        }

        assignCode(symbols, start, splitIndex, code + "0");
        assignCode(symbols, splitIndex + 1, end, code + "1");
    }

    public static void printNodes(Node root) {
        if (root.isLeaf()) {
            System.out.println("Symbol: " + root.symbol + ", Frequency: " + root.freq);
            return;
        }
        printNodes(root.left);
        printNodes(root.right);
    }

    public static double calculateEntropy(Symbol[] symbols) {
        double entropy = 0;
        for (Symbol symbol : symbols) {
            entropy += symbol.probability * Math.log(1 / symbol.probability) / Math.log(2);
        }
        return entropy;
    }

    public static double calculateAverageCodewordLength(Symbol[] symbols) {
        double averageLength = 0;
        for (Symbol symbol : symbols) {
            averageLength += symbol.probability * symbol.code.length();
        }
        return averageLength;
    }

    static class Node {
        public int freq;
        public String symbol;
        public Node left;
        public Node right;
        public int huff;

        public Node(int freq, String symbol) {
            this.freq = freq;
            this.symbol = symbol;
            this.left = null;
            this.right = null;
            this.huff = -1;
        }

        public Node(int freq, String symbol, Node left, Node right) {
            this.freq = freq;
            this.symbol = symbol;
            this.left = left;
            this.right = right;
            this.huff = -1;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }
    }
}
