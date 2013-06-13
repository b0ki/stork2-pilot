package si.eugo.stork;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="config")
public class SpocsStorkConfig {
	
	@XmlElement(name="storkAttribute")
	private List<StorkAttribute> attributes = new ArrayList<StorkAttribute>();

	public List<StorkAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<StorkAttribute> attributes) {
		this.attributes = attributes;
	}

}
