package com.forbait.games.snake;

import com.forbait.games.snake.server.GameInfo;
import com.forbait.games.util.Utils;
import io.orchestrate.client.*;

public class DB {

	private static final Client client = OrchestrateClient.builder("ee6a1d14-b41c-42f1-b87b-d393aee26e17").build();
	public static final long TIMEOUT = 30000;

	public static void postGame(GameInfo info)
	{
		final KvMetadata kvMetadata = DB.client.kv("hosts", info.name)
				.put(info)
				.get();

		System.out.println("DB.newG: Game created, ref: " + kvMetadata.getRef());
	}

	public static void getGames(ResponseListener<SearchResults<GameInfo>> listener)
	{
		System.out.println("DB.blockingGG: Requesting games list to listener");
		long time = System.currentTimeMillis();

		client.searchCollection("hosts")
				.limit(20)
				.get(GameInfo.class, "value.time:[" + (time - TIMEOUT) + " TO " + (time + TIMEOUT) + "]")
				.on(listener);
	}

	public static SearchResults<GameInfo> blockingGetGames()
	{
		System.out.println("DB.blockingGG: Requesting games list");
		long time = System.currentTimeMillis();

		SearchResults<GameInfo> results = client.searchCollection("hosts")
				.limit(20)
				.get(GameInfo.class, "value.time:[" + (time - TIMEOUT) + " TO " + (time + TIMEOUT) + "]")
				.get();

		if (results.getCount() == 0) {
			System.out.println("DB.getG: No games online");
		}

		return results;
	}

	public static void closePost(GameInfo info)
	{
		System.out.println("DB.closeP: Closing post");
		DB.client.kv("hosts", info.name)
				.delete()
				.get();
	}

	public static void main(String[] args)
	{
		GameInfo info = new GameInfo(Utils.getAddress(), 8000, "Jogo daqui 6", 30, 8, 0, 4);

		DB.postGame(info);
		SearchResults<GameInfo> games = DB.blockingGetGames();

		for (Result<GameInfo> result : games.getResults()) {
            System.out.println(result.getKvObject().getValue());
        }

		DB.closePost(info);
		SearchResults<GameInfo> empty = DB.blockingGetGames();

		final KvMetadata kvMetadata = DB.client.kv("hosts", info.name)
				.get(GameInfo.class)
				.get();

		System.out.println(kvMetadata);
	}
	
}
