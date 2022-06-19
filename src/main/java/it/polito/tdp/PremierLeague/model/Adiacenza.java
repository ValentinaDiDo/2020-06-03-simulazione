package it.polito.tdp.PremierLeague.model;

public class Adiacenza {

	private Player p1;
	int minutiP1;
	private Player p2;
	int minutiP2;
	private int delta;

	
	
	public Adiacenza(Player p1, int minutiP1, Player p2, int minutiP2, int delta) {
		super();
		this.p1 = p1;
		this.minutiP1 = minutiP1;
		this.p2 = p2;
		this.minutiP2 = minutiP2;
		this.delta = delta;
	}
	public Player getP1() {
		return p1;
	}
	public Player getP2() {
		return p2;
	}
	public int getDelta() {
		return delta;
	}
	public int getMinutiP1() {
		return minutiP1;
	}
	public int getMinutiP2() {
		return minutiP2;
	}
	
	
}
