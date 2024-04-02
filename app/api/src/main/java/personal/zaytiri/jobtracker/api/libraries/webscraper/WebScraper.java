package personal.zaytiri.jobtracker.api.libraries.webscraper;

import org.htmlunit.*;
import org.htmlunit.html.*;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;

import java.io.IOException;

public abstract class WebScraper {

    public abstract JobOffer process(String url) throws IOException;

    protected String getAllTextContent(HtmlElement element) {
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
