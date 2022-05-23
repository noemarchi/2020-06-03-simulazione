package it.polito.tdp.PremierLeague.model;

public class Avversario implements Comparable<Avversario>
{
	private Player p;
	private Integer delta;
	
	public Avversario(Player p, Integer delta) 
	{
		super();
		this.p = p;
		this.delta = delta;
	}
	
	public Player getP() {
		return p;
	}
	public void setP(Player p) {
		this.p = p;
	}
	public Integer getDelta() {
		return delta;
	}
	public void setDelta(Integer delta) {
		this.delta = delta;
	}

	@Override
	public int compareTo(Avversario other) 
	{
		return (-1) * this.delta.compareTo(other.getDelta());
	}

	@Override
	public String toString() {
		return String.format("%s --> %s", p, delta);
	}
	
	

}
