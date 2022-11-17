package object;

import jakarta.persistence.*;

@Entity
public class Type {

    @Id
    @Column(name = "idType")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    public Type(){

    }

    public Type(String name){
        this.name = name;
    }
}
