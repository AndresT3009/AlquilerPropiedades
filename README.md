# AlquilerPropiedades
Prueba tecnica de Quind - Sistema de Alquiler de propiedades

Property Rental System API

Overview

This system provides endpoints to manage property rentals. It allows users to:

Retrieve all properties.

Search for properties within a value range.

Add new properties.

Update existing properties.

Delete properties (with conditions).

Reserve properties for clients.

Features

Logging: Comprehensive logs for tracking and debugging.

Validation: Input validations to ensure data integrity.

Transactional Operations: Key operations are transactional to maintain consistency.

Error Handling: Detailed responses for various error scenarios.

Prerequisites

Java: Version 17 or higher.

Spring Boot: Version 3.3.3.

Database: PostgreSQL (or any compatible database).

Maven: For managing dependencies.

Docker (optional): For containerization.

Endpoints

1. Retrieve All Properties

GET /api/properties/all

Description: Fetches all available properties.

Response:

200 OK: List of properties.

500 Internal Server Error: Unexpected error.

2. Search Properties by Value Range

GET /api/properties/search

Parameters:

minValue (int): Minimum property value.

maxValue (int): Maximum property value.

Response:

200 OK: Properties matching the range.

400 Bad Request: Invalid range values.

404 Not Found: No properties found in the range.

3. Add New Property

POST /api/properties/save

Parameters:

propertyName (String): Name of the property.

city (String): City where the property is located.

address (String): Address of the property.

mortgageValue (int): Property’s mortgage value.

image (String): URL of the property image.

Response:

201 Created: Property created successfully.

403 Forbidden: Validation errors or constraints violations.

406 Not Acceptable: Location not allowed.

409 Conflict: Property already exists.

4. Delete Property

PATCH /api/properties/delete

Parameters:

propertyName (String): Name of the property to delete.

Conditions:

The property must not be rented.

The property must be created more than 30 days ago.

Response:

200 OK: Property deleted successfully.

403 Forbidden: Property cannot be deleted.

404 Not Found: Property not found.

5. Reserve Property

POST /api/properties/reserve

Parameters:

propertyId (Long): ID of the property to reserve.

clientDocument (Long): Document ID of the client reserving the property.

Response:

200 OK: Property reserved successfully.

403 Forbidden: Property or client does not exist, or property not available.

500 Internal Server Error: Unexpected error.

6. Update Property

PATCH /api/properties/update

Parameters:

propertyName (String): Current name of the property.

newPropertyName (String): New name for the property.

available (String): Availability status (“yes” or “no”).

city (String): New city for the property.

address (String): New address for the property.

mortgageValue (int): Updated mortgage value.

image (String): New image URL.

Response:

200 OK: Property updated successfully.

403 Forbidden: Validation errors or constraints violations.

404 Not Found: Property not found.

500 Internal Server Error: Unexpected error.

Logging

This API uses SLF4J with log levels:

INFO: For successful operations.

WARN: For validation warnings or potential issues.

ERROR: For unexpected exceptions.

Error Responses

Standard error responses include:

ErrorResponse:

{
"message": "Description of the error",
"details": "Detailed explanation or stack trace"
}

Running the Application

Clone the repository.

Configure the application.properties file for database connection:

spring.datasource.url=jdbc:postgresql://localhost:5432/property_rental
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

Build the project:

mvn clean install

Run the application:

java -jar target/property-rental-system.jar

Testing

Use tools like Postman or cURL to test the endpoints.

Future Enhancements

Add user authentication and authorization.

Implement property recommendations based on user preferences.

Enhance search functionality with filters like city, size, and amenities.

Contributors

[Your Name] - [Your Role]

License

This project is licensed under the MIT License.