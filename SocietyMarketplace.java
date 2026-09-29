import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SocietyMarketplace {

    // ==============================
    // DATA
    // ==============================

    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Service> services = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    static int userId = 1;
    static int serviceId = 1;
    static int bookingId = 1;

    // ==============================
    // USER
    // ==============================

    static class User {
        int id;
        String name;
        String phone;
        String password;
        String role;

        User(int id, String name, String phone,
             String password, String role) {

            this.id = id;
            this.name = name;
            this.phone = phone;
            this.password = password;
            this.role = role;
        }
    }

    // ==============================
    // SERVICE
    // ==============================

    static class Service {
        int id;
        String provider;
        String name;
        String description;
        String price;
        String availability;

        Service(int id, String provider,
                String name, String description,
                String price, String availability) {

            this.id = id;
            this.provider = provider;
            this.name = name;
            this.description = description;
            this.price = price;
            this.availability = availability;
        }
    }

    // ==============================
    // BOOKING
    // ==============================

    static class Booking {
        int id;
        String customer;
        String service;
        String provider;
        String date;
        String status;

        Booking(int id, String customer,
                String service, String provider,
                String date) {

            this.id = id;
            this.customer = customer;
            this.service = service;
            this.provider = provider;
            this.date = date;
            this.status = "Pending";
        }
    }

    // ==============================
    // MAIN
    // ==============================

    public static void main(String[] args) throws Exception {

        // Sample service
        users.add(new User(
                userId++,
                "Demo Provider",
                "9999999999",
                "1234",
                "Provider"
        ));

        users.add(new User(
                userId++,
                "Demo Customer",
                "8888888888",
                "1234",
                "Customer"
        ));

       int port = Integer.parseInt(
        System.getenv().getOrDefault("PORT", "8080")
);

HttpServer server =
        HttpServer.create(
                new InetSocketAddress("0.0.0.0", port), 0);
        
               
        server.createContext("/", SocietyMarketplace::home);

        server.createContext("/register",
                SocietyMarketplace::register);

        server.createContext("/customer",
                SocietyMarketplace::customer);

        server.createContext("/provider",
                SocietyMarketplace::provider);

        server.createContext("/addservice",
                SocietyMarketplace::addService);

        server.createContext("/book",
                SocietyMarketplace::book);

        server.createContext("/bookings",
                SocietyMarketplace::bookings);

        server.createContext("/action",
                SocietyMarketplace::bookingAction);

        server.setExecutor(null);

        System.out.println("----------------------------------");
        System.out.println(" Society Service Marketplace");
        System.out.println("----------------------------------");
        System.out.println("Website running at:");
        System.out.println("http://localhost:8080");
        System.out.println("----------------------------------");

        server.start();
    }

    // ==============================
    // HOME
    // ==============================

    static void home(HttpExchange exchange)
            throws IOException {

        String html = """

        <!DOCTYPE html>
        <html>
        <head>
        <title>Society Service Marketplace</title>

        <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial;
            background: #F7F5FF;
            color: #1E1B4B;
        }

        .container {
            display: flex;
            min-height: 100vh;
        }

        .left {
            width: 40%;
            background: #1E1B4B;
            color: white;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
        }

        .left h1 {
            font-size: 42px;
            margin: 10px;
        }

        .left h2 {
            color: #A5B4FC;
        }

        .left p {
            color: #C4B5FD;
        }

        .logo {
            font-size: 70px;
        }

        .right {
            width: 60%;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px;
        }

        .cards {
            display: flex;
            gap: 25px;
            width: 100%;
            max-width: 760px;
        }

        .box {
            background: white;
            padding: 30px;
            flex: 1;
            min-height: 270px;
            border-radius: 12px;
            box-shadow: 0 5px 25px rgba(0,0,0,0.1);
            text-align: center;
        }

        .box h2 {
            color: #1E1B4B;
        }

        .box p {
            color: #555;
            min-height: 45px;
        }

        button {
            width: 100%;
            padding: 13px;
            background: #2563EB;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background: #1D4ED8;
        }

        @media (max-width: 800px) {
            .container {
                flex-direction: column;
            }

            .left, .right {
                width: 100%;
            }

            .cards {
                flex-direction: column;
            }
        }

        </style>
        </head>

        <body>

   <div style="position:absolute; top:20px; right:30px;">
    <select id="language" onchange="changeLanguage()">
        <option value="en">English</option>
        <option value="hi">हिंदी</option>
        <option value="mr">मराठी</option>
    </select>
</div>
<!-- existing home page code -->
        <div class="container">

            <div class="left">

                <div class="logo"></div>

                <h1>Society</h1>

                <h2>Service Marketplace</h2>

                <p>
                Connect neighbours.<br>
                Share services.
                </p>

            </div>

            <div class="right">

                <div class="cards">

                    <div class="box">

                        <h2>Provider Registration</h2>

                        <p>
                        Register as a service provider
                        and offer your services.
                        </p>

                        <form action="/register" method="get">
                            <input type="hidden"
                                   name="role"
                                   value="Provider">

                            <button type="submit">
                                Register as Provider
                            </button>
                        </form>

                    </div>

                    <div class="box">

                        <h2> Customer Registration</h2>

                        <p>
                        Register as a customer
                        and book society services.
                        </p>

                        <form action="/register" method="get">
                            <input type="hidden"
                                   name="role"
                                   value="Customer">

                            <button type="submit">
                                Register as Customer
                            </button>
                        </form>

                    </div>

                </div>

            </div>

        </div>

<script>
<script>
function changeLanguage() {

    var lang = document.getElementById("language").value;

    var mainTitle = document.querySelector(".left h1");
    var mainSubtitle = document.querySelector(".left h2");
    var mainText = document.querySelector(".left p");

    var boxTitles = document.querySelectorAll(".box h2");
    var boxTexts = document.querySelectorAll(".box p");
    var buttons = document.querySelectorAll(".box button");

    if (lang === "hi") {

        mainTitle.innerText = "सोसाइटी";
        mainSubtitle.innerText = "सेवा मार्केटप्लेस";
        mainText.innerHTML = "पड़ोसियों से जुड़ें।<br>सेवाएँ साझा करें।";

        boxTitles[0].innerText = "🔧 सेवा प्रदाता पंजीकरण";
        boxTexts[0].innerText = "सेवा प्रदाता के रूप में पंजीकरण करें और अपनी सेवाएँ प्रदान करें।";
        buttons[0].innerText = "सेवा प्रदाता के रूप में पंजीकरण करें";

        boxTitles[1].innerText = "👤 ग्राहक पंजीकरण";
        boxTexts[1].innerText = "ग्राहक के रूप में पंजीकरण करें और सोसाइटी की सेवाएँ बुक करें।";
        buttons[1].innerText = "ग्राहक के रूप में पंजीकरण करें";

    }

    else if (lang === "mr") {

        mainTitle.innerText = "सोसायटी";
        mainSubtitle.innerText = "सेवा मार्केटप्लेस";
        mainText.innerHTML = "शेजाऱ्यांशी जोडा.<br>सेवा सामायिक करा.";

        boxTitles[0].innerText = "🔧 सेवा प्रदाता नोंदणी";
        boxTexts[0].innerText = "सेवा प्रदाता म्हणून नोंदणी करा आणि तुमच्या सेवा द्या.";
        buttons[0].innerText = "सेवा प्रदाता म्हणून नोंदणी करा";

        boxTitles[1].innerText = "👤 ग्राहक नोंदणी";
        boxTexts[1].innerText = "ग्राहक म्हणून नोंदणी करा आणि सोसायटीच्या सेवा बुक करा.";
        buttons[1].innerText = "ग्राहक म्हणून नोंदणी करा";

    }

    else {

        mainTitle.innerText = "Society";
        mainSubtitle.innerText = "Service Marketplace";
        mainText.innerHTML = "Connect neighbours.<br>Share services.";

        boxTitles[0].innerText = "🔧 Provider Registration";
        boxTexts[0].innerText = "Register as a service provider and offer your services.";
        buttons[0].innerText = "Register as Provider";

        boxTitles[1].innerText = "👤 Customer Registration";
        boxTexts[1].innerText = "Register as a customer and book society services.";
        buttons[1].innerText = "Register as Customer";
    }
}
</script>
</script>

        </body>
        </html>

        """;

        send(exchange, html);
    }

    // ==============================
    // LOGIN PAGE
    // ==============================

    static void login(HttpExchange exchange)
            throws IOException {

        String method =
                exchange.getRequestMethod();

        if (method.equalsIgnoreCase("POST")) {

            Map<String,String> data =
                    readData(exchange);

            String name = data.get("name");
            String password = data.get("password");
            String role = data.get("role");

            for (User u : users) {

                if (u.name.equals(name)
                        && u.password.equals(password)
                        && u.role.equals(role)) {

                    if (role.equals("Customer")) {
                        redirect(exchange,
                                "/customer?user=" +
                                encode(name));
                    } else {
                        redirect(exchange,
                                "/provider?user=" +
                                encode(name));
                    }

                    return;
                }
            }

            send(exchange,
                    page(
                    "Login Failed",
                    """
                    <h2>Invalid login details</h2>
                    <a href="/login">Try Again</a>
                    """
                    ));

            return;
        }

        String html = """

        <html>
        <head>
        <title>Login</title>

        <style>

        body {
            background:#F7F5FF;
            font-family:Arial;
        }

        .box {
            width:420px;
            margin:70px auto;
            background:white;
            padding:40px;
            border-radius:12px;
            box-shadow:0 5px 20px #ddd;
        }

        input, select {
            width:100%;
            padding:13px;
            margin:8px 0 15px;
        }

        button {
            width:100%;
            padding:13px;
            background:#6D28D9;
            color:white;
            border:0;
            border-radius:5px;
        }

        </style>
        </head>

        <body>

        <div class="box">

        <h1>Sign In</h1>

        <form method="post">

        <label>Name</label>

        <input name="name"
               required>

        <label>Password</label>

        <input type="password"
               name="password"
               required>

        <label>Role</label>

        <select name="role">

            <option>Customer</option>
            <option>Provider</option>

        </select>

        <button>
            Sign In →
        </button>

        </form>

        <p>
        Don't have an account?
        <a href="/register">
        Register
        </a>
        </p>

        </div>

        </body>
        </html>

        """;

        send(exchange, html);
    }

    // ==============================
    // REGISTER
    // ==============================

    static void register(HttpExchange exchange)
            throws IOException {

        Map<String,String> queryData =
                query(exchange);

        String selectedRole =
                queryData.get("role");

        if (!"Provider".equals(selectedRole)
                && !"Customer".equals(selectedRole)) {
            selectedRole = "Customer";
        }

        if (exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            Map<String,String> data =
                    readData(exchange);

            String name = data.get("name");
            String phone = data.get("phone");
            String password = data.get("password");
            String role = data.get("role");

            for (User u : users) {

                if (u.name.equalsIgnoreCase(name)
                        && u.role.equals(role)) {

                    send(exchange,
                            page(
                                "Error",
                                """
                                <h2>User already exists.</h2>
                                <a href="/register?role=ROLE">
                                Try Again
                                </a>
                                """.replace("ROLE",
                                           encode(role))
                            ));

                    return;
                }
            }

            users.add(new User(
                    userId++,
                    name,
                    phone,
                    password,
                    role
            ));

            // Automatically open the correct dashboard after registration.
            if ("Provider".equals(role)) {
                redirect(exchange,
                        "/provider?user=" + encode(name));
            } else {
                redirect(exchange,
                        "/customer?user=" + encode(name));
            }

            return;
        }

        String title;
        String description;

        if ("Provider".equals(selectedRole)) {
            title = "Provider Registration";
            description =
                    "Create your provider account and start offering services.";
        } else {
            title = "Customer Registration";
            description =
                    "Create your customer account and start booking services.";
        }

        String html = """

        <html>

        <head>

        <title>TITLE</title>

        <style>

        body {
            background:#F7F5FF;
            font-family:Arial;
        }

        .box {
            width:450px;
            margin:50px auto;
            background:white;
            padding:40px;
            border-radius:12px;
            box-shadow:0 5px 20px #ddd;
        }

        h1 {
            color:#1E1B4B;
        }

        .info {
            color:#555;
            margin-bottom:25px;
        }

        input {
            width:100%;
            padding:13px;
            margin:7px 0 15px;
            border:1px solid #ddd;
            border-radius:6px;
        }

        button {
            width:100%;
            padding:13px;
            background:#2563EB;
            color:white;
            border:0;
            border-radius:5px;
            font-size:16px;
        }

        </style>

        </head>

        <body>

        <div class="box">

        <h1>TITLE</h1>

        <p class="info">
        DESCRIPTION
        </p>

        <form method="post">

        <input type="hidden"
               name="role"
               value="ROLE">

        <label>Full Name</label>

        <input name="name"
               required>

     <label>Phone Number</label>

<input type="tel"
       name="phone"
       pattern="[6-9][0-9]{9}"
       maxlength="10"
       placeholder="Enter 10 digit number"
       required>

<label>Email Address</label>

<input type="email"
       name="email"
       placeholder="example@gmail.com"
       required>

  <label>Address</label>

      <textarea name="address"
             rows="3"
             placeholder="Enter your address"
             required></textarea>
       <label>Password</label>

        <input type="password"
               name="password"
               required>

        <button>
        Register
        </button>

        </form>

        </div>

        </body>

        </html>

        """
        .replace("TITLE", title)
        .replace("DESCRIPTION", description)
        .replace("ROLE", selectedRole);

        send(exchange, html);
    }

    // ==============================
    // CUSTOMER DASHBOARD
    // ==============================

    static void customer(HttpExchange exchange)
            throws IOException {

        Map<String,String> data =
                query(exchange);

        String user =
                data.get("user");

        StringBuilder html =
                new StringBuilder();

        html.append("""

        <html>

        <head>

        <title>Customer Dashboard</title>

        <style>

        body {
            margin:0;
            font-family:Arial;
            background:#F7F5FF;
            color:#1E1B4B;
        }

        .header {
            background:#1E1B4B;
            color:white;
            padding:22px 40px;
        }

        .header h1 {
            display:inline;
        }

        .content {
            padding:35px;
        }

        .card {
            background:white;
            padding:22px;
            margin:15px 0;
            border-radius:10px;
            box-shadow:0 3px 12px #ddd;
        }

        .price {
            color:#059669;
            font-weight:bold;
            font-size:20px;
        }

        button {
            background:#6D28D9;
            color:white;
            border:0;
            padding:10px 18px;
            border-radius:5px;
        }

        a {
            color:#6D28D9;
            font-weight:bold;
        }

        </style>

        </head>

        <body>

        <div class="header">

        <h1>🏘️ Society Service Marketplace</h1>

        </div>

        <div class="content">

        <h2>
        Welcome, USER 👋
        </h2>

        <p>
        Browse services available in your society.
        </p>

        <a href="/bookings?user=USER">
        📅 My Bookings
        </a>

        <hr>

        <h2>🔍 Available Services</h2>

        """.replace("USER", encode(user)));

        for (Service s : services) {

            html.append(
                    "<div class='card'>");

            html.append(
                    "<h2>"
                    + escape(s.name)
                    + "</h2>");

            html.append(
                    "<p>"
                    + escape(s.description)
                    + "</p>");

            html.append(
                    "<p>👤 Provider: "
                    + escape(s.provider)
                    + "</p>");

            html.append(
                    "<p>📅 "
                    + escape(s.availability)
                    + "</p>");

            html.append(
                    "<p class='price'>₹"
                    + escape(s.price)
                    + "</p>");

            html.append(
                    "<a href='/book?id="
                    + s.id
                    + "&user="
                    + encode(user)
                    + "'>");

            html.append(
                    "<button>Book Now →</button>");

            html.append("</a>");

            html.append("</div>");
        }

        html.append("""

        </div>

        </body>
        </html>

        """);

        send(exchange, html.toString());
    }

    // ==============================
    // PROVIDER DASHBOARD
    // ==============================

    static void provider(HttpExchange exchange)
            throws IOException {

        Map<String,String> data =
                query(exchange);

        String user =
                data.get("user");

        StringBuilder html =
                new StringBuilder();

        html.append("""

        <html>

        <head>

        <title>Provider Dashboard</title>

        <style>

        body {
            margin:0;
            font-family:Arial;
            background:#F7F5FF;
        }

        .header {
            background:#1E1B4B;
            color:white;
            padding:25px 40px;
        }

        .content {
            padding:35px;
        }

        .box {
            background:white;
            padding:25px;
            border-radius:10px;
            margin-bottom:30px;
            box-shadow:0 3px 12px #ddd;
        }

        input {
            width:100%;
            padding:12px;
            margin:8px 0 15px;
        }

        button {
            background:#6D28D9;
            color:white;
            padding:12px 20px;
            border:0;
            border-radius:5px;
        }

        .service {
            background:white;
            padding:20px;
            margin:15px 0;
            border-radius:10px;
        }

        a {
            color:#6D28D9;
            font-weight:bold;
        }

        </style>

        </head>

        <body>

        <div class="header">

        <h1>🛠️ Service Provider Dashboard</h1>

        </div>

        <div class="content">

        <h2>
        Welcome, USER 👋
        </h2>

        <div class="box">

        <h2>📋 List a New Service</h2>

        <form action="/addservice"
              method="post">

        <input type="hidden"
               name="provider"
               value="USER">

        <label>Service Name</label>

        <input name="name"
               required>

        <label>Description</label>

        <input name="description">

        <label>Price (₹)</label>

        <input name="price"
               required>

        <label>Availability</label>

        <input name="availability">

        <button>
        Publish Service →
        </button>

        </form>

        </div>

        <h2>🔖 My Services</h2>

        """.replace("USER", encode(user)));

        for (Service s : services) {

            if (s.provider.equals(user)) {

                html.append("""

                <div class="service">

                <h2>
                SERVICE
                </h2>

                <p>
                DESCRIPTION
                </p>

                <b>
                ₹PRICE
                </b>

                <p>
                Availability: AVAIL
                </p>

                </div>

                """
                .replace("SERVICE",
                        escape(s.name))
                .replace("DESCRIPTION",
                        escape(s.description))
                .replace("PRICE",
                        escape(s.price))
                .replace("AVAIL",
                        escape(s.availability)));
            }
        }

        html.append("""

        <hr>

        <h2>📅 My Bookings</h2>

        """);

        for (Booking b : bookings) {

            if (b.provider.equals(user)) {

                html.append("""

                <div class="service">

                <h3>
                SERVICE
                </h3>

                <p>
                Customer: CUSTOMER
                </p>

                <p>
                Date: DATE
                </p>

                <p>
                Status: STATUS
                </p>

                <a href="/action?id=ID&action=confirm">
                <button>Confirm</button>
                </a>

                <a href="/action?id=ID&action=reject">
                <button>Reject</button>
                </a>

                </div>

                """
                .replace("SERVICE",
                        escape(b.service))
                .replace("CUSTOMER",
                        escape(b.customer))
                .replace("DATE",
                        escape(b.date))
                .replace("STATUS",
                        escape(b.status))
                .replace("ID",
                        String.valueOf(b.id)));
            }
        }

        html.append("""
        </div>

        </body>

        </html>
        """);

        send(exchange, html.toString());
    }

    // ==============================
    // ADD SERVICE
    // ==============================

    static void addService(HttpExchange exchange)
            throws IOException {

        Map<String,String> data =
                readData(exchange);

        services.add(new Service(
                serviceId++,
                data.get("provider"),
                data.get("name"),
                data.get("description"),
                data.get("price"),
                data.get("availability")
        ));

        redirect(exchange,
                "/provider?user="
                + encode(data.get("provider")));
    }

    // ==============================
    // BOOK SERVICE
    // ==============================

    static void book(HttpExchange exchange)
            throws IOException {

        Map<String,String> data =
                query(exchange);

        int id =
                Integer.parseInt(data.get("id"));

        String user =
                data.get("user");

        Service selected = null;

        for (Service s : services) {

            if (s.id == id) {
                selected = s;
                break;
            }
        }

        if (selected == null) {

            send(exchange,
                    page(
                    "Error",
                    "<h2>Service not found.</h2>"
                    ));

            return;
        }

        if (exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            Map<String,String> form =
                    readData(exchange);

            String date =
                    form.get("date");

         String time =
        form.get("hour") + ":" +
        form.get("minute") + " " +
        form.get("ampm");

            bookings.add(
                    new Booking(
                            bookingId++,
                            user,
                            selected.name,
                            selected.provider,
                            date + " at " + time
                    )
            );

            send(exchange,
                    page(
                    "Booking Sent",
                    """
                    <h2>Booking Request Sent ✅</h2>

                    <p>
                    Your request has been sent to the provider.
                    </p>

                    <a href="/customer?user=USER">
                    Back to Dashboard
                    </a>
                    """
                    .replace(
                    "USER",
                    encode(user))
                    ));

            return;
        }

        String html = """

        <html>

        <head>

        <title>Book Service</title>

        <style>

        body {
            font-family:Arial;
            background:#F7F5FF;
        }

        .box {
            width:420px;
            margin:80px auto;
            background:white;
            padding:35px;
            border-radius:10px;
            box-shadow:0 3px 15px #ddd;
        }

        input {
            width:100%;
            padding:12px;
            margin:10px 0 20px;
        }

        button {
            width:100%;
            padding:12px;
            background:#6D28D9;
            color:white;
            border:0;
            border-radius:5px;
        }

        </style>

        </head>

        <body>

        <div class="box">

        <h2>
        📅 Book SERVICE
        </h2>

        <p>
        Provider: PROVIDER
        </p>

        <p>
        Price: ₹PRICE
        </p>

        <form method="post">

        <label>Select Date</label>

        <input type="date"
               name="date"
               required>

        
<label>Select Time</label>

<div style="display:flex; gap:8px; align-items:center;">

    <select name="hour" required
            style="width:90px; height:40px;">
        <option value="">Hour</option>
        <option value="01">01</option>
        <option value="02">02</option>
        <option value="03">03</option>
        <option value="04">04</option>
        <option value="05">05</option>
        <option value="06">06</option>
        <option value="07">07</option>
        <option value="08">08</option>
        <option value="09">09</option>
        <option value="10">10</option>
        <option value="11">11</option>
        <option value="12">12</option>
    </select>

    <select name="minute" required
            style="width:90px; height:40px;">
        <option value="">Minute</option>
        <option value="00">00</option>
        <option value="15">15</option>
        <option value="30">30</option>
        <option value="45">45</option>
    </select>

    <select name="ampm" required
            style="width:80px; height:40px;">
        <option value="AM">AM</option>
        <option value="PM">PM</option>
    </select>

</div>
        <button>
        Confirm Booking
        </button>

        </form>

        </div>

        </body>

        </html>

        """
        .replace(
                "SERVICE",
                escape(selected.name))
        .replace(
                "PROVIDER",
                escape(selected.provider))
        .replace(
                "PRICE",
                escape(selected.price));

        send(exchange, html);
    }

    // ==============================
    // BOOKINGS
    // ==============================

    static void bookings(HttpExchange exchange)
            throws IOException {

        Map<String,String> data =
                query(exchange);

        String user =
                data.get("user");

        StringBuilder html =
                new StringBuilder();

        html.append("""

        <html>

        <head>

        <title>My Bookings</title>

        <style>

        body {
            font-family:Arial;
            background:#F7F5FF;
            padding:40px;
        }

        .booking {
            background:white;
            padding:25px;
            margin:15px 0;
            border-radius:10px;
            box-shadow:0 3px 10px #ddd;
        }

        .status {
            color:#6D28D9;
            font-weight:bold;
        }

        </style>

        </head>

        <body>

        <h1>📅 My Bookings</h1>

        """);

        for (Booking b : bookings) {

            if (b.customer.equals(user)) {

                html.append("""

                <div class="booking">

                <h2>SERVICE</h2>

                <p>
                Provider: PROVIDER
                </p>

                <p>
                Date: DATE
                </p>

                <p class="status">
                Status: STATUS
                </p>

                </div>

                """
                .replace(
                        "SERVICE",
                        escape(b.service))
                .replace(
                        "PROVIDER",
                        escape(b.provider))
                .replace(
                        "DATE",
                        escape(b.date))
                .replace(
                        "STATUS",
                        escape(b.status)));
            }
        }

        html.append("""

        <br>

        <a href="javascript:history.back()">
        ← Back
        </a>

        </body>

        </html>

        """);

        send(exchange, html.toString());
    }

    // ==============================
    // CONFIRM / REJECT
    // ==============================

    static void bookingAction(
            HttpExchange exchange)
            throws IOException {

        Map<String,String> data =
                query(exchange);

        int id =
                Integer.parseInt(data.get("id"));

        String action =
                data.get("action");

        for (Booking b : bookings) {

            if (b.id == id) {

                if (action.equals("confirm")) {
                    b.status = "Confirmed";
                }

                if (action.equals("reject")) {
                    b.status = "Rejected";
                }

                redirect(exchange,
                        "/provider?user="
                        + encode(b.provider));

                return;
            }
        }

        send(exchange,
                page(
                "Error",
                "<h2>Booking not found</h2>"
                ));
    }

    // ==============================
    // HELPER FUNCTIONS
    // ==============================

    static Map<String,String> readData(
            HttpExchange exchange)
            throws IOException {

        InputStream input =
                exchange.getRequestBody();

        String body =
                new String(
                        input.readAllBytes(),
                        StandardCharsets.UTF_8);

        return parse(body);
    }

    static Map<String,String> query(
            HttpExchange exchange) {

        String q =
                exchange.getRequestURI()
                        .getRawQuery();

        return parse(q == null ? "" : q);
    }

    static Map<String,String> parse(
            String data) {

        Map<String,String> map =
                new HashMap<>();

        if (data == null ||
                data.isEmpty()) {

            return map;
        }

        for (String pair :
                data.split("&")) {

            String[] parts =
                    pair.split("=", 2);

            if (parts.length == 2) {

                try {

                    map.put(
                            URLDecoder.decode(
                                    parts[0],
                                    "UTF-8"),

                            URLDecoder.decode(
                                    parts[1],
                                    "UTF-8")
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }

    static String encode(String value) {

        try {

            return java.net.URLEncoder
                    .encode(
                            value == null
                                    ? ""
                                    : value,
                            "UTF-8");

        } catch (Exception e) {

            return "";
        }
    }

    static String escape(String text) {

        if (text == null)
            return "";

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    static void send(
            HttpExchange exchange,
            String html)
            throws IOException {

        byte[] response =
                html.getBytes(
                        StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set(
                "Content-Type",
                "text/html; charset=UTF-8"
                );

        exchange.sendResponseHeaders(
                200,
                response.length
        );

        OutputStream os =
                exchange.getResponseBody();

        os.write(response);
        os.close();
    }

    static void redirect(
            HttpExchange exchange,
            String location)
            throws IOException {

        exchange.getResponseHeaders()
                .set("Location", location);

        exchange.sendResponseHeaders(
                302,
                -1
        );

        exchange.close();
    }

    static String page(
            String title,
            String content) {

        return """

        <html>

        <head>

        <title>TITLE</title>

        <style>

        body {
            font-family:Arial;
            background:#F7F5FF;
            text-align:center;
            padding:80px;
            color:#1E1B4B;
        }

        .box {
            background:white;
            padding:40px;
            border-radius:12px;
            max-width:600px;
            margin:auto;
            box-shadow:0 3px 15px #ddd;
        }

        a {
            color:#6D28D9;
            font-weight:bold;
        }

        </style>

        </head>

        <body>

        <div class="box">

        CONTENT

        </div>

        </body>

        </html>

        """
        .replace("TITLE", title)
        .replace("CONTENT", content);
    }
}