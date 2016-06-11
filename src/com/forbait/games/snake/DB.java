package com.forbait.games.snake;

import com.forbait.games.snake.matchserver.MatchInfo;
import io.orchestrate.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class DB {

	public static Client client = OrchestrateClient.builder("ee6a1d14-b41c-42f1-b87b-d393aee26e17").build();
	
	public static void main(String[] args)
	{
		String ip = "";
		try {
			URL ipChecker = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(ipChecker.openStream()));
			ip = in.readLine();
			in.close();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		MatchInfo info = new MatchInfo(ip, Program.HOST_PORT, "New host", System.currentTimeMillis(), 4, 40, 2);
		
		final KvMetadata kvMetadata = DB.client.kv("hosts", info.name)
				.put(info)
				.get();
		
		System.out.println(kvMetadata.getRef());

		long time = System.currentTimeMillis();

		SearchResults<MatchInfo> results = client.searchCollection("hosts")
				.limit(20)
				.get(MatchInfo.class, "value.time:[" + (time - 180000) + " TO " + (time + 180000) + "]")
				.get();

		if (results.getCount() == 0) {
			System.out.println("No data.");
		}

		for (Result<MatchInfo> result : results) {
			System.out.println(result.getKvObject().getValue());
		}

//		KvObject<MatchInfo> object = client.kv("hosts", info.name)
//				.get(MatchInfo.class)
//				.get();
//
//		if (object == null) {
//			System.out.println("NULL");
//		} else {
//			MatchInfo data = object.getValue();
//			System.out.println(data);
//		}

//		client.kv("hosts", info.name)
//				.get(MatchInfo.class)
//				.on(new ResponseAdapter<KvObject<MatchInfo>>() {
//					@Override
//					public void onFailure(final Throwable error) {
//						System.out.println("FAIL");
//					}
//
//					@Override
//					public void onSuccess(final KvObject<MatchInfo> object) {
//						if (object == null) {
//							System.out.println("NULL");
//							return;
//						}
//
//						MatchInfo data = object.getValue();
//						System.out.println(data);
//					}
//				});
	}
	
}
