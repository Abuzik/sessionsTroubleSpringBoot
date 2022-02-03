package com.example.demo;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.client.MongoCollection;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mongodb.client.model.Filters.eq;

@RestController
public class Controller {
    private HttpSession httpSession;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> resp) throws Exception {
        MongoConnection connection = new MongoConnection("githubRepo", "users");
        MongoCollection<Document> collection = connection.getColl();
        boolean flag = true;
        Bson query = eq("username", resp.get("username"));
        if(collection.countDocuments(query) > 0) {
            flag = false;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already registered");
        }

        if(resp.get("username").length() > 25 || resp.get("username").length() < 3) {
            flag = false;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name should be more than 3 symbols (or equal) and less than 25 symbols");
        }

        if(resp.get("password").length() < 8) {
            flag = false;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User password should be more than 8 symbols");
        }

        if (flag) {
            try {
                collection.insertOne(new Document("username", resp.get("username")).append("password", Constants.hashPass(resp.get("password"))).append("role", "plain"));
            } catch (MongoBulkWriteException exception) {
                System.out.println("Exception occurred: " + exception);
            }
            return new ConcurrentHashMap<String, String>(Map.of("answer", "registered"));
        } else {
            return new ConcurrentHashMap<String, String>(Map.of("answer", "not registered"));
        }
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> resp, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.httpSession = session;
        MongoConnection connection = new MongoConnection("githubRepo", "users");
        MongoCollection<Document> collection = connection.getColl();
        Bson query = eq("username", resp.get("username"));
        if(collection.countDocuments(eq("name", resp.get("username"))) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not registered");
        }

        if(resp.get("username").length() > 25 || resp.get("username").length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name should be more than 3 symbols (or equal) and less than 25 symbols");
        }

        if(resp.get("password").length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User password should be more than 8 symbols");
        }
        String username = (String) this.httpSession.getAttribute("username");
        if(collection.countDocuments(eq("username", resp.get("username"))) > 0 && Constants.equalPass(collection.find(eq("username", resp.get("username"))).first().get("password").toString(), resp.get("password"))) {
            if (username == null) {
                this.httpSession.setAttribute("username", resp.get("username"));
                this.httpSession.setAttribute("role", collection.find(eq("username", resp.get("username")))
                        .first()
                        .get("role").toString());
            } else {
                System.out.println("Session exists");
            }
            System.out.println(this.httpSession.getId());
            System.out.println(this.httpSession.getAttribute("username"));
            return new ConcurrentHashMap<String, String>(Map.of("answer", "log in"));
        } else {
            return new ConcurrentHashMap<String, String>(Map.of("answer", "not registered"));
        }
    }

    @PostMapping("/getSession")
    public void getSession() {
        System.out.println(this.httpSession.getAttribute("username"));
    }
}
