type Person object {
    public string name = "default first name";
    public string lname;
    public map adrs;
    public int age = 999;

    __init (name, adrs, age) {

    }
};

type ObjectField object {
    public string key;

    __init (key) {

    }
};

function testExpressionAsStructIndex () returns (string) {
    ObjectField nameField = new ("name");
    Person emp = new ("Jack", {"country":"USA", "state":"CA"}, 25);
    return emp[nameField.key];
}
