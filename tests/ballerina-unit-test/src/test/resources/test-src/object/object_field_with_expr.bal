
function testFieldWithExpr() returns (int, string) {
    Person p = new;
    return (p.ep.pp, p.ep.name);
}


type Person object {
    public int age = 90;
    public Employee ep = new(88, "sanjiva");
};


type Employee object {
    public int pp;
    public string name;

    __init (pp, name) {

    }
};
