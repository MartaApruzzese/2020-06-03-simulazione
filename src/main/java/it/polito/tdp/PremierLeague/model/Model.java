package it.polito.tdp.PremierLeague.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private List<Player> vertici;
	private Map<Integer, Player> idMap;
	private Player topPlayer;
	private List<Player> best;
	private double gradoTitolarita;
	private int NGiocatori;
	
	public Model() {
		this.dao= new PremierLeagueDAO();
	}
	
	public void creaGrafo(double media) {
		this.grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap= new HashMap<>();
		this.vertici= new ArrayList<>();
		
		
		//Popolo idMap
		for(Player p: this.dao.listAllPlayers()) {
			idMap.put(p.playerID, p);
		}
		
		//Creo i vertici
		for(Player p: this.dao.getVertici(idMap, media)) {
			this.vertici.add(p);
		}
		Graphs.addAllVertices(this.grafo, vertici);
		
		
		//Creo gli archi
		for(Player p1: this.vertici) {
			for(Player p2: this.vertici) {
				double peso= this.dao.getPeso(p1, p2);
				if(peso!=666) {
					if(peso>0.0) {
						Graphs.addEdgeWithVertices(this.grafo, p1, p2, peso);
					}else if(peso<0.0){
						Graphs.addEdgeWithVertices(this.grafo, p2, p1, Math.abs(peso));
					}
				}
			}
		}
		
		
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public int getNVertici() {
		return this.vertici.size();
	}
	
	public List<Player> getVertici(){
		return this.vertici;
	}
	
	
	/**
	 * RISULUZIONE PUNTO 1D
	 */
	
	
	public List<TopPlayers> getTopPlayers(){
		
		List<TopPlayers> daInserire= new ArrayList<>();
		topPlayer=null;
		double max=0;
		for(Player p: this.vertici) {
			if(this.grafo.outDegreeOf(p)>max) {
				topPlayer=p;
				max=this.grafo.outDegreeOf(p);
			}
		}
		
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(topPlayer)) {
			daInserire.add(new TopPlayers(this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e)));
		}
		//Creo classe in cui registro player e peso relativo dell'arco e da lì metto in ordine.
		Collections.sort(daInserire);
		return daInserire;
	}
	
	public Player getTopPlayer() {
		return this.topPlayer;
	}
	
	
	/**
	 * RISOLUZIONE RICORSIONE
	 */
	
	public List<Player> calcolaPercorso(int N){
		this.NGiocatori=N;
		this.best= new ArrayList<>();
		this.gradoTitolarita=0;
		
		List<Player> parziale= new ArrayList<Player>();
		
		cerca(parziale, 0);
		return best;
	}

	private void cerca(List<Player> parziale, double grado) {
		
		//Condizioni di terminazione
		if(parziale.size()==this.NGiocatori) {
			//Controllo se è la best
			if(grado>this.gradoTitolarita) {
				this.gradoTitolarita= grado;
				best= new ArrayList<>(parziale);
			}
			return;
		}
		
		//Condizioni di ricorsione
		for(Player p: this.vertici) {
			if(!parziale.contains(p)) {
				if(contiene(parziale, p)) {
					parziale.add(p);
					double peso=0.0;
					for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(p)) {
						peso=peso+this.grafo.getEdgeWeight(e);
					}
					for(DefaultWeightedEdge e: this.grafo.incomingEdgesOf(p)) {
						peso=peso-this.grafo.getEdgeWeight(e);
					}
					cerca(parziale, grado+peso);
					parziale.remove(parziale.size()-1);
				}
			}
		}
	}

	private boolean contiene(List<Player> parziale, Player p) {
		List<Player> inseribili= new ArrayList<>(this.vertici);
		for(Player gioc: parziale) {
			//tolgo tutti gli uscenti.
			for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(gioc)) {
				Player sconfitto= this.grafo.getEdgeTarget(e);
				if(inseribili.contains(sconfitto)) {
					inseribili.remove(sconfitto);
				}
			}
		}
		return inseribili.contains(p);
	}

	public double getGradoTitolarita() {
		return gradoTitolarita;
	}

	public void setGradoTitolarita(double gradoTitolarita) {
		this.gradoTitolarita = gradoTitolarita;
	}
	
	
}


