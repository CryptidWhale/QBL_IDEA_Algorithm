package Servlet;

import Encryption.*;
import java.io.IOException;
import java.io.PrintWriter; // Not strictly needed for redirection, but often useful in servlets
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // Added to potentially store results for the next page

/**
 * Servlet acting as the middleman for IDEA encryption/decryption requests.
 * Fetches user input (key), processes it (placeholder for IDEA algorithm),
 * and redirects to the results page.
 *
 * @author Muhammad Iqbal bin Mohd Ariff (Updated by Greg)
 */
public class RunIDEA extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * This is where the core logic for fetching input, processing, and redirecting happens.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8"); // Standard content type for HTML response

        // 1. Fetch the input from the user
        // The HTML form sends the key with name="key" using POST method
        String userKey = "isTheCryptidWhaleReallyHere";

        // --- 2. Insert the process here ---
        // This is where you will integrate your IDEA algorithm logic.
        // For demonstration, let's assume you'll encrypt a fixed string
        // or perhaps pass another parameter for the text to encrypt from the HTML form.
        String plainTextToEncrypt = request.getParameter("key"); // Example text

        String encryptedText = "Error: Encryption not performed yet"; // Placeholder for encrypted output
        String decryptedText = "Error: Decryption not performed yet"; // Placeholder for decrypted output

        if (userKey != null && !userKey.trim().isEmpty()) {
            try {
                // Initialize your IDEA cipher with the user-provided key
                IDEA ideaCipher = new IDEA(userKey);

                // Perform encryption
                encryptedText = ideaCipher.encrypt(plainTextToEncrypt);

                // Perform decryption
                decryptedText = ideaCipher.decrypt(encryptedText);

                // You might want to store these results in the session so ResultPage.html can access them
                HttpSession session = request.getSession();
                session.setAttribute("originalText", plainTextToEncrypt);
                session.setAttribute("encryptionKey", userKey);
                session.setAttribute("encryptedResult", encryptedText);
                session.setAttribute("decryptedResult", decryptedText);

            } catch (Exception e) {
                // Log the exception for debugging purposes
                e.printStackTrace();
                // Optionally, store an error message in session to display on ResultPage.html
                HttpSession session = request.getSession();
                session.setAttribute("errorMessage", "An error occurred during encryption/decryption: " + e.getMessage());
            }
        } else {
             HttpSession session = request.getSession();
             session.setAttribute("errorMessage", "Encryption key cannot be empty!");
        }

        // --- 3. Redirect the page to ResultPage.html ---
        // Use sendRedirect to instruct the browser to go to a new URL.
        response.sendRedirect("ResultPage.jsp");
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * Delegates to processRequest.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * Delegates to processRequest.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for processing IDEA encryption/decryption requests.";
    }

}
