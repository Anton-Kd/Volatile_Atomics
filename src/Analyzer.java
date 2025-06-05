import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Analyzer {

    public static BlockingQueue<String> queueSymbol_A = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueSymbol_B = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueSymbol_C = new ArrayBlockingQueue<>(100);
    public static final long TEXTS = 10_000;

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < TEXTS; i++) {
                try {
                queueSymbol_A.put(generateText("abc", 100_000));
                System.out.println("\nПоложил 'a' " + i);
                queueSymbol_B.put(generateText("abc", 100_000));
                System.out.println("\nПоложил 'b' " + i);
                queueSymbol_C.put(generateText("abc", 100_000));
                System.out.println("\nПоложил 'c' " + i);
//                Thread.sleep(200);
                } catch (InterruptedException e) {
                    return;
                }

            }

        });

        Thread thread2 = new Thread(() -> {
            characterCounter(queueSymbol_A, 'a');
        });
        Thread thread3 = new Thread(() -> {
            characterCounter(queueSymbol_B, 'b');
        });
        Thread thread4 = new Thread(() -> {
            characterCounter(queueSymbol_C, 'c');
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void characterCounter(BlockingQueue<String> queueSymbol, char letter) {
        long maxSymbol = 0;
        try {
             while (queueSymbol.isEmpty()) {
                String string = queueSymbol.take();
                long count = string.chars()
                        .filter(ch -> ch == letter)
                        .count();
                if (count > maxSymbol) {
                    maxSymbol = count;
                }
                 System.out.printf("Максимальное колличество символов %s равно %d штук\n", letter, maxSymbol);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
