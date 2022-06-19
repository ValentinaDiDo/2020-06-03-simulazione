package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
	Map<Integer, Player> mGiocatori;
	
	List<Player> best;
	
	public Model() {
		dao = new PremierLeagueDAO();
		this.mGiocatori = new TreeMap<>();
		
	}
	
	public void creaGrafo(double media) {
		//DEFINISCO QUA IL GRAFO COSì OGNI VOLTA CHE CHIAMO IL METODO SI PULISCE
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		List<Player> giocatori = this.dao.getGiocatoriGolMedi(media);
		//POPOLO LA MAPPA
		for(Player p : giocatori) {
			this.mGiocatori.put(p.getPlayerID(), p);
		}
		
		
		Graphs.addAllVertices(this.grafo, giocatori);
		
		System.out.println("#VERTICI : "+this.grafo.vertexSet().size());
		
		List<Adiacenza> adiacenze = this.dao.getAllAdiacenzeCorretto(mGiocatori);
		
		for(Adiacenza a : adiacenze) {
			if(a.minutiP1 > a.minutiP2)
				Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getDelta());
			else
				Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), a.getDelta());
		}
		
		System.out.println("#ARCHI : "+this.grafo.edgeSet().size());
	}
	
	
	public Graph<Player, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public Player CalcolaTopPlayer() {
		Player top = null;
		
		int numBest = 0;
		
		for(Player p : this.grafo.vertexSet()) {
			int battuti = this.grafo.outgoingEdgesOf(p).size();
			
			if(battuti > numBest) {
				numBest = battuti;
				top = p;
			}		
		}
		return top;
	}
	
	public List<Avversario> giocatoriBattuti(){
		List<Avversario> battuti = new ArrayList<>();
		
		Player best = CalcolaTopPlayer();
		
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(best)) {
			int peso = (int) grafo.getEdgeWeight(e);
			Player p = grafo.getEdgeTarget(e);
			
			Avversario a = new Avversario(p, peso);
			battuti.add(a);
			
		}
		Collections.sort(battuti);
		return battuti;
	}
	
	public void calcolaDreamTeam(int k) {
		this.best = new ArrayList<>();
		List<Player> parziale = new ArrayList<>();
		
		//CONSIDERO TUTTI I VERTICI , POI MANO MANO MODIFICHERO' LA LISTA DI QUELLI DA PROVARE
		List<Player> daTestare = new ArrayList<>(this.grafo.vertexSet());
		
		ricorsiva(parziale, daTestare, k);
	}
	
	public void ricorsiva(List<Player> parziale, List<Player> daTestare, int k) {
		if(parziale.size() == k) {
			//TROVATO TEAM DELLA DIMENSIONE GIUSTA, CONTROLLO CHE SIA MIGLIORE
			//SE PARZIALE HA UN GRADO COMPLESSIVO MAGGIORE RISPETTO A QUELLO DI BEST, DIVENTA BEST
			if(calcolaGradoGiocatori(parziale) > calcolaGradoGiocatori(best)) {
				best = new ArrayList<>(parziale);
				//return; QUI NON HA SENSO PERCHE' SE LO FACCIO SOLO QUANDO TROVO UNA BEST, LA DIMENSIONE POI SUPERA IL LIMITE, QUINDI LO FACCIO OGNI VOTLA CHE PARZIALE.SIZE = K
			}
			return;
		}
		/*QUESTE RIGHE NON HANNO SENSO PERCHE' FACENDO IL RETURN QUANDO PARZIALE.SIZE = K, NON SUPERO MAI LA DIMENSIONE
		 * if(parziale.size()>k) {
		//	parziale.remove(parziale.size()-1); NON SERVE A UN CAZZO STA COSA 
			return;
		}*/
		
		//AGGIUNGO I VARI GIOCATORI + BACKTRACKING
		for(Player p : daTestare) {
			parziale.add(p);
		
			List<Player> nuovaDaTestare = getNuovaDaTestare(daTestare, p); //HO RIMOSSO TUTTI I GIOCATORI BATTUTI DA PLAYER
			ricorsiva(parziale, nuovaDaTestare, k);
			parziale.remove(p); //backtracking 
			//PERO' UNA VOLTA RAGGIUNTI I PRIMI 3 ELEMENTI LA DIMENSIONE RIMANE COSTANTE CON QUEI TRE, NON RESCO A RIDURRE LA DIMENSIONE
			//OK ORA SONO RIUSCITA
		}
	}
	
	
	
	public int calcolaGradoGiocatori(List<Player> parziale) {
		int pesoTot = 0;
		
		for(Player p : parziale) {
			//PER OGNI GIOCATORE CALCOLO IL GRADO
			
			//CALCOLO PESO ARCHI USCENTI
			int uscenti = 0;
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
				uscenti += this.grafo.getEdgeWeight(e);
			}
			
			//CALCOLO PESO ARCHI ENTRANTI
			int entranti = 0;
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
				entranti += this.grafo.getEdgeWeight(e);
			}
			
			int gradoP = uscenti - entranti;
			pesoTot += gradoP;
		}
		
		return pesoTot;
	}
	
	public List<Player> getNuovaDaTestare(List<Player> daTestare, Player p){
		List<Player> nuova = new ArrayList<>();
		nuova.addAll(daTestare); //AGGIUNGO IN QUESTO MODO PERCHE' SE METTESSI DATESTARE NEL COSTRUTTORE DELLA LISTA NUOVA, 
		//CREEREI UNA LISTA LINKATA A DATESTARE E SUCCEDEREBBE UN CASINO, IN QUESTO MODO HO UNA COPIA ED E' PIU' SAFE
		nuova.remove(p);
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
			Player daRimuovere = this.grafo.getEdgeTarget(e);
			nuova.remove(daRimuovere);
		}
		return nuova;
	}

	public List<Player> getBest() {
		return best;
	}
	
	
/*	public void creaGrafo(double mediaGol) {
		
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
	}*/

	
}
