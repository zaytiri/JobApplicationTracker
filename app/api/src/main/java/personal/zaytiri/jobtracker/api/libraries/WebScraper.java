package personal.zaytiri.jobtracker.api.libraries;

import org.htmlunit.*;
import org.htmlunit.html.*;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;

import java.io.IOException;

public class WebScraper {

    public JobOffer process(String url) throws IOException {
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

    private static String getAllTextContent(HtmlElement element) {
        StringBuilder textContent = new StringBuilder();

        for (DomNode child : element.getChildren()) {
            if (child instanceof DomText) {
                textContent.append(child.getTextContent().trim());
            } else if (child instanceof HtmlElement) {
                if( child instanceof HtmlParagraph){
                    textContent.append("\n");
                }
                if( child instanceof HtmlListItem){
                    textContent.append("\n - ");
                }
                textContent.append(getAllTextContent((HtmlElement) child));
            }
        }
        return textContent.toString();
    }
}
