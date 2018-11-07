package systemTest;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.junit.BeforeClass;
import org.junit.Test;


public class HtmlTest{
	protected final static String URL = "localhost";
	protected final static String GET = "GET";
	protected final static String POST = "POST";
	protected final static String BAD = "BAD";
	
}
