package net.specialattack.beacon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Motd {
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private static JsonObject description;
    private static JsonObject players;
    private static String favicon;

    public static void init() {
        description = new JsonObject();

        String motd = "A Minecraft Beacon";

        File motdFile = new File("motd.txt");
        if (motdFile.exists() && motdFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(motdFile))) {
                motd = reader.readLine();
                System.out.println("MOTD set to " + motd);

                Pattern pattern = Pattern.compile("([^\\\\]|^)&([0-9A-FK-ORa-fk-or])");
                Matcher matcher = pattern.matcher(motd);

                while (matcher.find()) {
                    motd = matcher.replaceAll("$1\u00A7$2");

                    matcher = pattern.matcher(motd);
                }

                motd = motd.replaceAll("\\\\n", "\n");
            } catch (IOException e) {
                System.out.println("Failed reading MOTD file");
            }
        }

        description.addProperty("text", motd);

        players = new JsonObject();
        players.addProperty("max", 0);
        players.addProperty("online", 0);

        URL faviconURL = Motd.class.getResource("server-icon.png");
        if (faviconURL != null) {
            try {
                BufferedImage img = ImageIO.read(faviconURL);
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                ImageIO.write(img, "png", Base64.getEncoder().wrap(out));
                favicon = "data:image/png;base64," + out.toString(StandardCharsets.ISO_8859_1.name());

                System.out.println("Favicon loaded!");
            } catch (IOException e) {
                System.out.println("Failed reading favicon: " + e.getMessage());
            }
        } else {
            System.out.println("Missing favicon, it will not be added!");
        }
    }

    public static String getMotd(int protocolVersion) {
        JsonObject element = new JsonObject();
        element.add("description", description);
        element.add("players", players);

        {
            JsonObject version = new JsonObject();
            version.addProperty("name", "Beacon");
            version.addProperty("protocol", protocolVersion);
            element.add("version", version);
        }

        if (favicon != null) {
            element.addProperty("favicon", favicon);
        }

        return gson.toJson(element);
    }

    public static String getDescription() {
        return gson.toJson(description);
    }
}
