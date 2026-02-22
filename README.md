# Spring Boot POS System – IJSE

**Student Name:** Akila Abeysekara  
**Batch:** GDSE74  
**Institute:** IJSE – Institute of Software Engineering

## Project Description

This is a **Point of Sale (POS)** system developed as part of the Graduate Diploma in Software Engineering program at IJSE.

- **Backend**: Spring Boot  
- **Frontend**: HTML, CSS, JavaScript, jQuery + AJAX  
- **Communication**: RESTful APIs  
- **Database**: MySQL  

The system enables users to manage **customers**, **items**, **orders**, and **payments** through a clean web interface.

## 🎥 System Demonstration Video

https://youtu.be/WtQB0ND338k

### Embedded Video

<h2 align="center">🎥 Spring Boot POS System – IJSE</h2>

[![Watch the Video](https://img.youtube.com/vi/WtQB0ND338k/maxresdefault.jpg)](https://youtu.be/WtQB0ND338k)
> Video demonstrates: login, customer/item CRUD, order placement, payment recording, search functionality, and REST API interaction via browser network tab.

## Technologies Used

### Backend
- Spring Boot  
- Spring Data JPA  
- Hibernate  
- REST API  
- MySQL  
- Maven  

### Frontend
- HTML5  
- CSS3  
- JavaScript (ES6+)  
- jQuery  
- AJAX  

## System Features

- User Login  
- Customer Management (CRUD)  
- Item Management (CRUD)  
- Order Management (place orders, select customer & items, auto-generate order ID)  
- View & Search Payment History (by payment ID, order ID, customer name)  
- Input validation & global exception handling  
- Clean success/error responses to frontend  

## System Architecture

**Layered Architecture**


Flow:
User Action
↓
JavaScript
↓
AJAX Request
↓
Spring Boot Controller
↓
Service Layer
↓
Database
↓
Response back to Frontend


## Project Structure
POS-System
│
├── POS_Back_End
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── exception
│
├── POS_Front_End
│   ├── pages
│   ├── css
│   └── js

---
**Akila Abeysekara**  
Graduate Diploma in Software Engineering  
IJSE – Institute of Software Engineering  
February 2026
