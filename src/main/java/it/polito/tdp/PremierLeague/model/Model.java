package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model 
{
	private Graph<Player, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer, Player> giocatoriAll;
	private List<Player> vertici;
	private List<Player> dreamTeam;
	private int gradoDream;
	
	public Model()
	{
		this.dao = new PremierLeagueDAO();
		
		List<Player> listaGiocatori = dao.listAllPlayers();
		giocatoriAll = new HashMap<Integer, Player>();
		
		for(Player p: listaGiocatori)
		{
			this.giocatoriAll.put(p.getPlayerID(), p);
		}
		
	}
	
	public void creaGrafo(double x)
	{
		// creo il grafo
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		vertici = dao.getVertici(x, giocatoriAll);
		Graphs.addAllVertices(grafo, vertici);
		
		// aggiungo gli archi
		for(Adiacenza adj : dao.getAdiacenze(vertici, giocatoriAll)) 
		{
			if(grafo.containsVertex(adj.getP1()) && grafo.containsVertex(adj.getP2())) 
			{
				if(adj.getPeso() < 0) 
				{
					//arco da p2 a p1
					Graphs.addEdgeWithVertices(grafo, adj.getP2(), adj.getP1(), ((double) -1)*adj.getPeso());
				} 
				else if(adj.getPeso() > 0)
				{
					//arco da p1 a p2
					Graphs.addEdgeWithVertices(grafo, adj.getP1(), adj.getP2(), adj.getPeso());
				}
			}
		}
	}
	
	public int nVertici()
	{
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi()
	{
		return this.grafo.edgeSet().size();
	}

	public TopPlayer getTopPlayer()
	{
		if(grafo == null)
		{
			return null;
		}
		
		Player top = null;
		Integer num = Integer.MIN_VALUE;
		
		for(Player p: grafo.vertexSet())
		{
			if(grafo.outDegreeOf(p) > num)
			{
				top = p;
				num = grafo.outDegreeOf(p);
			}
		}
		
		List<Avversario> avversari = new ArrayList<Avversario>();
		
		for(DefaultWeightedEdge arco: grafo.outgoingEdgesOf(top))
		{
			Avversario x = new Avversario(grafo.getEdgeTarget(arco), (int)grafo.getEdgeWeight(arco));
			avversari.add(x);
		}
		
		Collections.sort(avversari);
		
		TopPlayer best = new TopPlayer(top, avversari);
		return best;
	}

	public List<Player> getDreamTeam(int k)
	{
		if(grafo == null)
		{
			return null;
		}
		
		this.dreamTeam = new ArrayList<Player>();
		
		List<Player> parziale = new ArrayList<Player>();
		
		this.gradoDream = 0;
		
		cerca(parziale, k, new ArrayList<Player>(this.grafo.vertexSet()));
		
		return dreamTeam;
	}

	private void cerca(List<Player> parziale, int k, List<Player> giocatori) 
	{
		// CONDIZIONE DI TERMINAZIONE
		if(parziale.size() == k)
		{
			// è la squadra migliore?
			int grado = getGrado(parziale);
			if(grado > this.gradoDream)
			{
				this.dreamTeam = new ArrayList<Player>(parziale);
				this.gradoDream = grado;
			}
			
			return;
		}
		
		// RICORSIONE
		for(Player p: giocatori)
		{
			if(!parziale.contains(p))
			{
				parziale.add(p);
				
				//i "battuti" di p non possono più essere considerati
				
				// aggiungo tutti i giocatori
				List<Player> rimanenti = new ArrayList<>(giocatori);
				// tolgo tutti i successori del giocatore nel grafo --> i battuti
				rimanenti.removeAll(Graphs.successorListOf(grafo, p));
				// avvio un nuovo passo ricorsivo
				cerca(parziale, k, rimanenti);
				// backtracking
				parziale.remove(p);
			}
		}
		
	}

	private int getGrado(List<Player> team) 
	{
		int grado = 0;
		int in;
		int out;
		
		for(Player p: team)
		{
			in = 0;
			out = 0;
			
			for(DefaultWeightedEdge e: grafo.outgoingEdgesOf(p))
			{
				out = out + (int)grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e: grafo.incomingEdgesOf(p))
			{
				in = in + (int)grafo.getEdgeWeight(e);
			}
			
			grado = grado + (out - in);
		}
		
		return grado;
	}

	public int getGradoDreamTeam()
	{
		return this.gradoDream;
	}
}
