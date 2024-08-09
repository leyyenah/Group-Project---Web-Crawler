package multithreadWebCrawler;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

//This is implementing the runnable interface
public class WebCrawler implements Runnable {
	//make max_depth 3
    public static final int MAX_DEPTH = 4;
    private Thread thread;
    private String root;
    //create array to store the links
    private ArrayList<String> links = new ArrayList<String>();
    private int threadNum;
    
    public WebCrawler(int num, String url) {
        System.out.println("WebCrawler created");
        root = url;
        threadNum = num;
        
        thread = new Thread(this);
        thread.start();
    }
    
    //This is where the first URL goes and it is sent into the crawl method
    public void run() {
        crawl(1, root);
       
    }
    
    //takes in the current depth of the crawl and the URL
    private void crawl(int depth, String url) {
        if (depth <= MAX_DEPTH) {
            Document doc = request(url);
            
            //if the document is not null 
            if (doc != null) {
            	//It will extract all the links from the specified URL
                for (Element link : doc.select("a[href]")) {
                    String nextRoot = link.absUrl("href");
                    //if the links have not been visited before they are recursively called in crawl & goes to the next level of depth
                    if (!links.contains(nextRoot)) {
                        crawl(depth + 1, nextRoot);
                    }
                }
            }
        }
    }

    private Document request(String url) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if (con.response().statusCode() == 200) {
            	System.out.println("\n-> Thread #: " + threadNum + " - Next link @ " + url);

                String title = doc.title();
                System.out.println(title);
                links.add(url);

                return doc;
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }
    //this method returns the thread object that is associated with the web crawler instance
    public Thread getThread() {
        return thread;
    }
    
    
    public static void main(String[] args){
        ArrayList<WebCrawler> threads = new ArrayList<>();
        //It creates a new web crawler instance called threads
        threads.add(new WebCrawler(1, "https://www.bonappetit.com/recipes"));
        threads.add(new WebCrawler(2, "https://www.the-girl-who-ate-everything.com/"));
        threads.add(new WebCrawler(3, "https://copykat.com/"));
        
        //Iterates over each web crawler instance in the list and uses join to wait for all crawling threads before preceding
        for (int i = 0; i < threads.size(); i++) {
            WebCrawler webcrawler = threads.get(i);
            try {
                webcrawler.getThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
        
    
}