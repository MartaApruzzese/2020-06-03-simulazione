package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
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
	
	public List<Player> getVertici(Map<Integer, Player> idMap, double media){
		String sql="SELECT DISTINCT a.PlayerID AS id, AVG (a.Goals) AS media "
				+ "FROM players p, actions a "
				+ "WHERE p.PlayerID=a.PlayerID "
				+ "GROUP BY a.PlayerID ";
		List<Player> result= new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				double me= res.getDouble("media");
				
				if(me>media) {
					result.add(idMap.get(res.getInt("id")));
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	} 
	
	public double getPeso(Player p1, Player p2) {
		/*String sql="SELECT a1.TimePlayed AS tempo1, a2.TimePlayed AS tempo2 "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.TeamID<> a2.TeamID AND a1.MatchID= a2.MatchID "
				+ "AND a1.`Starts`=1 AND a2.`Starts`=1 "
				+ "AND a1.PlayerID=? AND a2.PlayerID=? ";*/
		String sql="SELECT a1.PlayerID, a2.PlayerID,SUM(a1.TimePlayed) AS tempo1, SUM(a2.TimePlayed) AS tempo2 "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.TeamID<> a2.TeamID AND a1.MatchID= a2.MatchID "
				+ "AND a1.`Starts`=1 AND a2.`Starts`=1 "
				+ "AND a1.PlayerID=? AND a2.PlayerID=? "
				+ "GROUP BY a1.PlayerID, a2.PlayerID ";
		
		double peso=0.0;
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, p1.getPlayerID());
			st.setInt(2, p2.getPlayerID());
			ResultSet res = st.executeQuery();
			while (res.next()) {

				double tempo1= res.getDouble("tempo1");
				double tempo2= res.getDouble("tempo2");
				peso=tempo1-tempo2;
			}
			conn.close();
			return peso;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 666;
		}
		
	}
}
