package object;

import jakarta.persistence.*;

@Entity
public class Part_Of_Day {

    @Id
    @Column(name = "idPartOfDay")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    public Part_Of_Day(){

    }

    public Part_Of_Day(String name){
        this.name = name;
    }
}
