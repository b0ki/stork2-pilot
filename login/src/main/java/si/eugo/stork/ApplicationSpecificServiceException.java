package si.eugo.stork;

public class ApplicationSpecificServiceException extends RuntimeException {
	
	private String msg;
	private String title;
	
	public ApplicationSpecificServiceException(String title, String msg) {
		this.msg = msg;
		this.title = title;
	}
	
	public String getMessage() {
		return msg;
	}
	
	public String getTitle() {
		return title;
	}
}