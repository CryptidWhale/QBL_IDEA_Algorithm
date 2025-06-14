<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Encryption/Decryption Results</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Google Fonts - Inter -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f0f2f5; /* Light gray background */
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            padding: 30px; /* Increased padding around the body for more breathing room */
            box-sizing: border-box;
        }
        .container {
            background-color: #ffffff;
            padding: 40px; /* Increased padding inside the container */
            border-radius: 16px; /* Slightly more rounded corners for a softer look */
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15); /* Slightly stronger, but still soft shadow */
            max-width: 750px; /* Made it a bit wider to accommodate content better */
            width: 100%; /* Full width on smaller screens */
            display: flex;
            flex-direction: column;
            gap: 25px; /* Increased gap between sections for more separation */
        }
        .result-section {
            background-color: #fcfdfe; /* Very subtle off-white for sections */
            border-radius: 10px; /* Slightly more rounded borders for sections */
            padding: 25px; /* Increased padding within each result section */
            border: 1px solid #e5e7eb; /* Subtle border */
            transition: all 0.2s ease-in-out; /* Smooth transition for hover effect */
        }
        .result-section:hover {
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08); /* Light shadow on hover for interactivity */
            transform: translateY(-2px); /* Slight lift on hover */
        }
        .result-label {
            font-weight: 700; /* Bolder label for better distinction */
            color: #2c3e50; /* Darker, professional gray for labels */
            margin-bottom: 12px; /* Increased margin below label */
            display: block;
            font-size: 1.05rem; /* Slightly larger label font size */
        }
        .result-value {
            background-color: #f0f4f8; /* Softer light blue-gray for values */
            border-radius: 8px; /* Rounded corners for value boxes */
            padding: 8px 15px; /* Adjusted: Reduced top/bottom padding, kept horizontal */
            font-family: 'JetBrains Mono', 'Fira Code', monospace; /* More code-friendly monospace fonts */
            white-space: pre-wrap; /* Preserve whitespace and wrap text */
            word-wrap: break-word; /* Break long words */
            font-size: 0.95rem; /* Slightly larger value font size for readability */
            color: #334155; /* Slightly darker text for contrast */
            border: 1px dashed #cbd5e1; /* Added a subtle dashed border */
        }
        .error-message {
            background-color: #ffe0e0; /* Softer red background for errors */
            color: #c0392b; /* Stronger red text for errors */
            padding: 20px; /* Increased padding for error message */
            border-radius: 10px; /* Rounded corners for error message */
            border: 1px solid #e74c3c;
            font-weight: 600; /* Semi-bold text */
            text-align: center; /* Center error message text */
        }
        button {
            background-color: #6a05ad; /* Deep purple button */
            color: #ffffff;
            font-weight: 700; /* Bolder button text */
            padding: 14px 28px; /* Larger button padding */
            border-radius: 10px; /* More rounded button */
            border: none;
            cursor: pointer;
            width: fit-content; /* Adjust width to content */
            align-self: center; /* Center the button */
            font-size: 1.15rem; /* Slightly larger button font */
            transition: background-color 0.2s ease-in-out, transform 0.1s ease-in-out, box-shadow 0.2s ease-in-out;
            margin-top: 15px; /* More space above button */
            box-shadow: 0 4px 10px rgba(106, 5, 173, 0.3); /* Subtle button shadow */
        }
        button:hover {
            background-color: #5a049a; /* Darker purple on hover */
            transform: translateY(-3px); /* More pronounced lift effect */
            box-shadow: 0 8px 15px rgba(106, 5, 173, 0.4); /* Stronger shadow on hover */
        }
        button:active {
            transform: translateY(0);
            box-shadow: 0 2px 5px rgba(106, 5, 173, 0.2);
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="text-3xl font-bold text-gray-800 text-center mb-6">Encryption/Decryption Results</h1>

        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="error-message">
                <strong>Error:</strong> <c:out value="${sessionScope.errorMessage}" />
            </div>
        </c:if>

        <c:if test="${empty sessionScope.errorMessage}">
            <div class="result-section">
                <span class="result-label">Original Text:</span>
                <div class="result-value">
                    <c:out value="${sessionScope.originalText}" />
                </div>
            </div>

            <div class="result-section">
                <span class="result-label">Encryption Key:</span>
                <div class="result-value">
                    <c:out value="${sessionScope.encryptionKey}" />
                </div>
            </div>

            <div class="result-section">
                <span class="result-label">Encrypted Text:</span>
                <div class="result-value">
                    <c:out value="${sessionScope.encryptedResult}" />
                </div>
            </div>

            <div class="result-section">
                <span class="result-label">Decrypted Text:</span>
                <div class="result-value">
                    <c:out value="${sessionScope.decryptedResult}" />
                </div>
            </div>
        </c:if>

        <button onclick="window.location.href='LandingPage.html'">Go Back</button>
    </div>
</body>
</html>
