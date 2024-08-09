package mtWebCrawler;

public class Main {
    public static void main(String[] args){
        ArrayList<WebCrawler> bots = new ArrayList<>();
        bots.add(new WebCrawler("https://abcnews.go.com",1));
        bots.add(new WebCrawler("https://www.npr.org",2));
        bots.add(new WebCrawler("https://www.nytimes.com",3));

        for(Webcrawler w : bots){
            try{
                w.getThread().join();
            }
            catch(IterruptedException e){
                e.printStackTrace();
            }
        }
    }
}