<a href="#">
    <img width="100%" src="https://capsule-render.vercel.app/api?type=waving&color=9745f5&height=120&section=header" />
</a>

![Typing SVG](https://readme-typing-svg.herokuapp.com/?color=9745f5&size=35&center=true&vCenter=true&width=1000&lines=Meet+the+Team;Ismail+Enniou;Malika+El+Abderrahmani;Fatima+El+Asri;Welcome+to+Our+Project!+:)

# Private School Management System

## Project Overview

This project is aimed at developing a **comprehensive management system for private schools**. The system is designed to streamline various administrative and educational tasks, providing an efficient platform for managing student enrollment, class scheduling, grade management, attendance tracking, and more.

Our platform is composed of **six microservices** that work together to deliver a seamless and modular management experience:  

1. **IAM-MS (Identity and Access Management):** Handles authentication, authorization, and role-based access control using Keycloak.  
2. **Operations-MS:** Manages school events, extracurricular activities, scheduling, and archives.  
3. **Academic-MS:** Covers academic features such as class management, grading, attendance, and curriculum tracking.  
4. **Finance-MS:** Manages school finances, including tuition payments, expense tracking, and budget reports.  
5. **HR-MS:** Handles human resources management, including payroll, hiring, and employee evaluations.  
6. **Messaging-MS:** Provides communication tools for announcements, messaging, and notifications.  

## Objectives

- Develop a **user-friendly** and **scalable** management system for private schools.  
- Implement key features to support administrators, teachers, students, and parents.  
- Ensure the system is **secure, performant**, and capable of handling a large number of users.  

## Tools and Technologies

- **Backend Framework:** Spring Boot  
- **Frontend Framework:** React.js with Tailwind CSS  
- **Database:** MySQL  
- **Authentication & Authorization:** Keycloak  
- **Deployment:** VPS server with Docker and Nginx  

## Team Members

<div style="display: flex; justify-content: center; align-items: center; border: 1px solid #ddd; border-radius: 10px; background-color: #f5f5f5; padding: 20px;">
  <div style="flex: 1; text-align: center; padding: 20px;">
    <h3>Ismail Enniou</h3>
    <img src="https://cdn.intra.42.fr/users/3378988aa2fe852a0b50ca7efc973232/small_ien-niou.jpg" alt="Ismail Enniou" style="width: 100px; height: 100px; border-radius: 50%; object-fit: cover;">
    <p><strong>Role:</strong> Project Manager</p>
    <p><strong>Expertise:</strong> Agile Methodologies, Team Leadership</p>
  </div>
  <div style="flex: 1; text-align: center; padding: 20px;">
    <h3>Malika El Abderrahmani</h3>
    <img src="https://cdn.intra.42.fr/users/6f21bbadf2cdf0ec18452ed96df9816a/small_mel-abde.jpg" alt="Malika El Abderrahmani" style="width: 100px; height: 100px; border-radius: 50%; object-fit: cover;">
    <p><strong>Role:</strong> Frontend Developer</p>
    <p><strong>Expertise:</strong> React.js, Tailwind CSS, UI/UX Design</p>
  </div>
  <div style="flex: 1; text-align: center; padding: 20px;">
    <h3>Fatima El Asri</h3>
    <img src="https://cdn.intra.42.fr/users/1773980665edff85923669726051d06a/small_fel-asri.jpg" alt="Fatima El Asri" style="width: 100px; height: 100px; border-radius: 50%; object-fit: cover;">
    <p><strong>Role:</strong> Backend Developer</p>
    <p><strong>Expertise:</strong> Java Spring Boot, MySQL, API Design</p>
  </div>
</div>

## Features of HR-MS

The **HR-MS** microservice is responsible for managing **Human Resources** for the school. Key features include:

- **Staff Profile Management**:
  - Manage and store staff information (teachers, admins, support staff).
  - Update and maintain employee details (name, contact, role, etc.).

- **Contract Management**:
  - Create, update, and manage staff contracts.
  - Allows for contract renewals, terminations, or modifications.

- **Leave Management**:
  - Staff can request leave, and admins can approve or deny requests.
  - Tracks leave history and balance for each employee.

- **Performance Evaluations**:
  - Track and manage performance evaluations for staff members.
  - Allows supervisors to rate and provide feedback on employee performance.

- **Payroll and Salary Management**:
  - Integration with **Finance-MS** for payroll management, salary details, and deductions.
  - Tracks payments and generates payroll reports.

- **CNSS Declarations**:
  - Handles CNSS (National Social Security Fund) declarations for staff.
  - Generates and submits CNSS reports as required by law.

- **HR Documentation**:
  - Stores and manages HR-related documentation such as contracts, evaluations, and declarations.

## Installation

To set up HR-MS locally, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Malikaelabderrahmani/rh-ms.git

## Repository Links

Below are the repositories for the six microservices:  

1. **[IAM-MS Repository](#)**  
2. **[Operations-MS Repository](#)**  
3. **[Academic-MS Repository](#)**  
4. **[Finance-MS Repository](#)**  
5. **[HR-MS Repository](#)**  
6. **[Messaging-MS Repository](#)**  

## Deployment

This microservice will be deployed using:  
- **Docker** for containerization.  
- **Nginx** as a reverse proxy.  
- **VPS server** for hosting.  

## Contributing

We welcome contributions to the **Private School Management System**! If you're interested in contributing, please follow these steps:  

1. Fork the repository.  
2. Create a new branch for your feature or bug fix.  
3. Submit a pull request for review.  

## License

This project is licensed under the [MIT License](LICENSE).  

---
