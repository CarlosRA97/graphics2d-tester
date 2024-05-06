package java.lang;

public class MathUtils {
    /**
     * @param num number to remove decimals
     * @param numOfDecimals number of decimals to keep
     * @return the number with only n decimals
     */
    public static double floorWithScale(double num, int numOfDecimals) {
        double mainPower = Math.pow(10, numOfDecimals);
        double secondaryPower = Math.pow(10, numOfDecimals - 1);
        return (int)((num * mainPower) - (num * secondaryPower) / 900) / mainPower + 1/mainPower;
    }

    /**
     * @param num number to remove decimals
     * @param numOfDecimals number of decimals to keep
     * @return the number with only n decimals
     */
    public static float floorWithScale(float num, int numOfDecimals) {
        float mainPower = (float) Math.pow(10, numOfDecimals);
        float secondaryPower = (float) Math.pow(10, numOfDecimals - 1);
        return (int)((num * mainPower) - (num * secondaryPower) / 900)/mainPower + 1/mainPower;
    }
}
