package it.polito.tdp.PremierLeague.model;

import java.util.List;

public class TopPlayer 
{
	private Player p;
	private List<Avversario> avversari;
	
	public TopPlayer(Player p, List<Avversario> avversari) 
	{
		super();
		this.p = p;
		this.avversari = avversari;
	}
	
	public Player getP() {
		return p;
	}
	public void setP(Player p) {
		this.p = p;
	}
	public List<Avversario> getAvversari() {
		return avversari;
	}
	public void setAvversari(List<Avversario> avversari) {
		this.avversari = avversari;
	}
	
	
	

}
