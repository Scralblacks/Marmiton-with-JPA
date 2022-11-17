package object;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Recipe {

    @Id
    @Column(name = "idRecipe")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String a_name;
    @Column(name = "instruction")
    private String b_instruction;
    @Column(name = "listIdIngredient")
    private String c_listIdIngredient;
    @Column(name = "listIdCookingTool")
    private String d_listIdCookingTool;
    @Column(name = "description")
    private String f_description;
    @Column(name = "whenLastCooked")
    private LocalDate e_whenLastCooked;

    @OneToOne
    @JoinColumn(name = "idType")
    private Type g_idType;

    @OneToOne
    @JoinColumn(name = "idPartOfDay")
    private Part_Of_Day h_idPartOfDay;

    public Recipe(){

    }

    public Recipe(long id, String name, String instruction, String listIdIngredient, String listIdCookingTool, LocalDate whenLastCooked, String description){
        this.id = id;
        this.a_name = name;
        this.b_instruction = instruction;
        this.c_listIdIngredient = listIdIngredient;
        this.d_listIdCookingTool = listIdCookingTool;
        this.e_whenLastCooked = whenLastCooked;
        this.f_description = description;
    }

    public Recipe(String name, String instruction, String listIdIngredient, String listIdCookingTool, LocalDate whenLastCooked, String description, Type idType, Part_Of_Day IdPartOfDay){
        this.a_name = name;
        this.b_instruction = instruction;
        this.c_listIdIngredient = listIdIngredient;
        this.d_listIdCookingTool = listIdCookingTool;
        this.e_whenLastCooked = whenLastCooked;
        this.f_description = description;
        this.g_idType = idType;
        this.h_idPartOfDay = IdPartOfDay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        this.a_name = a_name;
    }

    public String getB_instruction() {
        return b_instruction;
    }

    public void setB_instruction(String b_instruction) {
        this.b_instruction = b_instruction;
    }

    public String getC_listIdIngredient() {
        return c_listIdIngredient;
    }

    public void setC_listIdIngredient(String c_listIdIngredient) {
        this.c_listIdIngredient = c_listIdIngredient;
    }

    public String getD_listIdCookingTool() {
        return d_listIdCookingTool;
    }

    public void setD_listIdCookingTool(String d_listIdCookingTool) {
        this.d_listIdCookingTool = d_listIdCookingTool;
    }

    public String getF_description() {
        return f_description;
    }

    public void setF_description(String f_description) {
        this.f_description = f_description;
    }

    public LocalDate getE_whenLastCooked() {
        return e_whenLastCooked;
    }

    public void setE_whenLastCooked(LocalDate e_whenLastCooked) {
        this.e_whenLastCooked = e_whenLastCooked;
    }

    public Type getG_idType() {
        return g_idType;
    }

    public void setG_idType(Type g_idType) {
        this.g_idType = g_idType;
    }

    public Part_Of_Day getH_idPartOfDay() {
        return h_idPartOfDay;
    }

    public void setH_idPartOfDay(Part_Of_Day h_idPartOfDay) {
        this.h_idPartOfDay = h_idPartOfDay;
    }
}
