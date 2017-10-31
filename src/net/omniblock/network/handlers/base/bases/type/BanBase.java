package net.omniblock.network.handlers.base.bases.type;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.entity.Player;

import net.omniblock.network.handlers.base.sql.Database;
import net.omniblock.network.handlers.base.sql.make.MakeSQLQuery;
import net.omniblock.network.handlers.base.sql.make.MakeSQLUpdate;
import net.omniblock.network.handlers.base.sql.make.MakeSQLUpdate.TableOperation;
import net.omniblock.network.handlers.base.sql.type.TableType;
import net.omniblock.network.handlers.base.sql.util.Resolver;
import net.omniblock.network.handlers.base.sql.util.SQLResultSet;
import net.omniblock.network.handlers.base.sql.util.VariableUtils;

public class BanBase {

	public static void insertBanRegistry(String SQL) {

		Statement stm = null;

		try {

			stm = Database.conn.createStatement();
			stm.executeUpdate(SQL);

		} catch (SQLException e) {
		} finally {

			if (stm != null)

				try {
					stm.close();
				} catch (SQLException e) {
				}

		}

		return;

	}

	public static List<String[]> getBanRegistry(String player) {

		final String key = Resolver.getNetworkIDByName(player);

		List<String[]> registries = new ArrayList<String[]>();
		MakeSQLQuery msq = new MakeSQLQuery(TableType.BAN_REGISTRY)
				.select("p_mod", "ban_hash", "ban_reason", "ban_time_from", "ban_time_to").where("p_banned", key);

		try {
			SQLResultSet sqr = msq.execute();
			while (sqr.next()) {
				String[] registry = new String[5];

				registry[0] = sqr.get("ban_hash");
				registry[1] = sqr.get("p_mod");
				registry[2] = sqr.get("ban_reason");
				registry[3] = sqr.get("ban_time_from");
				registry[4] = sqr.get("ban_time_to");

				registries.add(registry);
			}

			return registries;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return registries;

	}

	public static boolean isBanned(String player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.BAN_DATA).select("banned").where("p_id",
				Resolver.getNetworkIDByName(player));

		try {
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				return sqr.get("banned");
			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return BooleanUtils.toBoolean(VariableUtils.BAN_STATUS);
	}

	public static void setBanStatus(String player, boolean status) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.BAN_DATA, TableOperation.UPDATE);

		msu.rowOperation("banned", BooleanUtils.toInteger(status));
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return;

	}

	public static boolean isBanned(Player player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.BAN_DATA).select("banned").where("p_id",
				Resolver.getNetworkID(player));

		try {
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				return sqr.get("banned");
			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return BooleanUtils.toBoolean(VariableUtils.BAN_STATUS);
	}

	public static void setBanStatus(Player player, boolean status) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.BAN_DATA, TableOperation.UPDATE);

		msu.rowOperation("banned", status == false ? 0 : 1);
		msu.whereOperation("p_id", Resolver.getNetworkID(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return;

	}

}
