import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class main {
    public static void main(String[] args) throws Exception {
        // Citirea listei de angajați
        List<Angajat> angajati = JSONHandler.citesteAngajati("src/main/resources/angajat.json");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Meniu ===");
            System.out.println("1. Afișează lista de angajați");
            System.out.println("2. Afișează angajații cu salariul peste 2500 RON");
            System.out.println("3. Afișează angajații din aprilie anul trecut cu funcții de conducere");
            System.out.println("4. Afișează angajații fără funcții de conducere, în ordine descrescătoare a salariilor");
            System.out.println("5. Afișează numele angajaților cu majuscule");
            System.out.println("6. Afișează salariile sub 3000 RON");
            System.out.println("7. Afișează datele primului angajat al firmei");
            System.out.println("8. Afișează statistici despre salarii");
            System.out.println("9. Verifică dacă există un angajat cu numele 'Ion'");
            System.out.println("10. Afișează numărul angajaților angajați vara anului trecut");
            System.out.println("0. Ieșire");
            System.out.print("Alege o opțiune: ");

            int optiune = scanner.nextInt();
            switch (optiune) {
                case 1 -> angajati.forEach(System.out::println);
                case 2 -> angajati.stream()
                        .filter(a -> a.getSalariu() > 2500)
                        .forEach(System.out::println);
                case 3 -> {
                    int anulTrecut = LocalDate.now().getYear() - 1;
                    angajati.stream()
                            .filter(a -> a.getDataAngajarii().getYear() == anulTrecut &&
                                    a.getDataAngajarii().getMonth() == Month.APRIL &&
                                    (a.getPost().toLowerCase().contains("sef") ||
                                            a.getPost().toLowerCase().contains("director")))
                            .forEach(System.out::println);
                }
                case 4 -> angajati.stream()
                        .filter(a -> !a.getPost().toLowerCase().contains("sef") &&
                                !a.getPost().toLowerCase().contains("director"))
                        .sorted(Comparator.comparingDouble(Angajat::getSalariu).reversed())
                        .forEach(System.out::println);
                case 5 -> angajati.stream()
                        .map(a -> a.getNume().toUpperCase())
                        .forEach(System.out::println);
                case 6 -> angajati.stream()
                        .map(Angajat::getSalariu)
                        .filter(s -> s < 3000)
                        .forEach(System.out::println);
                case 7 -> angajati.stream()
                        .min(Comparator.comparing(Angajat::getDataAngajarii))
                        .ifPresentOrElse(
                                System.out::println,
                                () -> System.out.println("Nu există angajați.")
                        );
                case 8 -> {
                    var statistici = angajati.stream()
                            .collect(Collectors.summarizingDouble(Angajat::getSalariu));
                    System.out.printf("Salariul mediu: %.2f%n", statistici.getAverage());
                    System.out.printf("Salariul minim: %.2f%n", statistici.getMin());
                    System.out.printf("Salariul maxim: %.2f%n", statistici.getMax());
                }
                case 9 -> angajati.stream()
                        .map(Angajat::getNume)
                        .filter(nume -> nume.contains("Ion"))
                        .findAny()
                        .ifPresentOrElse(
                                ion -> System.out.println("Firma are cel puțin un Ion angajat."),
                                () -> System.out.println("Firma nu are niciun Ion angajat.")
                        );
                case 10 -> {
                    int anulTrecut = LocalDate.now().getYear() - 1;
                    long angajatiVara = angajati.stream()
                            .filter(a -> a.getDataAngajarii().getYear() == anulTrecut &&
                                    (a.getDataAngajarii().getMonth() == Month.JUNE ||
                                            a.getDataAngajarii().getMonth() == Month.JULY ||
                                            a.getDataAngajarii().getMonth() == Month.AUGUST))
                            .count();
                    System.out.println(angajatiVara);
                }
                case 0 -> {
                    System.out.println("Programul se încheie.");
                    running = false;
                }
                default -> System.out.println("Opțiune invalidă. Reîncercați.");
            }
        }
    }
}
