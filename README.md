# WordSearch

Shopify Internship Challenge Fall 2019

What I am doing RN: Setting up the words diagonally

**CORE**

1. Create a word search mobile app for Android or iOS (depending on the discipline you’re applying to).
2. The word search should have at least a 10x10 grid.
3. Include at least the following 6 words: **Swift, Kotlin, ObjectiveC, Variable, Java, Mobile**. 
4. Keep track of how many words a user has found.
5. Make sure it compiles successfully.

**NON-CORE**

6. Randomize where the words are placed.
7. Make a slick UI with smooth animations.
8. Make it look good in portrait and landscape.
9. Feel free to add any additional features you can think of.
10. Allow the user to find the words by swiping over the words.

# Steps
Load words from a file. (Pending)
- Get dictionary.txt
- Randomly select words from the dictionary

Display random letters on a grid. (In progress)
- Use a 10x10 grid.
- Populate letters

Display the selected words and display them on the screen. **HARD** (In Progress)
- 6 directions (backwards and vertical, backwards and horizontal, backwards and diagonal, and regular with all the directions)
- Randomize where they are placed.

Implement selecting the words **HARD**
- Create a swiping function
- Once swiping keep track of words it has swiped over.
- If you release, check if the combination of letters is in the selected words array.

**EXTRA** (Create tasks later)
- Splash page
- Options for difficulty (size of grid and size of words)
- High-Score (SQLite Database)
