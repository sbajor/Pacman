package pl.edu.wat.wcy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "HighScores")
public class HighScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "PlayerNick")
    private String playerNick;

    @Column(name = "Score")
    private int score;

    private HighScore() {}

    public HighScore(String playerNick, int score) {
        this.playerNick = playerNick;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public String getPlayerNick() {
        return playerNick;
    }

    public int getScore() {
        return score;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPlayerNick(String playerNick) {
        this.playerNick = playerNick;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HighScore highScore = (HighScore) o;
        return id == highScore.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
