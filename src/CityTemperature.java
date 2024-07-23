import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CityTemperature {
    private static final String CSV_FILE = "merged_data[1].csv";
    private static final String CSV_SPLIT_BY = ",";
    private static final int CITY_INDEX = 1;
    private static final int TEMPERATURE_INDEX = 2;

    public static void main(String[] args) {
        long startTime = System.nanoTime(); // Début de la mesure du temps
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); // Début de la mesure de la mémoire

        Map<String, double[]> cityTemperatures = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            processFile(br, cityTemperatures);
            computeAverages(cityTemperatures);
            printResults(cityTemperatures);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime(); // Fin de la mesure du temps
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); // Fin de la mesure de la mémoire

        // Calculer et afficher le temps d'exécution et la mémoire utilisée
        long duration = endTime - startTime;
        long memoryUsed = endMemory - startMemory;

        System.out.println("Temps d'exécution: " + duration / 1_000_000 + " ms");
        System.out.println("Mémoire utilisée: " + memoryUsed / 1024 + " KB");
    }

    private static void processFile(BufferedReader br, Map<String, double[]> cityTemperatures) throws IOException {
        String line;
        // Lire et ignorer la première ligne (en-têtes)
        br.readLine();

        while ((line = br.readLine()) != null) {
            String[] data = line.split(CSV_SPLIT_BY);
            
            // Vérifiez si la ligne contient le nombre attendu de colonnes
            if (data.length < 3) {
                System.err.println("Ligne mal formée: " + line);
                continue;
            }

            String city = data[CITY_INDEX];
            String temperatureStr = data[TEMPERATURE_INDEX];

            // Vérifiez si la donnée de température est un nombre valide
            if (!isNumeric(temperatureStr)) {
                // Ignorez cette ligne si la température n'est pas valide
                continue;
            }

            double temperature = Double.parseDouble(temperatureStr);
            cityTemperatures.compute(city, (k, v) -> {
                if (v == null) {
                    return new double[]{temperature, temperature, temperature, 1}; // max, min, sum, count
                } else {
                    v[0] = Math.max(v[0], temperature); // Température maximale
                    v[1] = Math.min(v[1], temperature); // Température minimale
                    v[2] += temperature; // Somme des températures
                    v[3]++; // Nombre de températures
                    return v;
                }
            });
        }
    }

    private static void computeAverages(Map<String, double[]> cityTemperatures) {
        for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
            double[] temps = entry.getValue();
            temps[2] /= temps[3]; // Calcul de la température moyenne
        }
    }

    private static void printResults(Map<String, double[]> cityTemperatures) {
        for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
            double[] temps = entry.getValue();
            System.out.println(entry.getKey() + ": [Max: " + temps[0] + ", Min: " + temps[1] + ", Avg: " + temps[2] + "]");
        }
    }

    private static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
