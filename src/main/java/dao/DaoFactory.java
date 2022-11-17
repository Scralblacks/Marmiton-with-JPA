package dao;

import dao.CrudDAO;
import object.Recipe;

public class DaoFactory {

    private DaoFactory() {
    }

    public static CrudDAO<Recipe> getRecipeDao() {
        return new RecipeJpaDao();
    }
}