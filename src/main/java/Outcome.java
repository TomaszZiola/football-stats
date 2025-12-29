public enum Outcome {
    WIN(3, 'W'),
    DRAW(1, 'D'),
    LOSS(0, 'L');

    private final int points;
    private final char letter;

    Outcome(int points, char letter) {
        this.points = points;
        this.letter = letter;
    }

    public int points() {
        return points;
    }

    public char letter() {
        return letter;
    }

    public static Outcome forScore(int score, int opponentScore) {
        if (score > opponentScore) return WIN;
        if (score < opponentScore) return LOSS;
        return DRAW;
    }
}
