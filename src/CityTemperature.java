import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CityTemperature {
    public static void main(String[] args) {
        String csvFile = "merged_data[1].csv";
        Map<String, double[]> cityTemperatures = new HashMap<>();
        
        // Obtenir l'instance de Runtime pour la gestion de la mémoire
        Runtime runtime = Runtime.getRuntime();
        
        // Mesurer la mémoire avant l'exécution
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        long startTime = System.nanoTime(); // Début de la mesure du temps

        try (Stream<String> lines = Files.lines(Paths.get(csvFile))) {
            lines.skip(1) // Ignorer l'en-tête
                 .forEach(line -> {
                     String[] data = line.split(",");
                     if (data.length < 3) return;

                     String city = data[1];
                     double temperature;

                     try {
                         temperature = Double.parseDouble(data[2]);
                     } catch (NumberFormatException e) {
                         return; // Ignorer les lignes avec des températures non valides
                     }

                     cityTemperatures.merge(city, new double[] { temperature, temperature, temperature, 1 },
                         (existing, newTemps) -> {
                             existing[0] = Math.max(existing[0], newTemps[0]); // Max
                             existing[1] = Math.min(existing[1], newTemps[1]); // Min
                             existing[2] += newTemps[2]; // Sum
                             existing[3] += newTemps[3]; // Count
                             return existing;
                         }
                     );
                 });

            // Afficher les résultats
            cityTemperatures.forEach((city, temps) -> {
                temps[2] /= temps[3]; // Calculer la température moyenne
                System.out.printf("%s: [Max: %.1f, Min: %.1f, Avg: %.1f]%n", city, temps[0], temps[1], temps[2]);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime(); // Fin de la mesure du temps
        long duration = endTime - startTime; // Durée en nanosecondes

        // Mesurer la mémoire après l'exécution
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        // Afficher la durée d'exécution et la mémoire utilisée
        System.out.printf("Temps d'exécution: %.3f secondes%n", duration / 1_000_000_000.0);
        System.out.printf("Mémoire utilisée: %.2f Mo%n", memoryUsed / 1_048_576.0); // Convertir en mégaoctets
    }
}
