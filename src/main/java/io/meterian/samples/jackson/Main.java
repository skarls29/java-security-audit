package io.meterian.samples.jackson;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;

import org.slf4j.MDC;

import io.meterian.samples.jackson.product.ProductApi;
import io.meterian.samples.jackson.security.AuditLogger;
 
public class Main {
  
	private static final int HTTP_PORT = 8888;
 
    public static void main(String[] args) {
    	path("/products", () -> {
    		port(HTTP_PORT);
    		before("", (req, resp) -> MDC.put("sessionId", req.session().id()));
    		before("", AuditLogger::mdcBefore);
    		
    		get("", ProductApi::getProducts);
    		post("", ProductApi::addProduct);
    		
    		after("", AuditLogger::mdcAfter);
    		after("", (req, resp) -> MDC.clear());
    	});
    }

}