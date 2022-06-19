package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class ComparatoreDelta implements Comparator<DefaultWeightedEdge>{
	
	Graph<Player, DefaultWeightedEdge> grafo;
	
	public ComparatoreDelta(Graph<Player, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	
	@Override
	public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
		
		return (int)(grafo.getEdgeWeight(o2)-grafo.getEdgeWeight(o1));
	}

	
	
	
}
