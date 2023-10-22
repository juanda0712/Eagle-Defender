
package com.mygdx.utils;

import com.mygdx.models.SongInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;

public class SpotifyAuthenticator {
    private static String CLIENT_ID = "9cc33358a84845e288b47a8f59e6cd2f";
    private static String CLIENT_SECRET = "56269a922c9b487d8515563c89eccaf5";
    private static String REDIRECT_URI = "http://localhost:8888/callback";
    private String accessToken;

    public SpotifyAuthenticator() {
        openAuthorizationPage();
        String authorizationCode = getAuthorizationCodeFromUser();
        accessToken = requestAccessToken(authorizationCode);
    }

    public SongInfo getSongInfo(String songName) {
        SongInfo songInfo = new SongInfo();
        try {
            String access_token = getAccessToken();
            String trackUri = searchSong(songName);
            String trackId = extractTrackId(trackUri);
            JSONObject analizeResultJson = getAudioAnalysis(trackId, access_token);

            double trackTempo = analizeResultJson.getJSONObject("track").getDouble("tempo");
            double trackDuration = analizeResultJson.getJSONObject("track").getDouble("duration");

            songInfo.setTempo(trackTempo);
            songInfo.setDuration(trackDuration);

            return songInfo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getAccessToken() throws IOException {
        if (accessToken != null) {
            System.out.println("Ya tenia el token desde el inicio");
            return accessToken;
        }

        openAuthorizationPage();
        String authorizationCode = getAuthorizationCodeFromUser();
        accessToken = requestAccessToken(authorizationCode);
        return accessToken;
    }

    private void openAuthorizationPage() {
        try {
            String scope = "user-read-private user-read-email user-modify-playback-state user-read-playback-state user-read-currently-playing user-read-playback-position";

            CLIENT_ID = URLEncoder.encode(CLIENT_ID, "UTF-8");
            REDIRECT_URI = URLEncoder.encode(REDIRECT_URI, "UTF-8");
            scope = URLEncoder.encode(scope, "UTF-8");

            String authorizationUrl = "https://accounts.spotify.com/authorize" +
                    "?client_id=" + CLIENT_ID +
                    "&response_type=code" +
                    "&redirect_uri=" + REDIRECT_URI +
                    "&scope=" + scope +
                    "&show_dialog=true";

            openBrowser(authorizationUrl);
        } catch (IOException | URISyntaxException e) {
            System.out.println("No se pudo abrir el navegador");
            e.printStackTrace();
        }
    }

    private void openBrowser(String url) throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        }
    }

    private String getAuthorizationCodeFromUser() {
        try {
            int port = 8888;
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Esperando el código de autorización en el puerto " + port);

            Socket clientSocket = serverSocket.accept();
            String authorizationCode = extractAuthorizationCode(clientSocket);

            serverSocket.close();

            return authorizationCode;
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor web: " + e.getMessage());
            return null;
        }
    }

    private String extractAuthorizationCode(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            StringBuilder request = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
                request.append(inputLine).append("\r\n");
            }

