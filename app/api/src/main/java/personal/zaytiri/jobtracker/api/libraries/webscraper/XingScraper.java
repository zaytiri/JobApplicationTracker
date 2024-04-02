package personal.zaytiri.jobtracker.api.libraries.webscraper;

import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;

import java.io.IOException;
import java.util.List;

public class XingScraper extends WebScraper{
    @Override
    public JobOffer process(String url) throws IOException {
        JobOffer scrapedJobOffer = new JobOffer();

        final WebClient webClient = new WebClient();

        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);

        final HtmlPage page = webClient.getPage(url);

        final HtmlHeading1 jobRole = (HtmlHeading1) page.getByXPath("//h1[@class='headlinestyles__Headline-sc-1gpssxl-0 kXtUQV titlestyles__StyledHeadline-akj5c4-1 dDIkXE']").get(0);
        scrapedJobOffer.setRole(jobRole.getFirstChild().getNodeValue().trim());

        final HtmlParagraph jobCompany = (HtmlParagraph) page.getByXPath("//p[@class='body-copystyles__BodyCopy-x85e3j-0 eFAa-DJ']").get(0);
        scrapedJobOffer.setCompany(jobCompany.getFirstChild().getNodeValue().trim());

        final HtmlListItem jobLocation = (HtmlListItem) page.getByXPath("//li[@class='info-iconstyles__StyledInfoIcon-zjpzon-0 iOVJFp']").get(0);
        scrapedJobOffer.setLocation(jobLocation.getFirstChild().getNextSibling().getNodeValue().trim());

        final HtmlDivision description = (HtmlDivision) page.getByXPath("//div[@class='html-description-componentstyles__StyledHeader-sc-1ldlnne-1 pvbYu']").get(0);
        scrapedJobOffer.setDescription(getAllTextContent(description));

        return scrapedJobOffer;
    }
}
