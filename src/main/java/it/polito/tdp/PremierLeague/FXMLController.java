/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.ComparatoreDelta;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	Graph<Player, DefaultWeightedEdge> grafo;
	boolean grafoCreato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String n = txtGoals.getText();
    	if(n.equals("")) 
    		txtResult.setText("Inserisci un numero");
    	else {
    	double media = 0.0;
    	try {
    		media = Double.parseDouble(n);
    		//creo grafo
    		this.model.creaGrafo(media);
    		this.grafo = this.model.getGrafo();
    		
    		//STAMPO STATISTICHE
    		txtResult.setText("GRAFO CREATO");
    		txtResult.appendText("\n# VERTICI : "+this.grafo.vertexSet().size());
    		txtResult.appendText("\n#ARCHI : "+this.grafo.edgeSet().size());
    		
    		this.grafoCreato = true;
    	}catch(NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.setText("Devi inserire un numero");
    	}
    	}
    }

    @FXML
    void doDreamTeam(ActionEvent event) {

    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	//if(this.grafo.vertexSet().size() == 0) {
    	//if(this.grafo.equals(null)) {
    	if(this.grafoCreato == false) {
    		txtResult.setText("DEVI PRIMA CREARE IL GRAFO");
    	}else {
    		//CERCO TOP PLAYER
    		Player best = null;
    		int gradoBest = 0;
    		for(Player p : this.grafo.vertexSet()) {
    			//NUMERO DI GIOCATORI BATTUTI DA p
    			int gradoP = grafo.outgoingEdgesOf(p).size();
    			//oppure grafo.DegreeOf(p);
    			if(gradoP > gradoBest)
    				best = p;    			
    		}
    		
    		//HO TROVATO IL BEST
    		txtResult.setText("IL GIOCATORE MIGLIORE E': \n"+best.toString()+"\nHA BATTUTO: ");
    		
    		//ORDINO LA LISTA DI AVVERSARI
    		List<DefaultWeightedEdge> archi = new ArrayList<>();
    		archi.addAll(grafo.outgoingEdgesOf(best));
    		List<Player> avversari = new ArrayList<>();
    		
    		//ORDINO GLI ARCHI IN BASE AL DELTA DECRESCENTE
    		Collections.sort(archi, new ComparatoreDelta(grafo));
    		//POPOLO LA LISTA DI AVVERSARI
    		for(DefaultWeightedEdge e : archi) {
    			avversari.add(this.grafo.getEdgeTarget(e));
    			txtResult.appendText("\n"+grafo.getEdgeTarget(e).toString()+ " -- "+grafo.getEdgeWeight(e));
    		}
    	
  
    		
    		
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
    
}

