package java.awt;

public class AlphaCompositeFactory {
    /**
     * Recreates from given AlphaComposite a new AlphaComposite with some fixes for JamVM.
     * <br/>Fixes:
     * <li>If rule is Clear, opacity always 1.0f</li>
     * <li>If opacity is 1.0f, round opacity with scale of 4</li>
     * @param rule
     * @param alpha
     * @return AlphaComposite with opacity fixed for JamVM
     */
    public static AlphaComposite create(int rule, float alpha) {
        AlphaComposite ac = AlphaComposite.getInstance(rule, alpha);
        return create(ac);
    }

    /**
     * Recreates from given AlphaComposite a new AlphaComposite with some fixes for JamVM.
     * <br/>Fixes:
     * <li>If rule is Clear, opacity always 1.0f</li>
     * <li>If opacity is 1.0f, round opacity with scale of 4</li>
     * @param ac
     * @return AlphaComposite with opacity fixed for JamVM
     */
    public static AlphaComposite create(AlphaComposite ac) {
        return applyFixesAlphaComposite(ac);
    }


    private static AlphaComposite applyFixesAlphaComposite(AlphaComposite ac) {
        if (ac.getRule() == AlphaComposite.CLEAR) {
            ac = AlphaComposite.Clear;
        }

        if (ac.getAlpha() == 1.0f) {
            float newAlpha = MathUtils.floorWithScale(ac.getAlpha(), 4);
            System.out.println(newAlpha);
            ac = AlphaComposite.getInstance(ac.getRule(), newAlpha);
        }
        return ac;
    }
}
