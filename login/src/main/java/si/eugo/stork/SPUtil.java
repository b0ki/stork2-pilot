package si.eugo.stork;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



//import eu.stork.sp.SPUtil;

public class SPUtil {

	public static Properties loadConfigs(String path) throws IOException
	{
		Properties properties = new Properties();
		properties.load(SPUtil.class.getClassLoader().getResourceAsStream(path));
		return properties;
	}
	
	public static Properties loadConfigs(InputStream is) throws IOException
	{
		Properties properties = new Properties();
		properties.load(is);
		return properties;
	}
	
	public static Properties loadXMLConfigs(String path) throws IOException
	{
		Properties properties = new Properties();
		properties.loadFromXML(SPUtil.class.getClassLoader().getResourceAsStream(path));
		return properties;
	}
	
	public static InputStream loadConfInputStream(String path){
		return SPUtil.class.getClassLoader().getResourceAsStream(path);
	}
}
