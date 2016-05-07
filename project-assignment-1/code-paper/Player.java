package hangman;

import java.io.Serializable;

public class Player implements Serializable, Comparable<Player> {
	
	private String name;
	private int score;

	public Player(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public int compareTo(Player o) {
		return score - o.getScore();
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	

}
