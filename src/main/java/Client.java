import java.util.List;
import org.sql2o.*;

public class Client{
  private int id;
  private String name;
  private String gender;
  private String contact;
  private int stylist_id;

  public Client(String name, String gender, String contact, int stylist_id){
    this.name = name.toUpperCase();
    this.gender = gender;
    this.contact = contact;
    this.stylist_id = stylist_id;
  }
  public int getId(){
    return id;
  }
  public String getName(){
    return name;
  }
  public String getGender(){
    return gender;
  }
  public String getContact(){
    return contact;
  }

  public Stylist getStylist() {
    int id = stylist_id;
    Stylist stylist;
    if(id ==0){
       stylist = new Stylist("--","","");
    }else{
      try(Connection con = DB.sql2o.open()) {
        String sql = "SELECT * FROM stylists where id=:id";
         stylist = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchFirst(Stylist.class);
         }
    }
    return stylist;
  }

  public static List<Client> all() {
      String sql = "SELECT id, name, gender, contact, stylist_id FROM clients ORDER BY name";
      try(Connection con = DB.sql2o.open()) {
        return con.createQuery(sql).executeAndFetch(Client.class);
      }
  }
  public void save() {
   try(Connection con = DB.sql2o.open()) {
     String sql = "INSERT INTO clients(name, gender, contact, stylist_id) VALUES (:name, :gender, :contact, :stylist_id)";
     this.id = (int) con.createQuery(sql, true)
       .addParameter("name", this.name)
       .addParameter("gender", this.gender)
       .addParameter("contact", this.contact)
       .addParameter("stylist_id", this.stylist_id)
       .executeUpdate()
       .getKey();
    }
  }
  public static Client find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients where id=:id";
      Client client = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Client.class);
      return client;
    }
  }
  public void update(String name, String gender, String contact, int stylist_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET name = :name, gender = :gender, contact = :contact, stylist_id = :stylist_id WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("gender", gender)
        .addParameter("contact", contact)
        .addParameter("stylist_id", stylist_id)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM clients WHERE id = :id;";
    con.createQuery(sql).addParameter("id", id).executeUpdate();
    }
  }

}
