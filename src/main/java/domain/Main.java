package domain;

import jakarta.persistence.*;
import object.Part_Of_Day;
import object.Recipe;
import object.Type;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        Part_Of_Day matin = new Part_Of_Day("Matin");
        Part_Of_Day midi = new Part_Of_Day("Midi");
        Part_Of_Day soir = new Part_Of_Day("Soir");
        Type petitdej = new Type("Petit-déjeuner");
        Type dejeuner = new Type("Déjeuner");
        Type diner = new Type("Diner");
        Recipe recipe = new Recipe("Bœuf Bourguignon", "C'est des instrustions", "", "", LocalDate.now(), "", dejeuner, midi);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            //Create
            entityManager.persist(matin);
            entityManager.persist(midi);
            entityManager.persist(soir);
            entityManager.persist(petitdej);
            entityManager.persist(dejeuner);
            entityManager.persist(diner);
            entityManager.persist(recipe);
            // READ
           entityManager.find(Recipe.class, 1);
            //            Query query = entityManager.createQuery("Requete en JPQL");
            //UPADTE
            //            entityManager.merge()
            // DELETE
            //             entityManager.remove();
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
        } finally {
            emf.close();
        }

    }

}
