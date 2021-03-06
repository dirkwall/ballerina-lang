import ballerina/http;
import ballerina/mime;

listener http:MockListener testEP = new(9090);

type Person record {|
    string name;
    int age;
|};

type Stock record {|
    int id;
    float price;
|};

service echo on testEP {

    @http:ResourceConfig {
        body: "person"
    }
    resource function body1(http:Caller caller, http:Request req, string person) {
        json responseJson = { "Person": person };
        checkpanic caller->respond(untaint responseJson);
    }

    @http:ResourceConfig {
        methods: ["POST"],
        path: "/body2/{key}",
        body: "person"
    }
    resource function body2(http:Caller caller, http:Request req, string key, string person) {
        json responseJson = { Key: key, Person: person };
        checkpanic caller->respond(untaint responseJson);
    }

    @http:ResourceConfig {
        methods: ["GET", "POST"],
        body: "person"
    }
    resource function body3(http:Caller caller, http:Request req, json person) {
        json name = untaint person.name;
        json team = untaint person.team;
        checkpanic caller->respond({ Key: name, Team: team });
    }

    @http:ResourceConfig {
        methods: ["POST"],
        body: "person"
    }
    resource function body4(http:Caller caller, http:Request req, xml person) {
        string name = untaint person.getElementName();
        string team = untaint person.getTextValue();
        checkpanic caller->respond({ Key: name, Team: team });
    }

    @http:ResourceConfig {
        methods: ["POST"],
        body: "person"
    }
    resource function body5(http:Caller caller, http:Request req, byte[] person) {
        string name = untaint mime:byteArrayToString(person, "UTF-8");
        checkpanic caller->respond({ Key: name });
    }

    @http:ResourceConfig {
        methods: ["POST"],
        body: "person"
    }
    resource function body6(http:Caller caller, http:Request req, Person person) {
        string name = untaint person.name;
        int age = untaint person.age;
        checkpanic caller->respond({ Key: name, Age: age });
    }

    @http:ResourceConfig {
        methods: ["POST"],
        body: "person"
    }
    resource function body7(http:Caller caller, http:Request req, Stock person) {
        checkpanic caller->respond(());
    }

    @http:ResourceConfig {
        methods: ["POST"],
        body: "persons"
    }
    resource function body8(http:Caller caller, http:Request req, Person[] persons) {
        var jsonPayload = json.convert(persons);
        if (jsonPayload is json) {
            checkpanic caller->respond(untaint jsonPayload);
        } else {
            checkpanic caller->respond(untaint <string> jsonPayload.detail().message);
        }
    }
}
