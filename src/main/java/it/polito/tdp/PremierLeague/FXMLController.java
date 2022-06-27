/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.TopPlayers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

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
    	txtResult.clear();
    	double media;
    	try {
    		media= Double.parseDouble(txtGoals.getText());
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un valore numerico.\n");
    		return;
    	}
    	
    	this.model.creaGrafo(media);
    	txtResult.setText("Grafo Creato");
    	txtResult.appendText("\nNumero Vertici: "+this.model.getNVertici());
    	txtResult.appendText("\nNumbero Archi: "+this.model.getNArchi());
    	
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	txtResult.clear();
    	int nGiocatori;
    	try {
    		nGiocatori= Integer.parseInt(txtK.getText());
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un valore numerico.\n");
    		return;
    	}
    	
    	List<Player> dreamTeam= new ArrayList<>(this.model.calcolaPercorso(nGiocatori));
    	double grado=this.model.getGradoTitolarita();
    	txtResult.setText("Il grado di titolarità del Dream Team è : "+grado);
    	for(Player p: dreamTeam) {
    		txtResult.appendText("\n"+p.toString());
    	}
    }

    @FXML
    void doTopPlayer(ActionEvent event) {

    	txtResult.clear();
    	double media;
    	try {
    		media= Double.parseDouble(txtGoals.getText());
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un valore numerico.\n");
    		return;
    	}
    	
    	this.model.creaGrafo(media);
    	
    	List<TopPlayers> top= this.model.getTopPlayers();
    	txtResult.clear();
    	txtResult.setText("TOP PLAYER: "+this.model.getTopPlayer()+"\nAVVERSARI BATTUTI: \n");
    	for(TopPlayers p: top) {
    		txtResult.appendText("\n"+p.getPlayer().toString()+" | "+p.getPesoArco());
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
