package server;
import java.util.ArrayList;
import java.util.List;

public class WaitingList {

	private List<User> list;
	
	public WaitingList(){
		list = new ArrayList<User>();
	}
	
	public void addUser(User user){
		list.add(user);
	}
	
	public void removeUser(User user){
		list.remove(user);
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(User u : list){
			str.append(u.getName());
			str.append(" ");
		}
		return str.toString();
	}
}
