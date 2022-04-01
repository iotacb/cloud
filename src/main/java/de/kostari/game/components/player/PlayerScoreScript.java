package de.kostari.game.components.player;

import de.kostari.cloud.core.components.Component;

public class PlayerScoreScript extends Component {

    private int currentScore;
    private int highscore;

    public PlayerScoreScript() {
        currentScore = 0;
        highscore = 0;
    }

    public void increaseScore(int score) {
        currentScore += score;
        if (currentScore > highscore) {
            highscore = currentScore;
        }
    }

    public void decreaseScore(int score) {
        currentScore -= score;
    }

    public void resetScore() {
        currentScore = 0;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighscore() {
        return highscore;
    }

}
