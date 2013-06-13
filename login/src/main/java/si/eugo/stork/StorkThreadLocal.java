package si.eugo.stork;

final public class StorkThreadLocal {
	
	private static final ThreadLocal<StorkUser> storkUser = new ThreadLocal<StorkUser>();
	
	public static void setStorkUser(StorkUser su){
		storkUser.set(su);
	}
	
	public static StorkUser getStorkUser(){
		return storkUser.get();
	}

}
