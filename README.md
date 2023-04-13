# ReadingIsGood
 
<h3>Definition</h3>

ReadingIsGood is an online books retail firm which operates only on the Internet. Main target of ReadingIsGood is to deliver books from its one centralized warehouse to their customers within the same day. That is why stock consistency is the first priority for their vision operations.

<hr />

<h3>Capabilities</h3>
• Registering New Customer<br />
• Placing a new order<br />
• Tracking the stock of books<br />
• List all orders of the customer<br />
• Viewing the order details<br />
• Query Monthly Statistics<br />

<hr />

<h3>Tech Stack</h3>
ReadingIsGood is running as a backend service and tech component below was used while developing.<br /><br />
• Java 17<br />
• Spring Boot v.3.1.0<br />
• H2 Database<br />
• Maven<br />
• springdoc-openapi v.2.1.0<br />
• Spring Security<br />
• HTTP Basic Authentication<br />
• Docker<br />

<hr />

<h3>Run The Application</h3>
Execute command: <code>docker container run -d -p 8181:8181 manincodeinn/reading-is-good:0.0.1</code><br /><br />
<i>
Image will be pulled automatically then container will be run. The application will run on port 8181.</i>

<hr />

<h3>Authentication</h3>
<code>Username: getir</code><br />
<code>Password: getir</code>

<hr />

<h3>Test The Application</h3>
• Postman collection: <code>readingIsGood/src/test/postman/reading-is-good.postman_collection.json</code> <br/><br/>
• Open UI: <code>http://localhost:8181/swagger-ui.html</code><br/>

<hr />

<h3>Development Details</h3>

<h4>Starting Project</h4>
Spring Initializr was used to create the project. <code>https://start.spring.io</code><br/><br/>

<h4>RestAPIs</h4>

Swagger UI can be examine for RestAPIs details.<br/>

![image](https://user-images.githubusercontent.com/35554100/231875463-b7dbea59-e7fb-473c-863f-19e9b58a8948.png)<br/><br/>

<h4>Unit Test</h4>
Using the unit tests, the following coverage rates were obtained.<br/><br/>

![image](https://user-images.githubusercontent.com/35554100/231880729-cede4697-21c3-4cec-b5cc-83216fde4656.png)
