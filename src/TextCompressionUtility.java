import java.util.*;

public class TextCompressionUtility {

    // Tracks the current index for generating unique codes
    private static int codeIndex = 1;

    // Mapping between generated codes and original phrases
    private static Map<String, String> codeToPhrase = new HashMap<>();

    // Mapping between original phrases and their generated codes
    private static Map<String, String> phraseToCode = new HashMap<>();

    // Tracks the frequency of each phrase in the text
    private static Map<String, Integer> phraseCount = new HashMap<>();

    // Split text into words and punctuation
    private static String[] splitTextIntoWordsAndPunctuation(String text) {
        return text.split("(?<=\\b)(?=[.,!?:;])|(?<=[.,!?:;])(?=\\b)|\\s+");//  Edited  regex expression that splits both before and after word boundaries and punctuation
    }


    // Step 1: Identify recurring phrases,non-repeating phrases and assign codes
    private static String compressText(String text) {
        phraseCount.clear();
        phraseToCode.clear();
        codeToPhrase.clear();
        codeIndex = 1;

        String[] wordsAndPunctuation = splitTextIntoWordsAndPunctuation(text);

        // Count the frequency of each word/punctuation
        for (String word : wordsAndPunctuation) {
            phraseCount.put(word, phraseCount.getOrDefault(word, 0) + 1);
        }

        // Compress phrases appearing once and more than once
        for (Map.Entry<String, Integer> entry : phraseCount.entrySet()) {
            String code = "#" + codeIndex++;
            phraseToCode.put(entry.getKey(), code);
            codeToPhrase.put(code, entry.getKey());
        }

        // Replace phrases with codes
        String compressedText = text;
        for (Map.Entry<String, String> entry : phraseToCode.entrySet()) {
            compressedText = compressedText.replace(entry.getKey(), entry.getValue());
        }

        return compressedText;
    }

    // Step 2: Calculate the compression efficiency as a percentage
    private static double calculateCompressionRatio(String originalText, String compressedText) {
        double originalSize = originalText.length();
        double compressedSize = compressedText.length();
        return (1 - (compressedSize / originalSize)) * 100;
    }

    // Step 3: Decompress text by replacing codes with original phrases
    private static String decompress(String compressedText) {
        String decompressedText = compressedText;
        for (Map.Entry<String, String> entry : codeToPhrase.entrySet()) {
            decompressedText = decompressedText.replace(entry.getKey(), entry.getValue());
        }
        return decompressedText;
    }

    public static void process(String text) {

        // Added null and empty text check
        if (text == null || text.trim().isEmpty()) {
            System.out.println("Input text is empty or null.");
            return;
        }

        // Added check to ensure that the text to be entered does not contain "#"
        if (text.contains("#")) {
            System.out.println("Text must not contain #.");
            return;
        }

        System.out.println("\nOriginal Text:\n" + text);

        String compressedText = compressText(text);

        System.out.println("\nAssigning code to phrase:\n " + phraseToCode);
        System.out.println("\nCompressed Text:\n" + compressedText);

        double ratio = calculateCompressionRatio(text, compressedText);
        System.out.println("\nCompression efficiency: " + String.format("%.2f", ratio) + "%");
        if (ratio == 0) {
            System.out.println("The original and compressed texts have the same size!");
        }

        String decompressedText = decompress(compressedText);
        System.out.println("\nDecompressed Text:\n" + decompressedText);

        if (text.equals(decompressedText)) {
            System.out.println("\nDecompression verified successfully!");
        } else {
            System.out.println("\nError: Decompressed text does not match the original!");
        }
    }

    public static void main(String[] args) {
        String inputText = "Java is fun. Java is powerful.";
        process(inputText);
    }
}



