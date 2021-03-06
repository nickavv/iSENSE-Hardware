package edu.uml.cs.isense.objects;

/**
 * Class that includes every potential variable/object associated with a session
 * when a session is created.
 * 
 * @author iSENSE Android Development Team
 */
public class Session {
    public int session_id;
    public int owner_id;
    public int experiment_id;
    public String name = "";
    public String description = "";
    public String street = "";
    public String city = "";
    public String country = "";
    public double latitude;
    public double longitude;
    public String timecreated = "";
    public String timemodified = "";
    public String debug_data = "";
    public String firstname = "";
    public String lastname = "";
    public String timeobj = "";
    public int priv;
    public String experiment_name = "";
}
