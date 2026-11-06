package de.ixn075.smash.api.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.ixn075.smash.SmashPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDUtil {

    public static final Map<String, UUID> cached = new HashMap<>();

    public static @Nullable UUID fetch(String playerName) {
        if (cached.containsKey(playerName)) {
            return cached.get(playerName);
        }
        try {
            URL url = new URI("https://api.mojang.com/users/profiles/minecraft/" + playerName).toURL();
            URLConnection connection = url.openConnection();
            connection.setAllowUserInteraction(false);
            connection.setDoInput(false);
            connection.setDoOutput(true);
            connection.setDefaultUseCaches(false);
            connection.connect();
            UUID uuid;
            try (InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream())) {
                JsonElement element = JsonParser.parseReader(inputStreamReader);
                JsonObject object = element.getAsJsonObject();
                uuid = UUID.fromString(object.get("id").getAsString());
                SmashPlugin.getPlugin().getLogger().info("Caching uuid for player " + playerName + " (" + uuid + ")");
                cached.put(playerName, uuid);
            }
            return uuid;
        } catch (IOException | URISyntaxException exception) {
            //
        }
        return null;
    }
}
