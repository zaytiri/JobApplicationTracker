package personal.zaytiri.jobtracker.api.libraries.webscraper;

import org.eclipse.jetty.websocket.common.util.ReflectUtils;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;

import java.io.IOException;
import java.util.List;

public class LinkedInScraper extends WebScraper {
    @Override
    public JobOffer getGeneralJobInformation(String url) throws IOException {
        JobOffer scrapedJobOffer = new JobOffer();

        final WebClient webClient = new WebClient();

        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);

        final HtmlPage page = webClient.getPage(url);

        final HtmlHeading1 jobRole = (HtmlHeading1) page.getByXPath("//h1[@class='top-card-layout__title font-sans text-lg papabear:text-xl font-bold leading-open text-color-text mb-0 topcard__title']").get(0);
        scrapedJobOffer.setRole(jobRole.getFirstChild().getNodeValue().trim());

        final HtmlAnchor jobCompany = (HtmlAnchor) page.getByXPath("//a[@class='topcard__org-name-link topcard__flavor--black-link']").get(0);
        scrapedJobOffer.setCompany(jobCompany.getFirstChild().getNodeValue().trim());

        final HtmlSpan jobLocation = (HtmlSpan) page.getByXPath("//span[@class='topcard__flavor topcard__flavor--bullet']").get(0);
        scrapedJobOffer.setLocation(jobLocation.getFirstChild().getNodeValue().trim());

        final HtmlDivision description = (HtmlDivision) page.getByXPath("//div[@class='show-more-less-html__markup show-more-less-html__markup--clamp-after-5\n" +
                "            relative overflow-hidden']").get(0);
        scrapedJobOffer.setDescription(getAllTextContent(description));

        return scrapedJobOffer;
    }

    @Override
    public boolean isJobClosed(String url) throws IOException, RuntimeException {
        final WebClient webClient = new WebClient();

        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);

        final HtmlPage page = webClient.getPage(url);

        final List<HtmlFigureCaption> figcaptions = page.getByXPath("//figcaption");

        for(HtmlFigureCaption fc : figcaptions){
            if(fc.getAttribute("class").equals("closed-job__flavor--closed")){
                return true;
            }
        }

        return false;
    }
}
