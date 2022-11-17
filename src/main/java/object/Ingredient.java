package object;

import jakarta.persistence.*;

@Entity
public class Ingredient {

    @Id
    @Column(name = "idIngredient")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    public Ingredient(){

    }

    public Ingredient(String name){
        this.name = name;
    }
}
