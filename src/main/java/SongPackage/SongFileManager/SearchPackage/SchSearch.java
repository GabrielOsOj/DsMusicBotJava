package SongPackage.SongFileManager.SearchPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchSearch {

    private final String URL_SEARCH = "https://www.youtube.com/results?search_query=";
    private final String REGEX_CHAIN_VIDEO_ID = "\"videoId\":\"([^\"]+)\"";
    private final String REGEX_CHAIN_AD_FILTER = "videoRenderer";

    private final String MSG_ERROR_HTML_NULL = "HTML page not found!";
    private final String MSG_ERROR_ID_NOT_FOUND = "Video id not found in html page!";
    private final String MSG_ERROR_AD_FILTER_FAILED = "AD filter error!";
    private final String MSG_ERROR_BAD_URL = "Bad url";
    private final String MSG_ERROR_BAD_CONNECTION = "Bad connection";
    private final String MSG_ERROR_BAD_ENCODING = "Can't cencode ";

    public SchSearch() {
    }

    ;
        
    public String searchSongId(String songName) {

        String htmlPage = searchHTML(songName);

        if (htmlPage == null) {
            System.err.println(MSG_ERROR_HTML_NULL);
            return null;
        }

        String HTMLwithoutAds = this.adFilter(htmlPage);

        if (HTMLwithoutAds.isEmpty()) {
            System.err.println(MSG_ERROR_AD_FILTER_FAILED);
        }

        String id = this.findId(HTMLwithoutAds);

        if (id.isEmpty()) {
            System.err.println(MSG_ERROR_ID_NOT_FOUND);
            return null;
        }

        return id;
    }

    private String findId(String dataWithId) {

        Pattern pattern = Pattern.compile(REGEX_CHAIN_VIDEO_ID);
        Matcher matcher = pattern.matcher(dataWithId);

        while (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    /**
     * *
     * Remove ad tag from html
     *
     * @param HTMLWithAdTag
     * @return
     */
    private String adFilter(String HTMLWithAdTag) {

        Pattern pattern = Pattern.compile(REGEX_CHAIN_AD_FILTER);
        Matcher matcher = pattern.matcher(HTMLWithAdTag);

        while (matcher.find()) {

            return HTMLWithAdTag.substring(matcher.start());

        }

        return "";

    }

    /**
     * *
     * @deprecated Search html element with script tag
     * @param String with HTML not parsed yet
     * @return
     
    private String HTMLparser(String HTML) {

        Document doc = Jsoup.parse(HTML.toString());
        Elements script = doc.getElementsByTag("script");
        String HTMLparsed = "";
        // select var in script element, this format is var ytInitialData : {}...
        for (Element e : script) {
            if (e.toString().contains("var ytInitialData")) {
                HTMLparsed = e.html().toString();
                break;
            }
        }

        return HTMLparsed;
    }
*/
    private String searchHTML(String songName) {

        try {
            //prepair for petition
            HttpURLConnection con = this.setConnectionConfig(songName);

            //Search in YouTube for videos that match the song name                           
            BufferedReader response = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            //Parse the response
            StringBuilder htmlParsed = this.httpResponseParser(response);

            response.close();
            con.disconnect();

            return htmlParsed.toString();

        } catch (MalformedURLException e) {
            System.err.println(this.MSG_ERROR_BAD_URL);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.err.println(this.MSG_ERROR_BAD_ENCODING + songName);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(this.MSG_ERROR_BAD_CONNECTION);
        }

        return null;
    }

    ;
        
    
        // set connection config to simulate that petition comes from a web browser
        private HttpURLConnection setConnectionConfig(String songName) throws IOException {

        String encoded = URLEncoder.encode(songName, "UTF-8");
        URL url = URI.create(this.URL_SEARCH + encoded).toURL();

        HttpURLConnection newConnection = (HttpURLConnection) url.openConnection();
        newConnection.setRequestMethod("GET");
        newConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        newConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        return newConnection;
    }

    /**
     * *
     * create and return stringBuilder from the http Response, which contain all
     * html from the page, including youtube video id
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
    private StringBuilder httpResponseParser(BufferedReader httpResponse)
            throws IOException {

        String inputLine;
        StringBuilder HTMLcontent = new StringBuilder();

        while ((inputLine = httpResponse.readLine()) != null) {
            HTMLcontent.append(inputLine);
        }

        return HTMLcontent;
    }

}
