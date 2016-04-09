package org.pkframework.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.Json;

public class JsonConvertUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonConvertUtils.class);

	/**
	 * @deprecated
	 */
	public static Map convertPeriodsToString(Map map) {
		try {
			List leagues = (List) map.get("leagues");

			for (int i=0;i<leagues.size();i++) {
				Map league = (Map) leagues.get(i);
				List events = (List) league.get("events");

				for (int j=0;j<events.size();j++) {
					Map event = (Map) events.get(j);

					List periods = (List) event.get("periods");
					event.put("periods", Json.mapper.writeValueAsString(periods));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return map;
	}

	public static Map extractPeriods(Map odds, List periodsList) {
		try {
			List leagues = (List) odds.get("leagues");

			for (int i=0;i<leagues.size();i++) {
				Map league = (Map) leagues.get(i);
				List events = (List) league.get("events");

				for (int j=0;j<events.size();j++) {
					Map event = (Map) events.get(j);

					List periods = (List) event.get("periods");

					for (int k=0;k<periods.size();k++) {
						Map period = (Map) periods.get(k);

						String periodJson = Json.mapper.writeValueAsString(period);

						period.put("leagueId", league.get("id"));
						period.put("eventId", event.get("id"));
						period.put("periodNo", period.get("number"));
						period.put("cutoffDt", period.get("cutoff"));
						period.put("period", periodJson);
						period.put("hasMoneyLine", period.containsKey("moneyline"));
						period.put("hasTeamTotal", period.containsKey("teamTotal"));
						period.put("hasOdds", period.containsKey("spreads") || period.containsKey("totals")
								 || period.containsKey("moneyline") || period.containsKey("teamTotal"));

						periodsList.add(period);
					}

					event.remove("periods");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return odds;
	}

	public static Map convertPeriodsToMap(List list) {
		Map leagues = new HashMap();

		try {
			for (int i=0;i<list.size();i++) {
				Map event = (Map) list.get(i);
				Integer leagueId = (Integer) event.get("leagueId");

				List league = (List) leagues.get(leagueId);

				if (league == null) {
					league = new ArrayList();
					leagues.put(leagueId, league);
				}

				String periods = (String) event.get("periods");
				event.put("periods", Json.mapper.readValue(periods, List.class));

				league.add(event);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return leagues;
	}

}
