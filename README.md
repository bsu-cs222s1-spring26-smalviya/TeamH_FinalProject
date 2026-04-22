This project is a Java-based recipe search application built with JavaFX. Users can enter an ingredient, and the application retrieves a list of recipes containing that ingredient using TheMealDB API. Each recipe can be viewed in detail, saved to a personal list, and reloaded later. The system also includes allergen detection, highlighting unsafe ingredients in red across both search results and recipe details. Additional features include user login, persistent storage of saved recipes, and a clean separation between model, view, and controller components.

Authors:

RJ Martin,

Jiahao Peng,

Dwight Williams,

Griffin B Roach,

Requirements:

Java 17 or higher

Gradle (wrapper included)

Internet connection (for API calls)

Notes:

The application uses TheMealDB API; no API key is required.

Saved recipes are stored locally in users.txt.

Allergen highlighting works automatically based on user input.
