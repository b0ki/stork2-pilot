package si.eugo.stork;

import java.security.Principal;

public class StorkPrincipal implements Principal{
	
	StorkUser storkUser;
	
	

	public StorkPrincipal(StorkUser storkUser) {
		super();
		this.storkUser = storkUser;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return storkUser.getUsername();
	}

	public StorkUser getStorkUser() {
		return storkUser;
	}

	public void setStorkUser(StorkUser storkUser) {
		this.storkUser = storkUser;
	}
	
	

}
