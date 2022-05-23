/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Avversario;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.TopPlayer;
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
    void doCreaGrafo(ActionEvent event) 
    {
    	String gol = this.txtGoals.getText();
    	
    	if(gol == null || gol == "")
    	{
    		this.txtResult.setText("Inserisci un numero di gol!");
    		return;
    	}
    	
    	
    	double x;
    	
    	try
    	{
    		x = Double.parseDouble(gol);
    	}
    	catch (NumberFormatException e)
    	{
    		e.printStackTrace();
    		this.txtResult.setText("Inserisci un numero di gol in formato numerico!");
    		return;
    	}
    	
    	model.creaGrafo(x);
    	
    	this.txtResult.setText("Grafo creato!\n");
    	this.txtResult.appendText(String.format("# vertici: %d\n", model.nVertici()));
    	this.txtResult.appendText(String.format("# archi: %d\n", model.nArchi()));
    }

    @FXML
    void doDreamTeam(ActionEvent event) 
    {
    	String s = this.txtK.getText();
    	
    	if(s == null || s == "")
    	{
    		this.txtResult.setText("Inserisci un numero di giocatori del dream team!");
    		return;
    	}
    	
    	Integer k;
    	
    	try
    	{
    		k = Integer.parseInt(s);
    	}
    	catch(NumberFormatException e)
    	{
    		e.printStackTrace();
    		this.txtResult.setText("Inserisci un numero di giocatori del dream team! (numero intero)");
    		return;
    	}
    	
    	List<Player> dream = model.getDreamTeam(k);
    	
    	if(dream == null)
    	{
    		this.txtResult.setText("Crea prima il grafo!!!");
    		return;
    	}
    	
    	this.txtResult.setText("DREAM-TEAM: \n");
    	for(Player p: dream)
    	{
    		this.txtResult.appendText(p.toString() + "\n");
    	}
    	
    	this.txtResult.appendText(String.format("GRADO DI TITOLARITA': \n%d", model.getGradoDreamTeam()));
    }

    @FXML
    void doTopPlayer(ActionEvent event) 
    {
    	TopPlayer top = model.getTopPlayer();
    	
    	if(top == null)
    	{
    		this.txtResult.setText("Crea prima il grafo!");
    		return;
    	}
    	
    	this.txtResult.setText(String.format("TOP PLAYER: %s \n\n", top.getP()));
    	this.txtResult.appendText("AVVERSARI BATTUTI: \n");
    	
    	for(Avversario a : top.getAvversari())
    	{
    		this.txtResult.appendText(a.toString() + "\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() 
    {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) 
    {
    	this.model = model;
    }
}
