import org.json.JSONObject;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShamirSecretSharing {

    // Method to parse the JSON input from a file
    public static JSONObject readJSONFile(String filePath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONObject(content);
    }

    // Method to decode the y-values from various bases to base 10
    public static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);  // Convert value to base 10
    }

    // Lagrange interpolation to calculate constant term 'c'
    public static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k) {
        BigInteger result = BigInteger.ZERO;

        for (Map.Entry<Integer, BigInteger> entry1 : points.entrySet()) {
            int x_i = entry1.getKey();
            BigInteger y_i = entry1.getValue();
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (Map.Entry<Integer, BigInteger> entry2 : points.entrySet()) {
                int x_j = entry2.getKey();
                if (x_i != x_j) {
                    numerator = numerator.multiply(BigInteger.valueOf(-x_j));
                    denominator = denominator.multiply(BigInteger.valueOf(x_i - x_j));
                }
            }
            BigInteger term = y_i.multiply(numerator).divide(denominator);
            result = result.add(term);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Parse the first test case from the JSON file
        JSONObject jsonInput1 = readJSONFile("testcase1.json");
        JSONObject jsonKeys1 = jsonInput1.getJSONObject("keys");
        int n1 = jsonKeys1.getInt("n");
        int k1 = jsonKeys1.getInt("k");

        // Store the decoded points
        Map<Integer, BigInteger> points1 = new HashMap<>();
        for (String key : jsonInput1.keySet()) {
            if (!key.equals("keys")) {
                JSONObject root = jsonInput1.getJSONObject(key);
                int x = Integer.parseInt(key);
                int base = root.getInt("base");
                String value = root.getString("value");
                BigInteger y = decodeValue(value, base);
                points1.put(x, y);
            }
        }

        // Solve for the constant term 'c' using Lagrange interpolation
        BigInteger constantTerm1 = lagrangeInterpolation(points1, k1);
        System.out.println("Constant term for Test Case 1: " + constantTerm1);

        // Parse the second test case from the JSON file
        JSONObject jsonInput2 = readJSONFile("testcase2.json");
        JSONObject jsonKeys2 = jsonInput2.getJSONObject("keys");
        int n2 = jsonKeys2.getInt("n");
        int k2 = jsonKeys2.getInt("k");

        // Store the decoded points
        Map<Integer, BigInteger> points2 = new HashMap<>();
        for (String key : jsonInput2.keySet()) {
            if (!key.equals("keys")) {
                JSONObject root = jsonInput2.getJSONObject(key);
                int x = Integer.parseInt(key);
                int base = root.getInt("base");
                String value = root.getString("value");
                BigInteger y = decodeValue(value, base);
                points2.put(x, y);
            }
        }

        // Solve for the constant term 'c' using Lagrange interpolation
        BigInteger constantTerm2 = lagrangeInterpolation(points2, k2);
        System.out.println("Constant term for Test Case 2: " + constantTerm2);
    }
}