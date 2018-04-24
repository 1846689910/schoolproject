package utils.patterns;

public class Builder {
}
/** Builder Pattern */
class Person {
    String name;
    int age;
    String gender;
    String nationality;
    String degree;

    public Person(PersonInner inner) {
        this.name = inner.name;
        this.age = inner.age;
        this.gender = inner.gender;
        this.nationality = inner.nationality;
        this.degree = inner.degree;
    }

    static class PersonInner {
        String name;
        int age;
        String gender;
        String nationality;
        String degree;

        public PersonInner(String name, int age) {  // name and age are basic required fields that cannot be vacant
            // other properties could be added selectively
            this.name = name;
            this.age = age;
        }

        public PersonInner gender(String gender) {
            this.gender = gender;
            return this;
        }

        public PersonInner nationality(String nationality) {
            this.nationality = nationality;
            return this;
        }

        public PersonInner degree(String degree) {
            this.degree = degree;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }
}