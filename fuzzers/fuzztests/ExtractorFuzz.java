import org.apache.tika.parser.ParseContext;
import org.apache.tika.metadata.Metadata;
import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import iped.parsers.mail.MSGParser;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

public class ExtractorFuzz {

	private static ContentHandler prevHandler;
	
	public static void fuzzerTestOneInput(byte[] input) {
		
		MSGParser msgParser = new MSGParser();
		Metadata metadata = new Metadata();
		ContentHandler handler = new DefaultHandler();
		ParseContext ctx = new ParseContext();

		try {
			InputStream in = new ByteArrayInputStream(input);
			msgParser.parse(in, handler, metadata, ctx);


		} catch(Exception e) {
		}

	}
}
