package personal.zaytiri.jobtracker.api.libraries.webscraper;

public class WebScraperFactory {

    public static WebScraper findSuitableScraper(String url){
        if(url.contains("linkedin.com/jobs/view")){
            return new LinkedInScraper();
        } else if (url.contains("glassdoor.com/Job") || url.contains("glassdoor.com/job-listing")) {
            return new GlassDoorScraper();
        } else if (url.contains("xing.com/jobs")) {
            return new XingScraper();
        } else {
            return null;
        }
    }
}
