package de.pax.dsa.model;

import java.util.ArrayList;
import java.util.List;

public class IcarusModel {

	private List<Turn> turns;
	private List<Character> characters;

	public IcarusModel() {
		turns = new ArrayList<>();
		characters = new ArrayList<>();
	}

	public List<Turn> getTurns() {
		return turns;
	}

	public void setTurns(List<Turn> turns) {
		this.turns = turns;
	}

	public List<Character> getCharacters() {
		return characters;
	}

	public void setCharacters(List<Character> characters) {
		this.characters = characters;
	}

}
