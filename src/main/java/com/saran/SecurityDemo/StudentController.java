package com.saran.SecurityDemo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {
    List<Student> students = new ArrayList<>(List.of(
            new Student(1,"saran",22),
            new Student(2,"sunil",20)
    ));

    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }

    // refer below for CSRF Details
    @GetMapping("csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf"); // attribute name is taken from "localhost:8080/logout (view page source)"
    }

    @PostMapping("students")
    public Student addStudent(@RequestBody Student student) {
        students.add(student);
        return student;
    }
}

                    // What is CSRF (type of attack)

//What is CSRF (Cross-Site Request Forgery)?
//CSRF is a type of attack where a bad person (the attacker) tricks you into doing something you didn’t mean to do on a website where you’re already logged in with the help of session id.
//
//For example:
//
//Imagine you are logged into your bank’s website on your browser.
//        Now, you click on a fake link or visit a website made by an attacker.
//That attacker’s website sends a hidden request to your bank’s website using your login session.
//Since you’re already logged in, the bank’s website doesn’t realize the request is fake and thinks it’s coming from you.
//As a result, it may transfer money or change your account settings without you knowing!
//
//How CSRF Happens:
//You log in to a website (e.g., a shopping site or social media).
//Attacker sends a trick link (this could be hidden in an email, message, or malicious website).
//Clicking the link sends a fake request to the site you’re logged into.
//The site processes the request as if you made it, because you’re still logged in.

// TO PROTECT FROM CSRF ATTACKS

               //    1. -> USING  CSRF TOKEN

//The CSRF token is used to protect web applications from Cross-Site Request Forgery (CSRF) attacks. Here's how and when it is used in simple terms:
//
//When is the CSRF Token Used?
//Forms Submissions (POST Requests):
//
//Whenever a form is submitted on a website (e.g., login, making payments, updating settings), a CSRF token is often sent along with the form data to ensure the request is genuine.
//State-Changing Actions (Sensitive Operations):
//
//For actions that change data on the server (e.g., transferring money, deleting an account, updating profile information), CSRF tokens are added to the request to confirm it’s from a trusted user.
//AJAX Requests:
//
//If an application uses AJAX to make background requests (e.g., updating your profile picture without refreshing the page), a CSRF token is typically included to secure those requests as well.
//How Does the CSRF Token Work?
//Token Generation:
//When a user logs in or opens a web page with a form, the server generates a random CSRF token.
//The token is sent to the user's browser, either as a hidden input field in the form or as a header in AJAX requests.
//Token Validation:
//When the user submits the form or makes a request, the CSRF token is sent back to the server along with the request.
//The server checks if the token is valid and matches the one it issued.
//Request Approval:
//If the token is valid, the server processes the request.
//If the token is missing or incorrect, the server rejects the request.

//          2. -> CREATING SESSION ID FOR EACH REQUEST