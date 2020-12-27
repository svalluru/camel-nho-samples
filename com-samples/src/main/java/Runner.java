import com.data.transformations.DataBuilder;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

public class Runner {
    private static ProducerTemplate pt;

	public static void main(String[] args) throws Exception {
        CamelContext ctx =  new DefaultCamelContext();
        ctx.addRoutes(new DataBuilder());
        ctx.start();


        pt = ctx.createProducerTemplate();
        pt.requestBody("direct:callTransform", "Hello Sri");
        pt.requestBody("direct:callProcessor", "Hello Val");
        pt.requestBody("direct:callBean", "Test bean");
        pt.requestBody("direct:callPollEnrich", "Calling PollEnrich bean");
        pt.requestBody("direct:callEnrich", "Calling Enrich bean");


    }
}
