package it.polito.tdp.PremierLeague.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
/*Alla pressione del bottone “Crea Grafo”, si crei un grafo semplice, pesato e orientato i cui nodi sono i giocatori che hanno 
 * segnato almeno x goal in media a partita durante la stagione (provare con valori < 1).*/
	Graph<Player, DefaultWeightedEdge> grafo;
	PremierLeagueDAO dao;
	
	public Model() {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo(double mediaGol) {
		
		List<Player> giocatori = this.dao.getGiocatoriGolMedi(mediaGol);
		
		//aggiungo i vertici al grafo
		Graphs.addAllVertices(this.grafo, giocatori);
		
		//prendo le coppie di adiacenze dal dao
		List<Adiacenza> adiacenze = this.dao.getAllAdiacenze(mediaGol);
		
		for(Adiacenza a : adiacenze) {
			if(grafo.vertexSet().contains(a.getP1())&& grafo.vertexSet().contains(a.getP2())) {
				
				if(a.minutiP1 > a.minutiP2) { //decido la direzione dell'arco
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getDelta());
					//grafo.addEdge(a.getP1(), a.getP2());
					//grafo.setEdgeWeight(grafo.getEdge(a.getP1(),a.getP2()), a.getDelta());
				}else {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), a.getDelta());
					//grafo.addEdge(a.getP2(), a.getP1());
					//grafo.setEdgeWeight(grafo.getEdge(a.getP2(),a.getP1()), a.getDelta());
				}
			}
		}
		
		//PROVA STAMPA
		//System.out.println("CREATO GRAFO\n#VERTICI : "+this.grafo.vertexSet().size()+"\n#ARCHI : "+this.grafo.edgeSet().size());
	}

	public Graph<Player, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
}
