/**
 * 
 */
/**
 * @author hp
 *
 */
module ResourcesDownloader {
	opens org.manianis;
    requires java.desktop;
    requires java.logging;
    requires java.prefs;
    requires java.net.http;
    requires org.apache.commons.io;
	requires rsyntaxtextarea;
	requires org.apache.httpcomponents.client5.httpclient5;
	requires org.apache.httpcomponents.core5.httpcore5;
	requires slf4j.api;
	requires org.json;
}