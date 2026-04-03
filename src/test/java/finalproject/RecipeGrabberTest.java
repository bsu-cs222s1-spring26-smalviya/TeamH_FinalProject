package finalproject;

import edu.bsu.cs222.finalproject.JsonDataParser;
import edu.bsu.cs222.finalproject.RecipeGrabber;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeGrabberTest {

    @Test
    void testFetchRecipesByIngredientReturnsJson() throws Exception {
        RecipeGrabber grabber = new RecipeGrabber();
        String json = grabber.fetchRecipesByIngredient("chicken");

        assertNotNull(json);
        assertTrue(json.contains("meals"));
    }

    @Test
    void testJsonParserExtractsMealNames() {
        String fakeJson = """
                {
                  "meals": [
                    {"strMeal": "Chicken Curry"},
                    {"strMeal": "Chicken Alfredo"}
                  ]
                }
                """;

        JsonDataParser parser = new JsonDataParser();
        String[] meals = parser.parseMeals(fakeJson);

        assertEquals(2, meals.length);
        assertEquals("Chicken Curry", meals[0]);
        assertEquals("Chicken Alfredo", meals[1]);
    }
}
