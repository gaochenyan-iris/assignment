Assignment
The assessment consists of an API to be used for opening a new “current account” of already existing customers.

Requirements of the Assignment
• The API will expose an endpoint which accepts the user information (customerID, initialCredit).
• Once the endpoint is called, a new account will be opened connected to the user whose ID is customerID.
• Also, if initialCredit is not 0, a transaction will be sent to the new account.
• Another endpoint will output the user information showing Name, Surname, balance, and transactions of the accounts.

Built with
Spark - The web framework used
jsondb - the database used

Running the assignment
Prerequisites: Install java 8 and Maven 3.6.1
1. Run unit test, run $mvn test
2. Build the assignment, run $mvn package
3. Run integration test
• Execute the assignment, run $java -cp target/myApplication-0.0.1-SNAPSHOT.jar:target/lib/* app.App
• Curl command examples for integration test. 
 $curl -H "Content-Type:application/json" -X POST -d '{"customerId":"0001", "initialCredit":"2000"}' http://localhost:4567/account
 $curl http://localhost:4567/customer/0001
Notes: As there is no endpoint for creating new customer, the assignment has initiated with three customers whose customerIDs are 0001, 0002, 0003.
