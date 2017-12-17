import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    ProcessBuilder process = new ProcessBuilder();
     Integer port;
     if (process.environment().get("PORT") != null) {
         port = Integer.parseInt(process.environment().get("PORT"));
     } else {
         port = 4567;
     }

    setPort(port);

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("stylists", Stylist.all());
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/new_Stylist_Form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String gender = request.queryParams("gender");
      String contact = request.queryParams("contact");
      Stylist newStylist = new Stylist(name, gender, contact);
      newStylist.save();
      model.put("template", "templates/success.vtl");
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      get("/stylists/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      model.put("stylist", stylist);
      model.put("template", "templates/stylist.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("stylists/:id/clients/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      model.put("stylist", stylist);
      model.put("stylists", Stylist.all());
      model.put("template", "templates/new_Client_Form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/clients", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String gender = request.queryParams("gender");
      String contact = request.queryParams("contact");
      int stylist_id = Integer.parseInt(request.queryParams("stylist_id"));
      Client newClient = new Client(name, gender, contact, stylist_id);
      newClient.save();
      model.put("template", "templates/success.vtl");
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      get("/clients", (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("clients", Client.all());
        model.put("template", "templates/clients.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      get("clients/new", (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        // Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
        // model.put("stylist", stylist);
        model.put("stylists", Stylist.all());
        model.put("template", "templates/new_Client_Form.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      get("/stylists/:id/edit", (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
        String checkMale="";
        String checkFemale="";
        if(stylist.getGender().equals("M")){
          checkMale="checked=\"checked\"";
        }else{
          checkFemale="checked=\"checked\"";
        }
        model.put("checkMale", checkMale);
        model.put("checkFemale", checkFemale);
        model.put("stylist", stylist);
        model.put("template", "templates/edit_Stylist_Form.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      //UPDATING sTYLIST Details
      post("/stylists/:id/edit", (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
        String name = request.queryParams("name");
        String gender = request.queryParams("gender");
        String contact = request.queryParams("contact");
        stylist.update(name, gender, contact);
        model.put("template", "templates/success.vtl");
        return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());


  }
}
