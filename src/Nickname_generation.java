import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Nickname_generation {

    public static AtomicInteger count3 = new AtomicInteger(0);
    public static AtomicInteger count4 = new AtomicInteger(0);
    public static AtomicInteger count5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread1 = new Thread(() -> {
            List<String> palindromes = Arrays.stream(texts)
                    .filter(Nickname_generation::isPalindrome)
                    .distinct()
                    .sorted()
                    .toList();
            beautifulNames(palindromes);
        });

        Thread thread2 = new Thread(() -> {
            List<String> oneCharWords = Arrays.stream(texts)
                    .filter(text -> text.chars().distinct().count() == 1)
                    .distinct()
                    .sorted()
                    .toList();
            beautifulNames(oneCharWords);
        });

        Thread thread3 = new Thread(() -> {
            List<String> alphabeticalOrder = Arrays.stream(texts)
                    .distinct()
                    .sorted()
                    .toList();
            beautifulNames(alphabeticalOrder);
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread3.join();
        thread2.join();
        thread1.join();


        System.out.printf("""
                \nКрасивых слов с длиной 3: %s шт
                Красивых слов с длиной 4: %s шт
                Красивых слов с длиной 5: %s шт
                """, count3, count4, count5);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean isPalindrome(String str) {
//        String cleanStr = str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static void beautifulNames(List<String> nicknames) {
        for (String nickname : nicknames) {
            if (nickname.length() == 3) {
                count3.getAndIncrement();
            }
            if (nickname.length() == 4) {
                count4.getAndIncrement();
            }
            if (nickname.length() == 5) {
                count5.getAndIncrement();
            }
        }
    }
}
