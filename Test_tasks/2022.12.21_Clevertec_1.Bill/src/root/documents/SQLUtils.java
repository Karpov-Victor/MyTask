package root.documents;


import root.Goods;

import java.sql.*;


public class SQLUtils {

    public Connection getConnection(String connectionString) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/Bill", "postgres", "1111")) {
            if (conn != null) {
                return conn;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Goods getGoodbyID(int goodsID, Connection conn) {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM goods where id=" + goodsID);
            while (resultSet.next()) {
                String goodsName = resultSet.getString("name");
                double price = resultSet.getInt("price");
                Goods goods = new Goods(goodsID, goodsName, price);
                return goods;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
