package kr._19fstudy.effective_java.ch6.item37;

public enum Phase1 {

    SOLOD, LIQUID, GAS;

    public enum Transition {
        MELT, FREEZE, BOIL, CONDENSE, SUBLIME, DEPOSIT;

        private static final Transition[][] TRANSITIONS = {
                {null, MELT, SUBLIME},
                {FREEZE, null, BOIL},
                {DEPOSIT, CONDENSE, null}
        };

        public static Transition from(Phase1 from, Phase1 to) {
            return TRANSITIONS[from.ordinal()][to.ordinal()];
        }
    }

}
