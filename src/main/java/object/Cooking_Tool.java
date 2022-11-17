package object;

import jakarta.persistence.*;

@Entity
public class Cooking_Tool {

    @Id
    @Column(name = "idCookingTool")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    public Cooking_Tool(){

    }

    public Cooking_Tool(String name){
        this.name = name;
    }
}
