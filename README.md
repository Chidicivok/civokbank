CIVOK BANK
A spring boot banking backend application built to model core banking operations while applying secure API design, layered architecture
and simulate raal world banking transaction processing

Features
- Customer registration and account management
- Creation of multiple customer account types (NGN and USD)
- Internal transfers
- External transfers
- Withdrawal
- Deposits
- Multi-currency transactions using exchnage rates
- OTP generation
- OTP verification
- Role based authorization for customers and administrators
- Account Management
- Banking charger and FX commissions processing
- CIVOK Bank earnings tracking
- Secure password storage using BCrypt
- secure authentication and authorization
- Ownership based control for customer accounts
- DTO based access control for requests and responses
- Input validation and global exception handling


Technologies
- Java 21
- Spring Boot
- Spring Security
- Spring Web
- Spring Data JPA
- Maven
- MySQL workbench
- Lombok
- Postman
- Validation


Architecture
Civok bank solution follows a layered architecture
Controller -> Service -> Repository -> Database
Service interfaces seperate business contracts from their implementation while spring dependency injection manages application dependencies
and promotes loose coupling between application components

Project is still under developemnt
