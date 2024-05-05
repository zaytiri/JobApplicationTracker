package personal.zaytiri.jobtracker.api.libraries.webscraper;

import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;

import java.io.IOException;

public class GlassDoorScraper extends WebScraper{
    @Override
    public JobOffer getGeneralJobInformation(String url) throws IOException {
        JobOffer scrapedJobOffer = new JobOffer();

        final WebClient webClient = new WebClient();

        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);

        final HtmlPage page = webClient.getPage(url);

        final HtmlHeading1 jobRole = (HtmlHeading1) page.getByXPath("//h1[@class='heading_Heading__BqX5J heading_Level1__soLZs']").get(0);
        scrapedJobOffer.setRole(jobRole.getFirstChild().getNodeValue().trim());

        final HtmlHeading4 jobCompany = (HtmlHeading4) page.getByXPath("//h4[@class='heading_Heading__BqX5J heading_Subhead__Ip1aW']").get(0);
        scrapedJobOffer.setCompany(jobCompany.getFirstChild().getNodeValue().trim());

        final HtmlDivision jobLocation = (HtmlDivision) page.getByXPath("//div[@class='JobDetails_location__mSg5h']").get(0);
        scrapedJobOffer.setLocation(jobLocation.getFirstChild().getNodeValue().trim());

        final HtmlDivision description = (HtmlDivision) page.getByXPath("//div[@class='JobDetails_jobDescription__uW_fK JobDetails_showHidden__C_FOA']").get(0);
        scrapedJobOffer.setDescription(getAllTextContent(description));

        return scrapedJobOffer;
    }

    @Override
    public boolean isJobClosed(String url) throws IOException {
        return false;
    }
}
