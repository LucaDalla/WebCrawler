import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
//---------------------------------//
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il seed URL: ");
        String seedUrl = scanner.nextLine();
        scanner.close();

        Set<String> visited = new HashSet<>();
        crawl(seedUrl, 1, visited, 3);
        System.out.println("I siti visitabili in questa pagina sono terminati!");
    }
    public static void crawl(String url, int level, Set<String> visited, int maxDepth) {
        if (level <= maxDepth) {
            if (!visited.contains(url)) {
                visited.add(url);
                Document doc = request(url);
                if (doc != null) {
                    System.out.println("Link: " + url);
                    System.out.println("Title: " + doc.title());
                    boolean foundLinks = false;
                    for (Element link : doc.select("a[href]")) {
                        String nextLink = link.absUrl("href");
                        crawl(nextLink, level + 1, visited, maxDepth);
                        foundLinks = true;
                    }
                    if (!foundLinks) {
                        System.out.println("Non ci sono più link visitabili in questa pagina.");
                    }
                }
            }
        }
    }
    private static Document request(String url) {
        try {
            Connection conn = Jsoup.connect(url);
            Document doc = conn.get();
            if (conn.response().statusCode() == 200) {
                return doc;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}