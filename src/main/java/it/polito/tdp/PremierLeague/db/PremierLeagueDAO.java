package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers()
	{
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) 
			{
				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions()
	{
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) 
			{
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<Player> getVertici(double x, Map<Integer, Player> giocatoriAll)
	{
		String sql = "SELECT PlayerID "
				+ "FROM Actions "
				+ "GROUP BY PlayerID "
				+ "HAVING AVG(Goals) > ?";
		
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, x);
			ResultSet res = st.executeQuery();
			
			while (res.next()) 
			{
				Integer id = res.getInt("PlayerID");
				
				Player p = giocatoriAll.get(id);
				result.add(p);
			}
			conn.close();
			return result;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<Adiacenza> getAdiacenze(List<Player> vertici, Map<Integer, Player> giocatoriAll)
	{
		String sql = "SELECT a1.PlayerID AS p1, a2.PlayerID AS p2, (SUM(a1.TimePlayed) - SUM(a2.TimePlayed)) AS peso "
				+ "FROM Actions a1, Actions a2 "
				+ "WHERE a1.MatchID = a2.MatchID "
				+ "AND a1.TeamID != a2.TeamID "
				+ "AND a1.Starts = 1 "
				+ "AND a2.Starts = 1 "
				+ "AND a1.PlayerID > a2.PlayerID "
				+ "GROUP BY a1.PlayerID, a2.PlayerID";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) 
			{
				int id1 = res.getInt("p1");
				int id2 = res.getInt("p2");
				double peso = res.getDouble("peso");
				Player p1 = giocatoriAll.get(id1);
				Player p2 = giocatoriAll.get(id2);
				
				if(vertici.contains(p1) && vertici.contains(p2))
				{
					Adiacenza a = new Adiacenza(p1, p2, peso);
					
					result.add(a);
				}
			}
			
			conn.close();
			return result;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

}
