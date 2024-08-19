package xyz.nowaha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SteamOpenID {

    /**
     * Generate a login URL for Steam OpenID
     *
     * @param realm       The website base URL with protocol, i.e. https://example.com
     * @param callbackURL The URL to redirect to after login, i.e. https://example.com/callback
     * @return The login URL
     */
    public static String getLoginURL(String realm, String callbackURL) {
        return "https://steamcommunity.com/openid/login?" +
                "openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&" +
                "openid.identity=http://specs.openid.net/auth/2.0/identifier_select&" +
                "openid.ns=http://specs.openid.net/auth/2.0&" +
                "openid.mode=checkid_setup&" +
                "openid.return_to=" + callbackURL + "&" +
                "openid.realm=" + realm;
    }

    /**
     * Get the user SteamID from the response parameters
     *
     * @param responseParameters The response parameters given to the callback URL
     * @return The SteamID, or -1 if not found
     */
    public static long getSteamID(String responseParameters) {
        String[] parameters = responseParameters
                .replaceAll(".+\\?", "")
                .split("&");

        for (String parameter : parameters) {
            String[] keyValue = parameter.split("=");
            if (keyValue[0].equals("openid.claimed_id")) {
                String[] split = keyValue[1].split("/");
                return Long.parseLong(split[split.length - 1]);
            }
        }
        return -1L;
    }

    /**
     * Validate the login with the Steam OpenID server
     *
     * @param responseParameters The response parameters given to the callback URL
     * @return Whether the login is valid
     * @throws IOException If an I/O error occurs
     */
    public static boolean validateLogin(String responseParameters) throws IOException {
        String parameters = responseParameters
                .replaceAll(".+\\?", "")
                .replace("openid.mode=id_res", "openid.mode=check_authentication");

        URL url = new URL("https://steamcommunity.com/openid/login?" + parameters);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("is_valid:true")) return true;
        }
        return false;
    }
}
