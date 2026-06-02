import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static int LENGTH = 100;
    public static int THREAD_COUNT = 1000;
    public static String LETTERS = "RLRFR";

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute(LETTERS, LENGTH);
                int rCount = countingChars(route, 'R');

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(rCount)) {
                        sizeToFreq.put(rCount, sizeToFreq.get(rCount) + 1);
                    } else {
                        sizeToFreq.put(rCount, 1);
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Поиск часто встречающегося количества букв R
        int mostFreqCount = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();
        int mostFrequentFreq = sizeToFreq.get(mostFreqCount);

        System.out.println("Самое частое количество повторений " + mostFreqCount + " (встретилось " + mostFrequentFreq + " раз)");
        System.out.println("Другие размеры:");

        sizeToFreq.entrySet().stream()
                .filter(entry -> entry.getKey() != mostFreqCount)
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)"));

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    // Подсчет количества символов в строке
    public static int countingChars(String str, char ch) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }
    }
