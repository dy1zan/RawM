package today.doingit.App;

import java.util.ArrayList;

public class Group {

    private ArrayList<User> users = new ArrayList<User>();
    private String groupname;

    public Group(String name) {
        this.groupname = name;
    }

    /**
     * Adds a new user to an existing group.
     * @param user The user to add to the group.
     */
    public void addMember(User user) {
        users.add(user);
    }


    /**
     * Returns the group name
     * @return
     */
    public String getGroupName() {
        return groupname;
    }
}
