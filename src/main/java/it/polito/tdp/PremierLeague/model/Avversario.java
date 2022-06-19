package it.polito.tdp.PremierLeague.model;

public class Avversario implements Comparable<Avversario> {

	Player player;
	int peso;
	public Avversario(Player p, int peso) {
		super();
		this.player = p;
		this.peso = peso;
	}
	public Player getPlayer() {
		return player;
	}
	public int getPeso() {
		return peso;
	}
	
	
	
	@Override
	public String toString() {
		return  player.toString() + " | " + peso + "\n";
	}
	@Override
	public int compareTo(Avversario o) {
		// TODO Auto-generated method stub
		return (int)(o.peso-this.peso);
	}
	
	
}
