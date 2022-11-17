package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import object.Cooking_Tool;
import object.Ingredient;
import object.Recipe;
import utils.Methode;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecipeJpaDao implements CrudDAO<Recipe> {

    @Override
    public List<Recipe> findAll() throws SQLException {
        List<Recipe> resultList = new ArrayList<>();
        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            Query query = entityManager.createQuery("select r from Recipe r");
            resultList =  query.getResultList();
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }
        return resultList;
    }

    @Override
    public Optional<Recipe> findById(Long id) throws SQLException {

        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            Query query = entityManager.createQuery("select r from Recipe r where r.e_whenLastCooked <= ?1");
            et.commit();
            return (Optional<Recipe>) query.setParameter("?1", id).getSingleResult();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }
        return Optional.empty();
    }

    public List<Optional<Recipe>> findByKeyWord(String KeyWord) throws SQLException {

        List<Optional<Recipe>> resultList = new ArrayList();

        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            Query query = entityManager.createQuery("SELECT r FROM Recipe r " +
                    "WHERE r.a_name LIKE CONCAT(\"%\", ?1, \"%\") " +
                    "OR r.b_instruction LIKE CONCAT(\"%\", ?2, \"%\") " +
                    "OR r.f_description LIKE CONCAT(\"%\", ?3, \"%\")");

            resultList = query.setParameter("?1",KeyWord).setParameter("?2",KeyWord).setParameter("?3",KeyWord).getResultList();
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }
        return resultList;
    }

    public Optional<Recipe> findRand() throws SQLException {
        LocalDate dateMin = LocalDate.now().minus(6, ChronoUnit.DAYS);
        List validId = new ArrayList<>();
        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            Query query = entityManager.createQuery("select Recipe.id from Recipe r where r.e_whenLastCooked <= ?1");
            query.setParameter("?1", dateMin);
            validId = query.getResultList();
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }
        return findById((Long) validId.get((int)(validId.size() * Math.random())));
    }

    @Override
    public boolean delete(Long id) throws SQLException {

        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            Query query = entityManager.createQuery("delete from Recipe r where r.id = ?1");
            query.setParameter("?1", id).executeUpdate();
            et.commit();
            return true;
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }
        return false;
    }

    @Override
    public Recipe update(Recipe recipe) throws SQLException {
        String newName = updateName(recipe), newInstruction = updateInstruction(recipe), newIdListIngredient = updateListeIngredient(recipe),
                newIdListCookingTool = updateListeCookingTool(recipe), newDescription = updateDescription(recipe);
        LocalDate newDate = updateDate(recipe);
        recipe.setA_name(newName);
        recipe.setF_description(newDescription);
        recipe.setB_instruction(newInstruction);
        recipe.setD_listIdCookingTool(newIdListCookingTool);
        recipe.setC_listIdIngredient(newIdListIngredient);
        recipe.setE_whenLastCooked(newDate);
        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            Query query = entityManager.createQuery( "UPDATE Recipe " +
                    "SET Recipe.a_name = ?1, Recipe.f_description = ?2, Recipe.b_instruction = ?3, Recipe.d_listIdCookingTool = ?4," +
                    " Recipe.c_listIdIngredient = ?5, Recipe.e_whenLastCooked = ?6" +
                    "WHERE id = ?7");
            query.setParameter("?1", newName)
                    .setParameter("?2", newDescription)
                    .setParameter("?3", newInstruction)
                    .setParameter("?4", newIdListCookingTool)
                    .setParameter("?5", newIdListIngredient)
                    .setParameter("?6", newDate)
                    .setParameter("?7", recipe.getId())
                    .executeUpdate();
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }
        return recipe;
    }

    public String updateName(Recipe recipe){
        Boolean choice = Methode.askBoolean("Voulez-vous changer le nom de la recette ?");
        if (choice){
            return Methode.askString("Quel est le nouveau nom de la recette ?");
        } else {
            return recipe.getA_name();
        }
    }

    public String updateInstruction(Recipe recipe){
        Boolean choice = Methode.askBoolean("Voulez-vous changer les instructions de la recette ?");
        if (choice){
            return Methode.askString("Quels sont les nouvelles instructions de la recette ?");
        } else {
            return recipe.getB_instruction();
        }
    }

    public String updateDescription(Recipe recipe){
        Boolean choice = Methode.askBoolean("Voulez-vous changer la description de la recette ?");
        if (choice){
            return Methode.askString("Quelle est la nouvelle description de la recette ?");
        } else {
            return recipe.getF_description();
        }
    }

    public LocalDate updateDate(Recipe recipe){
        Boolean choice = Methode.askBoolean("Voulez-vous changer la date a laquelle vous avez cuisiné cette recette ?");
        if (choice){
            return Methode.askDate("Quelle est la nouvelle date ?");
        } else {
            return recipe.getE_whenLastCooked();
        }
    }

    public String updateListeIngredient(Recipe recipe) throws SQLException {
        Boolean choice = Methode.askBoolean("Voulez-vous changer la liste des ingrédients de la recette ?");
        String nameIngredient;
        List<Optional<Long>> listeIdIngredient = new ArrayList<>();
        if (choice){
            do {
                nameIngredient = Methode.askString("Y a-t-il des ingrédients à ajouter à la recette ? (sinon taper NON)");
                if (!nameIngredient.equals("NON")){
                    listeIdIngredient.add(addListedIdIngredient(nameIngredient));
                }
            } while (!nameIngredient.equals("NON"));
            return Arrays.toString(new List[]{listeIdIngredient});
        } else {
            return recipe.getC_listIdIngredient();
        }
    }

    public String updateListeCookingTool(Recipe recipe) throws SQLException {
        Boolean choice = Methode.askBoolean("Voulez-vous changer la liste des ustensiles de la recette ?");
        String nameCookingTool;
        List<Optional<Long>> listeIdCookingTool = new ArrayList<>();
        if (choice){
            do {
                nameCookingTool = Methode.askString("Y a-t-il des ustensiles à ajouter à la recette ? (sinon taper NON)");
                if (!nameCookingTool.equals("NON")){
                    listeIdCookingTool.add(addListedIdCookingTool(nameCookingTool));
                }
            } while (!nameCookingTool.equals("NON"));
            return Arrays.toString(new List[]{listeIdCookingTool});
        } else {
            return recipe.getD_listIdCookingTool();
        }
    }

    public static void createIngredient(String name) throws SQLException {

        Ingredient newIngredient = new Ingredient(name);
        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            entityManager.persist(newIngredient);
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }
    }

    public static void createCookingTool(String name) throws SQLException {

        Cooking_Tool newCookingTool = new Cooking_Tool(name);
        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            entityManager.persist(newCookingTool);
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }
    }

    public static Optional<Long> addListedIdIngredient(String name) throws SQLException {
        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            Query query = entityManager.createQuery("SELECT Ingredient.id FROM Ingredient WHERE name LIKE CONCAT(\"%\", ?1, \"%\") ");
            Optional<Long> id = (Optional<Long>) query.setParameter("?1", name).getSingleResult();
            et.commit();
            if (id.isPresent()) {
                return id;
            } else {
                System.out.println("L'ingrédient n'existait pas, il a été ajouté.");
                createIngredient(name);
                return addListedIdIngredient(name);
            }
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }
        return Optional.empty();
    }

    public static Optional<Long> addListedIdCookingTool(String name) throws SQLException {
        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            Query query = entityManager.createQuery("SELECT Cooking_Tool.id FROM Cooking_Tool WHERE name LIKE CONCAT(\"%\", ?1, \"%\")");
            Optional<Long> id = (Optional<Long>) query.setParameter("?1", name).getSingleResult();
            et.commit();
            if (id.isPresent()) {
                return id;
            } else {
                System.out.println("L'ustensile n'existait pas, il a été ajouté.");
                createCookingTool(name);
                return addListedIdCookingTool(name);
            }
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }
        return Optional.empty();
    }

    @Override
    public Recipe create(Recipe recipe) throws SQLException {
        long id = 0;
        String ingredientName, cookingToolName;
        StringBuilder ingredientList = null, cookingToolList = null;
        List<Optional<Long>> listeIdIngredient = new ArrayList<Optional<Long>>();
        List<Optional<Long>> listeIdCookingTool = new ArrayList<>();
        String name = Methode.askString("Quel est la nom  du plat à ajouter ?");
        String instruction = Methode.askString("Avez-vous des instructions ?");
        LocalDate lastUsed = Methode.askDate("Quand avez-vous manger ce plat pour la dernière fois ?");
        String description = Methode.askString("Avez-vous une description ?");
        EntityManagerFactory emf = ManagerFactory.getFactoryInstance();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
            try {
                et.begin();
                do {
                    ingredientName = Methode.askString("Y a-t-il des ingrédients à ajouter à la recette ? (sinon taper NON)");
                    if (!ingredientName.equals("NON")){
                        listeIdIngredient.add(addListedIdIngredient(ingredientName));
                    }
                } while (!ingredientName.equals("NON"));
                do {
                    cookingToolName = Methode.askString("Y a-t-il des ustensiles de cuisine à ajouter à la recette ? (sinon taper NON)");
                    if (!cookingToolName.equals("NON")){
                        listeIdCookingTool.add(addListedIdCookingTool(cookingToolName));
                    }
                } while (!cookingToolName.equals("NON"));
                for (Optional<Long> idInLIst : listeIdIngredient){
                    if (ingredientList.isEmpty()){
                        ingredientList.append(idInLIst);
                    } else {
                        ingredientList.append(", ").append(idInLIst);
                    }
                }
                for (Optional<Long> idInLIst : listeIdCookingTool){
                    if (cookingToolList.isEmpty()){
                        cookingToolList.append(idInLIst);
                    } else {
                        cookingToolList.append(", ").append(idInLIst);
                    }
                }
                recipe = new Recipe(id, name, instruction, String.valueOf(ingredientList), String.valueOf(cookingToolList), lastUsed, description);
                entityManager.persist(recipe);
                et.commit();
            } catch (Exception e) {
                if (et.isActive()) et.rollback();
            } finally {
                emf.close();
            }
            return recipe;
    }
}
/*


    public static void print(Recipe recipe) throws SQLException {
        StringBuilder listCookingTool = null , listIngredient = null;
        String queryC = "SELECT name FROM cooking_tool WHERE idCookingTool IN (SELECT CONVERT(value, UNSIGNED INTEGER) FROM STRING_SPLIT(?, \", \"))"
                , queryI = "SELECT name FROM ingredient WHERE idIngredient IN (SELECT CONVERT(value, UNSIGNED INTEGER) FROM STRING_SPLIT(?, \", \"))";
        try(Connection con = ConnectionManager.getConnectionInstance()) {
            try (PreparedStatement preparedStatement = con.prepareStatement(queryC)) {
                preparedStatement.setString(1, recipe.getListIdCookingTool());
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    listCookingTool.append(rs.getString(1));
                }
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try (PreparedStatement preparedStatement = con.prepareStatement(queryI)) {
                preparedStatement.setString(1, recipe.getListIdIngredient());
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    listIngredient.append(rs.getString(1));
                }
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        System.out.println(new StringBuilder("Id : ")
                .append(recipe.getId())
                .append("\nNom : ")
                .append(recipe.getName())
                .append("\nListe des ustensiles de cuisines : ")
                .append(listCookingTool)
                .append("\nListe des ingrédients : ")
                .append("")//listIngredient)
                .append("\nInstruction : ")
                .append("")//recipe.getInstruction())
                .append("\nDescription : ")
                .append(recipe.getDescription())
                .append("\nDate de dernière utilisation : ")
                .append(recipe.getWhenLastCooked())
        );
        }
    }


}
*/