import java.util.*;

public class TextCompressionUtility {

    private static int codeIndex = 1;
    private static Map<String, String> codeToPhrase = new HashMap<>();
    private static Map<String, String> phraseToCode = new HashMap<>();
    private static Map<String, Integer> phraseCount = new HashMap<>();


    // Step 1: Identify recurring phrases (three words) and assign codes
    private static String compressRepeatingThreeWordGroups(String text) {

        phraseToCode.clear();
        phraseCount.clear();
        String[] words = text.split("\\s+");


        // Identify recurring phrases (three words)
        for (int i = 0; i < words.length - 2; i++) {
            String phrase = words[i] + " " + words[i + 1] + " " + words[i + 2];
            phraseCount.put(phrase, phraseCount.getOrDefault(phrase, 0) + 1);
        }


        for (Map.Entry<String, Integer> entry : phraseCount.entrySet()) {
            if (entry.getValue() > 1) { // Only compress phrases appearing more than once
                String code = "#" + codeIndex++;
                phraseToCode.put(entry.getKey(), code);
                codeToPhrase.put(code, entry.getKey());
            }
        }


        // Replace phrases with codes
        String compressedText = text;
        for (Map.Entry<String, String> entry : phraseToCode.entrySet()) {
            compressedText = compressedText.replace(entry.getKey(), entry.getValue());
        }

        return compressRepeatingTwoWordGroups(compressedText);
    }


    // Step 2: Identify recurring phrases (two words) and assign codes
    private static String compressRepeatingTwoWordGroups(String text) {


        phraseCount.clear();
        String[] words = text.split("\\s+");

        System.out.println("words = " + Arrays.toString(words));

        // Identify recurring phrases (two words)
        for (int i = 0; i < words.length - 1; i++) {
            if (!words[i].contains("#") && !words[i + 1].contains("#")) {

                String phrase = words[i] + " " + words[i + 1];
                phraseCount.put(phrase, phraseCount.getOrDefault(phrase, 0) + 1);

            }

        }

        System.out.println("phraseCount = " + phraseCount);


        for (Map.Entry<String, Integer> entry : phraseCount.entrySet()) {
            if (!entry.getKey().contains("#") && entry.getValue() > 1) { // Only compress phrases appearing more than once
                String code = "#" + codeIndex++;
                phraseToCode.put(entry.getKey(), code);
                codeToPhrase.put(code, entry.getKey());
            }
        }


        // Replace phrases with codes
        String compressedText = text;
        for (Map.Entry<String, String> entry : phraseToCode.entrySet()) {
            compressedText = compressedText.replace(entry.getKey(), entry.getValue());
        }

        return compressRepeatingWords(compressedText);

    }


    // Step 3: Identify recurring phrases (one word) and assign codes
    private static String compressRepeatingWords(String text) {


        phraseCount.clear();
        String[] words = text.split("\\s+");

        // Identify recurring phrases (one word)
        for (String word : words) {
            if (!word.contains("#")) {
                phraseCount.put(word, phraseCount.getOrDefault(word, 0) + 1);
            }
        }


        for (Map.Entry<String, Integer> entry : phraseCount.entrySet()) {
            if (!entry.getKey().contains("#") && entry.getValue() > 1) { // Only compress phrases appearing more than once
                String code = "#" + codeIndex++;
                phraseToCode.put(entry.getKey(), code);
                codeToPhrase.put(code, entry.getKey());
            }
        }


        // Replace phrases with codes
        String compressedText = text;
        for (Map.Entry<String, String> entry : phraseToCode.entrySet()) {
            compressedText = compressedText.replace(entry.getKey(), entry.getValue());
        }

        return compressNonRepeatingRemainingWords(compressedText);

    }


    // Step 4: Identify non-repeating phrases and assign codes
    private static String compressNonRepeatingRemainingWords(String text) {


        phraseCount.clear();
        String[] words = text.split("\\s+");


        // Identify remaining phrases (non-repeating)
        for (String word : words) {
            if (!word.contains("#")) {
                phraseCount.put(word, phraseCount.getOrDefault(word, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : phraseCount.entrySet()) {
            if (!entry.getKey().contains("#") && entry.getValue() == 1) { // Compress phrases appearing once
                String code = "#" + codeIndex++;
                phraseToCode.put(entry.getKey(), code);
                codeToPhrase.put(code, entry.getKey());
            }
        }

        // Replace phrases with codes
        String compressedText = text;
        for (Map.Entry<String, String> entry : phraseToCode.entrySet()) {
            compressedText = compressedText.replace(entry.getKey(), entry.getValue());
        }

        return compressedText;

    }


    // Step 5: Calculate word level compression ratio
    private static double calculateUniqueWordCompressionRatio(String originalText, String compressedText) {
        double originalSize = originalText.split("\\s+").length;

        String[] words = compressedText.split("\\s+");
        Set<String> uniqueWords = new HashSet<>(Arrays.asList(words));
        double compressedSize = uniqueWords.size();

        return (1 - (compressedSize / originalSize)) * 100;
    }


    // Step 6: Decompress text by replacing codes with original phrases
    private static String decompress(String compressedText) {
        String decompressedText = compressedText;
        for (Map.Entry<String, String> entry : codeToPhrase.entrySet()) {
            decompressedText = decompressedText.replace(entry.getKey(), entry.getValue());
        }
        return decompressedText;
    }


    public static void process(String text) {


        if (text == null || text.trim().isEmpty()) {
            System.out.println("Input text is empty or null.");
            return;
        }

        if (text.contains("#")) {
            System.out.println("Text must not contain #.");
            return;
        }

        System.out.println("\nOriginal Text:\n" + text);

        int len = text.split("\\s+").length;
        String compressedText;

        if (len>5){
            compressedText = compressRepeatingThreeWordGroups(text);
        } else if (len>3) {
            compressedText = compressRepeatingThreeWordGroups(text);
        }else {
           compressedText = compressRepeatingWords(text);
        }


        System.out.println("\nCompressed Text:\n" + compressedText);

        System.out.println("\nAssigning code to phrase:\n " + phraseToCode);

        String decompressedText = decompress(compressedText);
        System.out.println("\nDecompressed Text:\n" + decompressedText);

        double ratio = calculateUniqueWordCompressionRatio(text, compressedText);
        System.out.println("\nCompression efficiency by words: " + String.format("%.2f", ratio) + "%");
        if (ratio == 0) {
            System.out.println("The original and compressed texts have the same size!");
        }

        if (text.equals(decompressedText)) {
            System.out.println("\nDecompression verified successfully!");
        } else {
            System.out.println("\nError: Decompressed text does not match the original!");
        }
    }


    public static void main(String[] args) {

        // String inputText =   "Java is fun. Java is powerful. Java is everywhere. Java is fun. Fun is important and everywhere.";

        String inputText = null;
        process(inputText);
    }
}



