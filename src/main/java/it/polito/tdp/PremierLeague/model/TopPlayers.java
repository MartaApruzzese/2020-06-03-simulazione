package it.polito.tdp.PremierLeague.model;

public class TopPlayers implements Comparable<TopPlayers>{
	private Player player;
	private double pesoArco;
	
	public TopPlayers(Player player, double pesoArco) {
		super();
		this.player = player;
		this.pesoArco = pesoArco;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public double getPesoArco() {
		return pesoArco;
	}

	public void setPesoArco(double pesoArco) {
		this.pesoArco = pesoArco;
	}

	@Override
	public int compareTo(TopPlayers o) {
		
		return (int)(o.pesoArco-this.pesoArco);
	}
	
	
}