            return extractCodeFromRequest(request.toString());
        }
    }

    private String extractCodeFromRequest(String request) {
        try {
            String urlLine = request.split("\n")[0].split(" ")[1];
            URI uri = new URI(urlLine);
            String query = uri.getQuery();
            String[] params = query.split("&");

            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals("code")) {
                    return keyValue[1];
                }
            }
        } catch (URISyntaxException e) {
            System.err.println("Error al analizar la URL: " + e.getMessage());
        }
        return null;
    }

    private String requestAccessToken(String authorizationCode) {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());

        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection connection = openConnection(url, "POST", authHeader);
            connection.setDoOutput(true);

            String requestBody = "grant_type=authorization_code" +
                    "&code=" + authorizationCode +
                    "&redirect_uri=" + REDIRECT_URI;

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(requestBody);
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return parseAccessTokenFromResponse(connection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpURLConnection openConnection(URL url, String method, String authHeader) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", authHeader);
        return connection;
    }

    private String parseAccessTokenFromResponse(HttpURLConnection connection) throws IOException {
        try (InputStream inputStream = connection.getInputStream();
             Scanner scanner = new Scanner(inputStream)) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getString("access_token");
        }
    }

    private String searchSong(String songName) {
        try {
            String access_token = getAccessToken();
            String apiUrl = "https://api.spotify.com/v1/search?q=" + songName + "&type=track";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = openConnection(url, "GET", "Bearer " + access_token);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return extractTrackUriFromSearchResponse(connection);
            } else {
                System.out.println("Error en la solicitud: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String extractTrackUriFromSearchResponse(HttpURLConnection connection) throws IOException {
        try (InputStream inputStream = connection.getInputStream();
             Scanner scanner = new Scanner(inputStream)) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            JSONObject searchResultJson = new JSONObject(response.toString());
            JSONArray tracks = searchResultJson.getJSONObject("tracks").getJSONArray("items");
            return tracks.getJSONObject(0).getString("uri");
        }
    }

    private JSONObject getAudioAnalysis(String trackId, String access_token) {
        try {
            String apiUrl = "https://api.spotify.com/v1/audio-analysis/" + trackId;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = openConnection(url, "GET", "Bearer " + access_token);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return parseResponseAsJson(connection);
            } else {
                System.out.println("Error en la solicitud: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject parseResponseAsJson(HttpURLConnection connection) throws IOException {
        try (InputStream inputStream = connection.getInputStream();
             Scanner scanner = new Scanner(inputStream)) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            return new JSONObject(response.toString());
        }
    }

    private String extractTrackId(String trackUri) {
        return trackUri.split(":")[2];
    }

    public String playSong(String songName) {
        try {
            String access_token = getAccessToken();
            String trackUri = searchSong(songName);

            if (trackUri != null) {
                String deviceId = getActiveDeviceId(access_token);

                if (deviceId != null) {
                    return startPlayback(access_token, trackUri, deviceId);
                } else {
                    return "No se encontraron dispositivos activos.";
                }
            } else {
                return "Canción no encontrada: " + songName;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getActiveDeviceId(String access_token) {
        try {
            String devicesApiUrl = "https://api.spotify.com/v1/me/player/devices";
            URL devicesUrl = new URL(devicesApiUrl);
            HttpURLConnection devicesConnection = openConnection(devicesUrl, "GET", "Bearer " + access_token);

            int devicesResponseCode = devicesConnection.getResponseCode();
            if (devicesResponseCode == 200) {
                return extractDeviceId(devicesConnection);
            } else {
                System.out.println("Error al obtener la lista de dispositivos: " + devicesResponseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String extractDeviceId(HttpURLConnection devicesConnection) throws IOException {
        try (InputStream inputStream = devicesConnection.getInputStream();
             Scanner scanner = new Scanner(inputStream)) {
            StringBuilder devicesResponse = new StringBuilder();
            while (scanner.hasNextLine()) {
                devicesResponse.append(scanner.nextLine());
            }

            JSONObject devicesJson = new JSONObject(devicesResponse.toString());
            JSONArray devices = devicesJson.getJSONArray("devices");

            if (devices.length() > 0) {
                return devices.getJSONObject(0).getString("id");
            } else {
                System.out.println("No se encontraron dispositivos activos.");
            }
        }
        return null;
    }

    private String startPlayback(String access_token, String trackUri, String deviceId) {
        try {
            String apiUrl = "https://api.spotify.com/v1/me/player/play";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = openConnection(url, "PUT", "Bearer " + access_token);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject requestBody = new JSONObject();
            JSONArray uris = new JSONArray();
            uris.put(trackUri);
            requestBody.put("uris", uris);
            requestBody.put("device_id", deviceId);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == 204) {
                return "Reproduciendo la canción: " + trackUri;
            } else {
                System.out.println("Error al iniciar la reproducción: " + responseCode);
                System.out.println("Respuesta de error: " + readErrorResponse(connection));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readErrorResponse(HttpURLConnection connection) {
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            return errorResponse.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
/*

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mygdx.models.SongInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpotifyAuthenticator {
    private static String CLIENT_ID = "9cc33358a84845e288b47a8f59e6cd2f";
    private static String CLIENT_SECRET = "56269a922c9b487d8515563c89eccaf5";
    private static String REDIRECT_URI = "http://localhost:8888/callback";
    private String accessToken;

    public void initialize() {
        openAuthorizationPage();
        String authorizationCode = getAuthorizationCodeFromUser();
        this.accessToken = requestAccessToken(authorizationCode);
    }

    private static void openAuthorizationPage() {
        try {
            String scope = "user-read-private user-read-email user-modify-playback-state user-read-playback-state user-read-currently-playing user-read-playback-position";

            // Codifica los valores de la cadena de consulta
            CLIENT_ID = URLEncoder.encode(CLIENT_ID, "UTF-8");
            REDIRECT_URI = URLEncoder.encode(REDIRECT_URI, "UTF-8");
            scope = URLEncoder.encode(scope, "UTF-8");

            // Construye la URL con los valores codificados
            String authorizationUrl = "https://accounts.spotify.com/authorize" +
                    "?client_id=" + CLIENT_ID +
                    "&response_type=code" +
                    "&redirect_uri=" + REDIRECT_URI +
                    "&scope=" + scope +
                    "&show_dialog=true";

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(authorizationUrl));
            } else {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + authorizationUrl);
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("No se pudo abrir el navegador");
            e.printStackTrace();
        }
    }


    private static String getAuthorizationCodeFromUser() {
        try {
            int port = 8888; // El puerto en el que escucharás
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Esperando el código de autorización en el puerto " + port);

            // Espera hasta que se reciba una conexión
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            StringBuilder request = new StringBuilder();

            // Lee las líneas de la solicitud
            while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
                request.append(inputLine).append("\r\n");
            }

            // Extrae el código de autorización de la solicitud
            String authorizationCode = extractAuthorizationCode(request.toString());

            // Cierra el socket y el servidor
            in.close();
            clientSocket.close();
            serverSocket.close();

            return authorizationCode;
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor web: " + e.getMessage());
            return null;
        }
    }

    private static String extractAuthorizationCode(String request) {
        try {
            // Separar la solicitud HTTP de la URL
            String[] requestLines = request.split("\n");
            String urlLine = requestLines[0].split(" ")[1];

            // Crear una instancia de URI
            URI uri = new URI(urlLine);

            // Obtener los parámetros de la cadena de consulta
            String query = uri.getQuery();

            // Dividir los parámetros de la cadena de consulta
            String[] params = query.split("&");

            // Buscar el parámetro "code" y extraer su valor
            String codeValue = null;
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals("code")) {
                    codeValue = keyValue[1];
                    break;
                }
            }
            return codeValue;
        } catch (URISyntaxException e) {
            System.err.println("Error al analizar la URL: " + e.getMessage());
            return null;
        }
    }

    private static String requestAccessToken(String authorizationCode) {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());

        try {
            // Realiza una solicitud POST para obtener el token de acceso
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", authHeader);
            connection.setDoOutput(true);

            String requestBody = "grant_type=authorization_code" +
                    "&code=" + authorizationCode +
                    "&redirect_uri=" + REDIRECT_URI;
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(requestBody);
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Procesa la respuesta y obtiene el token de acceso
                try (InputStream inputStream = connection.getInputStream();
                     Scanner scanner = new Scanner(inputStream)) {
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        response.append(scanner.nextLine());
                    }
                    return response.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String searchSong(String songName) {
        try {
            // Analiza la cadena JSON para obtener el token de acceso
            JSONObject tokenJson = new JSONObject(this.accessToken);
            String access_token = tokenJson.getString("access_token");

            // Construye la URL de la solicitud GET para buscar la canción
            String apiUrl = "https://api.spotify.com/v1/search?q=" + songName + "&type=track";
            URL url = new URL(apiUrl);

            // Abre una conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configura la solicitud GET
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + access_token);

            // Obtiene la respuesta de la solicitud
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // La solicitud fue exitosa, lee la respuesta
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String res = response.toString();
                JSONObject searchResultJson = new JSONObject(res);
                JSONArray tracks = searchResultJson.getJSONObject("tracks").getJSONArray("items");
                String trackUri = tracks.getJSONObject(0).getString("uri"); //spotify:track:uri
                return trackUri;

            } else {
                // La solicitud no fue exitosa, maneja el error de alguna manera
                System.out.println("Error en la solicitud: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String playSong(String songName) {
        try {
            //Token
            JSONObject tokenJson = new JSONObject(this.accessToken);
            String access_token = tokenJson.getString("access_token");

            // Usamos la función de búsqueda que ya tienes para buscar trackuri de la cancion
            String trackUri = searchSong(songName);

            // Verifica la existencia de dispositivos activos
            String devicesApiUrl = "https://api.spotify.com/v1/me/player/devices";
            URL devicesUrl = new URL(devicesApiUrl);
            HttpURLConnection devicesConnection = (HttpURLConnection) devicesUrl.openConnection();
            devicesConnection.setRequestMethod("GET");
            devicesConnection.setRequestProperty("Authorization", "Bearer " + access_token);

            int devicesResponseCode = devicesConnection.getResponseCode();

            if (devicesResponseCode == 200) {
                // La solicitud de dispositivos fue exitosa, lee la respuesta
                BufferedReader devicesReader = new BufferedReader(new InputStreamReader(devicesConnection.getInputStream()));
                StringBuilder devicesResponse = new StringBuilder();
                String devicesLine;

                while ((devicesLine = devicesReader.readLine()) != null) {
                    devicesResponse.append(devicesLine);
                }
                devicesReader.close();

                // Analiza la respuesta para verificar si hay dispositivos activos
                JSONObject devicesJson = new JSONObject(devicesResponse.toString());
                JSONArray devices = devicesJson.getJSONArray("devices");
                System.out.println("Dispositivos:" + devices);

                if (devices.length() > 0) {
                    // Hay dispositivos activos, puedes usar uno de ellos para la reproducción
                    // Obtiene el ID de un dispositivo y configúralo para la reproducción
                    String deviceId = devices.getJSONObject(0).getString("id");

                    // Usamos el URI para reproducir la canción
                    String apiUrl = "https://api.spotify.com/v1/me/player/play";
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("PUT");
                    connection.setRequestProperty("Authorization", "Bearer " + access_token);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    JSONObject requestBody = new JSONObject();
                    JSONArray uris = new JSONArray();
                    uris.put(trackUri);
                    requestBody.put("uris", uris);
                    requestBody.put("device_id", deviceId); // Agrega el device_id

                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = requestBody.toString().getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    int responseCode = connection.getResponseCode();

                    if (responseCode == 204) {
                        // La reproducción se inició con éxito
                        return "Reproduciendo la canción: " + songName;
                    } else {
                        // El servidor respondió con un código de error, mostramos más detalles
                        System.out.println("Error al iniciar la reproducción: " + responseCode);

                        // Lee la respuesta de error para obtener más información
                        BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder errorResponse = new StringBuilder();
                        String errorLine;

                        while ((errorLine = errorReader.readLine()) != null) {
                            errorResponse.append(errorLine);
                        }
                        errorReader.close();

                        // Imprime la respuesta de error
                        System.out.println("Respuesta de error: " + errorResponse.toString());

                        return null;
                    }
                }
            } else {
                System.out.println("Error al obtener la lista de dispositivos: " + devicesResponseCode);
                return null;
            }
            // No se encontraron canciones
            return "Canción no encontrada: " + songName;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SongInfo getSongInfo(String songName) {
        SongInfo songInfo = new SongInfo();
        try {
            // Analiza la cadena JSON para obtener el token de acceso
            JSONObject tokenJson = new JSONObject(this.accessToken);
            String access_token = tokenJson.getString("access_token");

            //Busca la cancion
            String trackUri = this.searchSong(songName);

            //divide la cadena para sacar el uri
            String[] parts = trackUri.split("track:");
            String trackId = parts[1];

            // Construye la URL de la solicitud GET para obtener información detallada de una canción
            String apiUrl = "https://api.spotify.com/v1/audio-analysis/" + trackId;
            URL url = new URL(apiUrl);

            // Abre una conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configura la solicitud GET
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + access_token);

            // Obtiene la respuesta de la solicitud
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // La solicitud fue exitosa, lee la respuesta
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject analizeResultJson = new JSONObject(response.toString());

                //Obtiene la duracion y el tempo de la cancion
                JSONObject trackJson = analizeResultJson.getJSONObject("track");
                double trackTempo = trackJson.getDouble("tempo");
                double trackDuration = trackJson.getDouble("duration");

                songInfo.setTempo(trackTempo);
                songInfo.setDuration(trackDuration);

                return songInfo;
            } else {
                // La solicitud no fue exitosa, maneja el error de alguna manera
                System.out.println("Error en la solicitud: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
*/
