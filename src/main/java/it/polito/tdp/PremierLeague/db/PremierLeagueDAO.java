package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
		public List<Player> getGiocatoriGolMedi(double media){
			String sql = "SELECT p.PlayerID, p.Name, AVG(a.Goals) as media "
					+ "FROM Players p, Actions a "
					+ "WHERE p.PlayerID= a.PlayerID "
					+ "GROUP BY p.PlayerID, p.Name "
					+ "HAVING AVG(a.Goals) > ?";
			
			List<Player> result = new ArrayList<Player>();
			Connection conn = DBConnect.getConnection();

			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setDouble(1, media);
				ResultSet res = st.executeQuery();
				while (res.next()) {

					Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
					
					result.add(player);
				}
				conn.close();
				return result;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public List<Adiacenza> getAllAdiacenze (double media){
			String sql = "SELECT p1.PlayerID as p1, p1.Name as n1, a1.TimePlayed as t1, p2.PlayerID as p2, p2.Name as n2, a2.TimePlayed as t2, ABS(SUM(a1.TimePlayed)-SUM(a2.TimePlayed)) as delta "
					+ "FROM Players p1, Players p2, Actions a1, Actions a2 "
					+ "WHERE a1.MatchID=a2.MatchID AND a1.TeamID<>a2.TeamID "
					+ "	AND a1.PlayerID=p1.PlayerID AND a2.PlayerID=p2.PlayerID "
					+ "	AND a1.Starts=1 AND a2.Starts=1 "
					+ "	AND p1.PlayerID>p2.PlayerID "
					+ "GROUP BY p1.PlayerID, p1.Name, a1.TimePlayed, p2.PlayerID, p2.Name, a2.TimePlayed ,ABS(a1.TimePlayed-a2.TimePlayed) "
					+ "HAVING AVG(a1.Goals)> ? AND AVG(a2.Goals)> ? ";
		
			List<Adiacenza> result = new ArrayList<>();
			
			Connection conn = DBConnect.getConnection();

			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setDouble(1, media);
				st.setDouble(2, media);
				ResultSet res = st.executeQuery();
				while (res.next()) {

					Player p1 = new Player(res.getInt("p1"), res.getString("n1"));
					int t1 = res.getInt("t1");
					Player p2 = new Player(res.getInt("p2"), res.getString("n2"));
					int t2 = res.getInt("t2");
					int delta = res.getInt("delta");
					
					if(delta>0) {
						result.add(new Adiacenza(p1,t1,p2,t2,delta));
					}
					
				}
				conn.close();
				return result;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		
		}
}
