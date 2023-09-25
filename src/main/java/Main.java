import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger cool3 = new AtomicInteger(0);
    public static AtomicInteger cool4 = new AtomicInteger(0);
    public static AtomicInteger cool5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();

        String[] texts = new String[100_000];

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>();

        var thread1 = new Thread(() -> {
            for (var text : texts) {
                if (text.contentEquals(new StringBuilder(text).reverse())) {
                    add(text);
                }
            }
        });
        threads.add(thread1);
        thread1.start();

        var thread2 = new Thread(() -> {
            for (var text : texts) {
                if (isOnlyOneChar(text)) {
                    add(text);
                }
            }
        });
        threads.add(thread2);
        thread2.start();

        var thread3 = new Thread(() -> {
            for (var text : texts) {
                if (isWordInAscendingOrder(text) && !isOnlyOneChar(text)) {
                    add(text);
                }
            }
        });
        threads.add(thread3);
        thread3.start();

        for (var thread : threads) {
            thread.join();
        }

        System.out.println("Красивых слов с длиной 3: " + cool3.get());
        System.out.println("Красивых слов с длиной 4: " + cool4.get());
        System.out.println("Красивых слов с длиной 5: " + cool5.get());
    }

    private static boolean isOnlyOneChar(String word) {
        return word.chars().allMatch(c -> c == word.charAt(0));
    }

    private static boolean isWordInAscendingOrder(String word) {
        if (word == null || word.isEmpty()) {
            return false; // Пустая строка или null не могут быть в возрастающем порядке.
        }

        char currentChar = word.charAt(0);
        for (int i = 1; i < word.length(); i++) {
            char nextChar = word.charAt(i);

            // Проверяем, что следующий символ идет после текущего в алфавитном порядке.
            if (nextChar < currentChar) {
                return false;
            }

            currentChar = nextChar;
        }

        return true; // Если все символы идут по возрастанию.
    }

    private static void add(String text) {
        switch (text.length()) {
            case 3:
                cool3.incrementAndGet();
            case 4:
                cool4.incrementAndGet();
            case 5:
                cool5.incrementAndGet();
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}
